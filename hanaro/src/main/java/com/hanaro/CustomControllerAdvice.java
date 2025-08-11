// package com.hanaro;
//
// import java.util.Map;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
//
// import com.hanaro.security.CustomJwtException;
//
// @RestControllerAdvice
// public class CustomControllerAdvice {
// 	@ExceptionHandler(CustomJwtException.class)
// 	protected ResponseEntity<?> handleJWTException(CustomJwtException e) {
// 		String message = e.getMessage();
// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", message));
// 	}
//
// }
