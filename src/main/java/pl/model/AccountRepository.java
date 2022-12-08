package pl.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("SELECT u from Account u Where u.userInfo = :userInfo")
    public Account getAccountByUserInfo(@Param("userInfo") UserInfo userInfo);
}
