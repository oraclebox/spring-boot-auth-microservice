package me.oraclebox.auth.service.exception

import me.oraclebox.exception.AuthenticationException
import me.oraclebox.http.ResultModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Global exception handler
 * Created by ROGER CHAN on 14/9/2016.
 */
@ControllerAdvice
class GlobalControllerExceptionHandler {

    private final Logger L = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    ResponseEntity<ResultModel> handleException(AuthenticationException ex) {
        def id = UUID.randomUUID().toString(); // case id
        L.warn("[" + id + "] " + ex.getMessage());
        return new ResponseEntity<>(ResultModel._403(ex.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ResultModel> handleException(Exception ex) {
        def id = UUID.randomUUID().toString(); // case id
        L.error("[" + id + "] " + ex.getMessage());
        return new ResponseEntity<>(ResultModel._500(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
