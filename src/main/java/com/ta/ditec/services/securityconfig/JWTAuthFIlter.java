
package com.ta.ditec.services.securityconfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.exception.TaExceptionFilter;

@Component
public class JWTAuthFIlter extends OncePerRequestFilter {

	@Autowired
	private JWTUtils jwtUtils;
	@Autowired
	private OurUserDetailsService ourUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String authHeader = request.getHeader("Authorization");
			final String jwtToken;
			final String userEmail;
			if (authHeader == null) {
				filterChain.doFilter(request, response);
				return;
			}
			jwtToken = authHeader.substring(7);
			userEmail = jwtUtils.extractUsername(jwtToken);
			if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);

				if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					securityContext.setAuthentication(token);
					SecurityContextHolder.setContext(securityContext);
				}

			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			TaExceptionFilter tae = new TaExceptionFilter(3000, "Token Exception.", "");
			response.setContentType("application/json");
			response.getWriter().write(convertObjectToJson(tae));
//			throw new TaException(ErrorCode.ALREADY_TITLE_EXIST, ErrorCode.ALREADY_EXIST.getErrorMsg());
		}
	}

	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}

//package com.ta.ditec.services.securityconfig;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.ta.ditec.services.exception.ErrorCode;
//import com.ta.ditec.services.exception.TaException;
//
//@Component
//public class JWTAuthFIlter extends OncePerRequestFilter {
//
//	@Autowired
//	private JWTUtils jwtUtils;
//	@Autowired
//	private OurUserDetailsService ourUserDetailsService;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		try {
//			final String authHeader = request.getHeader("Authorization");
//			if (authHeader != null && authHeader.startsWith("Bearer ")) {
//				final String jwtToken = authHeader.substring(7);
//				final String userEmail = jwtUtils.extractUsername(jwtToken);
//				if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//					UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);
//
//					if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
//						SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//						UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
//								null, userDetails.getAuthorities());
//						token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//						securityContext.setAuthentication(token);
//						SecurityContextHolder.setContext(securityContext);
//					}
//				}
//			}
//			filterChain.doFilter(request, response);
//		} catch (Exception e) {
//			// Log the exception
//			e.printStackTrace();
//
//			// You can return a custom error response here
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.setContentType("application/json");
//			response.getWriter()
//					.write(new TaExceptions(ErrorCode.ACCESS_DENIED_ERROR, ErrorCode.ACCESS_DENIED_ERROR.getErrorMsg())
//							.toJson());
//		}
//	}
//}
