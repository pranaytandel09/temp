package com.purplebits.emrd2.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.service.EntitiesService;
import com.purplebits.emrd2.service.ProjectService;
import com.purplebits.emrd2.service.RecordsEncryptionService;
import com.purplebits.emrd2.service.RecordsService;
import com.purplebits.emrd2.util.Encryptor;

@Service
public class RecordsEncryptionServiceImpl implements RecordsEncryptionService {
	
	@Autowired
	private Environment environment;
	@Autowired
	private EntitiesService entitiesService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private RecordsService recordsService;
	
	@Value("${encryptionFolder}")
	private String encrptionHomeDir;
	
	@Value("${encryptionTries}")
	private int encryptionTries;
	
	@Value("${uploadFolder}")
	private String uploadFolder;

	private final Logger logger = LogManager.getLogger(RecordsEncryptionServiceImpl.class);
	private final String className = RecordsEncryptionServiceImpl.class.getSimpleName();
	
	private static String entityName;
	private static String projectId;
	private static String projectName;
	private String destPdfName;
	
	@Override
	public void performeEncryption(List<RecordDTO> recordsToBeEncrpted) {
		EntitiesDTO currEntity = null;
		ProjectDTO currProj = null;
		String pdfPassword = new String();
		String decryptedProjPass = new String();
		File file = null;
		File fileCopied = null;
		File encryptionFolder = null;
		String applicationPDF = "application/pdf";
		String pdfExtension = "pdf";
		String filePathOnServer;
		
		Encryptor encryptor = new Encryptor();
		// Read record list one by one and do encryption according to type
		// of file.
		for (RecordDTO RecordDTO : recordsToBeEncrpted) {

			filePathOnServer = uploadFolder + File.separator + RecordDTO.getUploadLocation().trim();
			logger.debug(className + ".encryptFilesWithoutBookmark File URI read to be encrypted  :{}", filePathOnServer);

			// Extract Bar code, as the record Id is same as bar code
			String barcode = RecordDTO.getRecordId().trim();
			String batchId = barcode.substring(0, barcode.indexOf('D'));

			logger.debug(className + ".encryptFilesWithoutBookmark Barcode and BatchId extracted:{},{}", barcode, batchId);

			// extract the Eid and Pid from the barcode
			String eId = barcode.substring(1, barcode.indexOf('P'));
			String pId = barcode.substring(barcode.indexOf('P') + 1, barcode.indexOf('B'));
			logger.debug(className + ".encryptFilesWithoutBookmark entity ID and project Id extracted:{},{}", eId, pId);

			// Create for Encryption Folder
			String folderStructure = eId + File.separator + pId + File.separator + batchId;
			String encryptionDir = encrptionHomeDir +File.separator + folderStructure;

			logger.debug(className + ".encryptFilesWithoutBookmark Encrption directory: {}", encryptionDir);

			// Fetch the project details needed for encryption
			// If its a new project, find the
			// location again, else skip this as
			// all files belonging to same
			// project shall
			if (!pId.equals(projectId)) {
				projectId = pId;
				currProj = projectService.getProjectById(Integer.parseInt(pId));
				currEntity = entitiesService.getEntityById(Integer.parseInt(eId));
				pdfPassword = currProj.getProjectPassword();

				decryptedProjPass = encryptor.decryptPassword(pdfPassword);
				logger.debug(className + ".encryptFilesWithoutBookmark  Project and project password: {}, {}",
						currProj.getProjectDisplayName(), pdfPassword);
			}

			// Create the folder structure where the encrypted file may
			// reside
			encryptionFolder = new File(encryptionDir);
			if (!encryptionFolder.exists()) {
				logger.debug(className + ".encryptFilesWithoutBookmark Encrption directory: {} needs to be created",
						encryptionDir);
				encryptionFolder.mkdirs();
				logger.debug(className + ".encryptFilesWithoutBookmark  Created encrption folders: {}",
						encryptionFolder.toString());
			}

			String fileType = RecordDTO.getFileType();
			String fileLine;
			boolean fileEncrypted = false;
			/*
			 * Check whether file is pdf or not, if its type is pdf then it will
			 * encrypt the file else it will copy file to the encryption folder
			 * structure.
			 */
			File fileOnServer = new File(filePathOnServer);
			if (fileOnServer.exists()) {
				if (applicationPDF.equalsIgnoreCase(fileType) || pdfExtension.equalsIgnoreCase(fileType)) {
					// copy the file in temp and encrypt file
//					destPdfName = barcode + "_e2.pdf";
					destPdfName = barcode + "_e.pdf";
					logger.debug(className + ".encryptFilesWithoutBookmark  file name after encryption: {}", destPdfName);

					int maxTries = encryptionTries;
					int attempt = 0;
					logger.debug(className + ".encryptFilesWithoutBookmark pdf file on server to be encrypted "
							+ filePathOnServer);
					// For files which are not configured for OCR
					// functionality.

					do {
						fileEncrypted = encryptor.encryptPdf(filePathOnServer,
								encryptionDir + File.separator + destPdfName, decryptedProjPass, true);
//						fileEncrypted = encryptor.encryptFile(filePathOnServer,
//								encryptionDir + File.separator + destPdfName, decryptedProjPass);
						attempt++;
					} while (attempt < maxTries && !fileEncrypted);
					fileLine = folderStructure + File.separator + destPdfName;
				} else {
					// We need to copy files to encryption folder for uploading
					// files on Google Cloud.
					logger.info(className + ".encryptFilesWithoutBookmark skipping for Record {}, with File Type {}",
							RecordDTO.getRecordId(), fileType);
					file = new File(filePathOnServer);
					String fileName = filePathOnServer.substring(filePathOnServer.lastIndexOf(File.separator) + 1);
					fileCopied = new File(encryptionDir + File.separator + fileName);
					fileLine = folderStructure + File.separator + fileName;
					logger.debug(className + ".encryptFilesWithoutBookmark others file to be copied to " + encrptionHomeDir
							+ " with File Location " + fileLine);
					fileEncrypted = encryptor.encryptFile(filePathOnServer,
							encryptionDir + File.separator + fileName, decryptedProjPass);
//					try {
////						FileUtils.copyFile(file, fileCopied);
//						
//						java.nio.file.Files.copy(file.toPath(), fileCopied.toPath(), StandardCopyOption.REPLACE_EXISTING);
//						fileEncrypted = true;
//						logger.debug(className + ".encryptFilesWithoutBookmark file copied successfully");
//					} catch (IOException e) {
//						logger.debug(className + ".encryptFilesWithoutBookmark " + filePathOnServer + " doesn't exist");
//						fileEncrypted = false;
//					}
				}

				logger.info(className + ".encryptFilesWithoutBookmark  encrypted file {} to {} ", barcode,
						encryptionDir + File.separator + destPdfName);
				// If encrypted successfully update status ENCRYPTION_DONE in db.
				if (fileEncrypted) {
					RecordDTO.setUploadLocation(fileLine);
					RecordDTO.setPhysicalStatus(1);
					// update record status and file path in database.
					recordsService.updateRecord(RecordDTO);
					logger.debug(className + ".createEncryptionQueue recId : {} status updated in database.",
							RecordDTO.getRecordId());
					logger.debug(className + ".encryptFilesWithoutBookmark record path updated in database :{} ", fileLine);
					
					//doFtpFile(RecordDTO);
				}
			} else {
				logger.error(className + ".createEncryptionQueue File Not Found for Encryption");
				//logic to mark file as encryption failure or creating  a queue for fail encryption
			}
		}
	}

	// Check of ftp and update the status of record
//	public void doFtpFile(RecordDTO RecordDTO){
//		if(isFTPRequired) {
//			FTPData ftpData = new FTPData();
//			ftpData.ftpContent(RecordDTO);
//		} else {
//			RecordDTO.setUploadStatus(RecordDTOState.FTP_DONE.toString());
//            daoFactory.getSyncDAO().updateRecordDTO(RecordDTO);
//		}
//	}
	
//	public void sendEmail(String exceptionSub, String mailBody) {
//
//		logger.debug(className + ".sendEmail with exception subject {} and mailBody {}", exceptionSub, mailBody);
//		final EmailDAO emailDao = daoFactory.getEmailDAO();
//
//		CredentialsDAO credentialsDao = DAOFactory.getDefaultFactory().getCredentialsDAO();
//		Credentials credentials = credentialsDao.getCredentialDetail("exception.to");
//		String supportEmailId = credentials.getUserName();
//		credentials = credentialsDao.getCredentialDetail("exception.cc");
//		String exceptionCcIDs = credentials.getUserName();
//
//		Email email = new Email();
//		email.setToEmailId(supportEmailId);
//		email.setCcEmailId(exceptionCcIDs);
//		email.setEmailType("SERVER_EXCEPTION");
//		email.setStatus("UNSENT");
//		email.setSubject(exceptionSub);
//		email.setMailBody(mailBody);
//		emailDao.addEmailToBeSent(email);
//		logger.debug(className + ".sendEmail added successfully.");
//	}
}
