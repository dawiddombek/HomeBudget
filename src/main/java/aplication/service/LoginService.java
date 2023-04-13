package aplication.service;

import aplication.model.classes.Account;
import aplication.model.classes.Home;
import aplication.model.classes.Role;
import aplication.model.classes.UserInfo;
import aplication.model.repositories.AccountRepository;
import aplication.model.repositories.HomeRepository;
import aplication.model.repositories.RoleRepository;
import aplication.model.repositories.UserInfoRepository;
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

    public void addNewAdmin(String username, String password, String homeName, String roleName) {
        Home home = new Home(homeName);
        addNewUser(username, password, home, roleName);
    }

    public void addNewUser(String username, String password, Home home, String roleName) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(username);
        if(userInfo == null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            UserInfo user = new UserInfo(username, bCryptPasswordEncoder.encode(password), true);
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
}
