package ferreteria.tienda.producto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductoInicialRunner implements ApplicationRunner {
    private final ProductoRepository productoRepository;
    private final ObjectMapper objectMapper;

    public ProductoInicialRunner(ProductoRepository productoRepository, ObjectMapper objectMapper) {
        this.productoRepository = productoRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws IOException {
        if (productoRepository.count() > 0) return;
        try (InputStream input = new ClassPathResource("productos-iniciales.json").getInputStream()) {
            List<ProductoRequest> productos = objectMapper.readValue(input, new TypeReference<>() {});
            List<Producto> iniciales = productos.stream()
                    .map(p -> new Producto(p.codigo(), p.categoria(), p.subcategoria(), p.nombre(), p.marca(),
                            p.unidad(), p.pCompra(), p.pVenta(), p.stock(), p.stockMin()))
                    .toList();
            productoRepository.saveAll(iniciales);
        }
    }
}
