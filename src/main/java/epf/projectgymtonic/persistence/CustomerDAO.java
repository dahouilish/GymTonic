package epf.projectgymtonic.persistence;

import epf.projectgymtonic.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
 */
@Service
@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer>{

    @Query("SELECT c FROM Customer c WHERE c.mail = :Mail")
    Customer findCustomerByMail(@Param("Mail") String Mail);

    @Transactional
    @Modifying
    @Query("DELETE FROM Customer c WHERE c.mail = :Mail")
    void deleteCustomerByMail(@Param("Mail") String Mail);

}

