package ferreteria.microservicios.exception;

import ferreteria.microservicios.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDTO> manejarEstado(ResponseStatusException ex, HttpServletRequest request) {
        int status = ex.getStatusCode().value();
        String mensaje = ex.getReason() != null ? ex.getReason() : "Error";
        return ResponseEntity.status(status).body(armar(status, mensaje, Map.of(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> manejarValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(armar(400, "Error de validación", errores, request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> manejarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Map<String, String> errores = Map.of("cuerpo", "El JSON enviado es inválido o tiene campos con el tipo incorrecto");
        return ResponseEntity.badRequest().body(armar(400, "JSON inválido", errores, request));
    }

    public ResponseEntity<ErrorDTO> manejarBaseDeDatos(DataIntegrityViolationException ex, HttpServletRequest request) {
        Map<String, String> errores = Map.of("base_de_datos", "Ya existe un registro con esos datos");
        return ResponseEntity.status(409).body(armar(409, "Conflicto de datos", errores, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> manejarGeneral(Exception ex, HttpServletRequest request) {
        Map<String, String> errores = Map.of("error", "Ocurrió un error inesperado");
        return ResponseEntity.status(500).body(armar(500, "Error interno del servidor", errores, request));
    }

    private ErrorDTO armar(int status, String mensaje, Map<String, String> errores, HttpServletRequest request) {
        return new ErrorDTO(LocalDateTime.now(), status, mensaje, errores, request.getRequestURI());
    }
}