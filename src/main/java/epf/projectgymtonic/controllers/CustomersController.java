package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.form.LoginForm;
import epf.projectgymtonic.persistence.CustomerDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

/**
 * TODO class details.
 *
 * @author David Bernadet on 10/09/2018
 */
@Controller
public class CustomersController {

    private final CustomerDAO customerDao;

    public CustomersController(CustomerDAO customerDao) {
        this.customerDao = customerDao;
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
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {

        //On recupere les valeurs des input du formulaire login
        String email = loginForm.getMail();
        String password = loginForm.getPassword();

        //optionnel : recuperer les valeurs dans la BDD
        //TODO dans CustomerDAO
        Customer currentCustomer = customerDao.findCustomerByMail(email);

        if (currentCustomer == null) {
            //alert("L'adresse mail est invalide");
            System.out.println("USER INCONNU DE LA BDD : FAILURE !");
            displayAlertMessage("L'adresse mail et/ou le mot de passe est invalide");
            return "login";
        }
        System.out.println("CurrentCustomer : " + currentCustomer);

        String currentMail = currentCustomer.getMail();
        String currentPassword = currentCustomer.getPassword();

        //LOGGER
        //System.out.println("CurrentCustomer mail : " + currentMail + " PWD : " + currentPassword);

        //LOGGER
        //System.out.println("INPUT : Email : " + email + " et password : " + password);

        // Test de connexion : si = admin ,alors success
        if (currentMail.equals(email) && currentPassword.equals(password)) {
            System.out.println("USER RECONNU DANS LA BDD : SUCCESS !");
            switch (currentCustomer.getRole()) {
                case 1:
                    return "redirect:/customerPage";
                case 2:
                    return "redirect:/customer";
                default:
                    return "redirect:/customers";
            }

        }
        model.addAttribute("invalidCredentials", true);
        System.out.println("PASSWORD DOESN'T MATCH : FAILURE !");
        displayAlertMessage("L'adresse mail et/ou le mot de passe est invalide");
        // si failure, rester sur la page login
        return "login";
    }

    //TODO : supprimer ce WS et ajouter le html correspondant à la page des Customers
    @GetMapping("/customers")
    public String getCustomers(Model model) {
        model.addAttribute("data", customerDao.findAll());
        return "customers-list";
    }

    @GetMapping("/customer")
    public String addUserForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add_member";
    }

    @PostMapping("/customer")
    public String addCustomer(Customer customer, Model model) {
        System.out.println("LE MAIL ENTRE EST : " + customer.getMail());
        //In case the mail is already used
        if (customerDao.findCustomerByMail(customer.getMail()) != null) {
            displayAlertMessage("L'adresse mail est déjà utilisée.");
            return "redirect:/customer";
        }
        customerDao.save(customer);
        return "redirect:/customerPage";
    }

    @GetMapping("/customerPage")
    public String displayCustomerPage(Model model) {

        String m = LoginForm.getMail();

        if (m == null){
            displayAlertMessage("VOUS TENTEZ D'ACCEDER A UNE PAGE NON AUTORISEE");
            return "redirect:/login";
        }
        System.out.println("mail : " + m);
        Customer c = customerDao.findCustomerByMail(m);

        //Afficher
        model.addAttribute("c", c);
        //Afficher liste entiere :
        model.addAttribute("data", customerDao.findAll());

        return "customer_page";
    }

    @GetMapping("/adminPage")
    public String displayAdminPage() {
        return "adminPage";
    }

    private void displayAlertMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Erreur");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }


}

