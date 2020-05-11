package ru.animal.shelter.manager.filestorage.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        log.warn("Validation error:", e);
        return new ResponseEntity<>(getDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, HttpServletResponse response) {
        log.warn("Validation error: ", e);
        return new ResponseEntity<>(getDescription(e), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException e, HttpServletResponse response) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBindException(BindException e) {
        log.warn("Validation error: ", e);
        return new ResponseEntity<>(getDefaultMessage(e), HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleSyntaxError(HttpMessageNotReadableException e, HttpServletResponse response) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException) {
            Object value = ((InvalidFormatException) cause).getValue();
            log.warn("Неизвестное значение поля в JSON: " + value);
            return new ResponseEntity<>("Неизвестное значение поля в JSON: " + value, HttpStatus.BAD_REQUEST);
        }
        log.warn("Невалидный JSON");
        return new ResponseEntity<>("Невалидный JSON", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleThrowable(Throwable e, HttpServletResponse response) {
        log.error("Internal server error: ", e);
        return new ResponseEntity<>("Внутренняя ошибка сервера: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleRequestProcessingException(Throwable e, HttpServletResponse response) {
        log.error("Request processing error: ", e);
        return new ResponseEntity<>("Ошибка обработки запроса: " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleNotSupported(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        log.warn("Вызываемый метод не поддерживается");
        return new ResponseEntity<>("Вызываемый метод не поддерживается", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletResponse response) {
        log.warn("Невалидный JSON");
        return new ResponseEntity<>("Невалидный JSON", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletResponse response) {
        log.warn("Не передан обязательный параметр: " + ex.getParameterName());
        return new ResponseEntity<>("Не передан обязательный параметр: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletResponse response) {
        log.warn("Max file size exceeded: ", ex);
        long fileSizeInMb = ex.getMaxUploadSize() / 1024 / 1024;
        String message = String.format("Максимальный размер файла %d MB.%n " +
                "Обратитесь к администратору или загрузите файл частями.", fileSizeInMb);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    private String getDescription(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream().map(ErrorControllerAdvice::getErrorDescription)
                .collect(Collectors.joining(", "));
    }

    private String getDescription(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(ErrorControllerAdvice::getErrorDescription).collect(Collectors.joining(", "));
    }

    private static String getDefaultMessage(BindException e) {
        return e.getAllErrors().get(0).getDefaultMessage();
    }

    private static String getErrorDescription(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            FieldError fieldError = (FieldError) objectError;
            return fieldError.getField() + ": " + fieldError.getDefaultMessage();
        } else {
            return objectError.getDefaultMessage();
        }
    }

    private static String getErrorDescription(ConstraintViolation<?> constraintViolation) {
        Path propertyPath = constraintViolation.getPropertyPath();
        Path.Node lastNode = null;
        for (Path.Node node : propertyPath) {
            lastNode = node;
        }
        String propertyName = "";
        if (lastNode != null) {
            propertyName = lastNode.getName();
        }
        return propertyName + ": " + constraintViolation.getMessage();
    }
}

