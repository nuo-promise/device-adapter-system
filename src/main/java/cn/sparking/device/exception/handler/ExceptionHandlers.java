package cn.sparking.device.exception.handler;

import cn.sparking.device.constant.SparkingCommonResultMessage;
import cn.sparking.device.model.response.ShuBoDeviceResult;
import cn.sparking.device.exception.SparkingCommonCode;
import cn.sparking.device.exception.SparkingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller Method Resolver.
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    protected ShuBoDeviceResult handleExceptionHandler(final Exception exception) {
        log.error(exception.getMessage(), exception);
        String message;
        if (exception instanceof SparkingException) {
            SparkingException spkCommonException = (SparkingException) exception;
            message = spkCommonException.getMessage();
        } else {
            message = "The System is busy, please try again later";
        }
        return ShuBoDeviceResult.error(message);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    protected ShuBoDeviceResult handleDuplicateKeyException(final DuplicateKeyException exception) {
        log.error("duplicate key exception ", exception);
        return ShuBoDeviceResult.error(SparkingCommonResultMessage.UNIQUE_INDEX_CONFLICT_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ShuBoDeviceResult handleNullPointException(final NullPointerException exception) {
        log.error("null pointer exception ", exception);
        return ShuBoDeviceResult.error(SparkingCommonCode.NOT_FOUND_EXCEPTION, SparkingCommonResultMessage.NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ShuBoDeviceResult handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.warn("http request method not supported", e);
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMethod());
        sb.append(
                " method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(e.getSupportedHttpMethods()).forEach(t -> sb.append(t).append(" "));
        return ShuBoDeviceResult.error(sb.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ShuBoDeviceResult handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("method argument not valid", e);
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(f -> f.getField().concat(": ").concat(Optional.ofNullable(f.getDefaultMessage()).orElse("")))
                .collect(Collectors.joining("| "));
        return ShuBoDeviceResult.error(String.format("Request error! invalid argument [%s]", errorMsg));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ShuBoDeviceResult handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn("missing servlet request parameter", e);
        return ShuBoDeviceResult.error(String.format("%s parameter is missing", e.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ShuBoDeviceResult handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.warn("method argument type mismatch", e);
        return ShuBoDeviceResult.error(String.format("%s should be of type %s", e.getName(), Objects.requireNonNull(e.getRequiredType()).getName()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ShuBoDeviceResult handleConstraintViolationException(final ConstraintViolationException e) {
        log.warn("constraint violation exception", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        return ShuBoDeviceResult.error(violations.stream()
                .map(v -> v.getPropertyPath().toString().concat(": ").concat(v.getMessage()))
                .collect(Collectors.joining("| ")));
    }

    @ExceptionHandler(SparkingException.class)
    protected ShuBoDeviceResult handleShenyuException(final SparkingException exception) {
        log.error("device adapter exception ", exception);
        return ShuBoDeviceResult.error(SparkingCommonCode.ERROR, exception.getMessage());

    }
}
