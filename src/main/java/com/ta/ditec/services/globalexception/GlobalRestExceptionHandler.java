package com.ta.ditec.services.globalexception;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

import javax.mail.AuthenticationFailedException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.response.TaResponse;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * The type Global rest exception handler.
 */
@ControllerAdvice
public class GlobalRestExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalRestExceptionHandler.class);

	private static ResponseEntity<Object> frameResponse(ErrorCode errorCode, Exception e) {

		return frameResponse(errorCode, errorCode.getErrorMsg(), e);

	}

	private static ResponseEntity<Object> frameResponse(TaException e) {

		return frameResponse(e.getErrorCode(),
				StringUtils.isNotBlank(e.getErrorDesc()) ? e.getErrorDesc() : e.getErrorCode().getErrorMsg(), e);

	}

	private static ResponseEntity<Object> frameResponse(ErrorCode errorCode, String errorDesc, Exception e) {

//        LOGGER.error("Exception in controller layer", e);
		TaResponse response = new TaResponse();
		response.setCode(errorCode.getCode());
		response.setMessage(errorDesc);
		response.setType(errorCode.getType());

		return ResponseEntity.status(errorCode.getStatus()).body(response);

	}

	private static ResponseEntity<Object> handleException(BindException ex) {

		StringBuilder errorMsg = new StringBuilder();
		if (CollectionUtils.isNotEmpty(ex.getFieldErrors())) {

			errorMsg.append(ex.getBindingResult().getFieldErrors().stream()
					.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
					.collect(Collectors.joining(";")));
		}
		if (CollectionUtils.isNotEmpty(ex.getBindingResult().getGlobalErrors())) {

			errorMsg.append(";")
					.append(ex.getBindingResult().getGlobalErrors().stream()
							.map(objectError -> objectError.getObjectName() + ": " + objectError.getDefaultMessage())
							.collect(Collectors.joining(";")));
		}
		return frameResponse(ErrorCode.BAD_REQUEST, errorMsg.toString(), ex);
	}

	private static ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {

		StringBuilder errorMsg = new StringBuilder();
		if (CollectionUtils.isNotEmpty(ex.getBindingResult().getFieldErrors())) {

			errorMsg.append(ex.getBindingResult().getFieldErrors().stream()
					.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
					.collect(Collectors.joining(";")));
		}
		if (CollectionUtils.isNotEmpty(ex.getBindingResult().getGlobalErrors())) {

			errorMsg.append(";")
					.append(ex.getBindingResult().getGlobalErrors().stream()
							.map(objectError -> objectError.getObjectName() + ": " + objectError.getDefaultMessage())
							.collect(Collectors.joining(";")));
		}
		return frameResponse(ErrorCode.BAD_REQUEST, errorMsg.toString(), ex);
	}

	/**
	 * Handle method argument not valid exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpServletRequest request,
			MethodArgumentNotValidException e) {
		return handleException(e);
	}

	/**
	 * Handle json mapping exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ JsonMappingException.class })
	public ResponseEntity<Object> handleJsonMappingException(HttpServletRequest request, JsonMappingException e) {
		return frameResponse(ErrorCode.BAD_REQUEST, e);
	}

	/**
	 * Handle http message not readable exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpServletRequest request,
			HttpMessageNotReadableException e) {
		return frameResponse(ErrorCode.BAD_REQUEST, e);
	}

	/**
	 * Handle bind exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BindException.class })
	public ResponseEntity<Object> handleBindException(HttpServletRequest request, BindException e) {
		return handleException(e);
	}

	/**
	 * Handle runtime exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(HttpServletRequest request, RuntimeException e) {
		return frameResponse(ErrorCode.INTERNAL_ERROR, e);
	}

	/**
	 * Frame response response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> frameResponse(HttpServletRequest request, Exception e) {

		return frameResponse(ErrorCode.INTERNAL_ERROR, e);
	}

	/**
	 * Handle resource access exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<Object> handleResourceAccessException(HttpServletRequest request, ResourceAccessException e) {
		return frameResponse(ErrorCode.RESOURCE_ACCESS_ERROR, e);
	}

	/**
	 * Handle json parse exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<Object> handleJsonParseException(HttpServletRequest request, JsonParseException e) {

		LOGGER.error("Exception occurred while parsing Json : ", e);

		return frameResponse(new TaException(ErrorCode.BAD_REQUEST, "Request body parsing failed"));
	}

	/**
	 * Handle entity not found exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
		LOGGER.error("EntityNotFoundException occurred while parsing Json : ", e);
		return frameResponse(new TaException(ErrorCode.BAD_REQUEST, "Resource not found"));

	}

	// own exceptions
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(HttpServletRequest request,
			EmptyResultDataAccessException e) {
		LOGGER.error("EmptyResultDataAccessException occurred while parsing Json : ", e);
		return frameResponse(new TaException(ErrorCode.BAD_REQUEST, "Resource not found"));

	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(IndexOutOfBoundsException.class)
	public ResponseEntity<Object> handleIndexOutOfBoundsException(HttpServletRequest request,
			IndexOutOfBoundsException e) {
		LOGGER.error("EmptyResultDataAccessException occurred while parsing Json : ", e);
		return frameResponse(new TaException(ErrorCode.BAD_REQUEST, "Resource not found"));

	}

	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<Object> handleAuthenticationFailedException(HttpServletRequest request,
			AuthenticationFailedException e) {
		LOGGER.error("EmptyResultDataAccessException occurred while parsing Json : ", e);
		return frameResponse(new TaException(ErrorCode.RESOURCE_ACCESS_ERROR, "Resource not found"));

	}

	/**
	 * Handle bne exception response entity.
	 *
	 * @param request the request
	 * @param e       the e
	 * @return the response entity
	 */
	@ExceptionHandler(TaException.class)
	public ResponseEntity<Object> handleTaException(HttpServletRequest request, TaException e) {
		System.out.println("ta exception .");
		return frameResponse(e);

	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Object> handleExpiredJwtException(HttpServletRequest request, TaException e) {
		return frameResponse(e);

	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
		LOGGER.error("AccessDeniedException occurred: ", e);
		System.out.println("281");
		return frameResponse(new TaException(ErrorCode.ACCESS_DENIED_ERROR,
				"Access Denied: You are not authorized to access this resource."));
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handleJwtAuthenticationException(AuthenticationException e) {
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT: " + e.getMessage());
		return frameResponse(new TaException(ErrorCode.ACCESS_DENIED_ERROR,
				"Access Denied: You are not authorized to access this resource."));
	}

//	@ExceptionHandler(TaException.class)
//	public ResponseEntity<Object> handleInformationException(TaException ex) {
//		TaException errorResponse = new TaException(ex.getErrorCode(), ex.getMessage());
//		return new ResponseEntity<>(errorResponse, HttpStatus.OK);
//	}

//	private ResponseEntity<Object> frameResponse(TaException taException) {
//	    return ResponseEntity.status(HttpStatus.OK).body(taException);
//	}

}