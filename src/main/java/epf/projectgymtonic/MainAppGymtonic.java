package epf.projectgymtonic;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.models.GymTonicProgram;
import epf.projectgymtonic.models.Program;
import epf.projectgymtonic.models.ProgramAttribution;
import epf.projectgymtonic.persistence.CustomerDAO;
import epf.projectgymtonic.persistence.GymTonicProgramDAO;
import epf.projectgymtonic.persistence.ProgramAttributionDAO;
import epf.projectgymtonic.persistence.ProgramDAO;
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
    private CustomerDAO customerDao;
    @Autowired
    private ProgramDAO programDao;
    @Autowired
    private ProgramAttributionDAO programAttributionDao;
    @Autowired
    private GymTonicProgramDAO gymTonicProgramDao;

    public static void main(String[] args) {
        // initialization of the app, access other class from here
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainAppGymtonic.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    /**
     * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
     */

    @PostConstruct
    public void init() {
        /**UTILISATEURS TEST*/
        //customerDao.deleteAll();//TODO supprimer cette ligne à la fin
        customerDao.save(new Customer("David", "Bernadet", "david.bernadet@yahoo.fr", "david", "1996-03-02", "Homme", 1));
        customerDao.save(new Customer("Romain", "Cogen", "romain.cogen@epfedu.fr", "romain", "1996-12-24", "Homme", 1));
        customerDao.save(new Customer("Lancelot", "Du Lac", "lancelot.dulac@epfedu.fr", "lance", "1996-05-14", "Homme", 1));

        /**ADMIN*/
        customerDao.save(new Customer("admin", "admin", "admin", "admin", "admin", "admin", 2));

        /**PROGRAMMES GYMTONIC*/
        gymTonicProgramDao.save(new GymTonicProgram(null, "DG", "-FROM DAVID TO GOLIATH-", "Dessiner ses muscles. 8 à 12 répétitions par séries. " +
                "8 à 12 séries par groupe musculaire. Charges entre 60% et 75% de la charge maximale. Rythme d'execution lent.", "/assets/img/goliath.jpg"));
        gymTonicProgramDao.save(new GymTonicProgram(null, "F", "-FAST SUMMER BODY-", "Renforcement musculaire pour perdre le gras. " +
                "20 à 30 répétitions par séries. 5 à 10 séries par groupe musculaire. Charges entre 50% et 60% de la charge maximale. Rythme d'éxecution rapide.", "/assets/img/summer.jpg"));
        gymTonicProgramDao.save(new GymTonicProgram(null, "C", "CONSULTE TON MEDECIN !", "Votre volonté de perdre du poids est inquiétante compte tenu de votre état actuel.", "/img/consulte.jpg"));
        gymTonicProgramDao.save(new GymTonicProgram(null, "D", "-DECRASSAGE EXPRESS-", "Remise en forme. Routine quotidienne à rélaiser : " +
                "10 min d'échauffement puis : Air squats, Pompes, Curl, Rowing, Crunch, Planche, Burpees (10 à 20 répétitions, 1 min de repos entre chaque exercice, 1 fois).", "/assets/img/forme.jpg"));
        gymTonicProgramDao.save(new GymTonicProgram(null, "S", "-SAHARA DRY-", "Programme de sèche. 12 à 15 répétitions par séries. " +
                "5 à 10 séries par groupe musculaire. Charges à 50% de la charge maximale. Rythme d'éxecution rapide. 2 séances de 60 min de cardio par semaine.", "/assets/img/dry.jpg"));
        gymTonicProgramDao.save(new GymTonicProgram(null, "B", "-OBJECTIF BULLDOZER-", "Gain en puissance. 3 à 6 répétitions par séries. " +
                "3 à 5 séries par groupe musculaire. Charges entre 80% et 90% de la charge maximale. Rythme d'éxecution maximal.", "/assets/img/wrecking_ball.jpg"));
        gymTonicProgramDao.save(new GymTonicProgram(null, "FG", "-FONTE DES GLACES-", "Perte de poids. 30 min de cardio quotidien :" +
                "Vélo home-trainer, marche.", "/assets/img/fonte.jpg"));

        /**LIENS PROGRAMMES_GYMTONIC--CHAINES_DE_CHOIX*/
        //_______A1 (IMC : maigreur)
        programAttributionDao.save(new ProgramAttribution(null, "A1_B1_C1", "DG"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B1_C2", "F"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B1_C3", "C"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B2_C1", "DG"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B2_C2", "F"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B2_C3", "C"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B3_C1", "DG"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B3_C2", "F"));
        programAttributionDao.save(new ProgramAttribution(null, "A1_B3_C3", "C"));
        //_______A2 (IMC : standard)
        programAttributionDao.save(new ProgramAttribution(null, "A2_B1_C1", "DG"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B1_C2", "D"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B1_C3", "D"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B2_C1", "DG"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B2_C2", "F"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B2_C3", "S"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B3_C1", "DG"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B3_C2", "F"));
        programAttributionDao.save(new ProgramAttribution(null, "A2_B3_C3", "S"));
        //_______A3 (IMC : surpoids)
        programAttributionDao.save(new ProgramAttribution(null, "A3_B1_C1", "B"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B1_C2", "D"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B1_C3", "D"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B2_C1", "B"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B2_C2", "FG"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B2_C3", "FG"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B3_C1", "B"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B3_C2", "FG"));
        programAttributionDao.save(new ProgramAttribution(null, "A3_B3_C3", "FG"));

        /**PROGRAMMES TEST*/
        programDao.save(new Program(null, "david.bernadet@yahoo.fr", 70, 1.80F, null, "3-4",
                "muscle", "A1_B2_C1", "-FAST SUMMER BODY-", "Renforcement musculaire pour perdre le gras. " +
                "20 à 30 répétitions par séries. 5 à 10 séries par groupe musculaire. Charges entre 50% et 60% de la charge maximale. Rythme d'éxecution rapide.", "/assets/img/summer.jpg"));
        programDao.save(new Program(null, "david.bernadet@yahoo.fr", 70, 1.80F, null, "3-4",
                "endurance", "A1_B1_C3", "-FROM DAVID TO GOLIATH-", "Dessiner ses muscles. 8 à 12 répétitions par séries. " +
                "8 à 12 séries par groupe musculaire. Charges entre 60% et 75% de la charge maximale. Rythme d'execution lent.", "/assets/img/goliath.jpg"));
        programDao.save(new Program(null, "romain.cogen@epfedu.fr", 68, 1.80F, null, "1-2",
                "muscle", "A2_B3_C1", "T'es déjà parfait frr", "Rien à ajouter !", null));
    }
}
