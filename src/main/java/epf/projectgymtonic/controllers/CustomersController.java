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

        String email = loginForm.getMail();
        String password = loginForm.getPassword();
        //Get the customer from the mail
        Customer currentCustomer = customerDao.findCustomerByMail(email);
        //in case the password and the mail do not match
        if (currentCustomer == null) {
            displayAlertMessage("L'adresse mail et/ou le mot de passe est invalide");
            loginForm.setEmail(null);
            loginForm.setPassword(null);
            return "login";
        }

        String currentMail = currentCustomer.getMail();
        String currentPassword = currentCustomer.getPassword();

        // Test login : if mail and password do match
        if (currentMail.equals(email) && currentPassword.equals(password)) {
            if (currentCustomer.getRole() == 1 || currentCustomer.getRole() == 2) {
                return "redirect:/customerPage";
            }else{
                return "redirect:/";
            }
        }
        displayAlertMessage("L'adresse mail et/ou le mot de passe est invalide");
        return "login";
    }

    @GetMapping("/inscription")
    public String addUserForm(Model model) {
        model.addAttribute("customer", new Customer());
        if (LoginForm.getMail() != null) {
            displayAlertMessage("Vous êtes déjà inscrit ! : " + LoginForm.getMail());
            return "redirect:/";
        }
        return "inscription";
    }

    @PostMapping("/inscription")
    public String addCustomer(Customer customer, Model model) {
        //In case the mail is already used
        if (customerDao.findCustomerByMail(customer.getMail()) != null) {
            displayAlertMessage("L'adresse mail est déjà utilisée.");
            return "redirect:/inscription";
        }
        customerDao.save(customer);
        displayAlertMessage("Vous êtes inscrit, bienvenue " + customer.getFirstName() + " !");
        return "redirect:/login";
    }

    @GetMapping("/newProgram")
    public String addProgramForm(Model model) {
        return "new_program";
    }

    @PostMapping("/newProgram")
    public String addProgram(Program program, Model model) {
        program.setMail(LoginForm.getMail());
        program.setImc(program.getWeight() / (program.getHeight() * program.getHeight())); //IMC = Poids / Taille^2

        String choiceIMC = "";
        String choiceFreq = "";
        String choiceGoal = "";

        if (program.getImc() < 18.5F){choiceIMC = "A1";}
        if ( (program.getImc() >= 18.5F) & (program.getImc() <= 25.0F)){choiceIMC = "A2";}
        if (program.getImc() > 25.0F){choiceIMC = "A3";}

        if (program.getFrequence().equals("1 à 2 fois")){choiceFreq = "B1";}
        if (program.getFrequence().equals("3 à 4 fois")){choiceFreq = "B2";}
        if (program.getFrequence().equals("5 à 7 fois")){choiceFreq = "B3";}

        if (program.getGoal().equals("Prise de muscle")){choiceGoal = "C1";}
        if (program.getGoal().equals("Renforcement")){choiceGoal = "C2";}
        if (program.getGoal().equals("Perte de poids")){choiceGoal = "C3";}

        program.setChainOfChoices(choiceIMC + "_" + choiceFreq + "_" + choiceGoal);

        programDao.save(program);
        return "redirect:/customerPage";
    }

    @GetMapping("/customerPage")
    public String displayCustomerPage(Model model) {

        String m = LoginForm.getMail();
        if (m == null){ //in case the user isn't logged in
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
                break;
        }
        return "customer_page";
    }

    @GetMapping("/deleteCustomer")
    public String deleteThisCustomer(String mail) {
        customerDao.deleteCustomerByMail(mail);
        displayAlertMessage("User supprimé : " + mail);
        return "redirect:/";
    }

    @GetMapping("/error")
    public String displayError() {
        return "error";
    }

    @GetMapping("/modifyCustomer")
    public String modifyCustomerForm(Model model, String mail){

        if (LoginForm.getMail() == null){
            displayAlertMessage("Vous tentez d'accéder à une page non autorisée");
            return "redirect:/login";
        }
        model.addAttribute("customer", customerDao.findCustomerByMail(mail));
        return "modifyCustomer";
    }

    @PostMapping("/modifyCustomer")
    public String submitModification(Customer customer, String mail){
        customerDao.deleteCustomerByMail(mail);
        displayAlertMessage("Vos modifications ont bien été prises en compte");
        customerDao.save(customer);
        return "redirect:/";
    }

    private void displayAlertMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Erreur");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }


}

