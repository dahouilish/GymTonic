package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.form.LoginForm;
import epf.projectgymtonic.persistence.CustomerDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import java.sql.*;

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

    private static Properties propertiesLoader(String filename) throws IOException {
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream(filename);
        try {
            properties.load(input);
            return properties;
        } finally {
            input.close();
        }
    }

    private static ResultSet myDatas() {
        Properties idConnexion = null;
        ResultSet result = null;

        try {
            idConnexion = propertiesLoader("application.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dbUrl = idConnexion.getProperty("spring.datasource.url", null);
        String dbUsername = idConnexion.getProperty("spring.datasource.username", null);
        String dbPassword = idConnexion.getProperty("spring.datasource.password", null);

        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            //Création d'un objet Statement
            Statement state = conn.createStatement();

            //L'objet ResultSet contient le résultat de la requête SQL
            result = state.executeQuery("SELECT * FROM customer");

            /*while (result.next()) {
                if (result.getString("mail").equals("admin")) {
                    System.out.println("THIS IS FUCKING TRUE !!!!!!!!!!!!!!!");
                }
                //if (result.getString("").equals(""){
                //}
            }*/

            //On récupère les MetaData (structure de la BDD)
            //ResultSetMetaData resultMeta = result.getMetaData();

            result.close();
            state.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@ModelAttribute(name = "loginForm") LoginForm loginForm, Model model) {

        String mail = loginForm.getMail();
        String password = loginForm.getPassword();

        if ("admin".equals(mail) && "admin".equals(password)) {
            return "redirect:/customers";
        }
        System.out.println("Mail : " + mail + " et password : " + password);
        model.addAttribute("invalidCredentials", true);
        //String mail = customer.
        //System.out.println("here " + mail + password);


        return "login";
    }

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
        customerDao.save(customer);
        return "redirect:/customers";
    }


}

