package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.AclSidDTO;

public interface AclSidService {

	AclSidDTO findBySid(String roleName);

	AclSidDTO addNewPrincipleObject(AclSidDTO aclSid);

}
