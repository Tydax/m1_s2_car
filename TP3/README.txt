CAR

TP3 - Akka

Armand BOUR et Antonin DUREY

TP réalisé sans problème particulier.
Le main permet de choisir le noeud sur lequel il faut envoyer un message t(de greeter1 à greeter6), suivi du dit message à envoyer.


Scénarios de tests :

on supposera que tous les scénarios de tests portent sur 3 noeuds, chacun des noeuds étant reliés au 2 autres. les noeuds sont appelés N1, N2 et N3.

Si on envoie un message à N1, alors N2 et N3 reçoivent le même message

Si on envoie plusieurs messages à N1, alors N2 et N3 reçoivent les messages dans le même ordre que N1 les a reçu précédemment.

Si on envoie le message message plusieurs fois à N1, alors N2 et N3 recevront également plusieurs fois ce message.

Si on envoie un message à N1, alors celui-ci ne le reçoit pas une 2ème fois.

Si on envoie un message à N1, alors N2 le recevra 2 fois (une fois envoyé par N1, une fois envoyé par N3)
Idem pour N3
