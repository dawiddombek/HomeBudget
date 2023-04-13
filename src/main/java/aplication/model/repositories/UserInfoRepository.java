package aplication.model.repositories;

import aplication.model.classes.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query("SELECT u from UserInfo u Where u.username = :username")
    public UserInfo getUserInfoByUsername(@Param("username") String username);
}
