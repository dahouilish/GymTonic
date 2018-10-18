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
            // Sets the program's mail
            program.setMail(LoginForm.getMail());
        }
        // Sets the program's IMC
        program.setImc(program.getWeight() / (program.getHeight() * program.getHeight())); //IMC = Poids / Taille^2

        String choice1 = ""; // Choice A
        String choice2 = ""; // Choice B
        String choice3 = ""; // Choice C

        // A1 : underweight person
        if (program.getImc() < 18.5F) {
            choice1 = "A1";
        } // A2 : standard weight
        else if ((program.getImc() >= 18.5F) & (program.getImc() <= 25.0F)) {
            choice1 = "A2";
        } // A3 : overweight person
        else if (program.getImc() > 25.0F) {
            choice1 = "A3";
        }
        // B : relative to the frequence of the user's physical activities
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
        // C : relative to the user's objective
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

        // Sets the program's chain of choices. chainOfChoices is the concatenation of A & B & C
        program.setChainOfChoices(choice1 + "_" + choice2 + "_" + choice3);

        // Finds the matching between the chain of choices and our programs
        ProgramAttribution programAttribution = programAttributionDao.findProgramByChainOfChoices(program.getChainOfChoices());
        // Finds the GymTonic program by using the code
        GymTonicProgram gymTonicProgram = gymTonicProgramDao.findProgramByCode(programAttribution.getCode());

        program.setProposedProgram(gymTonicProgram.getName()); // Sets the program's name
        program.setDescription(gymTonicProgram.getDescription()); // Sets the program's description
        program.setImage(gymTonicProgram.getImage()); // Sets the program's picture

        // If the user is already registered
        if (!fastProgram) {
            // Display to the user the result of the attribution
            displayServices.displayAlertMessage("Votre programme", gymTonicProgram.getName() + "\n\nRetrouvez les détails du programme dans votre Page perso");
        } else {
            // In the case of a fast program : display the result and tells the user to sign up in order to save it
            displayServices.displayAlertMessage("Votre programme", gymTonicProgram.getName()  + "\n\nRetrouvez les détails du programme dans votre Page perso, \n" +
                    "Connectez-vous pour pouvoir enregistrer vos programmes.");
        }

        programDao.save(program);

        if (fastProgram) {
            return "redirect:/";
        } else {
            return "redirect:/customerPage";
        }
    }
}
