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

/*@Entity
data class Customer(@Id @GeneratedValue var id: Int? =  null, var firstName: String? = null, var lastName: String?= null) {
}*/

@Entity
data class Customer(@Id @GeneratedValue var id: Int? =  null, var prenom: String? = null, var nom: String?= null
                    ,var mail: String? = null, var password: String? = null, var date: String? = null, var sexe: String? = null) {
}



//TO DO : Creer les nouveaux parametres adaptés : age nom prénom blablabla
// .... Aranger en conséquence le fichier html

//TODO mail different : verifier lors de l'inscription que le mail entré n'est pas dans la base de donnée
//TODO : preciser role automatiquement pour les clients, le mettre à 1 pour eux et à 2 pour l'admin