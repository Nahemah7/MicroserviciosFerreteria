package ferreteria.tienda.comentario;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contactos")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 254)
    private String correo;

    @Column(nullable = false, length = 1000)
    private String comentario;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private Instant creadoEn;

    protected Comentario() {
    }

    public Comentario(String nombre, String correo, String comentario) {
        this.nombre = nombre;
        this.correo = correo;
        this.comentario = comentario;
        visible = true;
        creadoEn = Instant.now();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getComentario() { return comentario; }
    public boolean isVisible() { return visible; }
    public Instant getCreadoEn() { return creadoEn; }
    public void cambiarVisibilidad(boolean visible) { this.visible = visible; }
}
