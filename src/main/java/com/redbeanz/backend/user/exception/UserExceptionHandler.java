package com.redbeanz.backend.user.exception;

import com.redbeanz.backend.user.controller.UserController;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 회원 API의 400·409 응답만 처리합니다. 공통 예외 처리 구조는 이번 범위에서 만들지 않습니다.
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {

	public record ErrorResponse(String message, Map<String, String> errors) {
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleInvalid(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new LinkedHashMap<>();
		for (FieldError error : exception.getBindingResult().getFieldErrors()) {
			errors.putIfAbsent(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity.badRequest().body(new ErrorResponse("입력값을 확인하세요.", errors));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException exception) {
		return ResponseEntity.badRequest().body(new ErrorResponse("요청 본문을 읽을 수 없습니다.", Map.of()));
	}

	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateUserException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse(exception.getMessage(), Map.of(exception.getField(), exception.getMessage())));
	}
}
