package pe.edu.cibertec.patitas_frontend_wc_a.dto;


public record RequestLogin(
  String tipoDocumento,
  String numeroDocumento,
  String password

) {
}
