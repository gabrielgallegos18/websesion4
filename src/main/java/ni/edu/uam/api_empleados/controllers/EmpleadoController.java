package ni.edu.uam.api_empleados.controllers;

import jakarta.validation.Valid;
import ni.edu.uam.api_empleados.dto.EmpleadoDTO;
import ni.edu.uam.api_empleados.services.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    private final EmpleadoService servicio;

    public EmpleadoController(EmpleadoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> listar() {
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(mensaje("Empleado no encontrado")));
    }

    @PostMapping
    public ResponseEntity<EmpleadoDTO> registrar(
            @Valid @RequestBody EmpleadoDTO empleado) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicio.guardar(empleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoDTO empleado) {
        return servicio.actualizar(id, empleado)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(mensaje("Empleado no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!servicio.eliminar(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mensaje("Empleado no encontrado"));
        }
        return ResponseEntity.ok(mensaje("Empleado eliminado correctamente"));
    }

    private Map<String, String> mensaje(String texto) {
        return Map.of("mensaje", texto);
    }
}
