package epf.projectgymtonic.persistence;

import epf.projectgymtonic.models.ProgramAttribution;
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
public interface ProgramsAttributionDAO extends JpaRepository<ProgramAttribution, Integer> {

    @Query("SELECT p FROM ProgramAttribution p WHERE p.chainOfChoices = :ChainOfChoices")
    ProgramAttribution findProgramByChainOfChoices(@Param("ChainOfChoices") String ChainOfChoices);

}
