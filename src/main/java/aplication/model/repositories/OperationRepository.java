package aplication.model.repositories;

import aplication.model.classes.Operation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OperationRepository  extends CrudRepository<Operation, Long> {

    @Query("SELECT u from Operation u Where u.id = :id")
    public Operation getOperationById(@Param("id") Long id);
}
