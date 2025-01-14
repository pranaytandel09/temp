package com.purplebits.emrd2.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.ActiveUserDetailsDTO;
import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UserDetailsDTO;
import com.purplebits.emrd2.dto.UserGroupDTO;
import com.purplebits.emrd2.dto.UserGroupMembershipDTO;
import com.purplebits.emrd2.dto.UserGroupRoleDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.dto.request_response.UserMappedUserGroupRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.UsersRequestResponseDTO;
import com.purplebits.emrd2.entity.UserDetailsView;
import com.purplebits.emrd2.entity.UserGroupMembership;
import com.purplebits.emrd2.entity.UserGroups;
import com.purplebits.emrd2.entity.Users;
import com.purplebits.emrd2.exceptions.EmailSendingException;
import com.purplebits.emrd2.exceptions.UserAlreadyExistsException;
import com.purplebits.emrd2.exceptions.UserGroupsNotAssignedException;
import com.purplebits.emrd2.exceptions.UserGroupsNotFoundException;
import com.purplebits.emrd2.exceptions.UserNotFoundException;
import com.purplebits.emrd2.repositories.AppUserRepository;
import com.purplebits.emrd2.repositories.UserDetailsViewRepository;
import com.purplebits.emrd2.repositories.UserGroupMembershipRepository;
import com.purplebits.emrd2.repositories.UserGroupsRepository;
import com.purplebits.emrd2.service.EmailService;
import com.purplebits.emrd2.service.RolesService;
import com.purplebits.emrd2.service.UserGroupMembershipService;
import com.purplebits.emrd2.service.UserGroupsService;
import com.purplebits.emrd2.service.UserService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.EmailUtils;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class UserServiceImpl implements UserService {
	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	private final String className = UserServiceImpl.class.getSimpleName();
	@Autowired
	@Lazy
	private UserGroupsService userGroupsService;
	@Autowired
	private AppUserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	UserGroupsRepository userGroupsRepository;
	@Lazy
	@Autowired
	private RolesService rolesService;
	@Autowired
	private UserGroupMembershipRepository userGroupMembershipRepository;
	@Autowired
	private UserGroupMembershipService userGroupMembershipService;
	@Autowired
	private UserDetailsViewRepository userDetailsViewRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	Environment environment;
	@Autowired
	private EmailUtils emailUtils;

	@Override
	public UsersDTO addUser(UsersDTO UsersDTO) {

		Users users= userRepository.findByEmail(UsersDTO.getEmail());
		if (users!=null && users.getStatus() == Status.ACTIVE) {
			throw new UserAlreadyExistsException(environment.getProperty(ResponseMessages.USER_ALREADY_EXISTS_ERROR));
		}

		if (users!=null && users.getStatus() == Status.DELETED) {
			UsersDTO.setUserId(users.getUserId());
		}

		Users user = ObjectMapperUtils.map(UsersDTO, Users.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setStatus(Status.ACTIVE);
		user = userRepository.saveAndFlush(user);
		return ObjectMapperUtils.map(user, UsersDTO.class);
	}
	
	@Override
	public UsersDTO findUserById(Integer userId) {
		logger.info(className + " find User By Id() ");
		logger.debug(className + " findUserById() for userId: " + userId);
		Users user = userRepository.findById(userId).orElseThrow(() -> {
			return new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
		});
		UsersDTO userDTO = ObjectMapperUtils.map(user, UsersDTO.class);

		return userDTO;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UsersDTO deleteUserById(Integer userId) {
		logger.info("method delete user by id invoked.");
		logger.debug(className + " deleteUserById() for userId: " + userId);
		Users user = userRepository.findByIdAndStatus(userId, Status.ACTIVE).orElseThrow(() -> {
			return new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
		});

		userGroupMembershipRepository.deleteAllByUsersUserId(userId);
		user.setStatus(Status.DELETED);
		user = userRepository.saveAndFlush(user);
		UsersDTO userDTO = ObjectMapperUtils.map(user, UsersDTO.class);
		logger.info(className + " findUserById() succeeded for userId: " + userId);
		return userDTO;
	}

	public UsersDTO updateUser(UsersDTO userDTO) {
		logger.info(className + " updateUser() invoked for ");
		Users existingUser = userRepository.findByEmail(userDTO.getEmail());
		if (existingUser == null) {
			throw new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
		}
		existingUser.setFullName(userDTO.getFullName());
//	    existingUser.setUsername(userDTO.getUsername());
		if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		}
		existingUser.setStatus(Status.ACTIVE);
		existingUser = userRepository.saveAndFlush(existingUser);
		return ObjectMapperUtils.map(existingUser, UsersDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UsersDTO mapUsersToGroups(UsersDTO userDTO, List<UserGroupsDTO> userGroupsDTO) {
		logger.info("method to add users to userGroup invoked.");
		logger.debug(className + " mapUsersToGroups()");
		Users checkUsersByEmail = userRepository.findByEmail(userDTO.getEmail());

		if (checkUsersByEmail != null && checkUsersByEmail.getStatus() != Status.DELETED) {
			throw new UserAlreadyExistsException(environment.getProperty(ResponseMessages.USER_ALREADY_EXISTS_ERROR));

		} else if (checkUsersByEmail != null && checkUsersByEmail.getStatus().equals(Status.DELETED)) {
			userDTO.setUserId(checkUsersByEmail.getUserId());
		}

		Users user = ObjectMapperUtils.map(userDTO, Users.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setStatus(Status.ACTIVE);
		user = userRepository.saveAndFlush(user);
		UsersDTO usersDTO = ObjectMapperUtils.map(user, UsersDTO.class);
		try {
			String emailBody = emailUtils.generateWelcomeEmailBody(usersDTO.getFullName(), usersDTO.getEmail());
			String subject = "Welcome to Our Platform!";
			emailService.sendEmail(usersDTO.getEmail(), subject, emailBody);
		} catch (Exception e) {
			logger.error(className + "Failed to send welcome email to " + e);
			throw new EmailSendingException(environment.getProperty(ResponseMessages.ERROR_EMAIL_SENDING));

		}

		for (UserGroupsDTO userGroup : userGroupsDTO) {
			Optional<UserGroups> userGroupOp = userGroupsRepository.findByGroupIdAndStatus(userGroup.getGroupId(),
					Status.ACTIVE);
			if (!userGroupOp.isPresent())
				throw new UserGroupsNotFoundException(
						environment.getProperty(ResponseMessages.USER_GROUP_NOT_FOUND_ERROR));

			UserGroupMembershipDTO userGroupMembership = userGroupMembershipService
					.findByUserIdAndGroupId(usersDTO.getUserId(), userGroup.getGroupId());
			if (userGroupMembership == null) {
				UserGroupMembershipDTO membership = new UserGroupMembershipDTO();
				membership.setUsers(usersDTO);
				membership.setGroups(userGroup);
				membership.setStatus(Status.ACTIVE);
				membership.setCreatedAt(new Timestamp(System.currentTimeMillis()));
				userGroupMembershipService.addUserMembership(membership);
			} else {
				if (userGroupMembership.getStatus().equals(Status.ACTIVE)) {
					continue;
				} else {
					userGroupMembershipService.addUserMembership(userGroupMembership);
				}
			}
		}

		return usersDTO;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserMappedUserGroupRequestResponseDTO mapUsersToGroupsUpdate(UsersDTO userDTO,
			List<UserGroupsDTO> userGroupsDTO) {
		logger.info("method to update mapping of user to userGroup invoked.");
		logger.debug(className + " mapUsersToGroupsUpdate() ");
		if (userGroupsDTO.isEmpty())
			throw new UserGroupsNotAssignedException(
					environment.getProperty(ResponseMessages.USER_GROUPS_NOT_ASSIGNED_ERROR));

		Users user = ObjectMapperUtils.map(userDTO, Users.class);
		Optional<Users> savedUser = userRepository.findByUserIdAndStatus(user.getUserId(), Status.ACTIVE);
		if (savedUser.isEmpty()) {
			throw new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
		}

		userRepository.saveAndFlush(user);
		Users userObj = savedUser.get();

		List<UserGroupMembership> membershipsToSave = new ArrayList<>();

		userGroupMembershipRepository.deleteAllByUsersUserId(userObj.getUserId());

		for (UserGroupsDTO groupDTO : userGroupsDTO) {
			Integer groupId = groupDTO.getGroupId();

			UserGroups userGroup = userGroupsRepository.findByGroupIdAndStatus(groupId, Status.ACTIVE)
					.orElseThrow(() -> new UserGroupsNotFoundException(
							environment.getProperty(ResponseMessages.USER_GROUP_NOT_FOUND_ERROR)));

			UserGroupMembership newMembership = new UserGroupMembership();
			newMembership.setUsers(userObj);
			newMembership.setGroups(userGroup);
			newMembership.setStatus(Status.ACTIVE);
			membershipsToSave.add(newMembership);

		}

		if (!membershipsToSave.isEmpty()) {
			userGroupMembershipRepository.saveAllAndFlush(membershipsToSave);
		}

		List<UserGroupsDTO> userGroupResponseDTOs = membershipsToSave.stream().map(membership -> {
			UserGroupsDTO dto = ObjectMapperUtils.map(membership.getGroups(), UserGroupsDTO.class);
			dto.setDisplayName(membership.getGroups().getDisplayName());
			dto.setRoles(ObjectMapperUtils.map(membership.getGroups().getRoles(), RolesDTO.class));
			dto.setCreatedAt(membership.getGroups().getCreatedAt());
			dto.setUpdatedAt(membership.getGroups().getUpdatedAt());
			dto.setStatus(membership.getGroups().getStatus());
			return dto;
		}).collect(Collectors.toList());

		return new UserMappedUserGroupRequestResponseDTO(ObjectMapperUtils.map(savedUser, UsersDTO.class),
				userGroupResponseDTOs);
	}

	public static List<UserGroupRoleDTO> parseGroupRoleMappings(String input) {
		List<UserGroupRoleDTO> mappings = new ArrayList<>();
//		String regex = "\\(group_id - (\\d+) and role_id - (\\d+) and group_name - ([^\\)]+?) and role_name - ([^\\)]+?)\\)";
		String regex = "\\(group_id - (\\d+) and role_id - (\\d+) and group_name - (.+?) and role_name - (.+?)\\)";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			// Extract values from the match
			Integer groupId = Integer.parseInt(matcher.group(1).trim());
			Integer roleId = Integer.parseInt(matcher.group(2).trim());
			String groupName = matcher.group(3).trim();
			String roleName = matcher.group(4).trim();

			// Create and add a new bean
			RolesDTO rolesDTO = new RolesDTO();
			rolesDTO.setRoleId(roleId);
			rolesDTO.setRoleName(roleName);
			UserGroupDTO userGroupsDTO = new UserGroupDTO();
			userGroupsDTO.setGroupId(groupId);
			userGroupsDTO.setGroupName(groupName);
			userGroupsDTO.setDisplayName(groupName);

			UserGroupRoleDTO userGroupRoleDTO = new UserGroupRoleDTO();
			userGroupRoleDTO.setRole(rolesDTO);
			userGroupRoleDTO.setUserGroup(userGroupsDTO);

			mappings.add(userGroupRoleDTO);
		}

		return mappings;
	}

	@Override
	public List<UsersRequestResponseDTO> getActiveUsers() {
		logger.info("method to get active users invoke");
		logger.debug(className + " getActiveUsers()");
		List<Users> usersList = userRepository.findAllByStatus(Status.ACTIVE);
		List<UsersDTO> usersDTOs = ObjectMapperUtils.mapAll(usersList, UsersDTO.class);

		return UsersRequestResponseDTO.getUsersRequestResponseDTO(usersDTOs);
	}

	@Override
	public PaginationResponseDTO<ActiveUserDetailsDTO> getUserInfoBySpecifications(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType, Specification<UserDetailsView> spec) {
		logger.info(className + " getUserInfoBySpecifications() invoked.");
		Sort sort = (sortType.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		PaginationResponseDTO<ActiveUserDetailsDTO> pageResponse = new PaginationResponseDTO<>();

		Page<UserDetailsView> usersPage = this.userDetailsViewRepository.findAll(spec, p);
		List<UserDetailsView> usersList = usersPage.getContent();
		List<ActiveUserDetailsDTO> result = ObjectMapperUtils.mapAll(usersList, ActiveUserDetailsDTO.class);

		pageResponse.setData(result);
		PaginationDTO pgDto = new PaginationDTO();
		pgDto.setPage(usersPage.getNumber());
		pgDto.setLimit(usersPage.getSize());
		pgDto.setTotalPages(usersPage.getTotalPages());
		pgDto.setLastPage(usersPage.isLast());
		pgDto.setTotalCounts(usersPage.getTotalElements());
		pageResponse.setPagination(pgDto);

		return pageResponse;

	}

	@Override
	public PaginationResponseDTO<UserDetailsDTO> getActiveUserDetailsBysearch(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType, Specification<UserDetailsView> spec) {
		PaginationResponseDTO<UserDetailsDTO> pageResponse = new PaginationResponseDTO<>();

		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<UserDetailsView> usersPage = userDetailsViewRepository.findAll(spec, pageable);
		List<UserDetailsView> activeUsers = usersPage.getContent();
		List<UserDetailsDTO> userDetailsDTOList = new ArrayList<>();

		for (UserDetailsView userDetailsView : activeUsers) {
			UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
			UsersDTO usersDTO = new UsersDTO();
			usersDTO.setUserId(userDetailsView.getUserId());
			usersDTO.setFullName(userDetailsView.getFullName());
			usersDTO.setEmail(userDetailsView.getEmail());

			List<UserGroupRoleDTO> userGroupRoleDTOs = parseGroupRoleMappings(
					userDetailsView.getDetailedGroupRoleMappings());
			userDetailsDTO.setUser(usersDTO);
			userDetailsDTO.setUserGroupRoles(userGroupRoleDTOs);
			userDetailsDTOList.add(userDetailsDTO);
		}

		pageResponse.setData(userDetailsDTOList);
		PaginationDTO pgDto = new PaginationDTO();
		pgDto.setPage(usersPage.getNumber());
		pgDto.setLimit(usersPage.getSize());
		pgDto.setTotalPages(usersPage.getTotalPages());
		pgDto.setLastPage(usersPage.isLast());
		pgDto.setTotalCounts(usersPage.getTotalElements());
		pageResponse.setPagination(pgDto);

		return pageResponse;
	}

	@Override
	public String createQueryForUserSearch(String search) {
		StringBuilder actualQuery = new StringBuilder();
		String result;
		if (search != null && !search.isEmpty()) {
			if (search.contains(",")) {
				List<String> userInputList = Arrays.asList(search.split("\\s*,\\s*"));

				actualQuery.append("(");
				for (int i = 0; i < userInputList.size(); i++) {
					if (i == userInputList.size() - 1) {
						actualQuery.append("fullName ==" + "'*" + userInputList.get(i)).append("*'").append(")")
								.append(",");
					} else {
						actualQuery.append("fullName ==" + "'*" + userInputList.get(i)).append("*'").append(",");
					}

				}

				actualQuery.append("(");
				for (int i = 0; i < userInputList.size(); i++) {
					if (i == userInputList.size() - 1) {
						actualQuery.append("email ==" + "'*" + userInputList.get(i)).append("*'").append(")")
								.append(";");
					} else {
						actualQuery.append("email ==" + "'*" + userInputList.get(i)).append("*'").append(",");
					}

				}

			} else {
				actualQuery.append("(fullName ==" + "'*" + search + "*'").append(")").append(",");
				actualQuery.append("(email ==" + "'*" + search + "*'").append(")").append(";");
			}
		}
		actualQuery.append("status ==" + "'*true*'").append(";");
		if (actualQuery.charAt(actualQuery.length() - 1) == ';'
				|| actualQuery.charAt(actualQuery.length() - 1) == ',') {
			result = actualQuery.substring(0, actualQuery.length() - 1);
		} else {
			result = actualQuery.toString();
		}
		return result;
	}

	@Override
	public String createQueryForActiveUserSearch(String search) {
		String result;
		StringBuilder actualQuery = new StringBuilder();

		if (search != null && !search.isEmpty()) {

			if (search.contains(",")) {
				List<String> userInputList = Arrays.asList(search.split("\\s*,\\s*"));

				actualQuery.append("(");
				for (int i = 0; i < userInputList.size(); i++) {
					if (i == userInputList.size() - 1) {
						actualQuery.append("fullName ==" + "'*" + userInputList.get(i)).append("*'").append(")")
								.append(",");
					} else {
						actualQuery.append("fullName ==" + "'*" + userInputList.get(i)).append("*'").append(",");
					}

				}

				actualQuery.append("(");
				for (int i = 0; i < userInputList.size(); i++) {
					if (i == userInputList.size() - 1) {
						actualQuery.append("email ==" + "'*" + userInputList.get(i)).append("*'").append(")")
								.append(",");
					} else {
						actualQuery.append("email ==" + "'*" + userInputList.get(i)).append("*'").append(",");
					}

				}

				actualQuery.append("(");
				for (int i = 0; i < userInputList.size(); i++) {
					if (i == userInputList.size() - 1) {
						actualQuery.append("groupDisplayNames ==" + "'*" + userInputList.get(i)).append("*'")
								.append(")").append(",");
					} else {
						actualQuery.append("groupDisplayNames ==" + "'*" + userInputList.get(i)).append("*'")
								.append(",");
					}

				}

				actualQuery.append("(");
				for (int i = 0; i < userInputList.size(); i++) {
					if (i == userInputList.size() - 1) {
						actualQuery.append("roleNames ==" + "'*" + userInputList.get(i)).append("*'").append(")")
								.append(";");
					} else {
						actualQuery.append("roleNames ==" + "'*" + userInputList.get(i)).append("*'").append(",");
					}

				}

			} else {
				actualQuery.append("(fullName ==" + "'*" + search + "*'").append(")").append(",");
				actualQuery.append("(email ==" + "'*" + search + "*'").append(")").append(",");
				actualQuery.append("(groupDisplayNames ==" + "'*" + search + "*'").append(")").append(",");
				actualQuery.append("(roleNames ==" + "'*" + search + "*'").append(")").append(";");
			}
		}
		actualQuery.append("status ==" + "'*true*'").append(";");
		if (actualQuery.charAt(actualQuery.length() - 1) == ';'
				|| actualQuery.charAt(actualQuery.length() - 1) == ',') {
			result = actualQuery.substring(0, actualQuery.length() - 1);
		} else {
			result = actualQuery.toString();
		}
		return result;
	}

	@Override
	public PaginationResponseDTO<UsersRequestResponseDTO> getUsersOfSameTeams(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType, Integer loggedInUserId) {
		logger.info("method to get users of same UserGroup invoke");
		logger.debug(className + " getUsersOfSameTeams()");
		
		Sort sort = (sortType.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		PaginationResponseDTO<UsersRequestResponseDTO> pageResponse = new PaginationResponseDTO<>();

		Page<Users> usersPage = this.userRepository.findUsersInSameGroup(loggedInUserId, Status.ACTIVE,pageable);
		List<Users> usersList = usersPage.getContent();
		List<UsersDTO> result = ObjectMapperUtils.mapAll(usersList, UsersDTO.class);

		pageResponse.setData(UsersRequestResponseDTO.getUsersRequestResponseDTO(result));
		PaginationDTO pgDto = new PaginationDTO();
		pgDto.setPage(usersPage.getNumber());
		pgDto.setLimit(usersPage.getSize());
		pgDto.setTotalPages(usersPage.getTotalPages());
		pgDto.setLastPage(usersPage.isLast());
		pgDto.setTotalCounts(usersPage.getTotalElements());
		pageResponse.setPagination(pgDto);

		return pageResponse;
	}

	
}
