package epf.projectgymtonic.form;

public class LoginForm {

    private static String email;
    private String password;

    public LoginForm() {
        super();
    }

    public static String getMail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

