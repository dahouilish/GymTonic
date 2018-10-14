package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.form.LoginForm;
import epf.projectgymtonic.models.GymTonicProgram;
import epf.projectgymtonic.models.Program;
import epf.projectgymtonic.models.ProgramAttribution;
import epf.projectgymtonic.persistence.CustomerDAO;
import epf.projectgymtonic.persistence.GymTonicProgramDAO;
import epf.projectgymtonic.persistence.ProgramAttributionDAO;
import epf.projectgymtonic.persistence.ProgramDAO;
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

    private final CustomerDAO customerDao;
    private final ProgramDAO programDao;
    private final ProgramAttributionDAO programAttributionDao;
    private final GymTonicProgramDAO gymTonicProgramDao;
    //private boolean test = false;
    //private String currentMail;

    public CustomersController(CustomerDAO _customerDao, ProgramDAO _programDao,
                               ProgramAttributionDAO _programAttributionDao, GymTonicProgramDAO _gymTonicProgramDao) {
        this.customerDao = _customerDao;
        this.programDao = _programDao;
        this.programAttributionDao = _programAttributionDao;
        this.gymTonicProgramDao = _gymTonicProgramDao;
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
            displayAlertMessage("Erreur","Vous etes déjà connecté");
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
            displayAlertMessage("Validé","Vous n'etes pas encore connecté");
            return "redirect:/";
        }

        loginForm.setEmail(null);
        loginForm.setPassword(null);
        displayAlertMessage("Validé","Vous vous êtes déconnecté !");
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
            displayAlertMessage("Erreur","L'adresse mail et/ou le mot de passe est invalide");
            loginForm.setEmail(null);
            loginForm.setPassword(null);
            return "login";
        }

        String currentMail = currentCustomer.getMail();
        String currentPassword = currentCustomer.getPassword();

        // Test login : if mail and password do match
        if (currentMail.equals(email) && currentPassword.equals(password)) {
            if (currentCustomer.getRole() == 1 || currentCustomer.getRole() == 2) {
                //Program program = programsDao.selectTemporaryProgram();
                //program.setMail(LoginForm.getMail());
                return "redirect:/customerPage";
            }else{
                return "redirect:/";
            }
        }
        displayAlertMessage("Erreur","L'adresse mail et/ou le mot de passe est invalide");
        return "login";
    }

    @GetMapping("/inscription")
    public String addUserForm(Model model) {
        model.addAttribute("customer", new Customer());
        if (LoginForm.getMail() != null) {
            displayAlertMessage("Erreur","Vous êtes déjà inscrit ! : " + LoginForm.getMail());
            return "redirect:/";
        }
        return "inscription";
    }

    @PostMapping("/inscription")
    public String addCustomer(Customer customer, Model model) {
        //In case the mail is already used
        if (customerDao.findCustomerByMail(customer.getMail()) != null) {
            displayAlertMessage("Erreur","L'adresse mail est déjà utilisée.");
            return "redirect:/inscription";
        }
        customerDao.save(customer);
        displayAlertMessage("Validé","Vous êtes inscrit, bienvenue " + customer.getFirstName() + " !");
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

        System.out.println("yes 3");
        if (program.getImc() < 18.5F) {
            choice1 = "A1";
        }
        else if ((program.getImc() >= 18.5F) & (program.getImc() <= 25.0F)) {
            choice1 = "A2";
        }
        else if (program.getImc() > 25.0F) {
            choice1 = "A3";
        }
        switch (program.getFrequence()) {
            case "0 fois":
                choice2 = "B1";
                break;
            case "1 à 2 fois":
                choice2 = "B2";
                break;
            case "3 à 7 fois":
                choice2 = "B3";
                break;
        }

        switch (program.getGoal()) {
            case "Prise de masse":
                choice3 = "C1";
                break;
            case "Renforcement musculaire":
                choice3 = "C2";
                break;
            case "Perte de poids":
                choice3 = "C3";
                break;
        }

        program.setChainOfChoices(choice1 + "_" + choice2 + "_" + choice3);

        ProgramAttribution programAttribution = programAttributionDao.findProgramByChainOfChoices(program.getChainOfChoices());
        GymTonicProgram gymTonicProgram = gymTonicProgramDao.findProgramByCode(programAttribution.getCode());

        program.setProposedProgram(gymTonicProgram.getName());
        program.setDescription(gymTonicProgram.getDescription());
        program.setImage(gymTonicProgram.getImage());

        if (!fastProgram) {
            displayAlertMessage("Votre programme", gymTonicProgram.getName() + "\n\nRetrouvez les détails du programme dans votre Page perso");
        } else {
            displayAlertMessage("Votre programme", gymTonicProgram.getName()  + "\n\nRetrouvez les détails du programme dans votre Page perso, \n" +
                    "Connectez-vous pour pouvoir enregistrer vos programmes.");
        }

        programDao.save(program);

        if (fastProgram) {
            //programDao.deleteTemporaryPrograms();
            //program.setMail("temporaryUser@gymtonic.com");
            return "redirect:/";
        } else {
            return "redirect:/customerPage";
        }
    }

    @GetMapping("/customerPage")
    public String displayCustomerPage(Model model) {

        String m = LoginForm.getMail();
        if (m == null){ //in case the user isn't logged in
            displayAlertMessage("Erreur","Vous tentez d'accéder à une page non autorisée");
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
        displayAlertMessage("Validé","User supprimé : " + mail);
        return "redirect:/";
    }

    @GetMapping("/error")
    public String displayError() {
        return "error";
    }

    @GetMapping("/modifyCustomer")
    public String modifyCustomerForm(Model model, String mail){

        if (LoginForm.getMail() == null){
            displayAlertMessage("Erreur","Vous tentez d'accéder à une page non autorisée");
            return "redirect:/login";
        }
        model.addAttribute("customer", customerDao.findCustomerByMail(mail));
        return "modifyCustomer";
    }

    @PostMapping("/modifyCustomer")
    public String submitModification(Customer customer, String mail){
        customerDao.deleteCustomerByMail(mail);
        displayAlertMessage("Validé","Vos modifications ont bien été prises en compte");
        customerDao.save(customer);
        return "redirect:/";
    }

    private void displayAlertMessage(String title, String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

}

