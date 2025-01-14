package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.ContractDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.request_response.ContractRequestResponseDTO;

public interface ContractService {

	ContractDTO addNewContract(ContractDTO contractDTO);
	ContractDTO updateContract(ContractDTO contractDTO);
	ContractDTO deleteContract(ContractDTO contractDTO);
	ContractDTO getContractById(Integer contractId);
	PaginationResponseDTO<ContractRequestResponseDTO> getAllContract(Integer pageNumber, Integer pageSize, String sortBy, String sortType);
	PaginationResponseDTO<ContractRequestResponseDTO> getAllContractsByProjectId(Integer pageNumber, Integer pageSize, String sortBy, String sortType,Integer projectId);
}
