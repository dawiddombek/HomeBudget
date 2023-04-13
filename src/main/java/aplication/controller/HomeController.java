package aplication.controller;

import aplication.model.classes.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import aplication.model.repositories.UserInfoRepository;
import aplication.service.HomeService;

@Controller
@RequestMapping("/")
public class HomeController {

    private final UserInfoRepository userInfoRepository;

    private final HomeService homeService;

    @Autowired
    public HomeController(UserInfoRepository userInfoRepository, HomeService homeService) {
        this.userInfoRepository = userInfoRepository;
        this.homeService = homeService;
    }

    @GetMapping("/homePage")
    public String getHomePage(Model model) {
        model.addAttribute("home", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getAccount().getHome());
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "homePage";
    }

    @GetMapping("/viewUserPage")
    public String getViewUserPage(@RequestParam("accountId") Long id) {
        homeService.setUserView(id);
        return "redirect:/userPage";
    }

    @GetMapping("/addNewUserPage")
    public String getAddNewUser() {
        return "newUserPage";
    }

    @PostMapping("/addNewUser")
    public String postAddNewUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("role") String role) {
        String homeName = userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getAccount().getHome().getName();
        homeService.addNewUserToHome(username, password, homeName, role);
        return "redirect:/homePage";
    }

    @GetMapping("/deleteAccount")
    public String getDeleteAccount(@RequestParam("accountId") Long id) {
        if(userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getAccount().getId().equals(id)) {
            homeService.deleteAccount(id);
            return "redirect:/logout";
        }
        homeService.deleteAccount(id);
        return "redirect:/homePage";
    }

    @GetMapping("/deleteYourAccount")
    public String getDeleteYourAccount() {
        homeService.deleteAccount(userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getAccount().getId());
        return "redirect:/logout";
    }

    @GetMapping("/backToUserPage")
    public String getBackToUserPage() {
        homeService.updateUserViewInRepository(SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/userPage";
    }
}
