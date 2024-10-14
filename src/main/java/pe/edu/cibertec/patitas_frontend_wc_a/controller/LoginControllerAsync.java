package pe.edu.cibertec.patitas_frontend_wc_a.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.RequestClose;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.RequestLogin;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.ResponseClose;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.ResponseLogin;
import pe.edu.cibertec.patitas_frontend_wc_a.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAutenticacion;
    //RestTemplate restTemplate;

    @PostMapping("/autenticar-async")
    public Mono<ResponseLogin> autenticar(@RequestBody RequestLogin requestLogin) {

        //Validamos los campos
        if (requestLogin.tipoDocumento() == null || requestLogin.tipoDocumento().trim().length() == 0 ||
                requestLogin.numeroDocumento() == null || requestLogin.numeroDocumento().trim().length() == 0 ||
                requestLogin.password() == null || requestLogin.password().trim().length() == 0) {

            //En caso no ingresen todos los datos, salta este error
            return Mono.just(new ResponseLogin("01", "Datos insuficientes", "", ""));

        }

        try {
            //hacemos la solicitud

            //y recibimos la data (response)
            return webClientAutenticacion.post()
                    .uri("/login")
                    .body(Mono.just(requestLogin), RequestLogin.class)
                    .retrieve()
                    .bodyToMono(ResponseLogin.class)
                    .flatMap(response -> {
                        //manipulación... cambia el "return" que le daremos
                        if (response.codigo().equals("00")) {
                            return Mono.just(new ResponseLogin(
                                    "00", "", response.nombreUsuario(), response.correoUsuario()));
                        } else {
                            return Mono.just(new ResponseLogin(
                                    "02", "Autenticación fallida", "", ""));
                        }

                    });
            //si le agregamos un .block lo hacemos sincronico
            //osea que bloquea el proceso hasta que recibimos una sorpresa
            //ResponseLogin response = monoResponse.block();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Mono.just(new ResponseLogin("99", e.getMessage(), "", ""));
        }

    }

}
