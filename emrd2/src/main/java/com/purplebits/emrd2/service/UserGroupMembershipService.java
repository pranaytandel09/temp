package com.purplebits.emrd2.service;

import java.util.List;
import java.util.Optional;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UserGroupMembershipDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupMembershipRequestResponseDTO;
import com.purplebits.emrd2.entity.Roles;
import com.purplebits.emrd2.entity.UserGroupMembership;
import com.purplebits.emrd2.types.Status;


public interface UserGroupMembershipService {
	
	public UserGroupMembershipDTO addUserMembership(UserGroupMembershipDTO userGroupMembershipDTO);
	public UserGroupMembershipDTO updateUserMembership(UserGroupMembershipDTO userGroupMembershipDTO);
	public UserGroupMembershipDTO getUserMembershipById(Integer userGroupMembershipId);
	public PaginationResponseDTO<UserGroupMembershipRequestResponseDTO> getAllUserGroupMemberShip(Integer pageNumber, Integer pageSize, String sortBy, String sortType);
	public UserGroupMembershipDTO deleteUserGroupMembershipById(Integer userGroupMembershipId);
	public List<Integer> findAllMembershipIdsByGroupId(Integer groupId);
	public  List<Integer> findGroupIdsByUserId(Integer userId);
	
	
	public List<Integer> findAllGroupMembershipIdsByUserId(Integer userId);
	public Optional<UserGroupMembership> findByUserIdAndGroupIdAndStatus(Integer userId, Integer groupId,
			Status status);
	public Integer deleteAllUserGroupMembershipsByGroupId(Integer groupId);
	public List<Roles> findRolesByUserId(Integer userId);
	UserGroupMembershipDTO findByUserIdAndGroupId(Integer userId, Integer groupId);
	List<RolesDTO> findRolesByLoggedInUserId(Integer userId);
	

}
