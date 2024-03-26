package store.ojuara.authapi.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import jakarta.websocket.DecodeException;
import lombok.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import store.ojuara.authapi.service.message.MessageSourceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static store.ojuara.authapi.shared.message.MessageSourceKeys.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final MessageSourceService messageService;

    //Tratando as validações do BeanValidator
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public List<Erro> onMethodArgumentNotValidException(HttpServletRequest req,
                                                        MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return criarErrosValidation(bindingResult);
    }

    //Exceções que precisam de uma mensagem padrão
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public List<Erro> onIllegalArgumentException(HttpServletRequest req, IllegalArgumentException e) {
        return criarErrosException(e, ILLEGAL_ARGUMENT);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(NullPointerException.class)
    public List<Erro> onNullPointerException(HttpServletRequest req, NullPointerException e) {
        return criarErrosException(e, NULL_POINTER);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public List<Erro> onDataIntegrityViolationException(HttpServletRequest req, DataIntegrityViolationException e) {
        return criarErrosException(e, REGISTRO_DUPLICADO);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public List<Erro> onIncorrectResultSizeDataAccessException(HttpServletRequest req, IncorrectResultSizeDataAccessException e) {
        //List<Erro> errors = criarErrosValidationHandler(e.getExceptionHandlerAdviceCache(), REGISTRO_DUPLICADO);
        return criarErrosException(e, REGISTRO_DUPLICADO);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public List<Erro> onDataAccessApiUsageException(HttpServletRequest req, InvalidDataAccessApiUsageException e) {
        return criarErrosException(e, ILLEGAL_ARGUMENT);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public List<Erro> onHttpRequestMethodNotSupportedException(HttpServletRequest req,
                                                               HttpRequestMethodNotSupportedException e) {
        return criarErrosException(e, REQUEST_METHOD);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Erro> onHttpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException e) {
        return criarErrosException(e, MESSAGE_NOT_READABLE);
    }

    @ExceptionHandler({ DecodeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public List<Erro> onDecodeException(HttpServletRequest req, DecodeException e) {
        return criarErrosException(e, SERVICE_ERRO);
    }

    //Exceções com mensagens padrões
    @ExceptionHandler({ ValidationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Erro> onValidationException(HttpServletRequest req, ValidationException e) {
        return criarErrosException(e);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Erro> onMethodArgumentTypeMismatchException(HttpServletRequest req,
                                                            MethodArgumentTypeMismatchException e) {

        return criarErrosException(e);
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object onException(HttpServletRequest req, Exception e) {
        if (req.getServletPath().contains("/views")) {
            return new ModelAndView("error");
        } else {
            return criarErrosException(e);
        }
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ EntityNotFoundException.class })
    public List<Erro> onEntityNotFoundException(HttpServletRequest req, EntityNotFoundException e) {
        return criarErrosException(e);
    }


    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Erro> onConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {
        return criarErrosException(e);
    }

    private List<Erro> criarErrosValidation(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String mensagemDesenvolvedor = "Anotação: '@" + fieldError.getCode() +
                    "' da Classe: " + fieldError.getObjectName();
            String mensagem = messageService.getMessageFieldSource(fieldError);
            erros.add(new Erro(mensagemDesenvolvedor, mensagem));
        }
        return erros;
    }

    private List<Erro> criarErrosValidationHandler(
            Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache,
            String mensagem) {
        List<Erro> errors = new ArrayList<Erro>();
        //Throwable cause = findRootCause(e);

        String message = mensagem;
        String mensagemDesenvolvedor = ""; //e.getLocalizedMessage();
        if(message != null && message.startsWith("{key.")) {
            mensagem = messageService.getMessage(message);
        }else {
            mensagemDesenvolvedor = message;
        }
        if (mensagem == null) {
            mensagemDesenvolvedor = ""; //cause.fillInStackTrace().toString();
            mensagem = message;
        }
        if (mensagem.startsWith("{key.")) {
            mensagem = messageService.getMessage(mensagem);
        }
        errors.add(new Erro(mensagemDesenvolvedor, mensagem));
        return errors;
    }

    private List<Erro> criarErrosException(Exception e, String mensagem) {
        List<Erro> errors = new ArrayList<Erro>();
        Throwable cause = findRootCause(e);
        String message = cause.getMessage();
        String mensagemDesenvolvedor = e.getLocalizedMessage();
        if(message != null && message.startsWith("{key.")) {
            mensagem = messageService.getMessage(message);
        }else {
            mensagemDesenvolvedor = message;
        }
        if (mensagem == null) {
            mensagemDesenvolvedor = cause.fillInStackTrace().toString();
            mensagem = message;
        }
        if (mensagem.startsWith("{key.")) {
            mensagem = messageService.getMessage(mensagem);
        }
        errors.add(new Erro(mensagemDesenvolvedor, mensagem));
        return errors;
    }

    private List<Erro> criarErrosException(Exception e) {

        return criarErrosException(e,null);
    }

    private static Throwable findRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Erro {
        private String mensagemDesenvolvedor;
        private String mensagem;
    }
}