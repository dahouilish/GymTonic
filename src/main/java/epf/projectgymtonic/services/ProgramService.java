package epf.projectgymtonic.services;

import epf.projectgymtonic.form.LoginForm;
import epf.projectgymtonic.models.GymTonicProgram;
import epf.projectgymtonic.models.Program;
import epf.projectgymtonic.models.ProgramAttribution;
import epf.projectgymtonic.persistence.GymTonicProgramDAO;
import epf.projectgymtonic.persistence.ProgramAttributionDAO;
import epf.projectgymtonic.persistence.ProgramDAO;
import org.jetbrains.annotations.NotNull;

public class ProgramService {

    private DisplayServices displayServices = new DisplayServices();

    public ProgramService() {
        super();
    }

    @NotNull
    public String newProgram(ProgramAttributionDAO programAttributionDao, GymTonicProgramDAO gymTonicProgramDao, Program program, Boolean fastProgram, ProgramDAO programDao) {
        if (!fastProgram) {
            program.setMail(LoginForm.getMail());
        }
        program.setImc(program.getWeight() / (program.getHeight() * program.getHeight())); //IMC = Poids / Taille^2

        String choice1 = "";
        String choice2 = "";
        String choice3 = "";

        //System.out.println("yes 3");
        if (program.getImc() < 18.5F) {
            choice1 = "A1";
        }
        else if ((program.getImc() >= 18.5F) & (program.getImc() <= 25.0F)) {
            choice1 = "A2";
        }
        else if (program.getImc() > 25.0F) {
            choice1 = "A3";
        }
        switch (program.getFrequence()) {
            case "0 fois":
                choice2 = "B1";
                break;
            case "1 à 2 fois":
                choice2 = "B2";
                break;
            case "3 à 7 fois":
                choice2 = "B3";
                break;
        }

        switch (program.getGoal()) {
            case "Prise de masse":
                choice3 = "C1";
                break;
            case "Renforcement musculaire":
                choice3 = "C2";
                break;
            case "Perte de poids":
                choice3 = "C3";
                break;
        }

        program.setChainOfChoices(choice1 + "_" + choice2 + "_" + choice3);

        ProgramAttribution programAttribution = programAttributionDao.findProgramByChainOfChoices(program.getChainOfChoices());
        GymTonicProgram gymTonicProgram = gymTonicProgramDao.findProgramByCode(programAttribution.getCode());

        program.setProposedProgram(gymTonicProgram.getName());
        program.setDescription(gymTonicProgram.getDescription());
        program.setImage(gymTonicProgram.getImage());
        //TODO:
        System.out.println(program.getImage());

        // If the user is already registered
        if (!fastProgram) {
            displayServices.displayAlertMessage("Votre programme", gymTonicProgram.getName() + "\n\nRetrouvez les détails du programme dans votre Page perso");
        } else {
            displayServices.displayAlertMessage("Votre programme", gymTonicProgram.getName()  + "\n\nRetrouvez les détails du programme dans votre Page perso, \n" +
                    "Connectez-vous pour pouvoir enregistrer vos programmes.");
        }

        programDao.save(program);

        if (fastProgram) {
            //programDao.deleteTemporaryPrograms();
            //program.setMail("temporaryUser@gymtonic.com");
            return "redirect:/";
        } else {
            return "redirect:/customerPage";
        }
    }
}
