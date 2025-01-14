package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.request_response.ResetPasswordResponseDTO;

public interface ResetPasswordService {
	ResetPasswordResponseDTO sendOtp(String email) throws Exception;

	ResetPasswordResponseDTO verifyOtp(String email, String otp);

	ResetPasswordResponseDTO resetPassword(String email, String otp, String password);
}
