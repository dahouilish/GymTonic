package epf.projectgymtonic.models

import javax.persistence.Entity
import javax.persistence.Id

/**
 * Cette classe utilise Kotlin (parce qu'on est grave des Hipsters).
 * La "data class" représente un "POJO" (plain-old java object), c'est à dire un objet qui n'est destiné qu'à "représenter" une entité.
 * Ici, en l'occurence, un utilisateur.
 * @author David Bernadet, Romain Cogen, Lancelot Deguerre on 09/10/2018
 */

@Entity
data class Customer(var firstName: String? = null, var lastName: String?= null,
                    @Id var mail: String? = null, var password: String? = null, var date: String? = null, var gender: String? = null,
                    var role: Int? = 1)
