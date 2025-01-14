package com.purplebits.emrd2.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UserGroupMembershipDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupMembershipRequestResponseDTO;
import com.purplebits.emrd2.entity.Roles;
import com.purplebits.emrd2.entity.UserGroupMembership;
import com.purplebits.emrd2.entity.UserGroups;
import com.purplebits.emrd2.exceptions.UserGroupMembershipNotFoundException;
import com.purplebits.emrd2.exceptions.UserGroupsNotFoundException;
import com.purplebits.emrd2.repositories.AppUserRepository;
import com.purplebits.emrd2.repositories.UserGroupMembershipRepository;
import com.purplebits.emrd2.repositories.UserGroupsRepository;
import com.purplebits.emrd2.service.UserGroupMembershipService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Service
public class UserGroupMembershipServiceImpl implements UserGroupMembershipService {

	private final Logger logger = LogManager.getLogger(UserGroupMembershipServiceImpl.class);
	private final String className = UserGroupMembershipServiceImpl.class.getSimpleName();

	@Autowired
	Environment environment;
	@Autowired
	private UserGroupMembershipRepository userGroupMembershipRepository;

	@Autowired
	private UserGroupsRepository userGroupsRepository;

	@Autowired
	private AppUserRepository userRepository;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupMembershipDTO addUserMembership(UserGroupMembershipDTO userGroupMembershipDTO) {
		logger.info(className + " addUserGroupMembership() invoked for " + userGroupMembershipDTO);

		UserGroupMembership userGroupMembership = ObjectMapperUtils.map(userGroupMembershipDTO,
				UserGroupMembership.class);
		userGroupMembership.setStatus(Status.ACTIVE);

		Optional<UserGroups> usersGroupOpt = userGroupsRepository
				.findByGroupIdAndStatus(userGroupMembershipDTO.getGroups().getGroupId(), Status.ACTIVE);

		UserGroups userGroup = usersGroupOpt
				.orElseThrow(() -> new UserGroupsNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND)));
		userGroupMembership.setGroups(userGroup);

		userGroupMembership = userGroupMembershipRepository.saveAndFlush(userGroupMembership);

		return ObjectMapperUtils.map(userGroupMembership, UserGroupMembershipDTO.class);

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupMembershipDTO updateUserMembership(UserGroupMembershipDTO userGroupMembershipDTO) {
		logger.info(className + " updateUserGroupMembership() invoked for " + userGroupMembershipDTO);

		Optional<UserGroupMembership> userGroupMembershipOpt = userGroupMembershipRepository
				.findByUserGroupMembershipIdAndStatus(userGroupMembershipDTO.getMembershipId(), Status.ACTIVE);
		if (!userGroupMembershipOpt.isPresent()) {
			throw new UserGroupMembershipNotFoundException(environment.getProperty(ResponseMessages.USER_GROUP_MEMBERSHIP_NOT_FOUND_ERROR));
		}

		UserGroupMembership userGroupMembership = ObjectMapperUtils.map(userGroupMembershipDTO,
				UserGroupMembership.class);
		userGroupMembership.setStatus(Status.ACTIVE);

		userGroupMembership = userGroupMembershipRepository.saveAndFlush(userGroupMembership);

		return ObjectMapperUtils.map(userGroupMembership, UserGroupMembershipDTO.class);
	}

	@Override
	public UserGroupMembershipDTO getUserMembershipById(Integer userGroupMembershipId) {
		logger.info(className + " getUserMembershipById() invoked for id: " + userGroupMembershipId);

		Optional<UserGroupMembership> userGroupMembershipOpt = userGroupMembershipRepository
				.findByUserGroupMembershipIdAndStatus(userGroupMembershipId, Status.ACTIVE);

		if (!userGroupMembershipOpt.isPresent()) {
			throw new UserGroupMembershipNotFoundException(environment.getProperty(ResponseMessages.USER_GROUP_MEMBERSHIP_NOT_FOUND_ERROR));
		}

		return ObjectMapperUtils.map(userGroupMembershipOpt.get(), UserGroupMembershipDTO.class);
	}

	@Override
	public PaginationResponseDTO<UserGroupMembershipRequestResponseDTO> getAllUserGroupMemberShip(Integer pageNumber,
			Integer pageSize, String sortBy, String sortType) {

		logger.info(className + " getAllUserGroupMemberShip() invoked");

		Sort sort = (sortType.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		PaginationResponseDTO<UserGroupMembershipRequestResponseDTO> pageResponse = new PaginationResponseDTO<>();

		Page<UserGroupMembership> userGroupMembershipPage = userGroupMembershipRepository.findAllByStatus(Status.ACTIVE,
				pageable);
		List<UserGroupMembership> memberships = userGroupMembershipPage.getContent();

		List<UserGroupMembershipDTO> membershipDTOs = ObjectMapperUtils.mapAll(memberships,
				UserGroupMembershipDTO.class);

		pageResponse.setData(
				UserGroupMembershipRequestResponseDTO.getUserGroupMembershipRequestResponseDTO(membershipDTOs));
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(userGroupMembershipPage.getNumber());
		paginationDTO.setLimit(userGroupMembershipPage.getSize());
		paginationDTO.setTotalPages(userGroupMembershipPage.getTotalPages());
		paginationDTO.setLastPage(userGroupMembershipPage.isLast());
		paginationDTO.setTotalCounts(userGroupMembershipPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);

		return pageResponse;
	}


	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupMembershipDTO deleteUserGroupMembershipById(Integer userGroupMembershipId) {
	    logger.info(className + " deleteUserGroupMembershipById() invoked for id: " + userGroupMembershipId);
	    Optional<UserGroupMembership> userGroupMembershipOpt = userGroupMembershipRepository
	            .findByUserGroupMembershipIdAndStatus(userGroupMembershipId, Status.ACTIVE);
	    
	    if (!userGroupMembershipOpt.isPresent()) {
	        throw new UserGroupMembershipNotFoundException(environment.getProperty(ResponseMessages.USER_GROUP_MEMBERSHIP_NOT_FOUND_ERROR));
	    }
	    UserGroupMembership userGroupMembership = userGroupMembershipOpt.get();
	    userGroupMembership.setStatus(Status.DELETED);
	    userGroupMembership = userGroupMembershipRepository.saveAndFlush(userGroupMembership);
	    return ObjectMapperUtils.map(userGroupMembership, UserGroupMembershipDTO.class);
	}

	
	

	

	@Override
	public List<Integer> findAllMembershipIdsByGroupId(Integer groupId) {
	    // Fetch all membership IDs for the specified groupId
	    return userGroupMembershipRepository.findAllMembershipIdsByGroupId(groupId);
	}
	@Override
    public List<Integer> findGroupIdsByUserId(Integer userId) {
        return userGroupMembershipRepository.findGroupIdsByUserId(userId,Status.ACTIVE);
    }
	 public List<Integer> findAllGroupMembershipIdsByUserId(Integer userId) {
	        return userGroupMembershipRepository.findAllGroupMembershipIdsByUserId(userId,Status.ACTIVE);
	    }

	

	 public Optional<UserGroupMembership> findByUserIdAndGroupIdAndStatus(Integer userId, Integer groupId,
				Status status){
		 return userGroupMembershipRepository.findByUserIdAndGroupIdAndStatus(userId, groupId,status);
	 }

	 @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public Integer deleteAllUserGroupMembershipsByGroupId(Integer groupId) {
		
		return userGroupMembershipRepository.deleteAllUserGroupMembershipsByGroupId(groupId);
	}
	 
	 @Override
		public List<Roles> findRolesByUserId(Integer userId){
			return userGroupMembershipRepository.findRolesByUserId(userId);
			
		}

	 public UserGroupMembershipDTO findByUserIdAndGroupId(Integer userId, Integer groupId){
		  Optional<UserGroupMembership> userGroupOp = userGroupMembershipRepository.findByUserIdAndGroupId(userId, groupId);
		  if(userGroupOp.isPresent())return ObjectMapperUtils.map(userGroupOp.get(), UserGroupMembershipDTO.class);
		  return null;
	 }
	 
	 @Override
		public List<RolesDTO> findRolesByLoggedInUserId(Integer userId){
			 List<Roles> rolesList = userGroupMembershipRepository.findRolesByUserId(userId);
			 return ObjectMapperUtils.mapAll(rolesList, RolesDTO.class);
		}
	 
}
