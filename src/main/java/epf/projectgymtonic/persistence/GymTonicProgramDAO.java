package epf.projectgymtonic.persistence;

import epf.projectgymtonic.models.GymTonicProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
 */
@Service
@Repository
public interface GymTonicProgramDAO extends JpaRepository<GymTonicProgram, Integer> {

    @Query("SELECT p FROM GymTonicProgram p WHERE p.code = :Code")
    GymTonicProgram findProgramByCode(@Param("Code") String Code);

}
