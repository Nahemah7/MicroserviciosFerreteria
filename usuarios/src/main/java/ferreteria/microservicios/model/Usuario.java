package ferreteria.microservicios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String run;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    @NotBlank
    @Email
    @Column(unique = true)
    private String mail;

    @NotBlank
    @Size(min = 6)
    private String clave;

    @NotBlank
    private String tipoUsuario = "Cliente";

    private String region;
    private String comuna;
    private String direccion;

    private boolean activo = true;
}
