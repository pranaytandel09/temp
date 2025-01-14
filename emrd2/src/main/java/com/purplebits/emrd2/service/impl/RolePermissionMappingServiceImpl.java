package com.purplebits.emrd2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.RolePermissionMappingDTO;
import com.purplebits.emrd2.entity.RolePermissionMapping;
import com.purplebits.emrd2.repositories.RolePermissionMappingRepository;
import com.purplebits.emrd2.service.RolePermissionMappingService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Service
public class RolePermissionMappingServiceImpl implements RolePermissionMappingService {

	private final Logger logger = LogManager.getLogger(RolePermissionMappingServiceImpl.class);
	private final String className = RolePermissionMappingServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;
	@Autowired
	private RolePermissionMappingRepository rolePermissionMappingRepository;
	

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public List<RolePermissionMappingDTO> addAllNewRolePermission(List<RolePermissionMappingDTO> rolePermissionsDTOs) {
		 logger.info(environment.getProperty(ResponseMessages.ADD_ALL_NEW_ROLE_PERMISSIONS_LOG));
		logger.debug(className +" addAllNewRolePermission() ");
		
		List<RolePermissionMappingDTO> result= new ArrayList<>();
		for(RolePermissionMappingDTO rolePermissionDTO : rolePermissionsDTOs) {
			
			Optional<RolePermissionMapping> rolePermissionOp = rolePermissionMappingRepository.
			findByRoleIdAndPermissionId(rolePermissionDTO.getRoles().getRoleId(),
					rolePermissionDTO.getPermissions().getPermissionId());
			
			if(rolePermissionOp.isPresent() && rolePermissionOp.get().getStatus().equals(Status.ACTIVE)) {
				continue;
			}
			
			
			if(rolePermissionOp.isPresent() && rolePermissionOp.get().getStatus().equals(Status.DELETED)) {
				rolePermissionDTO.setRolePermissionMappingId(rolePermissionOp.get().getRolePermissionMappingId());
			}
			
			RolePermissionMapping rolePermissionMapping = ObjectMapperUtils.map(rolePermissionDTO, RolePermissionMapping.class);
			rolePermissionMapping.setStatus(Status.ACTIVE);
			rolePermissionMappingRepository.saveAndFlush(rolePermissionMapping);
			result.add(ObjectMapperUtils.map(rolePermissionMapping, RolePermissionMappingDTO.class));
		}
		return result;
	}

	@Override
	public List<Integer> getAllDistinctPermissionsByRoleIds(List<Integer> roleIds) {
		 logger.info(environment.getProperty(ResponseMessages.GET_ALL_DISTINCT_PERMISSIONS_BY_ROLE_IDS_LOG));
		logger.debug(className +" addAllNewRolePermission() ");
		return rolePermissionMappingRepository.getAllDistinctPermissionsByRoleIds(roleIds, Status.ACTIVE);
		
	}
}
