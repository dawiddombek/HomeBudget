package pl.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HomeRepository extends CrudRepository<Home, Long> {

    @Query("SELECT h from Home h Where h.name = :homeName")
    public Home getHomeByHomeName(@Param("homeName") String homeName);
}
