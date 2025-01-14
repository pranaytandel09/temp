package com.purplebits.emrd2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 
 * @(#) Encryptor.java v 0.1 Nov 18, 2010
 * 
 *      Copyright (c) 2010-2012 PurpleBits Infosystems Pvt. Ltd. Vadodara,
 *      Gujarat, India All rights reserved.
 * 
 *      This software is the confidential and proprietary information of
 *      PurpleBits Infosystems Pvt. Ltd. ("Confidential Information"). You shall
 *      not disclose such Confidential Information and shall use it only in
 *      accordance with the terms of the license agreement you entered into with
 *      PurpleBits.
 * 
 *      This class does encryption and decryption of all the passwords, database
 *      and pdf files. For pdf it uses itext library. For database and
 *      passwords, it uses jasypt library.
 * 
 * @author Deepak Gupta
 */
public class Encryptor {

	private final static Logger logger = LogManager.getLogger(Encryptor.class.getName());

	private static final String PASSWORD_ENCRYPTION_KEY = "purpl3Plut0"; // Password
	// encryption
	// key
	private static final String OWNER_PDF_ENCRYTPION_KEY_SUFFIX = "Opdf123"; // Suffix
	// for pdf's owner encryption key

	private static final int PDF_ENCRYPTOPN_KEY_LENGTH = 256; // PDF Encryption
																// key length
	
	
	// Define the length of the encryption key.
	// Possible values are 40 or 128 (256 will be available in PDFBox 2.0).

	/**
	 * 
	 * @param src      - src file (complete path)
	 * @param dest     - dest file (complete path)
	 * @param password - password (project password in decrypted format)
	 * @return true if decryption successful, false otherwise
	 */
	public boolean decryptPdf(String src, String dest, String password) {

		logger.debug("Encryptor.decryptPDF src =  " + src + ", dest = " + dest);

		String oPasswd = password + OWNER_PDF_ENCRYTPION_KEY_SUFFIX;
		PDDocument reader = null;
		Boolean fileDecrypted = false;
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(src);
			reader = Loader.loadPDF(new File(src), oPasswd);

			reader.setAllSecurityToBeRemoved(true);
			reader.save(dest);
			reader.close();
			fileDecrypted = true;
		} catch (FileNotFoundException e) {
			// If dest file is not found then let's create the entire path first
			// and
			// retry it.
			logger.error("Encryptor.decryptPDF File Not Found", e.getMessage());
			File destFile = new File(dest);
			destFile = new File(destFile.getParent());
			destFile.mkdirs();
			try {
				inputStream = new FileInputStream(src);
				reader = Loader.loadPDF(new File(src), oPasswd);
				reader.setAllSecurityToBeRemoved(true);
				reader.save(dest);
				inputStream.close();
				fileDecrypted = true;
			} catch (IOException ioe) {
				logger.error("Encryptor.decryptPDF ERROR while decrypting in FileNotFound block", ioe.getMessage());
			}
		} catch (IOException ioe) {
			logger.error("Encryptor.decryptPDF IOException ERROR while decrypting", ioe.getMessage());
		} catch (Exception e) {
			logger.error("Encryptor.decryptPDF ERROR while decrypting", e.getMessage());
		} finally {
			try {
				if(reader!=null) {
					reader.close();
				}
				
			} catch (IOException e) {
				logger.error("Encryptor.decryptPDF IOException occurred decrypting File " + e.getMessage());
			} catch (NullPointerException npe) {
				// Added exception when corrupted file is loaded.
				logger.error("Encryptor.decryptPDF Null Pointer Exception occurred while decrypting File " + npe.getMessage());
			}
		}
		return fileDecrypted;
	}

	/**
	 * 
	 * @param src      - src file (complete path)
	 * @param dest     - dest file (complete path)
	 * @param password - password (project password in decrypted format)
	 * @return true if decryption successful, false otherwise
	 */

	// Encrypt PDF Using PDFBox library
	public boolean encryptPdf(String src, String dest, String oPassword, boolean printAllowed) {

		logger.debug("Encryptor.encryptPDF src  = " + src + ", dest = " + dest);

		String oPasswd = oPassword + OWNER_PDF_ENCRYTPION_KEY_SUFFIX;
		String uPasswd = oPassword;
		PDDocument doc = null;
		boolean fileEncrypted = false;
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(src);
			doc = Loader.loadPDF(new File(src));

			AccessPermission ap = new AccessPermission();

			if (printAllowed) {
				ap.setCanPrint(true);
			} else {
				ap.setCanPrint(false);
			}

			// owner password (to open the file with all permissions) is "12345"
			// user password (to open the file but with restricted permissions,
			// is empty here)
			StandardProtectionPolicy spp = new StandardProtectionPolicy(oPasswd, uPasswd, ap);
			spp.setEncryptionKeyLength(PDF_ENCRYPTOPN_KEY_LENGTH);
			spp.setPermissions(ap);
			doc.protect(spp);
			doc.save(dest);
			doc.close();
			inputStream.close();
			logger.debug("Encryptor.encryptPDF  Encrypted PDF created Successfully.");
			// Check whether the file is encrypted or not.
			/*
			 * Trying to open encrypted pdf, if it is not encrypted , it open successfully
			 * without providing password. Else it will throw IOExcpetion and encryptedFile
			 * will be null.
			 */
			PDDocument encryptedFile = null;
			try {
				inputStream = new FileInputStream(dest);
				encryptedFile = Loader.loadPDF(new File(dest));
			} catch (FileNotFoundException e) {
				logger.error("Encryptor.encryptPDF "
						+ " Encrypted copy of Record is not available in File Structure.\n Error : " + e.getMessage());
			} catch (IOException e) {
				logger.error("Encryptor.encryptPDF  IOException while loading pdf " + dest + " file.\n Error : "
						+ e.getMessage());
			} catch (Exception e) {
				logger.error("Encryptor.encryptPDF " + " Generic Exception During Encryption check of pdf " + dest
						+ " record.\n Error : " + e.getMessage());
			} finally {
				if (encryptedFile == null && inputStream != null) {
					try {
						// Check whether file is completely saved successfully
						// or not after encryption.
						// If we will not able to close the file so there is
						// some issue while loading and saving encrypted file.
						inputStream.close();
						logger.debug("Encryptor.encryptPDF  Record is encrypted.");
						fileEncrypted = true;
					} catch (IOException e) {
						logger.error(
								"Encryptor.encryptPDF " + " Error while closing stream.\n Error : " + e.getMessage());
					}
				} else if (encryptedFile != null) {
					try {
						inputStream.close();
						encryptedFile.close();
						logger.info("Encryptor.encryptPDF  Record is not encrypted.");
					} catch (IOException e) {
						logger.error("Encryptor.encryptPDF " + " Error while closing input stream or pdf " + dest
								+ " .\n Error : " + e.getMessage());
					}
				} else if (inputStream == null) {
					logger.error("Encryptor.encryptPDF  Error while readig file " + dest + " for verifaction.\n.");
				}
			}
		} catch (IOException ioe) {
			logger.error("Encryptor.encryptPDF ERROR IOException {} ", ioe.getMessage());
		} catch (Exception e) {
			logger.error("Encryptor.encryptPDF ERROR IOException {} ", e.getMessage());
		}

		return fileEncrypted;

	}

	// This method is not used right now. Will be used once the roles stuff is
	// updated.
	public boolean encryptPdf(String src, String dest, String oPassword, String uPassword) {

		logger.debug("Encryptor.encryptPDF src = " + src + " dest = " + dest);

		String oPasswd = oPassword + OWNER_PDF_ENCRYTPION_KEY_SUFFIX;
		String uPasswd = uPassword;
		PDDocument doc = null;
		Boolean fileDecrypted = false;

		try {
			doc = Loader.loadPDF(new File(src));

			AccessPermission ap = new AccessPermission();
			ap.setCanPrint(true);

			// owner password (to open the file with all permissions) is "12345"
			// user password (to open the file but with restricted permissions,
			// is empty here)
			StandardProtectionPolicy spp = new StandardProtectionPolicy(oPasswd, uPasswd, ap);
			spp.setEncryptionKeyLength(PDF_ENCRYPTOPN_KEY_LENGTH);
			spp.setPermissions(ap);
			doc.protect(spp);
			doc.save(dest);
			fileDecrypted = true;
		} catch (IOException ioe) {
			logger.error("Encryptor.encryptPDF ERROR IOException ", ioe.getMessage());
		} catch (Exception e) {
			logger.error("Encryptor.encryptPDF ERROR IOException ", e.getMessage());
		} catch (OutOfMemoryError outMemory) {
			logger.error("Encryptor.encryptPDF ERROR OutOfMemoryError", outMemory.getMessage());
		} finally {
			try {
				if(doc!=null) {
					doc.close();
				}
				
			} catch (IOException e) {
				logger.error("Encryptor.encryptPDF Closing PDF when error occurred while encrypting File " + e.getMessage());
			} catch (NullPointerException npe) {
				// Added exception when corrupted file is loaded.
				logger.error("Encryptor.encryptPDF Null Pointer Exception occurred while encrypting File " + npe.getMessage());
			}
		}
		return fileDecrypted;

	}
	

	/**
	 * Used to encrypt the password using jasypt
	 * 
	 * @param txtPasswd - password that needs to be encrypted
	 * @return encrypted password
	 */
	public String encryptPassword(String txtPasswd) {

		try {
			logger.debug("Encryptor.encryptPassword");

			StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
			textEncryptor.setPassword(PASSWORD_ENCRYPTION_KEY);
			return textEncryptor.encrypt(txtPasswd);
		} catch (Exception e) {
			logger.error("Encryptor.encryptPassword ", e.getMessage());
			return null;
		}

	}


	/**
	 * Used to decrypts the existing encrypted password using jasypt
	 * 
	 * @param encryptedPasswd - password in encrypted form
	 * @return normal string password
	 */
	public String decryptPassword(String encryptedPasswd) {

		try {
			logger.trace("Encryptor.decryptPassword");

			StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
			textEncryptor.setPassword(PASSWORD_ENCRYPTION_KEY);
			return textEncryptor.decrypt(encryptedPasswd);
		} catch (Exception e) {
			logger.error("Encryptor.decryptPassword ", e.getMessage());
			return null;
		}
	}

	public String generatePassword() {
		String value = null;
		char[] chars = new char[5];
		int i = Math.abs((int) Double.doubleToLongBits(Math.random() * 10000000));
		chars[0] = (char) (((i) % 26) + 97);
		i = Math.abs((int) Double.doubleToLongBits(Math.random() * 10000000));
		chars[1] = (char) (((i) % 26) + 97);
		i = Math.abs((int) Double.doubleToLongBits(Math.random() * 10000000));
		chars[2] = (char) (((i) % 26) + 97);
		i = Math.abs((int) Double.doubleToLongBits(Math.random() * 10000000));
		chars[3] = (char) (((i) % 26) + 97);
		i = Math.abs((int) Double.doubleToLongBits(Math.random() * 10000000));
		chars[4] = (char) (((i) % 26) + 97);
		i = Math.abs((int) Double.doubleToLongBits(Math.random() * 10000000));
		value = new String(chars) + Integer.toString(((i) % 100000));
		return value;
	}
	
	public boolean encryptFile(String src, String dest, String password) {
	    boolean fileEncrypted = false;
	    try (FileInputStream fis = new FileInputStream(src);
	         FileOutputStream fos = new FileOutputStream(dest)) {
	    	
	    	// Determine file size
	        File srcFile = new File(src);
	        long fileSize = srcFile.length();
	        // Adjust buffer size based on file size
	        int bufferSize;
	        if (fileSize <= 1L * 1024 * 1024 * 1024) { // <= 1GB
	            bufferSize = 512 * 1024; // 512KB
	        } else { // > 1GB
	            bufferSize = 1024 * 1024; // 1MB
	        }

	        // Create a secure AES key using the password
	        byte[] key = generateKeyFromPassword(password);

	        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	        
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

	        // Read source file and encrypt it
	        byte[] inputBuffer = new byte[bufferSize];  // Larger buffer size for better performance, have to define it in property file
	        int bytesRead;
	        while ((bytesRead = fis.read(inputBuffer)) != -1) {
	            byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
	            if (outputBuffer != null) {
	                fos.write(outputBuffer);
	            }
	        }
	        
	        byte[] outputBytes = cipher.doFinal();
	        if (outputBytes != null) {
	            fos.write(outputBytes);
	        }

	        fileEncrypted = true;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return fileEncrypted;
	}

//	public boolean decryptFile(String src, String dest, String password) {
//	    boolean fileDecrypted = false;
//	    try (FileInputStream fis = new FileInputStream(src)) {
//	        // Ensure the destination directory exists
//	        File destFile = new File(dest);
//	        File parentDir = destFile.getParentFile();
//	        if (parentDir != null && !parentDir.exists()) {
//	            if (!parentDir.mkdirs()) {
//	                System.err.println("Failed to create destination directories.");
//	                return false;
//	            }
//	        }
//
//	        try (FileOutputStream fos = new FileOutputStream(dest)) {
//	            // Create a secure AES key using the password
//	            byte[] key = generateKeyFromPassword(password);
//
//	            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
//	            Cipher cipher = Cipher.getInstance("AES");
//	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//
//	            // Read source file and decrypt it
//	            byte[] inputBuffer = new byte[262144];  // Larger buffer size for better performance
//	            int bytesRead;
//	            while ((bytesRead = fis.read(inputBuffer)) != -1) {
//	                byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
//	                if (outputBuffer != null) {
//	                    fos.write(outputBuffer);
//	                }
//	            }
//
//	            byte[] outputBytes = cipher.doFinal();
//	            if (outputBytes != null) {
//	                fos.write(outputBytes);
//	            }
//
//	            fileDecrypted = true;
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return fileDecrypted;
//	}

	public boolean decryptFile(String src, String dest, String password) {
	    boolean fileDecrypted = false;
	    try (FileInputStream fis = new FileInputStream(src)) {
	    	
	        // Determine file size
	        File srcFile = new File(src);
	        long fileSize = srcFile.length();
	        // Adjust buffer size based on file size
	        int bufferSize;
	        if (fileSize <= 1L * 1024 * 1024 * 1024) { // <= 1GB
	            bufferSize = 512 * 1024; // 512KB
	        } else { // > 1GB
	            bufferSize = 1024 * 1024; // 1MB
	        }

	        // Ensure the destination directory exists
	        File destFile = new File(dest);
	        File parentDir = destFile.getParentFile();
	        if (parentDir != null && !parentDir.exists()) {
	            if (!parentDir.mkdirs()) {
	                System.err.println("Failed to create destination directories.");
	                return false;
	            }
	        }

	        try (FileOutputStream fos = new FileOutputStream(dest)) {
	            // Create a secure AES key using the password
	            byte[] key = generateKeyFromPassword(password);

	            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	            Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);

	            // Read source file and decrypt it using the dynamically determined buffer size
	            byte[] inputBuffer = new byte[bufferSize];
	            int bytesRead;
	            while ((bytesRead = fis.read(inputBuffer)) != -1) {
	                byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
	                if (outputBuffer != null) {
	                    fos.write(outputBuffer);
	                }
	            }

	            byte[] outputBytes = cipher.doFinal();
	            if (outputBytes != null) {
	                fos.write(outputBytes);
	            }

	            fileDecrypted = true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return fileDecrypted;
	}


	private byte[] generateKeyFromPassword(String password) throws Exception {
	    MessageDigest sha = MessageDigest.getInstance("SHA-256");
	    byte[] key = sha.digest(password.getBytes("UTF-8"));
	    byte[] aesKey = new byte[16];
	    System.arraycopy(key, 0, aesKey, 0, aesKey.length);
	    return aesKey;
	}

	private String bytesToHex(byte[] bytes) {
	    StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02x", b));
	    }
	    return sb.toString();
	}


}
