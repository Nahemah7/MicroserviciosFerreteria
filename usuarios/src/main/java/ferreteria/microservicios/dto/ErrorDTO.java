package ferreteria.microservicios.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorDTO(
        LocalDateTime timestamp,
        int status,
        String mensaje,
        Map<String, String> errores,
        String path
) {
    
}