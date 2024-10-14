package pe.edu.cibertec.patitas_frontend_wc_a.dto;

public record ResponseLogin(
      String codigo,
      String mensaje,
      String nombreUsuario,
      String correoUsuario
) {
}
