package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.AclClassDTO;

public interface AclClassService {

	AclClassDTO findByClassName(String name);

	AclClassDTO addNewAclClass(AclClassDTO aclClassDTO);

}
