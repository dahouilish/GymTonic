#----------------#
#    GymTonic Lance    #  
#----------------#

AUTHENTIFICATION

	Soit CONNEXION
	--> validation des identifiants
		--> redirection vers PAGE PERSO

	Soit INSCRIPTION:
	--> nom/prenom
	--> mail
	--> password
	--> homme/femme
	--> date de naissance
		--> redirection vers PAGE PERSO
	
PAGE PERSO:
	--> Mes programmes
	--> Nouveau programme
	
NOUVEAU PROGRAMME:
	--> nbr/fois par semaine
	--> gouts sportifs
	--> optionnel : IMC
	--> poids/taille

--> Bouton Valider

PROPOSITION du programme et 
--> affichage de plusieurs programmes potentiels
--> affichage des détails du programme actuellement selectionné

PAGE ADMIN
--> ajouter/delete un programme


User: mail/nom/password/ID : admin ou client

BASE DE DONNEES


-- HOW TO GIT ? --

git status (voir les modifications, affichées en rouge)
git add <nom_du_fichier> (ou * si il y a plusieurs fichiers à add d'un coup)
git commit -m "[DBE] Raison du commit"
git push origin master

git pull origin master pour "Mettre à Jour"

ATTENTION !
une fois loginé, on stock le mail du gus en cours pour aller chercher ses parametres directement de la BDD;


TODO David :

Recuperer identifiants de la BDD
Puis empecher mail en double lors de l'inscription

TODO Lancelot :

Page d'accueil


TODO Romain : 

CustomerPage : Mise en page, affichage données
Puis creation de programmes (formulaire)



