package pl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.model.UserInfoRepository;

@Controller
@RequestMapping("/")
public class HomeController {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public HomeController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/homePage")
    public String getHomePage(Model model) {
        model.addAttribute("home", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getAccount().getHome());
        return "homePage";
    }
}
