package epf.projectgymtonic.form;

public class TempProgram {

    private static String mail;
    private static String gymTonicProgram;

    public TempProgram() {
        super();
    }

    public static String getMail() {
        return mail;
    }

    public static String getChainOfChoices() {
        return gymTonicProgram;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setGymTonicProgram(String gymTonicProgram) {
        this.gymTonicProgram = gymTonicProgram;
    }

    public void flushMail() { mail = null; }

    public void flushGymTonicProgram() { gymTonicProgram = null; }
}
