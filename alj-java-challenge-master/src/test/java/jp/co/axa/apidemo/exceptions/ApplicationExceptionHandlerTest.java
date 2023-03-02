package jp.co.axa.apidemo.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ApplicationExceptionHandlerTest.class})
class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler applicationExceptionHandler;

    @BeforeEach
    public void setup() {
        applicationExceptionHandler = new ApplicationExceptionHandler();
    }

    @Test
    void testHandleNotFound() {

        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        ResponseEntity<Object> responseEntity = applicationExceptionHandler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertEquals("Resource Not Found", apiError.getMessage());
        assertEquals("Resource not found", apiError.getError());
        assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
        assertEquals(LocalDateTime.now().getDayOfYear(), apiError.getTimestamp().getDayOfYear());
    }

    @Test
    void testHandleServerError() {

        RuntimeException ex = new RuntimeException("Something went wrong");

        ResponseEntity<Object> responseEntity = applicationExceptionHandler.handleServerError(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertEquals("Internal server error", apiError.getMessage());
        assertEquals("Something went wrong", apiError.getError());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiError.getStatus());
        assertEquals(LocalDateTime.now().getDayOfYear(), apiError.getTimestamp().getDayOfYear());
    }
}

