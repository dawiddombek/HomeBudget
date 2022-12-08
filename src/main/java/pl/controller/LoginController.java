package pl.controller;

import pl.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("registerNewHome")
    public String postRegister(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("homeName") String homeName) {
        if(loginService.checkIfUserExist(username) == null && loginService.checkIfHomeExist(homeName) == null) {
            loginService.saveNewAccount(username, password, homeName, "Admin");
            return "login";
        }
        return "register";
    }
}
