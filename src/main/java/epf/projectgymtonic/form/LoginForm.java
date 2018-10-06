package epf.projectgymtonic.form;

public class LoginForm {

    private static String mail;
    private String password;

    public LoginForm() {
        super();
    }

    public static String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

