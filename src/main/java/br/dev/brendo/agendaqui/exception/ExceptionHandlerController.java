package br.dev.brendo.agendaqui.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    private final MessageSource messageSource;

    public ExceptionHandlerController(MessageSource message) {
        this.messageSource = message;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessageDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var err = e.getBindingResult().getFieldErrors().getFirst();
        var message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
        ExceptionMessageDTO errorDTO = new ExceptionMessageDTO(message);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionMessageDTO> handleRuntimeException(RuntimeException e) {
        ExceptionMessageDTO errorDTO = new ExceptionMessageDTO(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
