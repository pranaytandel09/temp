package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.RecordDTO;

public interface RecordsEncryptionService {

	void performeEncryption(List<RecordDTO> recordsToBeEncrpted);

}
