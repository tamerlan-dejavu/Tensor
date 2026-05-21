package com.tensorcipher.api.controller;

import com.tensorcipher.api.model.CipherRequest;
import com.tensorcipher.api.model.CipherResponse;
import com.tensorcipher.api.model.ErrorResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cipher")
@CrossOrigin(origins = "*")
public class CipherController {

    private static final Logger log = LoggerFactory.getLogger(CipherController.class);

    // cipher-core classes are in the default package — accessed via reflection-free direct instantiation
    // since they have no package declaration, they are loaded as default-package classes
    private final Object tensorCipher;

    public CipherController() {
        try {
            Class<?> clazz = Class.forName("TensorCipher");
            this.tensorCipher = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize TensorCipher", e);
        }
    }

    /**
     * Encrypts the provided text using the given key.
     */
    @PostMapping("/encrypt")
    public ResponseEntity<?> encrypt(@Valid @RequestBody CipherRequest request) {
        log.debug("Encrypt request: text length={}", request.getText().length());
        long start = System.currentTimeMillis();
        try {
            String result = (String) tensorCipher.getClass()
                    .getMethod("encrypt", String.class, String.class)
                    .invoke(tensorCipher, request.getText(), request.getKey());
            long elapsed = System.currentTimeMillis() - start;
            log.debug("Encrypt completed in {}ms", elapsed);
            return ResponseEntity.ok(new CipherResponse(result, "encrypt", elapsed));
        } catch (Exception e) {
            log.error("Encrypt failed", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Encryption failed", 500, e.getMessage()));
        }
    }

    /**
     * Decrypts the provided ciphertext using the given key.
     */
    @PostMapping("/decrypt")
    public ResponseEntity<?> decrypt(@Valid @RequestBody CipherRequest request) {
        log.debug("Decrypt request: text length={}", request.getText().length());
        long start = System.currentTimeMillis();
        try {
            String result = (String) tensorCipher.getClass()
                    .getMethod("decrypt", String.class, String.class)
                    .invoke(tensorCipher, request.getText(), request.getKey());
            long elapsed = System.currentTimeMillis() - start;
            log.debug("Decrypt completed in {}ms", elapsed);
            return ResponseEntity.ok(new CipherResponse(result, "decrypt", elapsed));
        } catch (Exception e) {
            log.error("Decrypt failed", e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Decryption failed", 500, e.getMessage()));
        }
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        log.debug("Health check");
        return ResponseEntity.ok(Map.of("status", "ok", "version", "1.0.0"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Internal server error", 500, e.getMessage()));
    }
}
