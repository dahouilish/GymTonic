package epf.projectgymtonic;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.persistence.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories
public class MainAppGymtonic {

    @Autowired
    private CustomerDAO customerDao;

    public static void main(String[] args) {
        // Point d'entrée de l'application.
        // On dit à Spring de s'initialiser
        // Il va aller "regarder" nos classes et détecter les annotations des singletons
        // (@Controller, @Repository, @Component, @Service, etc...)
        // Ensuite, il va construire l'arbre de dépendances et le résoudre en injectant les données dans les bonnes classes
        SpringApplication.run(MainAppGymtonic.class);
    }

    /**
     * On appelle cette méthode lorsque Spring a terminé son initialisation.
     * Ici, on va réinitialiser la DB et insérer 3 entrées.
     */
    @PostConstruct
    public void init() {
        customerDao.deleteAll();
        customerDao.save(new Customer(null,"Loic", "Ortola","loic@hotmail.fr","mypass","hello","Monsieur"));
        customerDao.save(new Customer(null, "Ambroise", "Soullier","ambroise@gmail.com","12345","le23","Madame"));
        customerDao.save(new Customer(null, "Harry", "Covert","harry.cover@outlook.fr","azerty","le12","Monsieur"));
        /**ADMIN*/
        customerDao.save(new Customer(null, "admin", "admin", "admin", "admin", "admin", "admin"));
    }
}