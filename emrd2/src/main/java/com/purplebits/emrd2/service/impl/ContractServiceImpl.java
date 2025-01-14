package com.purplebits.emrd2.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.ContractDTO;
import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.request_response.ContractRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.ProjectsRequestResponseDTO;
import com.purplebits.emrd2.entity.Contract;
import com.purplebits.emrd2.entity.Project;
import com.purplebits.emrd2.exceptions.ContractNotFoundException;
import com.purplebits.emrd2.exceptions.ProjectNameAlreadyExistException;
import com.purplebits.emrd2.exceptions.ProjectNotFoundException;
import com.purplebits.emrd2.repositories.ContractRepository;
import com.purplebits.emrd2.service.ContractService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class ContractServiceImpl implements ContractService {

	private final Logger logger = LogManager.getLogger(ContractServiceImpl.class);
	private final String className = ContractServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private ContractRepository contractRepository;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public ContractDTO addNewContract(ContractDTO contractDTO) {
		logger.debug(className + " addNewContract()");
		
		
		Contract contract = ObjectMapperUtils.map(contractDTO, Contract.class);
		contract.setStatus(Status.ACTIVE);
		contract=contractRepository.saveAndFlush(contract);
		return ObjectMapperUtils.map(contract, ContractDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public ContractDTO updateContract(ContractDTO contractDTO) {
		logger.debug(className + " updateContract()");
		
		Optional<Contract> contractOp = contractRepository.findById(contractDTO.getContractId());
		if(!contractOp.isPresent())throw new ContractNotFoundException(environment.getProperty(ResponseMessages.CONTRACT_NOT_EXIST));
		
		Contract contract = ObjectMapperUtils.map(contractDTO, Contract.class);
		contract=contractRepository.saveAndFlush(contract);
		return ObjectMapperUtils.map(contract, ContractDTO.class);
	}

	@Override
	public ContractDTO deleteContract(ContractDTO contractDTO) {
		logger.debug(className + " deleteContract()");
		
		Optional<Contract> contractOp = contractRepository.findById(contractDTO.getContractId());
		if(!contractOp.isPresent())throw new ContractNotFoundException(environment.getProperty(ResponseMessages.CONTRACT_NOT_EXIST));
		contractRepository.deleteById(contractDTO.getContractId());
		return contractDTO;
	}

	@Override
	public ContractDTO getContractById(Integer contractId) {
		logger.info("method to get contract by id invoked." + contractId);
		logger.debug(className + " getContractById()");
		
		Optional<Contract> contractOp = contractRepository.findById(contractId);
		if(!contractOp.isPresent())throw new ContractNotFoundException(environment.getProperty(ResponseMessages.CONTRACT_NOT_EXIST));
		
		return ObjectMapperUtils.map(contractOp.get(), ContractDTO.class);
	}

	@Override
	public PaginationResponseDTO<ContractRequestResponseDTO> getAllContract(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType) {
		logger.info("method to get all contracts invoked.");
		logger.debug(className + " getAllContract()");
		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<ContractRequestResponseDTO> pageResponse= new PaginationResponseDTO<>();
	    
		Page<Contract> contractPage = contractRepository.findAll(pageable);
		List<Contract> contract = contractPage.getContent();
	    List<ContractDTO> contractDTO = ObjectMapperUtils.mapAll(contract, ContractDTO.class);
	    pageResponse.setData(ContractRequestResponseDTO.getContractRequestResponseDTO(contractDTO));
	    PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(contractPage.getNumber());
		paginationDTO.setLimit(contractPage.getSize());
		paginationDTO.setTotalPages(contractPage.getTotalPages());
		paginationDTO.setLastPage(contractPage.isLast());
		paginationDTO.setTotalCounts(contractPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
	}

	@Override
	public PaginationResponseDTO<ContractRequestResponseDTO> getAllContractsByProjectId(Integer pageNumber,
			Integer pageSize, String sortBy, String sortType, Integer projectId) {
		logger.info("method to get all contracts by projectId invoked.");
		logger.debug(className + " getAllContractsByProjectId()");
		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<ContractRequestResponseDTO> pageResponse= new PaginationResponseDTO<>();
	    
		Page<Contract> contractPage = contractRepository.findAllByProjectId(projectId,pageable);
		List<Contract> contract = contractPage.getContent();
	    List<ContractDTO> contractDTO = ObjectMapperUtils.mapAll(contract, ContractDTO.class);
	    pageResponse.setData(ContractRequestResponseDTO.getContractRequestResponseDTO(contractDTO));
	    PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(contractPage.getNumber());
		paginationDTO.setLimit(contractPage.getSize());
		paginationDTO.setTotalPages(contractPage.getTotalPages());
		paginationDTO.setLastPage(contractPage.isLast());
		paginationDTO.setTotalCounts(contractPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
	}
}
