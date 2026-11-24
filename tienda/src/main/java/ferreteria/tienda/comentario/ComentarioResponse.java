package ferreteria.tienda.comentario;

import java.time.Instant;

public record ComentarioResponse(
        Long id, String nombre, String correo, String comentario, boolean visible, Instant creadoEn) {
    public static ComentarioResponse desde(Comentario contacto) {
        return new ComentarioResponse(contacto.getId(), contacto.getNombre(), contacto.getCorreo(),
                contacto.getComentario(), contacto.isVisible(), contacto.getCreadoEn());
    }
}
