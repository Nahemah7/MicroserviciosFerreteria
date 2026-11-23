package ferreteria.tienda.comentario;

import java.time.Instant;

public record ComentarioResponse(
        Long id, String codigoProducto, String nombre, String texto, boolean visible, Instant creadoEn) {
    public static ComentarioResponse desde(Comentario comentario) {
        return new ComentarioResponse(comentario.getId(), comentario.getCodigoProducto(),
                comentario.getNombre(), comentario.getTexto(), comentario.isVisible(), comentario.getCreadoEn());
    }
}
