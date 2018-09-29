package epf.projectgymtonic.persistence;

import epf.projectgymtonic.models.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 10/09/2018
 */
@Repository
public interface ProgramDAO extends JpaRepository<Program, Integer> {


    @Query("SELECT * FROM Program program WHERE program.mail = :Mail")
    Program findProgramsByMail(@Param("Mail") String Mail);

}
