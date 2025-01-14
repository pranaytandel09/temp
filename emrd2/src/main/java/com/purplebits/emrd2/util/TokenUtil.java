package com.purplebits.emrd2.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.Factory;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;

/**
 * 
 * @since 2022-02-28
 * @author prayas
 *
 */

@Component
public class TokenUtil {

	static JsonWebKey jsonWebKey = null;
	private static final Logger logger = LogManager.getLogger(TokenUtil.class);
	private static final String CLASS_NAME = TokenUtil.class.getSimpleName();
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";

	static {
		// Setting up Direct Symmetric Encryption and Decryption
		KeyGenerator gen;
		try {
			gen = KeyGenerator.getInstance("AES");
			gen.init(256);
			SecretKey secretKey = gen.generateKey();
			JWK jwk = new OctetSequenceKey.Builder(secretKey).build();
			String jwkJson = jwk.toJSONString();
			new JsonWebKey.Factory();
			jsonWebKey = Factory.newJwk(jwkJson);

		} catch (NoSuchAlgorithmException e) {
			logger.error(CLASS_NAME+e);
		} catch (JoseException e) {
			logger.error(CLASS_NAME+e);
		}
	}

	public String getJWEString(String userLogin, String displayName, String userPassword, String userRole,
			String loginType, String application) {
		final String METHOD_NAME = ".getJWEString()";

		// Prepare JWT with claims set
		JwtClaims jwtClaims = new JwtClaims();
		jwtClaims.setSubject(userLogin);
		jwtClaims.setClaim("Name", displayName);
		jwtClaims.setClaim("Password", userPassword);
		jwtClaims.setClaim("Role", userRole);
		jwtClaims.setClaim("LoginType", loginType);
		jwtClaims.setClaim("Application", application);
		jwtClaims.setIssuedAtToNow();
		jwtClaims.setIssuer("purpledocs.com");
		jwtClaims.setAudience("Customer");
		jwtClaims.setGeneratedJwtId();
		jwtClaims.setExpirationTimeMinutesInTheFuture(1500);
		jwtClaims.setNotBeforeMinutesInThePast(1);

		JsonWebSignature jws = new JsonWebSignature();

		// The payload of the JWS is JSON content of the JWT Claims
		jws.setPayload(jwtClaims.toJson());
		jws.setKeyIdHeaderValue(jsonWebKey.getKeyId());
		jws.setKey(jsonWebKey.getKey());

		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);

		String jwt = null;
		try {
			jwt = jws.getCompactSerialization();
		} catch (JoseException e) {
			logger.error(CLASS_NAME + METHOD_NAME + " JoseException while generating json web token."+e);
		}

		JsonWebEncryption jwe = new JsonWebEncryption();
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		jwe.setKey(jsonWebKey.getKey());
		jwe.setKeyIdHeaderValue(jsonWebKey.getKeyId());
		jwe.setContentTypeHeaderValue("JWT");
		jwe.setPayload(jwt);

		String jweSerialization = null;
		try {
			jweSerialization = jwe.getCompactSerialization();
		} catch (JoseException e) {
			logger.error(CLASS_NAME + METHOD_NAME + " JoseException while serializing json web token."+e);
		}

		return jweSerialization;
	}

	public Map<String, Object> isValid(String token) {

		Map<String, Object> validationMap = null;

		JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireSubject().setRequireExpirationTime()
				.setAllowedClockSkewInSeconds(30).setExpectedIssuer("purpledocs.com").setExpectedAudience("Customer")
				.setDecryptionKey(jsonWebKey.getKey()).setVerificationKey(jsonWebKey.getKey()).build();

		try {
			// Validate the JWT and process it to the Claims
			JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
			String userLogin = jwtClaims.getSubject();
			String userDispName = jwtClaims.getClaimValue("Name").toString();
			String userPassword = jwtClaims.getClaimValue("Password").toString();
			String userRole = jwtClaims.getClaimValue("Role").toString();
			String application = jwtClaims.getClaimValue("Application").toString();
			String loginType = jwtClaims.getClaimValue("LoginType").toString();
			/*
			 * Encryptor encryptor = new Encryptor(); String password =
			 * encryptor.decryptPassword(userPassword);
			 */
			validationMap = new HashMap<>();
			validationMap.put("userLogin", userLogin);
			validationMap.put("userDispName", userDispName);
			validationMap.put("userRole", userRole);
			validationMap.put("loginType", loginType);
			validationMap.put("application", application);
			validationMap.put("userPassword", userPassword);

		} catch (InvalidJwtException | MalformedClaimException e) {
			logger.error(CLASS_NAME+"JWT is Invalid: " + e.getMessage());
		} 

		return validationMap;
	}

	public void generateRefreshToken(Map<String, Object> validatedMap, HttpServletResponse httpResponse) {
		String userRole = (String) validatedMap.get("userRole");
		String userLogin = (String) validatedMap.get("userLogin");
		String application = (String) validatedMap.get("application");
		String loginType = (String) validatedMap.get("loginType");
		String displayName = (String) validatedMap.get("userDispName");
		String userPassword = (String) validatedMap.get("userPassword");

		// Attach refresh token if request validated successfully
		String refershToken = getJWEString(userLogin, displayName, userPassword, userRole, loginType, application);
		httpResponse.addHeader(AUTHORIZATION_PROPERTY, AUTHENTICATION_SCHEME + " " + refershToken);
	}

	public void validateToken(String token, HttpServletResponse response) {
		token = token.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		Map<String, Object> validatedMap = isValid(token);
		if (validatedMap == null) {
			try {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				PrintWriter output = response.getWriter();
				output.write("Your Session Expires Login Again.");
				output.close();
			} catch (IOException ioe) {
				logger.error(CLASS_NAME+" IO Exception while validating details of token" + ioe);
			}
		}
	}
}
