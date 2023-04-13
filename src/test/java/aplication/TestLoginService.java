package aplication;

import aplication.model.classes.Home;
import aplication.model.classes.UserInfo;
import aplication.model.repositories.HomeRepository;
import aplication.model.repositories.UserInfoRepository;
import aplication.service.HomeService;
import aplication.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestLoginService {

    private final LoginService loginService;

    private final HomeService homeService;

    private final UserInfoRepository userInfoRepository;

    private final HomeRepository homeRepository;

    @Autowired
    public TestLoginService(LoginService loginService, HomeService homeService, UserInfoRepository userInfoRepository, HomeRepository homeRepository) {
        this.loginService = loginService;
        this.homeService = homeService;
        this.userInfoRepository = userInfoRepository;
        this.homeRepository = homeRepository;
    }

    @Test
    public void testCheckIfUserExist() {
        UserInfo userInfo1 = loginService.checkIfUserExist("Dawid");

        assertEquals("Dawid", userInfo1.getUsername());
    }

    @Test
    public void testCheckIfHomeExist() {
        Home home = loginService.checkIfHomeExist("Dom Dawida");

        assertEquals("Dom Dawida", home.getName());
    }

    @Test
    public void testAddNewUser() {
        Home home = new Home("Test Home");
        loginService.addNewUser("Test", "Test", home, "Użytkownik");

        assertEquals("Test", userInfoRepository.getUserInfoByUsername("Test").getUsername());

        UserInfo userInfo = userInfoRepository.getUserInfoByUsername("Test");

        userInfoRepository.delete(userInfo);
        homeRepository.delete(userInfo.getAccount().getHome());

    }
}
