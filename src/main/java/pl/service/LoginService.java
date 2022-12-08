package pl.service;

import pl.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AccountRepository accountRepository;

    private final HomeRepository homeRepository;

    private final UserInfoRepository userInfoRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public LoginService(AccountRepository accountRepository, HomeRepository homeRepository, UserInfoRepository userInfoRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.homeRepository = homeRepository;
        this.userInfoRepository = userInfoRepository;
        this.roleRepository = roleRepository;
    }

    public UserInfo checkIfUserExist(String username) {
        return userInfoRepository.getUserInfoByUsername(username);
    }

    public Home checkIfHomeExist(String homeName) {
        return homeRepository.getHomeByHomeName(homeName);
    }

    public void saveNewAccount(String username, String password, String homeName, String roleName) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserInfo user = new UserInfo(username, bCryptPasswordEncoder.encode(password), true);
        Home home = new Home(homeName);
        Account account = new Account(0);
        Role role =  new Role(roleName);

        accountRepository.save(account);
        homeRepository.save(home);
        userInfoRepository.save(user);
        roleRepository.save(role);

        home.getAccounts().add(account);
        account.setHome(home);
        account.setUserInfo(user);
        user.setAccount(account);
        user.getRoles().add(role);

        accountRepository.save(account);
        homeRepository.save(home);
        userInfoRepository.save(user);
    }
}
