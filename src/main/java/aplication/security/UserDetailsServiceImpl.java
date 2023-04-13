package aplication.security;

import aplication.model.classes.UserInfo;
import aplication.model.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(username);
        if(userInfo == null) {
            throw new UsernameNotFoundException("Could not find User");
        }
        return new MyUserDetails(userInfo);
    }
}
