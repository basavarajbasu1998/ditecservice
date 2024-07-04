package com.ta.ditec.services.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ta.ditec.services.encrypt.SHAEnc;

public class CustomShaPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return SHAEnc.encryptData(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// Implement matching logic if needed
		return encode(rawPassword).equals(encodedPassword);
	}
}
