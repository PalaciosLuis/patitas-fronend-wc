package pe.edu.cibertec.patitas_frontend_wc_a.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.RequestLogin;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.ResponseLogin;
import pe.edu.cibertec.patitas_frontend_wc_a.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

@Controller

@RequestMapping("/login")
public class LoginController {

  @Autowired
  WebClient webClientAutenticacion;
  //RestTemplate restTemplate;


  @GetMapping("/inicio")
  public String inicio(Model model){

    LoginModel loginmodel = new LoginModel("00","","Pepe");
    model.addAttribute("loginmodel",loginmodel);

    return "inicio";
  }

  @PostMapping("/autenticar")
  public String autenticar(@RequestParam("tipoDocumento") String tipodocumento,
                           @RequestParam("numeroDocumento") String numerodocumento,
                           @RequestParam("password")String password,
                           Model model){

    //TAREA: Creamos la variable para enviar los datos al front
    LoginModel loginmodel;

    //Hecho en clase (mas optimo)

    //Validamos los campos
    if (tipodocumento == null || tipodocumento.trim().length() == 0 ||
    numerodocumento == null || numerodocumento.trim().length() == 0 ||
      password == null || password.trim().length() == 0){


      //En caso no ingresen todos los datos, salta este error
      loginmodel = new LoginModel("01","Datos insuficientes","");
      model.addAttribute("loginmodel",loginmodel);
      return "inicio";
    }

    try {
      //hacemos la solicitud
      RequestLogin request = new RequestLogin(tipodocumento, numerodocumento, password);
      //y recibimos la data (response)
      Mono<ResponseLogin> monoResponse = webClientAutenticacion.post()
        .uri("/login")
        .body(Mono.just(request), RequestLogin.class)
        .retrieve()
        .bodyToMono(ResponseLogin.class);
      //si le agregamos un .block lo hacemos sincronico
      //osea que bloquea el proceso hasta que recibimos una sorpresa
      ResponseLogin response = monoResponse.block();

      if(response.codigo().equals("00")){
        loginmodel = new LoginModel("00",
          "",response.nombreUsuario());
        model.addAttribute("loginmodel",loginmodel);
        return "principal";
      }
      else {
        loginmodel = new LoginModel("01","Datos insuficientes","");
        model.addAttribute("loginmodel",loginmodel);
        return "inicio";
      }

    }catch (Exception e){
      loginmodel = new LoginModel("99","Servicio no responde","");
      model.addAttribute("loginmodel",loginmodel);
      System.out.println(e.getMessage());
      return "inicio";
    }

  }

}
