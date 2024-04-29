package in.nucleusteq.plasma.exception;

import in.nucleusteq.plasma.payload.ErrorResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RequestTimeOutException.class)
    public ResponseEntity<ErrorResponce> requestTimeOutExceptionHandler(
            final RequestTimeOutException requestTimeOutException) {
        String message = requestTimeOutException.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(message);
        ErrorResponce response = ErrorResponce.builder().success(false).errorMessages(errors).build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponce> unauthorizedAccessExceptionHandler(
            final UnauthorizedAccessException accessException ){
        String message = accessException.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(message);
        ErrorResponce responce = ErrorResponce.builder().success(false)
                .errorMessages(errors).errorCode(HttpStatus.UNAUTHORIZED.value()).build();
        return  new ResponseEntity<>(responce,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponce> resourceNotFoundExceptionHandler(
            final ResourceNotFoundException resourceNotFoundException) {
        String message = resourceNotFoundException.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(message);
        ErrorResponce response = ErrorResponce.builder()
                .success(false)
                .errorMessages(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponce> badRequestExceptionHandler(
            final BadRequestException badRequestException) {
        String message = badRequestException.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(message);
        ErrorResponce response = ErrorResponce.builder()
                .success(false)
                .errorMessages(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponce> alreadyExistExceptionHandler(
            final AlreadyExistsException alreadyExistException) {
        String message = alreadyExistException.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(message);
        ErrorResponce response = ErrorResponce.builder()
                .success(false)
                .errorMessages(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponce> duplicateExceptionHandler(
            final DuplicateException duplicateException) {
        String message = duplicateException.getMessage();
        List<String> errors = new ArrayList<>();
        errors.add(message);
        ErrorResponce response = ErrorResponce.builder()
                .success(false)
                .errorMessages(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
