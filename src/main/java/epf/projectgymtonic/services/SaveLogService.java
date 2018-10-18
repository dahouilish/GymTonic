package epf.projectgymtonic.services;

import epf.projectgymtonic.models.Customer;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveLogService {

    /**
     * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 10/09/2018
     */

    private String filename;
    private DateFormat dateFormat;

    public SaveLogService() {
        filename = "logfile.txt";
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    // this function save the several connexion and inscription logs in a file : logfile.txt
    public void saveNewCustomer(Date date, String type, Customer customer) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        writer.append(dateFormat.format(date) + " - " + type + " : " + customer.toString());
        writer.close();
    }
}