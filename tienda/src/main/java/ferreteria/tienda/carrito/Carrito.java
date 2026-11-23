package ferreteria.tienda.carrito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "carritos")
public class Carrito {
    @Id
    private String id;

    private Instant creadoEn;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();

    protected Carrito() {
    }

    public Carrito(String id) {
        this.id = id;
        creadoEn = Instant.now();
    }

    public String getId() { return id; }
    public Instant getCreadoEn() { return creadoEn; }
    public List<ItemCarrito> getItems() { return items; }

    public void agregar(ItemCarrito item) {
        item.asignarCarrito(this);
        items.add(item);
    }

    public void quitar(ItemCarrito item) {
        items.remove(item);
        item.asignarCarrito(null);
    }

    public void vaciar() {
        items.forEach(item -> item.asignarCarrito(null));
        items.clear();
    }
}
