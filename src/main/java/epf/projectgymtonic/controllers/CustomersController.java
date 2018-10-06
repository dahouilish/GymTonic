package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.form.LoginForm;
import epf.projectgymtonic.models.GymTonicProgram;
import epf.projectgymtonic.models.ProgramAttribution;
import epf.projectgymtonic.models.Program;
import epf.projectgymtonic.persistence.CustomersDAO;
import epf.projectgymtonic.persistence.GymTonicProgramsDAO;
import epf.projectgymtonic.persistence.ProgramsAttributionDAO;
import epf.projectgymtonic.persistence.ProgramsDAO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

/**
 * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
 */
@Controller
public class CustomersController {

    private final CustomersDAO customersDao;
    private final ProgramsDAO programsDao;
    private final ProgramsAttributionDAO programsAttributionDao;
    private final GymTonicProgramsDAO gymTonicProgramsDao;
    //private boolean test = false;
    //private String currentMail;

    public CustomersController(CustomersDAO _customersDao, ProgramsDAO _programsDao,
                               ProgramsAttributionDAO _programsAttributionDao, GymTonicProgramsDAO _gymTonicProgramsDao) {
        this.customersDao = _customersDao;
        this.programsDao = _programsDao;
        this.programsAttributionDao = _programsAttributionDao;
        this.gymTonicProgramsDao = _gymTonicProgramsDao;
    }

    /*
     * Ceci sera mappé sur l'URL '/users'.
     * C'est le routeur de Spring MVC qui va détecter et appeler directement cette méthode.
     * Il lui fournira un "modèle", auquel on pourra rajouter des attributs.
     * Ce modèle sera ensuite forwardé à une page web (dans resources/templates).
     * Le nom de la template est retourné par la fonction. Ici, elle appelle donc le template "users".
     * @param model le modèle
     * @return
     */

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm() {
        if (LoginForm.getMail() != null) {
            displayAlertMessage("Erreur", "Vous etes déjà connecté");
            return "redirect:/";
        }
        displayAlertMessage("Erreur", "Vous n'etes pas encore connecté");
        return "login";
    }

    //TODO : afficher slmt si connecté !
    @GetMapping(value = "/exit")
    public String exit(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {
        Customer currentCustomer = customersDao.findCustomerByMail(LoginForm.getMail());
        System.out.println("le gus est : " + LoginForm.getMail());
        //System.out.println("le gus est : " + currentCustomer.getAuthenticated());
        //currentCustomer.setAuthenticated(false);
        //System.out.println("le gus est : " + currentCustomer.getAuthenticated());
        if (LoginForm.getMail() == null) {
            displayAlertMessage("Erreur", "Vous n'etes pas encore connecté");
            return "redirect:/";
        }

        loginForm.setMail(null);
        loginForm.setPassword(null);
        displayAlertMessage("Erreur", "Vous vous êtes déconnecté !");
        //customerDao.deleteAll();
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {

        //Recuperation des valeurs des input du formulaire login
        String mail = LoginForm.getMail();
        String password = loginForm.getPassword();
        //Recuperation du customer correspondant au mail
        Customer currentCustomer = customersDao.findCustomerByMail(mail);

        if (currentCustomer == null) {
            System.out.println("USER INCONNU DE LA BDD : FAILURE !");
            displayAlertMessage("Erreur", "L'adresse mail et/ou le mot de passe est invalide");
            loginForm.setMail(null);
            loginForm.setPassword(null);
            return "login";
        }
        /*System.out.println("CurrentCustomer LOGGED IN ? : " + currentCustomer.getAuthenticated());
        currentCustomer.setAuthenticated(true);
        System.out.println("CurrentCustomer LOGGED IN ? : " + currentCustomer.getAuthenticated());*/

        System.out.println("CurrentCustomer : " + currentCustomer);

        String currentMail = currentCustomer.getMail();
        String currentPassword = currentCustomer.getPassword();

        //LOGGER
        //System.out.println("CurrentCustomer mail : " + currentMail + " PWD : " + currentPassword);
        //System.out.println("INPUT : mail : " + mail + " et password : " + password);

        // Test de connexion : si = admin ,alors success
        if (currentMail.equals(mail) && currentPassword.equals(password)) {
            System.out.println("USER RECONNU DANS LA BDD : SUCCESS !");
            switch (currentCustomer.getRole()) {
                case 1:
                    return "redirect:/customerPage";
                case 2:
                    return "redirect:/adminPage";
                default:
                    System.out.println("Cas par defaut du switch");
                    return "redirect:/";
            }

        }
        //model.addAttribute("invalidCredentials", true);
        System.out.println("PASSWORD DOESN'T MATCH : FAILURE !");
        displayAlertMessage("Erreur", "L'adresse mail et/ou le mot de passe est invalide");
        // si failure, rester sur la page login
        return "login";
    }

    //TODO : supprimer ce WS et ajouter le html correspondant à la page des Customers
    /*@GetMapping("/customers")
    public String getCustomers(Model model) {
        model.addAttribute("data", customerDao.findAll());
        return "customers-list";
    }*/

    @GetMapping("/inscription")
    public String addUserForm(Model model) {
        model.addAttribute("customer", new Customer());
        if (LoginForm.getMail() != null) {
            displayAlertMessage("Erreur", "Vous êtes déjà inscrit !");
            System.out.println("LE MAIL ENTRE EST : " + LoginForm.getMail());
            return "redirect:/";
        }
        return "inscription";
    }

    @PostMapping("/inscription")
    public String addCustomer(Customer customer, Model model) {
        System.out.println("LE MAIL ENTRE EST : " + customer.getMail());
        //In case the mail is already used
        if (customersDao.findCustomerByMail(customer.getMail()) != null) {
            displayAlertMessage("Erreur", "L'adresse mail est déjà utilisée.");
            return "redirect:/inscription";
        }
        customersDao.save(customer);
        //test = true;
        //currentMail = customer.getMail();
        displayAlertMessage("Erreur", "Vous êtes inscrit, bienvenue " + customer.getFirstName() + " !");
        //TODO : si on veut rediriger directement sur customerPage, il faudra set mail et pwd de loginForm (static...)
        //OU PAS ! ducon va , pointe juste sur login apres inscription et c good
        return "redirect:/login";
    }


    @GetMapping("/newProgram")
    public String addProgramForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "new_program";
    }

    @PostMapping("/newProgram")
    public String addProgram(Program program, Model model) {
        return newProgram(program, false);
    }

    @GetMapping("/fastProgram")
    public String addFastProgramForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "fast_program";
    }

    @PostMapping("/fastProgram")
    public String addFastProgram(Program program, Model model) {
        return newProgram(program, true);
    }

    @NotNull
    private String newProgram(Program program, Boolean fastProgram) {
        if (!fastProgram) {
            program.setMail(LoginForm.getMail());
        }
        program.setImc(program.getWeight() / (program.getHeight() * program.getHeight())); //IMC = Poids / Taille^2

        String choice1 = "";
        String choice2 = "";
        String choice3 = "";

        if (program.getImc() < 18.5F) {
            choice1 = "A1";
        }
        if ((program.getImc() >= 18.5F) & (program.getImc() <= 25.0F)) {
            choice1 = "A2";
        }
        if (program.getImc() > 25.0F) {
            choice1 = "A3";
        }

        if (program.getFrequence().equals("0 fois")) {
            choice2 = "B1";
        }
        if (program.getFrequence().equals("1 à 2 fois")) {
            choice2 = "B2";
        }
        if (program.getFrequence().equals("3 à 7 fois")) {
            choice2 = "B3";
        }

        if (program.getGoal().equals("Prise de masse")) {
            choice3 = "C1";
        }
        if (program.getGoal().equals("Renforcement musculaire")) {
            choice3 = "C2";
        }
        if (program.getGoal().equals("Perte de poids")) {
            choice3 = "C3";
        }

        program.setChainOfChoices(choice1 + "_" + choice2 + "_" + choice3);

        ProgramAttribution programAttribution = programsAttributionDao.findProgramByChainOfChoices(program.getChainOfChoices());
        GymTonicProgram gymTonicProgram = gymTonicProgramsDao.findProgramByCode(programAttribution.getCode());


        program.setProposedProgram(gymTonicProgram.getName());

        if (!fastProgram) {
            displayAlertMessage("Votre programme", gymTonicProgram.getName() + "\n\nRetrouvez les détails du programme dans votre Page perso");
        } else {
            displayAlertMessage("Votre programme", gymTonicProgram.getName()  + "\n\nRetrouvez les détails du programme dans votre Page perso, \n" +
                    "Inscrivez-vous pour pouvoir enregistrer vos programmes.");
        }

        //TODO :
        //si fast : on affiche le programme obtenu +
        // + "Pour enregistrer ce programme, inscrivez-vous : " + on affiche un bouton d'inscription
        //modifier inscritpion pour qu'elle puisse push le programme gardé en mémoire tant que le gars n'aie
        //pas fini son inscription

        programsDao.save(program);
        if (fastProgram) {
            return "redirect:/";
        } else {
            return "redirect:/customerPage";
        }
    }

    @GetMapping("/customerPage")
    public String displayCustomerPage(Model model) {

        String m = LoginForm.getMail();
        //System.out.println("mail : " + m);
        if (m == null) { //&& test == false) {
            //m = currentMail;
            displayAlertMessage("Erreur", "VOUS TENTEZ D'ACCEDER A UNE PAGE NON AUTORISEE");
            return "redirect:/login";
        }
        //System.out.println("mail : " + m);
        //test = false;
        Customer c = customersDao.findCustomerByMail(m);
        model.addAttribute("c", c); //Afficher

        model.addAttribute("p", programsDao.findProgramsByMail(m)); //Afficher tous les programmes d'un customer


        //Afficher liste entiere :
        //model.addAttribute("data", customerDao.findAll());

        return "customer_page";
    }

    @GetMapping("/adminPage")
    public String displayAdminPage(Model model) {
        System.out.println(customersDao.findAll()); //Debug
        model.addAttribute("data", customersDao.findAll());
        return "admin_page";
    }

    private void displayAlertMessage(String title, String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

}

