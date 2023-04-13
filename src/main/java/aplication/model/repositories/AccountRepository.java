package aplication.model.repositories;

import aplication.model.classes.Account;
import aplication.model.classes.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("SELECT u from Account u Where u.userInfo = :userInfo")
    public Account getAccountByUserInfo(@Param("userInfo") UserInfo userInfo);

    @Query("SELECT u from Account u Where u.id = :id")
    public Account getAccountById(@Param("id") Long id);

}
