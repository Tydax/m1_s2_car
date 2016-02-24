#Master 1 : Semestre 6
##Construction d'applications réparties
###Practical no. 1

Armand Bour et Antonin Durey
This first practical consists in implementing a FTP server using TCP port.

####Exécution
Pour lancer le serveur FTP :
`java -jar m1_s2_car_tp1_dureyantonin_bourarmand.jar`

Le serveur est par défaut lancé sur le port `2048`, et possède un utilisateur (login : `azerty`, mot de passe : `qwerty`). Le *working directory* est par défaut le chemin du dossier du .jar.

####Parties fonctionnelles
Tout dans le TP fonctionne.
Le meilleur moyen de tester le TP est de le faire avec la commande `ftp`. Celle-ci fait quelques choses en plus par rapport à la commande `telnet`, comme par exemple envoyer des commandes PORT qui indiquent sur quel port la réponse de la commande suivante doit être envoyée.

####Tests
Pour les tests, nous testons chacun des codes de retour des fonctions `processXXX`. Pour l'envoi et la réception de fichiers, nous n'avons testé que les cas où cela ne fonctionne pas. En effet, pour tester si cela fonctionne, cela aurait revenu à créer un client ftp.

