package com.purplebits.emrd2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;

import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.dto.RolePermissionsDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.request_response.RolePermissionsRequestResponseDTO;
import com.purplebits.emrd2.entity.Permissions;
import com.purplebits.emrd2.entity.RolePermissions;
import com.purplebits.emrd2.exceptions.PermissionNotFoundException;
import com.purplebits.emrd2.exceptions.RoleDeletionException;
import com.purplebits.emrd2.exceptions.RoleNotFoundException;
import com.purplebits.emrd2.exceptions.RolePermissionNotFoundException;
import com.purplebits.emrd2.repositories.RolePermissionsRepository;
import com.purplebits.emrd2.service.PermissionsService;
import com.purplebits.emrd2.service.RolePermissionsService;
import com.purplebits.emrd2.service.RolesService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.Logger;
@Service
public class RolePermissionsServiceImpl implements RolePermissionsService{
	
	private final Logger logger = LogManager.getLogger(RolePermissionsServiceImpl.class);
	private final String className = RolePermissionsServiceImpl.class.getSimpleName();
	
	@Autowired
	private RolePermissionsRepository rolePermissionsRepository;
	
	@Autowired
    Environment environment;
	@Autowired
	@Lazy
	RolesService  rolesService;
	@Autowired
	PermissionsService permissionsService;
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public RolePermissionsDTO addNewRolePermissions(RolePermissionsDTO rolePermissionsDTO) {
		logger.info("method to add new role permission mapping invoked.");
		logger.debug(className + "addNewRolePermissions()");
		RolesDTO rolesDTO=rolesService.getRoleById(rolePermissionsDTO.getRoles().getRoleId());
		if(rolesDTO==null)
			throw new RoleNotFoundException(environment.getProperty(ResponseMessages.ROLES_NOT_FOUND_ERROR));	
		PermissionsDTO permissionsDTO=permissionsService.getPermissionById(rolePermissionsDTO.getPermissions().getPermissionId());
		if(permissionsDTO==null)
			throw new PermissionNotFoundException(environment.getProperty(ResponseMessages.PERMISSION_NOT_FOUND_ERROR));
		
		Optional<RolePermissions> rolePermissionOp = rolePermissionsRepository.findByPermissionIdAndRoleId(
				rolePermissionsDTO.getPermissions().getPermissionId(), rolePermissionsDTO.getRoles().getRoleId());
		
		RolePermissions rolePermissions = ObjectMapperUtils.map(rolePermissionsDTO, RolePermissions.class);
		if(rolePermissionOp.isPresent()) {
			rolePermissions= rolePermissionOp.get();
			rolePermissions.setStatus(Status.ACTIVE);
			rolePermissions=rolePermissionsRepository.saveAndFlush(rolePermissions);
		}
		else {
			rolePermissions.setStatus(Status.ACTIVE);
			rolePermissions=rolePermissionsRepository.saveAndFlush(rolePermissions);
		}
		
		return ObjectMapperUtils.map(rolePermissions, RolePermissionsDTO.class);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public RolePermissionsDTO updateRolePermissions(RolePermissionsDTO rolePermissionsDTO) {
		logger.info("method to update role permission mapping invoked.");
		logger.debug(className + "updateRolePermissions()"+rolePermissionsDTO);
		
		Optional<RolePermissions> rolePermission = rolePermissionsRepository.findById(rolePermissionsDTO.getRolePermissionId());
		if(!rolePermission.isPresent()) {
			throw new RolePermissionNotFoundException(environment.getProperty(ResponseMessages.ROLE_PERMISSION_NOT_FOUND_ERROR));
		}
		RolePermissions rolePermissions=ObjectMapperUtils.map(rolePermissionsDTO, RolePermissions.class);
		if(rolePermissionsDTO.getStatus().equals(Status.DELETED)) {
			//delete case
			Integer count = rolePermissionsRepository.
					findCountByRoleIdAndStatus(rolePermissionsDTO.getRoles().getRoleId(), Status.ACTIVE);
			if(count==1) {
				Optional<RolePermissions> rolePermissionOp = rolePermissionsRepository.findByRoleIdAndStatus(rolePermissionsDTO.getRoles().getRoleId(),
						 Status.ACTIVE);
				if(rolePermissionOp.isPresent() &&
						rolePermissionOp.get().getRolePermissionId().equals(rolePermissionsDTO.getRolePermissionId()))
					throw new RoleDeletionException(environment.getProperty(ResponseMessages.DELETE_ROLE_PERMISSION_ERROR));
			}
		}
		rolePermissions=rolePermissionsRepository.saveAndFlush(rolePermissions);
		
		return ObjectMapperUtils.map(rolePermissions, RolePermissionsDTO.class);
	}
	@Override
	public RolePermissionsDTO getRolePermissionsById(Integer rolePermissionId) {
		logger.info("method to get RolePermissions By Id invoked.");
		logger.debug(className + "getRolePermissionsById()");
		Optional<RolePermissions> rolePermission = rolePermissionsRepository.findByRolePermissionIdAndStatus(rolePermissionId,Status.ACTIVE);
		if(!rolePermission.isPresent()) {
			throw new RolePermissionNotFoundException(environment.getProperty(ResponseMessages.ROLE_PERMISSION_NOT_FOUND_ERROR));
		}
		return ObjectMapperUtils.map(rolePermission.get(), RolePermissionsDTO.class);
	}
	@Override
	public PaginationResponseDTO<RolePermissionsRequestResponseDTO> getAllRolePermissions(Integer pageNumber,
			Integer pageSize, String sortBy, String sortType) {
		logger.info(environment.getProperty(ResponseMessages.GET_ALL_ROLE_PERMISSIONS_LOG));
    	logger.debug(className + " getAllRolePermissions() invoked");
        Sort sort = (sortType.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        PaginationResponseDTO<RolePermissionsRequestResponseDTO> pageResponse = new PaginationResponseDTO<>();

        Page<RolePermissions> rolePermissionsPage = rolePermissionsRepository.findAllByStatus(Status.ACTIVE, pageable);
        List<RolePermissions> rolePermissions = rolePermissionsPage.getContent();
        List<RolePermissionsDTO> rolePermissionsDTO = ObjectMapperUtils.mapAll(rolePermissions, RolePermissionsDTO.class);

        pageResponse.setData(RolePermissionsRequestResponseDTO.getRolePermissionsRequestResponseDTO(rolePermissionsDTO));

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPage(rolePermissionsPage.getNumber());
        paginationDTO.setLimit(rolePermissionsPage.getSize());
        paginationDTO.setTotalPages(rolePermissionsPage.getTotalPages());
        paginationDTO.setLastPage(rolePermissionsPage.isLast());
        paginationDTO.setTotalCounts(rolePermissionsPage.getTotalElements());

        pageResponse.setPagination(paginationDTO);
        return pageResponse;
		
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public RolePermissionsDTO deleteRolePermissions(Integer rolePermissionId) {
		logger.info("method to delete Role Permissions invoked.");
		logger.debug(className + "deleteRolePermissions()");
		RolePermissions rolePermission = rolePermissionsRepository
	            .findByRolePermissionIdAndStatus(rolePermissionId, Status.ACTIVE)
	            .orElseThrow(() -> 
	                new RolePermissionNotFoundException(
	                    environment.getProperty(ResponseMessages.ROLE_PERMISSION_NOT_FOUND_ERROR)
	                )
	            );
		rolePermission.setStatus(Status.DELETED);
		 RolePermissions updatedRolePermission = rolePermissionsRepository.saveAndFlush(rolePermission);

		
		return ObjectMapperUtils.map(updatedRolePermission, RolePermissionsDTO.class);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void deleteAllRolesByRoleId(Integer roleId) {
		logger.info("method to delete role permissions mapping by role id invoked.");
		logger.debug(className + "deleteAllRolesByRoleId() : ");
		rolePermissionsRepository.deleteAllByRoleId(roleId, Status.DELETED);
	}
	@Override
	public List<RolePermissionsDTO> addOrUpdateRoleWithPermissions(List<RolePermissionsDTO> rolesWithPermissionsDTOs) {
		logger.info("method to add or update role permission mapping invoked.");
		logger.debug(className + " addOrUpdateRoleWithPermissions() ");
		List<RolePermissionsDTO> result= new ArrayList<>();
		List<RolePermissionsDTO> rolePermissionsDTOsToAdd=new ArrayList<>();
		List<RolePermissionsDTO> rolePermissionsDTOsToDelete=new ArrayList<>();
		
		for(RolePermissionsDTO rolePermissionsDTO:rolesWithPermissionsDTOs) {
			if(rolePermissionsDTO.getRolePermissionId()!=null && rolePermissionsDTO.getStatus().equals(Status.DELETED)) {
				rolePermissionsDTOsToDelete.add(rolePermissionsDTO);
			}else if(rolePermissionsDTO.getRolePermissionId()==null){
				rolePermissionsDTOsToAdd.add(rolePermissionsDTO);
			}
		}
		
		for(RolePermissionsDTO rolePermissionsDTO:rolePermissionsDTOsToAdd) {
			RolePermissions rolePermissions=ObjectMapperUtils.map(rolePermissionsDTO, RolePermissions.class);
		
				//add case
				Optional<RolePermissions> rolePermissionOp = rolePermissionsRepository.findByPermissionIdAndRoleId(
						rolePermissionsDTO.getPermissions().getPermissionId(), rolePermissionsDTO.getRoles().getRoleId());
				
				if(rolePermissionOp.isPresent()) {
					rolePermissions= rolePermissionOp.get();
					rolePermissions.setStatus(Status.ACTIVE);
					rolePermissions=rolePermissionsRepository.saveAndFlush(rolePermissions);
				}
				else {
					rolePermissions.setStatus(Status.ACTIVE);
					rolePermissions=rolePermissionsRepository.saveAndFlush(rolePermissions);
				}
				result.add(ObjectMapperUtils.map(rolePermissions, RolePermissionsDTO.class));
			
			
		} 
		
		for(RolePermissionsDTO rolePermissionsDTO:rolePermissionsDTOsToDelete) {
			RolePermissions rolePermissions=ObjectMapperUtils.map(rolePermissionsDTO, RolePermissions.class);
		
			//update case
			Integer count = rolePermissionsRepository.
					findCountByRoleIdAndStatus(rolePermissionsDTO.getRoles().getRoleId(), Status.ACTIVE);
			if(count==1) {
				Optional<RolePermissions> rolePermissionOp = rolePermissionsRepository.findByRoleIdAndStatus(rolePermissionsDTO.getRoles().getRoleId(),
						 Status.ACTIVE);
				if(rolePermissionOp.isPresent() &&
						rolePermissionOp.get().getRolePermissionId().equals(rolePermissionsDTO.getRolePermissionId())
						&& rolePermissionsDTO.getStatus().equals(Status.DELETED))
					throw new RoleDeletionException(environment.getProperty(ResponseMessages.DELETE_ROLE_PERMISSION_ERROR));
			}
			rolePermissions=rolePermissionsRepository.saveAndFlush(rolePermissions);
			result.add(ObjectMapperUtils.map(rolePermissions, RolePermissionsDTO.class));
		
		}
		return result;
	}
	
	@Override
	public List<RolePermissionsDTO> getAllRolePermissionsById(Integer roleId) {
		 logger.info("method to get all role permission mapping by role id invoked.");
		 logger.debug(className + " getAllRolePermissionsById() ");
		List<RolePermissions> rolePermissions = rolePermissionsRepository.findAllByRoleIdAndStatus(roleId, Status.ACTIVE);
		return ObjectMapperUtils.mapAll(rolePermissions, RolePermissionsDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public List<PermissionsDTO> findDistinctPermissionsByRoleIdAndStatus(Integer roleId) {
		logger.info(environment.getProperty("method to get all permission by role id invoked." ));
		 logger.debug(className + " findDistinctPermissionsByRoleIdAndStatus() ");
		List<Permissions> permissions = rolePermissionsRepository.
       findDistinctPermissionsByRoleIdAndStatus(roleId,Status.ACTIVE);
		return ObjectMapperUtils.mapAll(permissions, PermissionsDTO.class);
	}
	
	
	   
}
