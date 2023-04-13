package aplication.service;

import aplication.model.classes.Account;
import aplication.model.classes.Home;
import aplication.model.classes.UserInfo;
import aplication.model.repositories.AccountRepository;
import aplication.model.repositories.HomeRepository;
import aplication.model.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    private final AccountRepository accountRepository;

    private final HomeRepository homeRepository;

    private final UserInfoRepository userInfoRepository;

    private final LoginService loginService;

    @Autowired
    public HomeService(AccountRepository accountRepository, HomeRepository homeRepository, UserInfoRepository userInfoRepository, LoginService loginService) {
        this.accountRepository = accountRepository;
        this.homeRepository = homeRepository;
        this.userInfoRepository = userInfoRepository;
        this.loginService = loginService;
    }

    public void setUserView(Long id) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userInfo.getAccount().setUserView(accountRepository.getAccountById(id).getUserInfo().getUsername());
        userInfoRepository.save(userInfo);
    }

    public void addNewUserToHome(String username, String password, String homeName, String roleName) {
        Home home = homeRepository.getHomeByHomeName(homeName);
        loginService.addNewUser(username, password, home, roleName);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.getAccountById(id);
        String homeName = account.getHome().getName();
        accountRepository.delete(account);
        if(homeRepository.getHomeByHomeName(homeName).getAccounts().isEmpty()) {
            Home home = homeRepository.getHomeByHomeName(homeName);
            homeRepository.delete(home);
        }

    }

    public void updateUserViewInRepository(String username) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(username);
        userInfo.getAccount().setUserView(username);
        userInfoRepository.save(userInfo);
    }
}
