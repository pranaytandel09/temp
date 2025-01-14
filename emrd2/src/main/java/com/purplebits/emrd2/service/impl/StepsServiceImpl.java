package com.purplebits.emrd2.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.StepDTO;
import com.purplebits.emrd2.entity.Step;
import com.purplebits.emrd2.repositories.StepRepository;
import com.purplebits.emrd2.service.StepsService;
import com.purplebits.emrd2.types.DependencyType;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class StepsServiceImpl implements StepsService {

	private final Logger logger = LogManager.getLogger(StepsServiceImpl.class);
	private final String className = StepsServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private StepRepository stepRepository;
	
	@Override
	public PaginationResponseDTO<StepDTO> getStepsByContractId(int contractId, int pageNumber,int pageSize, String sortBy, String sortType) {
		logger.info("method to get step by contract id invoked.");
		logger.debug(className + " getStepsByContractId()");
		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<StepDTO> pageResponse= new PaginationResponseDTO<>();
	    Page<Step> stepPage = stepRepository.findStepsByContractIdAndDependencyType(contractId, DependencyType.Independent,pageable);
	    List<Step> steps = stepPage.getContent();
	    List<StepDTO> stepsDTO = ObjectMapperUtils.mapAll(steps, StepDTO.class);
	    pageResponse.setData(stepsDTO);
	    PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(stepPage.getNumber());
		paginationDTO.setLimit(stepPage.getSize());
		paginationDTO.setTotalPages(stepPage.getTotalPages());
		paginationDTO.setLastPage(stepPage.isLast());
		paginationDTO.setTotalCounts(stepPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
		
	}

}
