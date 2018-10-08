package epf.projectgymtonic.persistence;

import epf.projectgymtonic.models.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 10/09/2018
 */
@Repository
public interface ProgramsDAO extends JpaRepository<Program, Integer> {

    @Query("SELECT p FROM Program p WHERE p.mail = :Mail")
    List<Program> findProgramsByMail(@Param("Mail") String Mail);

    @Transactional
    @Modifying
    @Query("DELETE FROM Program p WHERE p.mail = 'temporaryUser@gymtonic.com'")
    void deleteTemporaryPrograms();

    @Query("SELECT p FROM Program p WHERE p.mail = 'temporaryUser@gymtonic.com'")
    Program selectTemporaryProgram();

}
