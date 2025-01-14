package com.purplebits.emrd2.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupWithMembersResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupWithUsersResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupsRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.UsersRequestResponseDTO;
import com.purplebits.emrd2.entity.UserGroupDetails;


public interface UserGroupsService {

	public UserGroupsDTO addNewUserGroups(UserGroupsDTO userGroupsDTO);
	public UserGroupsDTO updateUserGroups(UserGroupsDTO userGroupsDTO);
	public UserGroupsDTO getUserGroupsById(Integer groupId);
	public PaginationResponseDTO<UserGroupsRequestResponseDTO> getAllUserGroups(Integer pageNumber, Integer pageSize, String sortBy, String sortType);	
	void deleteAllUserGroupsRolesByRoleId(Integer roleId);
	public UserGroupsDTO deleteUserGroups(Integer groupId);
	
//	public DeleteUserGroupMemberRequestResponseDTO	deleteUserGroupMember(DeleteUserGroupMemberRequestResponseDTO deleteUserGroupMemberRequestResponseDTO);
	public UserGroupsDTO deleteUserGroupWithMembers(Integer groupId);
	
	PaginationResponseDTO<UserGroupWithUsersResponseDTO> getAllUserGroupWithMembersAndRole(
		    int pageNumber, int pageSize, String sortBy, String sortType);
	UserGroupsDTO getUserGroupsByRoleId(Integer roleId);
	public UserGroupWithMembersResponseDTO addNewUserGroupWithMembers(UserGroupsDTO userGroupsDTO,
			List<UsersRequestResponseDTO> users);
	public UserGroupWithMembersResponseDTO updateUserGroupWithMembers(UserGroupsDTO userGroupsDTO,
			List<UsersRequestResponseDTO> users);
	public List<Integer> getAllUserGroupsById(List<Integer> groupIds);
	public PaginationResponseDTO<UserGroupWithUsersResponseDTO> getAllUserGroupWithMembersAndRoleByFilterCriteria(
			Integer pageNumber, Integer pageSize, String sortBy, String sortType, Specification<UserGroupDetails> spec);
	public String createQuery(String search);
	
}
