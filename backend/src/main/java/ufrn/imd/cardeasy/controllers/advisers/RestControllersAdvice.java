package ufrn.imd.cardeasy.controllers.advisers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ufrn.imd.cardeasy.dtos.ErrorDTO;
import ufrn.imd.cardeasy.dtos.ValidationErrorDTO;
import ufrn.imd.cardeasy.errors.ValidationError;
import ufrn.imd.cardeasy.errors.files.EmptyFileException;
import ufrn.imd.cardeasy.errors.files.FileException;
import ufrn.imd.cardeasy.errors.files.InvalidImageFormat;

@RestControllerAdvice
public class RestControllersAdvice extends ResponseEntityExceptionHandler {
  @ExceptionHandler(ValidationError.class)
  public ResponseEntity<ValidationErrorDTO> handleValidationError(
    ValidationError e
  ) {
    
    Map<String, String> errors = new LinkedHashMap<>();
    errors.put(e.getField(), e.getMessage());

    return ResponseEntity
      .status(e.getStatusCode())
      .body(new ValidationErrorDTO(
        e.getStatusCode(),
        errors
      ));
  };

  @ExceptionHandler(HttpStatusCodeException.class)
  public ResponseEntity<ErrorDTO> handleError(
    HttpStatusCodeException e
  ) {
    
    return ResponseEntity
      .status(e.getStatusCode())
      .body(new ErrorDTO(
        e.getStatusCode(),
        e.getMessage()
      ));
  };

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
    @NonNull MethodArgumentNotValidException e,
    @NonNull HttpHeaders headers, 
    @NonNull HttpStatusCode status, 
    @NonNull WebRequest request
  ) {
    Map<String, String> errors = new LinkedHashMap<>();

    e.getBindingResult().getAllErrors().forEach((error) -> {
      String field = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errors.put(field, message);
    });

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new ValidationErrorDTO(
        HttpStatus.BAD_REQUEST,
        errors
      ));
  };

  @ExceptionHandler({EmptyFileException.class, InvalidImageFormat.class})
  public ResponseEntity<ErrorDTO> handleBadFileError(
    FileException e
  ) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new ErrorDTO(
        HttpStatus.BAD_REQUEST,
        e.getMessage()
      ));
  };

  @ExceptionHandler(FileException.class)
  public ResponseEntity<ErrorDTO> handleFileError(
    FileException e
  ) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new ErrorDTO(
        HttpStatus.BAD_REQUEST,
        e.getMessage()
      ));
  };

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorDTO> handleAuthenticationError(
    AuthenticationException e
  ) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(new ErrorDTO(
        HttpStatus.UNAUTHORIZED,
        "falha de autenticação"
      ));
  };

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDTO> handleInternalError(
    Exception e
  ) {
    e.printStackTrace();
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ErrorDTO(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "erro interno do servidor"
      ));
  };

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
    @NonNull Exception e,
    @Nullable Object body,
    @NonNull HttpHeaders headers,
    @NonNull HttpStatusCode status,
    @NonNull WebRequest request
  ) {
    return ResponseEntity
      .status(status)
      .body(new ErrorDTO(
        status,
        e.getMessage().toLowerCase()
      ));
  };
};
