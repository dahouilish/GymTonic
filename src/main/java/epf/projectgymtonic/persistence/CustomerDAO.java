package epf.projectgymtonic.persistence;

import epf.projectgymtonic.models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 10/09/2018
 */
@Repository
public interface CustomerDAO extends CrudRepository<Customer, Integer> {

}
