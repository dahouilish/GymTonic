package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.form.LoginForm;
import epf.projectgymtonic.models.Program;
import epf.projectgymtonic.persistence.CustomerDAO;
import epf.projectgymtonic.persistence.ProgramDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

/**
 * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
 */
@Controller
public class CustomersController {

    private final CustomerDAO customerDao;
    private final ProgramDAO programDao;
    //private boolean test = false;
    //private String currentMail;

    public CustomersController(CustomerDAO customerDao, ProgramDAO programDao) {
        this.customerDao = customerDao;
        this.programDao = programDao;
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
            displayAlertMessage("Vous etes déjà connecté");
            return "redirect:/";
        }
        displayAlertMessage("Vous n'etes pas encore connecté");
        return "login";
    }

    //TODO : afficher slmt si connecté !
    @GetMapping(value = "/exit")
    public String exit(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {
        Customer currentCustomer = customerDao.findCustomerByMail(LoginForm.getMail());
        System.out.println("le gus est : " + LoginForm.getMail());
        //System.out.println("le gus est : " + currentCustomer.getAuthenticated());
        //currentCustomer.setAuthenticated(false);
        //System.out.println("le gus est : " + currentCustomer.getAuthenticated());
        if (LoginForm.getMail() == null){
            displayAlertMessage("Vous n'etes pas encore connecté");
            return "redirect:/";
        }

        loginForm.setEmail(null);
        loginForm.setPassword(null);
        displayAlertMessage("Vous vous êtes déconnecté !");
        //customerDao.deleteAll();
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {

        //Recuperation des valeurs des input du formulaire login
        String email = loginForm.getMail();
        String password = loginForm.getPassword();
        //Recuperation du customer correspondant au mail
        Customer currentCustomer = customerDao.findCustomerByMail(email);

        if (currentCustomer == null) {
            System.out.println("USER INCONNU DE LA BDD : FAILURE !");
            displayAlertMessage("L'adresse mail et/ou le mot de passe est invalide");
            loginForm.setEmail(null);
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
        //System.out.println("INPUT : Email : " + email + " et password : " + password);

        // Test de connexion : si = admin ,alors success
        if (currentMail.equals(email) && currentPassword.equals(password)) {
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
        displayAlertMessage("L'adresse mail et/ou le mot de passe est invalide");
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
            displayAlertMessage("Vous êtes déjà inscrit !");
            System.out.println("LE MAIL ENTRE EST : " + LoginForm.getMail());
            return "redirect:/";
        }
        return "inscription";
    }

    @PostMapping("/inscription")
    public String addCustomer(Customer customer, Model model) {
        System.out.println("LE MAIL ENTRE EST : " + customer.getMail());
        //In case the mail is already used
        if (customerDao.findCustomerByMail(customer.getMail()) != null) {
            displayAlertMessage("L'adresse mail est déjà utilisée.");
            return "redirect:/inscription";
        }
        customerDao.save(customer);
        //test = true;
        //currentMail = customer.getMail();
        displayAlertMessage("Vous êtes inscrit, bienvenue " + customer.getFirstName() + " !");
        //TODO : si on veut rediriger directement sur customerPage, il faudra set mail et pwd de loginForm (static...)
        //OU PAS ! ducon va , pointe juste sur login apres inscription et c good
        return "redirect:/login";
    }

    @GetMapping("/customerPage")
    public String displayCustomerPage(Model model) {

        String m = LoginForm.getMail();
        //System.out.println("mail : " + m);
        if (m == null){ //&& test == false) {
            //m = currentMail;
            displayAlertMessage("VOUS TENTEZ D'ACCEDER A UNE PAGE NON AUTORISEE");
            return "redirect:/login";
        }
        //System.out.println("mail : " + m);
        //test = false;
        Customer customer = customerDao.findCustomerByMail(m);
        model.addAttribute("customer", customer); //Afficher

        System.out.println("ici viens le bug");
        //model.addAttribute("program", programDao.findProgramsByMail(m)); //Afficher tous les programmes


        //Afficher liste entiere :
        //model.addAttribute("data", customerDao.findAll());

        return "customer_page";
    }

    @GetMapping("/adminPage")
    public String displayAdminPage(Model model) {
        model.addAttribute("data", customerDao.findAll());
        return "admin_page";
    }

    private void displayAlertMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Erreur");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }


}

