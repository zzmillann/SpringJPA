package es.daw.productoapirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO>handleValidationExceptions(MethodArgumentNotValidException ex){

        Map<String,String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));

        ErrorDTO errorDTO = new ErrorDTO(
                "Error de validacion",
                LocalDateTime.now(),
                errores
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO>handleExceptions(Exception ex){

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Pedazo de error generico");
        errorDTO.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);



    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDTO>handleNumberFormatExceptions(NumberFormatException ex){

        ErrorDTO errorDTO = new ErrorDTO(
                "Invalid number format",
                LocalDateTime.now(),
                new HashMap<>()



        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);

    }
}
