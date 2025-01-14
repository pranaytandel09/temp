package com.purplebits.emrd2.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Component;

import com.purplebits.emrd2.entity.Permissions;
import com.purplebits.emrd2.repositories.PermissionsRepository;
import com.purplebits.emrd2.util.CustomPermission;


@Component
public class CustomPermissionFactory implements PermissionFactory {

    private final PermissionsRepository permissionsRepository;

    // Constructor-based dependency injection
    public CustomPermissionFactory(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public Permission buildFromMask(int mask) {
        // Fetch the permission based on the mask from the database
        Optional<Permissions> permissionOpt = permissionsRepository.findByMask(mask);
        if (permissionOpt.isPresent()) {
            Permissions permission = permissionOpt.get();
            return new CustomPermission(permission.getMask(), permission.getPermissionName());
        }
        // If no matching permission found, return a basic Permission with the mask only
        return new CustomPermission(mask, "UNKNOWN");
    }

	@Override
	public Permission buildFromName(String name) {
		
		Optional<Permissions> permissionOpt = permissionsRepository.findByPermissionName(name);
        if (permissionOpt.isPresent()) {
            Permissions permission = permissionOpt.get();
           
            return new CustomPermission(permission.getMask(), permission.getPermissionName());
        }
        throw new IllegalArgumentException("Permission with name " + name + " not found");
    
	}

	@Override
	public List<Permission> buildFromNames(List<String> names) {
		List<Permission> permissions = new ArrayList<>();
        for (String name : names) {
            permissions.add(buildFromName(name));
        }
        return permissions;
	}
}
