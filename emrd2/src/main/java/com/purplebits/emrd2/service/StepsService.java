package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.StepDTO;


public interface StepsService {

	PaginationResponseDTO<StepDTO> getStepsByContractId(int contractId, int pageNumber,int pageSize, String sortBy, String sortType);
}
