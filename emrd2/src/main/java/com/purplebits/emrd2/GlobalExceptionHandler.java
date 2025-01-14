package com.purplebits.emrd2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import com.purplebits.emrd2.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.util.ResponseMessages;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	Environment environment;
	@Value("${support.purpledocs.email}")
	private String purpleDocsMail;
	@Value("${support.unm.email}")
	private String unmMail;

	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
	private final String className = GlobalExceptionHandler.class.getSimpleName();

	// Handle BadCredentialsException
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Response<Object>> handleBadCredentialsException(BadCredentialsException ex,
			WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.BAD_CREDENTIALS_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.UNAUTHORIZED);
		response.setMessage(environment.getProperty(ResponseMessages.TOKEN_BLACKLISTED_ERROR));
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
//pattern not found digital collection
	@ExceptionHandler(PatternNotFoundException.class)
	public ResponseEntity<Response<Object>> handlePatternNotFoundException(PatternNotFoundException ex, WebRequest request) {
//		logger.error("PATTERN_NOT_FOUND: " + ex.getMessage(), ex);
		logger.info(ex.getMessage());
		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.NOT_FOUND); // Assuming you have a code for not found errors
		response.setMessage("The requested file pattern was not found.");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(InvalidFileException.class)
	public ResponseEntity<Response<Object>> handleInvalidFileException(InvalidFileException ex, WebRequest request) {
//		logger.error("INVALID_FILE_EXCEPTION: " + ex.getMessage(), ex);
		logger.info(ex.getMessage());

		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.BAD_REQUEST); // Replace with the appropriate code
		response.setMessage("The uploaded file is invalid. " + ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FileNameNotMatchException.class)
	public ResponseEntity<Response<Object>> handleFileNameNotMatchException(FileNameNotMatchException ex, WebRequest request) {
//		logger.error("FILE_NAME_NOT_MATCH_EXCEPTION: " + ex.getMessage(), ex);
		logger.info(ex.getMessage());

		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.BAD_REQUEST); // Replace with the appropriate code
		response.setMessage("The file name does not match the required pattern. " + ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnsupportedDelimiterException.class)
	public ResponseEntity<Response<Object>> handleUnsupportedDelimiterException(UnsupportedDelimiterException ex, WebRequest request) {
//		logger.error("UNSUPPORTED_DELIMITER_EXCEPTION: " + ex.getMessage(), ex);
		logger.info(ex.getMessage());

		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.BAD_REQUEST); // Replace with the appropriate code
		response.setMessage("The file contains an unsupported delimiter. " + ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FileSavingException.class)
	public ResponseEntity<Response<Object>> handleFileSavingException(FileSavingException ex, WebRequest request) {
//		logger.error("FILE_SAVING_EXCEPTION: " + ex.getMessage(), ex);
		logger.info(ex.getMessage());

		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.INTERNAL_SERVER_ERROR);
		response.setMessage("An error occurred while saving the file. " + ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			WebRequest request) {
		logger.error(
				className + environment.getProperty(ResponseMessages.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_LOG_MESSAGE),
				ex);

		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.BAD_REQUEST);
		response.setMessage(environment.getProperty(ResponseMessages.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Response<Object>> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		logger.error(
				className + environment.getProperty(ResponseMessages.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_LOG_MESSAGE),
				ex);

		Response<Object> response = new Response<>();
		response.setCode(ResponseCode.BAD_REQUEST);
		response.setMessage(environment.getProperty(ResponseMessages.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Response<Object>> handleNullPointerException(NullPointerException ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.NULL_POINTER_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String[] mails = { purpleDocsMail, unmMail };
		response.setResult(mails);

		response.setMessage(environment.getProperty(ResponseMessages.NULL_POINTER_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<Response<Object>> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.NUMBER_FORMAT_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(environment.getProperty(ResponseMessages.NUMBER_FORMAT_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Response<Object>> handleSQLException(SQLException ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.SQL_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String[] mails = { purpleDocsMail, unmMail };
		response.setResult(mails);
		response.setMessage(environment.getProperty(ResponseMessages.SQL_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Response<Object>> handleIllegalArgumentException(IllegalArgumentException ex,
			WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.ILLEGAL_ARGUMENT_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(environment.getProperty(ResponseMessages.ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InputMismatchException.class)
	public ResponseEntity<Response<Object>> handleInputMismatchException(InputMismatchException ex,
			WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.INPUT_MISMATCH_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(environment.getProperty(ResponseMessages.INPUT_MISMATCH_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Response<Object>> handleNoSuchElementException(NoSuchElementException ex,
			WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.NO_SUCH_ELEMENT_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(environment.getProperty(ResponseMessages.NO_SUCH_ELEMENT_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	// common Exeption related to files and folder operations

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<Response<Object>> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.FILE_NOT_FOUND_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(environment.getProperty(ResponseMessages.FILE_NOT_FOUND_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Response<Object>> handleIOException(IOException ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.IO_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String[] mails = { purpleDocsMail, unmMail };
		response.setResult(mails);
		response.setMessage(environment.getProperty(ResponseMessages.IO_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(FileAlreadyExistsException.class)
	public ResponseEntity<Response<Object>> handleFileAlreadyExistsException(FileAlreadyExistsException ex,
			WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.FILE_ALREADY_EXISTS_EXCEPTION_LOG_MESSAGE),
				ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.CONFLICT.value());
		response.setMessage(environment.getProperty(ResponseMessages.FILE_ALREADY_EXISTS_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Response<Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.ACCESS_DENIED_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.FORBIDDEN.value());
		response.setMessage(environment.getProperty(ResponseMessages.ACCESS_DENIED_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(DirectoryNotEmptyException.class)
	public ResponseEntity<Response<Object>> handleDirectoryNotEmptyException(DirectoryNotEmptyException ex,
			WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.DIRECTORY_NOT_EMPTY_EXCEPTION_LOG_MESSAGE),
				ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(environment.getProperty(ResponseMessages.DIRECTORY_NOT_EMPTY_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Response<Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.RUNTIME_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String[] mails = { purpleDocsMail, unmMail };
		response.setResult(mails);
		response.setMessage(environment.getProperty(ResponseMessages.RUNTIME_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ResponseEntity<Response<Object>> handleAccessDeniedException(Exception ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.ACCESS_DENIED_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.FORBIDDEN.value());
		response.setMessage(environment.getProperty(ResponseMessages.ACCESS_DENIED_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Response<Object>> handleResponseStatusException(ResponseStatusException ex) {
		logger.error(className + environment.getProperty(ResponseMessages.UNAUTHORIZED), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.UNAUTHORIZED.value());
		response.setMessage(environment.getProperty(ResponseMessages.UNAUTHORIZED));
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<Object>> handleException(Exception ex, WebRequest request) {
		logger.error(className + environment.getProperty(ResponseMessages.RUNTIME_EXCEPTION_LOG_MESSAGE), ex);
		Response<Object> response = new Response<>();
		response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String[] mails = { purpleDocsMail, unmMail };
		response.setResult(mails);
		response.setMessage(environment.getProperty(ResponseMessages.RUNTIME_EXCEPTION_MESSAGE));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Utility method to sanitize log entries
	private String sanitize(String input) {
		if (input == null) {
			return "";
		}
		// Remove line breaks and special characters that could be used for log
		// injection
		return input.replaceAll("[\\r\\n]", "").replaceAll("[^a-zA-Z0-9 ,.-]", "");
	}
}
