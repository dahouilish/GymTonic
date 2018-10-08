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
                //TODO: delete switch - display is conditionnal
                case 1:
                    return "redirect:/customerPage";
                case 2:
                    return "redirect:/customerPage";
                default:
                    System.out.println("Cas par defaut du switch");
                    return "redirect:/";
            }
            //return "redirect:/customerPage";

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
            displayAlertMessage("Vous êtes déjà inscrit ! : " + LoginForm.getMail());
            //System.out.println("LE MAIL ENTRE EST : " + LoginForm.getMail());
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
        //customerDao.findCustomerByMail(LoginForm.getMail())
                //customer.getMail();TODO HELLO YOU
        customerDao.save(customer);
        System.out.println("LE MAIL 222 ENTRE EST : " + customer.getMail());
        System.out.println("LE MAIL 222 ENTRE EST : " + customer.getFirstName());
        displayAlertMessage("L'adresse mail est : " + customer.getMail());
        //test = true;
        //currentMail = customer.getMail();
        displayAlertMessage("Vous êtes inscrit, bienvenue " + customer.getFirstName() + " !");
        //TODO : si on veut rediriger directement sur customerPage, il faudra set mail et pwd de loginForm (static...)
        //OU PAS ! ducon va , pointe juste sur login apres inscription et c good
        return "redirect:/login";
    }


    @GetMapping("/newProgram")
    public String addProgramForm(Model model) {
        //model.addAttribute("customer", new Customer());
        return "new_program";
    }

    @PostMapping("/newProgram")
    public String addProgram(Program program, Model model) {
        program.setMail(LoginForm.getMail());
        program.setImc(program.getWeight() / (program.getHeight() * program.getHeight())); //IMC = Poids / Taille^2

        String choice1 = "";
        String choice2 = "";
        String choice3 = "";

        if (program.getImc() < 18.5F){choice1 = "A1";}
        if ( (program.getImc() >= 18.5F) & (program.getImc() <= 25.0F)){choice1 = "A2";}
        if (program.getImc() > 25.0F){choice1 = "A3";}

        if (program.getFrequence().equals("1 à 2 fois")){choice2 = "B1";}
        if (program.getFrequence().equals("3 à 4 fois")){choice2 = "B2";}
        if (program.getFrequence().equals("5 à 7 fois")){choice2 = "B3";}

        if (program.getGoal().equals("Prise de muscle")){choice3 = "C1";}
        if (program.getGoal().equals("Renforcement")){choice3 = "C2";}
        if (program.getGoal().equals("Perte de poids")){choice3 = "C3";}

        program.setChainOfChoices(choice1 + "_" + choice2 + "_" + choice3);

        programDao.save(program);
        return "redirect:/customerPage";
    }

    @GetMapping("/customerPage")
    public String displayCustomerPage(Model model) {

        String m = LoginForm.getMail();
        //System.out.println("mail : " + m);
        if (m == null){ //&& test == false) {
            //m = currentMail;
            displayAlertMessage("Vous tentez d'accéder à une page non autorisée");
            return "redirect:/login";
        }

        Customer c = customerDao.findCustomerByMail(m);
        switch (c.getRole()) {
            case 1:
                model.addAttribute("c", c); //display user info
                model.addAttribute("p", programDao.findProgramsByMail(m)); //display user program info
            case 2:
                model.addAttribute("c", c); //injected to test role
                model.addAttribute("data", customerDao.findAll()); //used to display all data user
            default:
                System.out.println("Cas par defaut du switch");
        }
        return "customer_page";
    }

    @GetMapping("/deleteCustomer")
    public String deleteThisCustomer(String mail) {
        customerDao.deleteCustomerByMail(mail);
        //customerDao.delete(mail);
        displayAlertMessage("User supprimé : " + mail);
        return "redirect:/";
    }

    @GetMapping("/error")
    public String displayError() {
        //displayAlertMessage("Erreur technique, redirection...");
        return "error";
    }


    /*@GetMapping("/adminPage")
    public String displayAdminPage(Model model) {

        String m = LoginForm.getMail();
        //System.out.println("mail : " + m);
        if (m == null){ //&& test == false) {
            //m = currentMail;
            displayAlertMessage("Vous tentez d'accéder à une page non autorisée");
            return "redirect:/login";
        }

        System.out.println(customerDao.findAll()); //Debug
        model.addAttribute("data", customerDao.findAll());
        return "admin_page";
    }*/



    private void displayAlertMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Erreur");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }


}

