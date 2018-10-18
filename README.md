---GymTonic---

Notre site permet la création de programmes de sport personnalisés en fonction des caractétistiques morphologiques
et des objectifs de l'utilisateur. Il s'agit donc d'attribuer un programme, parmi ceux qui sont stockés dans
notre base de données, selon les réponses de l'utilisateur à un formulaire qui lui est proposé.

Description des fonctions Front-office implémentées :

----------------------------------------------------------

1) Page d'acceuil. 
Elle permet la navigation entre les différentes fonctions du site, grâce aux onglets du bandeau de haut de page premièrement.
Celui ci permet d'être redirigier vers la page d'inscription d'un nouvel utilisateur, ou la page de connexion. Le "logo"
du site en haut à gauche permet de revenrir à la page d'accueil.
Au centre se trouve la fonctionnalité mise en avant sur notre site, la création d'un "programme rapide", c'est à dire la
création d'un programme pour un utilisateur non connnecté.
En bas de la page se trouvent dans un bandeau blanc des informations supplémentaires telles que du contenu à but marketing
et des liens revnoyant vers les divers réseaux sociaux du site (inexistants actuellement, ces liens redirigent donc vers
les pages d'accueil des sites).

Les bandeaux du haut et du bas se retrouvent sur toutes les autres pages du site, l'affichage des onglets (bandeau du haut)
est celà dit dynamque en fonction de la page sur laquelle on se trouve et de l'état de connexion.

----------------------------------------------------------

2) L'inscription
Un formulaire d'inscription doit être rempli. Il comporte la saisie des champs suivants :
- Prénom, nom, mail, saisis dans une entrée texte
- Le mot de passe, saisi dans une entrée texte mais dont les caractères sont cachés par des point noirs
- La date de naissance, mise sous le format MM/DD/YYYY qui peut être rentrée à la main et manipulée par 
les flèches montantes et descendantes ou par une fenetre de calendrier (grosse flèche vers le bas)
- Le sexe, saisie via une liste déroulante.

Au moment de valider l'inscription, si l'adresse mail renseignée a déjà été utilisée, 
une alerte s'affiche et les champs sont vidés. Une fois inscrit, l'utilisateur est redirigé vers la page de connexion.

----------------------------------------------------------

3) Session utilisateur (Se connecter / Déconexion)
La connexion se fait via les identifiants suivants : addresse mail et mot de passe.
On ne peut se connecter qu'avec des identifiants déjà rentrés dans la base de données après l'inscription.

Si un des deux champs renseignés est incorrect, alors un message d'erreur indique à l'utilisateur de réitérer
l'action. Une fois connecté l'utilisateur est redirigé vers sa page Mon compte.

Si l'adresse mail et le mot de passe rentrés sont respectivement "admin" et "admin", l'utilisateur est alors redirigé vers
la page d'administrateur.

Pour un utilisateur connecté, l'onglet du bandeau supérieur deveient alors "Déconnexion".

----------------------------------------------------------

4) Page personnelle (Mon compte)

L'affichage de cette page est dynamique selon le type d'uitlisateur : client ou administrateur.

Client :
Depuis sa page perso l'utilisateur a accès à tous les programmes que lui et suelement lui a créés.
(nom du programme, description, illustration) et à l'ajout de nouveaux. Cette page permet aussi la consultation 
et la modification de ses informations personnelles (redirection vers la page de modification qui est identique 
à la page d'inscription mais avec les champs pré-rempli par les informations de l'utilisateur).

Administrateur :
Affichage de la liste des personnes inscrites et de leur informations.

----------------------------------------------------------

5) Création de programme depuis la page perso
L'utilisateur se voit demander de remplir un formulaire de création de programme
Les champs suivants doivent être renseignés :
- poids et taille via des entrées texte avec flèches pour faire monter ou descendre les valeurs (comprises entre 1kg et 999kg
et 0,01m à 3m)
- fréquence de pratique actuelle de l'effort et objectif via des listes déroulantes

Puis au moment de valider le formulaire se fait l'attribution d'un programme type, choisi parmi les mutliples programmes
contenus dans la table GymTonicPrograms. Chaque combinaison possible des champs du formulaire est reliée à un des
programmes de cette table (matching n to 1 ).
Le résultat est communiqué vers l'utilisateur via un messages pop up pour. Celui affiche le nom du programme et indique de se
référer au tableau Mes programmes de la page Mon compte pour avoir le détail de l'exercice à réaliser. 

----------------------------------------------------------

6) Création de "programme rapide" depuis la page d'accueil
Cette fonctionnalité est identique à la cration d'un nouveau programme sauf qu'elle est rendue disponible pour les utilisateurs
non connectés. Au moment de valider le formulaire, le pop up indique le résultat de l'attribution de programme mais conseille
en plus de s'inscrire pour pouvoir "sauvegarder" les programmes ainsi créés. 

----------------------------------------------------------

Notes: Nous avons inclu un logfile rescensant toutes les connexions et inscriptions au cours du temps.

----------------------------------------------------------

Pistes d'amélioration :
- Stockage du hash des mots de passe et vérification des hashs et non du mot de passe en clair lors de la connexion
- Développement d'un système de suivi des clients dans leur programme, pour permettre d'accroitre l'utilisation du site
- Stockage en mémoire du dernier programme rapide créé, pour permettre de l'affilier à un utilisateur qui s'inscrirait
après l'usage de cette fonctionnalité
- Prise en compte de nouvelles variables pour l'attribution de programmes (sexe, âge, ...)
- Algorithme d'attribution repensé. Le système de matching serait alors plus complexe et poussé.
- Retravailler la connexion, en l'état actuel elle n'est pas très propre, c'est plutôt un principe de simulation de connexion.
Il faudrait créer un système d'authentification plus poussé.
- Pour une raison inconnue, les users inscrits en base disparaissent lorsqu'on relance l'application, comme si un delete all avait été appliqué.
