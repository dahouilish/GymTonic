package epf.projectgymtonic;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.models.GymTonicProgram;
import epf.projectgymtonic.models.ProgramAttribution;
import epf.projectgymtonic.models.Program;
import epf.projectgymtonic.persistence.CustomersDAO;
import epf.projectgymtonic.persistence.GymTonicProgramsDAO;
import epf.projectgymtonic.persistence.ProgramsAttributionDAO;
import epf.projectgymtonic.persistence.ProgramsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories
public class MainAppGymtonic {

    @Autowired
    private CustomersDAO customersDao;
    @Autowired
    private ProgramsDAO programsDao;
    @Autowired
    private ProgramsAttributionDAO programsAttributionDao;
    @Autowired
    private GymTonicProgramsDAO gymTonicProgramsDao;

    public static void main(String[] args) {
        // Point d'entrée de l'application.
        // On dit à Spring de s'initialiser
        // Il va aller "regarder" nos classes et détecter les annotations des singletons
        // (@Controller, @Repository, @Component, @Service, etc...)
        // Ensuite, il va construire l'arbre de dépendances et le résoudre en injectant les données dans les bonnes classes
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainAppGymtonic.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    /**
     * On appelle cette méthode lorsque Spring a terminé son initialisation.
     * Ici, on va réinitialiser la DB et insérer 3 entrées.
     */
    @PostConstruct
    public void init() {
        /**UTILISATEURS TEST*/
        customersDao.deleteAll();//TODO supprimer cette ligne à la fin
        customersDao.save(new Customer(null, "David", "Bernadet", "david.bernadet@yahoo.fr", "david", "1996-03-02", "Monsieur", 1, false));
        customersDao.save(new Customer(null,"Romain", "Cogen", "romain.cogen@epfedu.fr", "romain", "1996-12-24", "Monsieur", 1, false));
        customersDao.save(new Customer(null,"Lancelot", "Du Lac", "lancelot.dulac@epfedu.fr", "lance", "1996-05-14", "Monsieur", 1, false));
        /**ADMIN*/
        customersDao.save(new Customer(null, "admin", "admin", "admin", "admin", "admin", "admin", 2, false));
        /**PROGRAMMES GYMTONIC*/
        gymTonicProgramsDao.save(new GymTonicProgram(null, "DG", "From David to Goliath", ""));
        gymTonicProgramsDao.save(new GymTonicProgram(null, "F", "Fast Summer Body", ""));
        gymTonicProgramsDao.save(new GymTonicProgram(null, "C", "Consulte ton Médecin !", ""));
        gymTonicProgramsDao.save(new GymTonicProgram(null, "D", "Décrassage Express", ""));
        gymTonicProgramsDao.save(new GymTonicProgram(null, "S", "Sahara Dry", ""));
        gymTonicProgramsDao.save(new GymTonicProgram(null, "B", "Objectif Bulldozer", ""));
        gymTonicProgramsDao.save(new GymTonicProgram(null, "FG", "Fonte des Glaces", ""));
        /**LIENS PROGRAMMES_GYMTONIC--CHAINES_DE_CHOIX */
        //_______A1 (IMC : maigreur)
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B1_C1", "DG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B1_C2", "F"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B1_C3", "C"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B2_C1", "DG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B2_C2", "F"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B2_C3", "C"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B3_C1", "DG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B3_C2", "F"));
        programsAttributionDao.save(new ProgramAttribution(null, "A1_B3_C3", "C"));
        //_______A2 (IMC : standard)
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B1_C1", "DG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B1_C2", "D"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B1_C3", "D"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B2_C1", "DG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B2_C2", "F"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B2_C3", "S"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B3_C1", "DG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B3_C2", "F"));
        programsAttributionDao.save(new ProgramAttribution(null, "A2_B3_C3", "S"));
        //_______A3 (IMC : surpoids)
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B1_C1", "B"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B1_C2", "FG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B1_C3", "FG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B2_C1", "B"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B2_C2", "FG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B2_C3", "FG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B3_C1", "B"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B3_C2", "FG"));
        programsAttributionDao.save(new ProgramAttribution(null, "A3_B3_C3", "FG"));


        /**PROGRAMMES TEST*/
        programsDao.save(new Program(null, "david.bernadet@yahoo.fr", 70, 1.80F, null, "3-4", "muscle", "A1_B2_C1", "Fast Summer Body"));
        programsDao.save(new Program(null, "david.bernadet@yahoo.fr", 70, 1.80F, null, "3-4", "endurance", "A1_B1_C3", "From David to Goliath"));
        programsDao.save(new Program(null, "romain.cogen@epfedu.fr", 68, 1.80F, null, "1-2", "muscle", "A2_B3_C1", "T'es déjà parfait frr"));

    }
}
