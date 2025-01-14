package com.purplebits.emrd2.service;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.purplebits.emrd2.dto.ActiveUserDetailsDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.UserDetailsDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.dto.request_response.UserMappedUserGroupRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.UsersRequestResponseDTO;
import com.purplebits.emrd2.entity.UserDetailsView;

public interface UserService {

	UsersDTO addUser(UsersDTO user);
	UsersDTO findUserById(Integer userId);
	UsersDTO mapUsersToGroups(UsersDTO userDTO, List<UserGroupsDTO> userGroupsDTO);
	UsersDTO updateUser(UsersDTO user);   
 UsersDTO deleteUserById(Integer userId);
UserMappedUserGroupRequestResponseDTO mapUsersToGroupsUpdate(UsersDTO userDTO, List<UserGroupsDTO> userGroupsDTO);
List<UsersRequestResponseDTO> getActiveUsers();
PaginationResponseDTO<ActiveUserDetailsDTO> getUserInfoBySpecifications(Integer pageNumber, Integer pageSize,
		String sortBy, String sortType, Specification<UserDetailsView> spec);
PaginationResponseDTO<UserDetailsDTO> getActiveUserDetailsBysearch(Integer pageNumber, Integer pageSize,
		String sortBy, String sortType, Specification<UserDetailsView> spec);
String createQueryForUserSearch(String search);
String createQueryForActiveUserSearch(String search);
PaginationResponseDTO<UsersRequestResponseDTO> getUsersOfSameTeams(Integer pageNumber, Integer pageSize, String sortBy,
		String sortType, Integer loggedInUserId);
}
