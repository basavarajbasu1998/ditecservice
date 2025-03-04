package com.ta.ditec.services.securityconfig;
//

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.function.Function;
//
//@Component
//public class JWTUtils {
//
//	private SecretKey Key;
//	private static final long EXPIRATION_TIME = 86400000; // 24hours or 86400000 milisecs
////	private static final long EXPIRATION_TIME = 60000;
//
//	public JWTUtils() {
//		String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
//		byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
//		this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
//	}
//
//	public String generateToken(UserDetails userDetails) {
//		return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
//				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(Key).compact();
//	}
//
//	public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
//		return Jwts.builder().claims(claims).subject(userDetails.getUsername())
//				.issuedAt(new Date(System.currentTimeMillis()))
//				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(Key).compact();
//	}
//
//	public String extractUsername(String token) {
//		return extractClaims(token, Claims::getSubject);
//	}
//
//	private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
//		return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
//	}
//
//	public boolean isTokenValid(String token, UserDetails userDetails) {
//		final String username = extractUsername(token);
//		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//	}
//
//	public boolean isTokenExpired(String token) {
//		return extractClaims(token, Claims::getExpiration).before(new Date());
//	}
//
//}

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtils {

	private SecretKey key;
	private static final long EXPIRATION_TIME = 86400000; // 24 hours or 86400000 milliseconds
//	private static final long EXPIRATION_TIME = 30000;

	public JWTUtils() {
		String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
		byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
		this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
	}

	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(key).compact();
	}

	public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
		return Jwts.builder().claims(claims).subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(key).compact();
	}

	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
		try {
		return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
		}catch(Exception ex)
		{
//			System.out.println("asdfasd");
			throw new TaException(ErrorCode.ACCESS_DENIED_ERROR, ErrorCode.ACCESS_DENIED_ERROR.getErrorMsg());
		}
		
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public boolean isTokenExpired(String token) {
		return extractClaims(token, Claims::getExpiration).before(new Date());
	}

	public String getTokenExpirationStatus(String token) {
		if (isTokenExpired(token)) {
			throw new TaException(ErrorCode.ACCESS_DENIED_ERROR, ErrorCode.ACCESS_DENIED_ERROR.getErrorMsg());
		} else {
			return "Token is valid.";
		}
	}
}
