package epf.projectgymtonic.controllers;

import epf.projectgymtonic.models.Customer;
import epf.projectgymtonic.persistence.CustomerDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

        /**
         * Ceci sera mappé sur l'URL '/users'.
         * C'est le routeur de Spring MVC qui va détecter et appeler directement cette méthode.
         * Il lui fournira un "modèle", auquel on pourra rajouter des attributs.
         * Ce modèle sera ensuite forwardé à une page web (dans resources/templates).
         * Le nom de la template est retourné par la fonction. Ici, elle appelle donc le template "users".
         * @param model le modèle
         * @return
         */
        @GetMapping("/customers")
        public  String getCustomers (Model model) {
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

