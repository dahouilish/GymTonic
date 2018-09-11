/*
package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoginController {


    */
/*
     * Ceci sera mappé sur l'URL '/users'.
     * C'est le routeur de Spring MVC qui va détecter et appeler directement cette méthode.
     * Il lui fournira un "modèle", auquel on pourra rajouter des attributs.
     * Ce modèle sera ensuite forwardé à une page web (dans resources/templates).
     * Le nom de la template est retourné par la fonction. Ici, elle appelle donc le template "users".
     * @param model le modèle
     * @return
     *//*


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm(Model model) {
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

    @PostMapping(value = "/login")
    public String verifyLogin(Model model, @RequestParam String mail, @RequestParam String password) {
        System.out.println("here " + mail + password);
        */
/*Properties idConnexion = null;
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
            ResultSet result = state.executeQuery("SELECT * FROM customer");

            while (result.next()) {
                if (result.getString("mail").equals("admin")){
                    System.out.println("THIS IS FUCKING TRUE !!!!!!!!!!!!!!!");
                }
                //if (result.getString("").equals(""){
                //}
            }

            //On récupère les MetaData (structure de la BDD)
            ResultSetMetaData resultMeta = result.getMetaData();

            result.close();
            state.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }*//*


        if (mail.equals("admin") && password.equals("admin")) {
            return "redirect:/customers";
        } else {
            return "login";
        }
    }
}*/
