package com.example.tkemali_restaurant.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(EmailAlreadyExistsException.class)
//    public ModelAndView handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
//        ModelAndView modelAndView = new ModelAndView("profile");
//        modelAndView.addObject("errorMessage", ex.getMessage());
//        return modelAndView;
//    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "Ресурс не найден",
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Некорректный запрос",
            List.of(ex.getMessage())
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(AuthenticationException ex) {
        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Ошибка авторизации",
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.toList());
        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Ошибка валидации",
            errors
        );
        System.out.println(errors);
        return ResponseEntity.badRequest().body(error);
    }

    // Общий обработчик для остальных ошибок
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        ApiError error = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Внутренняя ошибка сервера",
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleResourceNotFound(NoResourceFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}