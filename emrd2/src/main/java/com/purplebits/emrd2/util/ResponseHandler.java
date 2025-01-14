/**
 * Description of the file.
 * 
 *  Deep Singh Tomar
 *  PurpleDocs
 * 13-Mar-2024 8:05:00 pm
 */
package com.purplebits.emrd2.util;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created By @Deep For Company - PurpleDocs 
 */

public class ResponseHandler {

	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj,
			String requestId) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("code", status.value());
		map.put("result", responseObj);
		map.put("requestId", requestId);
		map.put("timestamp", Instant.now().toString());

		return new ResponseEntity<>(map, status);
	}

}
