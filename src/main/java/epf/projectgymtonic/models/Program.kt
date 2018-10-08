package epf.projectgymtonic.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Cette classe utilise Kotlin (parce qu'on est grave des Hipsters).
 * La "data class" représente un "POJO" (plain-old java object), c'est à dire un objet qui n'est destiné qu'à "représenter" une entité.
 * Ici, en l'occurence, un utilisateur.
 * @author Loïc Ortola on 10/09/2018
 */
@Entity
data class Program(@Id @GeneratedValue var id: Int? =  null, var mail: String? = null, var weight: Int? = null,
                   var height: Float? = null, var imc: Float? = null, var frequence: String? = null,
                   var goal: String? = null, var chainOfChoices: String? = null, var proposedProgram: String? = null,
                   var description: String? = null, var image: String? = null)
