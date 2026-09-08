package ni.edu.uam.api_empleados.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmpleadoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarRetorna200() throws Exception {
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void registrarRetorna201ConJson() throws Exception {
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombres").value("Ana María"))
                .andExpect(jsonPath("$.apellidos").value("López Pérez"))
                .andExpect(jsonPath("$.cargo").value("Analista de sistemas"));
    }

    @Test
    void validarRetorna400ConErrores() throws Exception {
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombres": "",
                                  "apellidos": "Pérez",
                                  "cargo": "",
                                  "salario": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.mensaje").value("Los datos enviados no son válidos"))
                .andExpect(jsonPath("$.errores", hasKey("nombres")))
                .andExpect(jsonPath("$.errores", hasKey("cargo")))
                .andExpect(jsonPath("$.errores", hasKey("salario")));
    }

    @Test
    void registrarSinBodyRetorna400ConMensajeJson() throws Exception {
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.mensaje").value("Debe enviar el cuerpo de la solicitud en formato JSON"));
    }

    @Test
    void buscarInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/empleados/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Empleado no encontrado"));
    }

    @Test
    void actualizarYEliminarEmpleado() throws Exception {
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido()))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombres": "Ana María",
                                  "apellidos": "López Pérez",
                                  "cargo": "Coordinadora de sistemas",
                                  "salario": 20500.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cargo").value("Coordinadora de sistemas"));

        mockMvc.perform(delete("/api/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Empleado eliminado correctamente"));

        mockMvc.perform(get("/api/empleados/1"))
                .andExpect(status().isNotFound());
    }

    private String jsonValido() {
        return """
                {
                  "nombres": "Ana María",
                  "apellidos": "López Pérez",
                  "cargo": "Analista de sistemas",
                  "salario": 18500.00
                }
                """;
    }
}
