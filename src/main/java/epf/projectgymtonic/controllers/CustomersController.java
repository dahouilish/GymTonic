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
    private final GymTonicProgramDAO gymTonicProgramDao; //list of all programs available
    //Services
    private DisplayServices displayServices = new DisplayServices();
    private ProgramService programService = new ProgramService();
    private SaveLogService saveLogService = new SaveLogService();
    private Date date = new Date();

    public CustomersController(CustomerDAO _customerDao, ProgramDAO _programDao,
                               ProgramAttributionDAO _programAttributionDao, GymTonicProgramDAO _gymTonicProgramDao) {
        this.customerDao = _customerDao;
        this.programDao = _programDao;
        this.programAttributionDao = _programAttributionDao;
        this.gymTonicProgramDao = _gymTonicProgramDao;

    }

    /*
     * Web Services on this page, other services are declared in other classes.
     */

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm() {
        if (LoginForm.getMail() != null) {
            displayServices.displayAlertMessage("Erreur","Vous etes déjà connecté");
            return "redirect:/";
        }
        return "login";
    }

    // Log out
    @GetMapping(value = "/exit")
    public String exit(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {
        Customer currentCustomer = customerDao.findCustomerByMail(LoginForm.getMail());

        if (LoginForm.getMail() == null){
            displayServices.displayAlertMessage("Erreur","Vous n'etes pas encore connecté");
            return "redirect:/";
        }
        // when the user log out, flush the loginForm
        loginForm.setEmail(null);
        loginForm.setPassword(null);
        displayServices.displayAlertMessage("Validé","Vous vous êtes déconnecté !");
        return "redirect:/";
    }

    // login in
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
        // create a new customer and link ot to the data entered in the input form
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
        // save the customer in the db and in the logfile
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
        model.addAttribute("fast", false); // fast = false
        return "new_program";
    }

    // when you are logged in and you want to create another program
    @PostMapping("/newProgram")
    public String addProgram(Program program, Model model) {
        // fastProgram = false
        return programService.newProgram(programAttributionDao, gymTonicProgramDao, program, false, programDao);
    }

    // when you are NOT logged in and you want to create a program to try
    @GetMapping("/fastProgram")
    public String addFastProgramForm(Model model) {
        model.addAttribute("fast", true); // fast = true
        return "new_program";
    }

    @PostMapping("/fastProgram")
    public String addFastProgram(Program program, Model model) {
        // fastProgram = true
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
            case 1: // in case the user is a lambda user
                model.addAttribute("c", c); //display user info
                model.addAttribute("p", programDao.findProgramsByMail(m)); //display user program info
            case 2: // in case it's the admin
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

    //WS Modify customer already in database
    @GetMapping("/modifyCustomer")
    public String modifyCustomerForm(Model model, String mail){
        // prevent non logged in users to access this area
        if (LoginForm.getMail() == null){
            displayServices.displayAlertMessage("Erreur","Vous tentez d'accéder à une page non autorisée");
            return "redirect:/login";
        }
        model.addAttribute("customer", customerDao.findCustomerByMail(mail));
        return "modifyCustomer";
    }

    @PostMapping("/modifyCustomer")
    public String submitModification(Customer customer, String mail){
        // 'update' data wanted
        customerDao.deleteCustomerByMail(mail);
        displayServices.displayAlertMessage("Validé","Vos modifications ont bien été prises en compte");
        customerDao.save(customer);
        return "redirect:/";
    }
}

