package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.form.LoginForm;
import epf.projectgymtonic.models.Program;
import epf.projectgymtonic.persistence.CustomerDAO;
import epf.projectgymtonic.persistence.GymTonicProgramDAO;
import epf.projectgymtonic.persistence.ProgramAttributionDAO;
import epf.projectgymtonic.persistence.ProgramDAO;
import epf.projectgymtonic.services.DisplayServices;
import epf.projectgymtonic.services.ProgramService;
import epf.projectgymtonic.services.SaveLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
 */
@Controller
public class CustomersController {

    private final CustomerDAO customerDao;
    private final ProgramDAO programDao;
    private final ProgramAttributionDAO programAttributionDao;
    private final GymTonicProgramDAO gymTonicProgramDao;
    //Services
    private DisplayServices displayServices = new DisplayServices();
    private ProgramService programService = new ProgramService();
    private SaveLogService saveLogService = new SaveLogService();
    //Date et Heure
    private Date date = new Date();

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
            displayServices.displayAlertMessage("Erreur","Vous etes déjà connecté");
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
            displayServices.displayAlertMessage("Validé","Vous n'etes pas encore connecté");
            return "redirect:/";
        }

        loginForm.setEmail(null);
        loginForm.setPassword(null);
        displayServices.displayAlertMessage("Validé","Vous vous êtes déconnecté !");
        //customerDao.deleteAll();
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) throws IOException {

        String email = loginForm.getMail();
        String password = loginForm.getPassword();
        //Get the customer from the mail
        Customer currentCustomer = customerDao.findCustomerByMail(email);
        //in case the password and the mail do not match
        if (currentCustomer == null) {
            displayServices.displayAlertMessage("Erreur","L'adresse mail et/ou le mot de passe est invalide");
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
                saveLogService.saveNewCustomer(date, "Connexion", currentCustomer);
                return "redirect:/customerPage";
            }else{
                return "redirect:/";
            }
        }
        displayServices.displayAlertMessage("Erreur","L'adresse mail et/ou le mot de passe est invalide");
        return "login";
    }

    @GetMapping("/inscription")
    public String addUserForm(Model model) {
        model.addAttribute("customer", new Customer());
        if (LoginForm.getMail() != null) {
            displayServices.displayAlertMessage("Erreur","Vous êtes déjà inscrit ! : " + LoginForm.getMail());
            return "redirect:/";
        }
        return "inscription";
    }

    @PostMapping("/inscription")
    public String addCustomer(Customer customer, Model model) throws IOException {
        //In case the mail is already used
        if (customerDao.findCustomerByMail(customer.getMail()) != null) {
            displayServices.displayAlertMessage("Erreur","L'adresse mail est déjà utilisée.");
            return "redirect:/inscription";
        }
        customerDao.save(customer);
        saveLogService.saveNewCustomer(date, "Inscription", customer);
        displayServices.displayAlertMessage("Validé","Vous êtes inscrit, bienvenue " + customer.getFirstName() + " !");
        return "redirect:/login";
    }

    @GetMapping("/newProgram")
    public String addProgramForm(Model model) {

        if (LoginForm.getMail() == null){ //in case the user isn't logged in
            displayServices.displayAlertMessage("Erreur","Vous tentez d'accéder à une page non autorisée");
            return "redirect:/login";
        }
        model.addAttribute("fast", false);
        return "new_program";
    }

    @PostMapping("/newProgram")
    public String addProgram(Program program, Model model) {

        return programService.newProgram(programAttributionDao, gymTonicProgramDao, program, false, programDao);
    }

    @GetMapping("/fastProgram")
    public String addFastProgramForm(Model model) {
        model.addAttribute("fast", true);
        return "new_program";
    }

    @PostMapping("/fastProgram")
    public String addFastProgram(Program program, Model model) {
        return programService.newProgram(programAttributionDao, gymTonicProgramDao, program, true, programDao);
    }

    @GetMapping("/customerPage")
    public String displayCustomerPage(Model model) {

        String m = LoginForm.getMail();
        if (m == null){ //in case the user isn't logged in
            displayServices.displayAlertMessage("Erreur","Vous tentez d'accéder à une page non autorisée");
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
        displayServices.displayAlertMessage("Validé","User supprimé : " + mail);
        return "redirect:/";
    }

    /** WS Modify customer already in database */

    @GetMapping("/modifyCustomer")
    public String modifyCustomerForm(Model model, String mail){

        if (LoginForm.getMail() == null){
            displayServices.displayAlertMessage("Erreur","Vous tentez d'accéder à une page non autorisée");
            return "redirect:/login";
        }
        model.addAttribute("customer", customerDao.findCustomerByMail(mail));
        return "modifyCustomer";
    }

    @PostMapping("/modifyCustomer")
    public String submitModification(Customer customer, String mail){
        customerDao.deleteCustomerByMail(mail);
        displayServices.displayAlertMessage("Validé","Vos modifications ont bien été prises en compte");
        customerDao.save(customer);
        return "redirect:/";
    }
}

