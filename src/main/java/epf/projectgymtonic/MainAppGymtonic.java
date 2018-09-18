package epf.projectgymtonic;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.persistence.CustomerDAO;
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
        customerDao.deleteAll();//TODO supprimer cette ligne à la fin
        customerDao.save(new Customer(null,"David", "Bernadet","david.bernadet@yahoo.fr","david","1996-03-02","Monsieur", 1, false));
        customerDao.save(new Customer(null, "Romain", "Cogen","romain.cogen@epfedu.fr","romain","1996-12-24","Monsieur", 1, false));
        customerDao.save(new Customer(null, "Lancelot", "Du Lac","lancelot.dulac@epfedu.fr","lance","1996-05-14","Monsieur", 1, false));
        /**ADMIN*/
        customerDao.save(new Customer(null, "admin", "admin", "admin", "admin", "admin", "admin", 2, false));
    }
}