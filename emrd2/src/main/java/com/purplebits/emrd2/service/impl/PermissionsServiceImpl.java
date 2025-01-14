package com.purplebits.emrd2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.entity.Permissions;
import com.purplebits.emrd2.exceptions.DuplicateDisplayNameException;
import com.purplebits.emrd2.exceptions.DuplicatePermissionsException;
import com.purplebits.emrd2.exceptions.PermissionNotFoundException;
import com.purplebits.emrd2.repositories.PermissionsRepository;
import com.purplebits.emrd2.service.PermissionsService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class PermissionsServiceImpl implements PermissionsService {
	private final Logger logger = LogManager.getLogger(PermissionsServiceImpl.class);
	private final String className = PermissionsServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private PermissionsRepository permissionsRepository;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public PermissionsDTO addNewPermission(PermissionsDTO permissionsDTO) {
		logger.info("method to add permission invoked.");
		logger.debug(className + " addNewPermission()");
		Optional<Permissions> permissionOp = permissionsRepository
				.findByPermissionNameAndStatus(permissionsDTO.getPermissionName(), Status.ACTIVE);
		if (permissionOp.isPresent()) {
			throw new DuplicatePermissionsException(
					environment.getProperty(ResponseMessages.DUPLICATE_PERMISSIONS_ERROR));
		}

		Optional<Permissions> displayOp = permissionsRepository
				.findByDisplayNameAndStatus(permissionsDTO.getDisplayName(), Status.ACTIVE);

		if (displayOp.isPresent()) {
			throw new DuplicateDisplayNameException(
					environment.getProperty(ResponseMessages.DUPLICATE_DISPLAY_NAME_ERROR));
		}
		permissionsDTO.setStatus(Status.ACTIVE);

		Permissions entity = ObjectMapperUtils.map(permissionsDTO, Permissions.class);
		Integer maxMask = permissionsRepository.findMaxMask();
		int nextMaskValue = (maxMask != null) ? maxMask * 2 : 1;

		// Set the mask value for the new permission
		entity.setMask(nextMaskValue);
		Permissions savedEntity = permissionsRepository.saveAndFlush(entity);
		return ObjectMapperUtils.map(savedEntity, PermissionsDTO.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public List<PermissionsDTO> getAllPermissions() {
		logger.info("method to get all permissions invoked.");
		logger.debug(className + " getAllPermissions()");
		List<Permissions> entities = permissionsRepository.findByStatus(Status.ACTIVE);
		return ObjectMapperUtils.mapAll(entities, PermissionsDTO.class);
	}

	@Override
	public PermissionsDTO getPermissionById(Integer permissionId) {
		logger.info("method to get permission invoked.");
		logger.debug(className + " addNewPermission()");
		Permissions entity = permissionsRepository.findByPermissionIdAndStatus(permissionId, Status.ACTIVE)
				.orElseThrow(() -> new PermissionNotFoundException(
						environment.getProperty(ResponseMessages.PERMISSIONS_NOT_FOUND_ERROR)));
		return ObjectMapperUtils.map(entity, PermissionsDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public PermissionsDTO updatePermission(PermissionsDTO permissionsDTO) {
		logger.info("method to update permission invoked.");
		logger.debug(className + " updatePermission()");

		Optional<Permissions> permissionOp = permissionsRepository
				.findByPermissionIdAndStatus(permissionsDTO.getPermissionId(), Status.ACTIVE);
		if (!permissionOp.isPresent()) {
			throw new PermissionNotFoundException(
					environment.getProperty(ResponseMessages.PERMISSIONS_NOT_FOUND_ERROR));
		}
		Optional<Permissions> displayOp = permissionsRepository
				.findByDisplayNameAndStatus(permissionsDTO.getDisplayName(), Status.ACTIVE);

		if (displayOp.isPresent()) {
			throw new DuplicateDisplayNameException(
					environment.getProperty(ResponseMessages.DUPLICATE_DISPLAY_NAME_ERROR));
		}

		Permissions permissions = permissionOp.get();

		permissions.setDisplayName(permissionsDTO.getDisplayName());
		Permissions updatedEntity = permissionsRepository.saveAndFlush(permissions);
		return ObjectMapperUtils.map(updatedEntity, PermissionsDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public PermissionsDTO deletePermission(Integer permissionId) {
		logger.info("method to delete permission invoked.");
		logger.debug(className + " deletePermission()");
		Permissions entity = permissionsRepository.findByPermissionIdAndStatus(permissionId, Status.ACTIVE)
				.orElseThrow(() -> new PermissionNotFoundException(
						environment.getProperty(ResponseMessages.PERMISSIONS_NOT_FOUND_ERROR)));
		entity.setStatus(Status.DELETED);
		permissionsRepository.saveAndFlush(entity);
		return ObjectMapperUtils.map(entity, PermissionsDTO.class);
	}

	@Override
	public List<PermissionsDTO> findAllByPermissionIdIn(List<Integer> maskIds) {
		logger.info("method to get all permissions based on maskedID invoked.");
		logger.debug(className + " findAllByPermissionIdIn()");

		List<Permissions> permissions = permissionsRepository.findAllByPermissionMaskIn(maskIds);
		return ObjectMapperUtils.mapAll(permissions, PermissionsDTO.class);
	}

	@Override
	public List<PermissionsDTO> getAllPermissionsLesserThanGivenMask(int mask) {
		logger.info("method to get all permissions less than maskId invoked.");
		logger.debug(className + " getAllPermissionsLesserThanGivenMask()");
		List<Permissions> permissions = permissionsRepository.findAllByPermissionMaskLessThan(mask);
		return ObjectMapperUtils.mapAll(permissions, PermissionsDTO.class);
	}

	@Override
	public List<PermissionsDTO> addAllNewPermission(List<PermissionsDTO> permissionsDTOs) {
		logger.info("method to add multiple permissions invoked.");
		logger.debug(className + " addAllNewPermission() ");
		List<PermissionsDTO> permissionDTOs = new ArrayList<>();
		for (PermissionsDTO permissionsDTO : permissionsDTOs) {
			permissionDTOs.add(addNewPermission(permissionsDTO));
		}

		return permissionDTOs;
	}

	@Override
	public List<PermissionsDTO> getPermissionsByPermissionIds(List<Integer> allDistinctPermissionsByRoleIds) {
		logger.info("method to get permission by id invoked.");
		logger.debug(className + " addAllNewPermission() ");
		List<Permissions> permissions = permissionsRepository.findAllByPermissionIdIn(allDistinctPermissionsByRoleIds);
		return ObjectMapperUtils.mapAll(permissions, PermissionsDTO.class);
	}

	@Override
	public PermissionsDTO getPermissionByName(String name) {
		logger.info("method to get permission by name invoked.");
		Optional<Permissions> permissionOp = permissionsRepository.findByPermissionName(name);
		if (!permissionOp.isPresent())
			throw new PermissionNotFoundException(
					environment.getProperty(ResponseMessages.PERMISSIONS_NOT_FOUND_ERROR));
		return ObjectMapperUtils.map(permissionOp.get(), PermissionsDTO.class);
	}

}
