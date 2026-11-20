package ferreteria.microservicios.dto;

import ferreteria.microservicios.model.Usuario;

public record UsuarioResponse(
    Long id,
    String run,
    String nombre,
    String apellidos,
    String mail,
    String tipoUsuario,
    String region,
    String comuna,
    String direccion,
    boolean activo
) {
    public static UsuarioResponse desde(Usuario u) {
        return new UsuarioResponse(
            u.getId(), 
            u.getRun(), 
            u.getNombre(), 
            u.getApellidos(), 
            u.getMail(), 
            u.getTipoUsuario(), 
            u.getRegion(), 
            u.getComuna(), 
            u.getDireccion(), 
            u.isActivo()
        );
    }
}
