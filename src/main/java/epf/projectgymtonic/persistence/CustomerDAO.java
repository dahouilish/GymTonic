package epf.projectgymtonic.persistence;

import epf.projectgymtonic.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 10/09/2018
 */
@Service
@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer>{

    @Query("SELECT customer FROM Customer customer WHERE customer.mail = :Mail")
    Customer findCustomerByMail(@Param("Mail") String Mail);

    //@Query("SELECT c FROM Customer c WHERE c.mail = :Mail")
    //Customer findMailAlreadyUsed(@Param("Mail") String Mail);

}

