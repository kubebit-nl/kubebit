package nl.kubebit.core.infrastructure.system.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import nl.kubebit.core.entities.common.exception.EntityAlreadyExistsException;
import nl.kubebit.core.entities.common.exception.EntityInvalidStatusException;
import nl.kubebit.core.entities.common.exception.EntityNotCreatedException;
import nl.kubebit.core.entities.common.exception.EntityNotDeletedException;
import nl.kubebit.core.entities.common.exception.EntityNotFoundException;
import nl.kubebit.core.entities.common.exception.EntityNotUpdatedException;

import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 *
 */
@ControllerAdvice
public class CustomExceptionHandler {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * handle EntityException
     *
     * @param e EntityException
     * @return ResponseEntity
     */
    @ExceptionHandler({
            EntityNotCreatedException.class,
            EntityNotUpdatedException.class,
            EntityNotDeletedException.class,
            EntityInvalidStatusException.class
    })
    public ResponseEntity<String> handleEntityException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }

    /**
     * handle EntityAlreadyExistsException
     *
     * @param e EntityAlreadyExistsException
     * @return ResponseEntity
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * handle EntityNotFoundException
     *
     * @param e EntityNotFoundException
     * @return ResponseEntity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * handle AsyncRequestTimeoutException
     *
     * @param e AsyncRequestTimeoutException
     * @return ResponseEntity
     */
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<String> handleAsyncRequestTimeoutExceptions(AsyncRequestTimeoutException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(e.getMessage());
    }

    /**
     * handle AsyncRequestNotUsableException
     *
     * @param e AsyncRequestNotUsableException
     * @return ResponseEntity
     */
    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public ResponseEntity<String> handleAsyncRequestNotUsableExceptions(AsyncRequestNotUsableException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }

    // ----------

    /**
     * handle HttpRequestMethodNotSupportedException
     *
     * @param e HttpRequestMethodNotSupportedException
     * @return ResponseEntity
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedExceptions(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("method '" + e.getMethod() + "' not allowed");
    }

    /**
     * handle NoResourceFoundException
     *
     * @param e NoResourceFoundException
     * @return ResponseEntity
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundExceptions(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("endpoint '" + e.getResourcePath() + "' not found");
    }

    /**
     * handle Exception
     *
     * @param e Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.warn("error class: {}", e.getClass().getCanonicalName());
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
