# Description de la remise

### Nom complet: XXXXX
### NIP: 111 111 111
### Liste des codes et descriptions des fonctionnalités sélectionnées:
Exemple:
- (FA2) Intégration du Service Mesh Consul-Connect ==> 5%
- (FA21) Intégration de la fonctionnalité de Service Discovery de Consul-Connect ==> 5%
- (FA22) Observabilité des services et de leurs états (healthcheck) au travers du UI de Consul ==> 5%
- (FA23) Définition d'Intentions limitant la communication entre les services au strict nécessaire ==> 10%
- (FA24) Configuration de Canary Deployment et/ou Blue-green/A-B Deployment ==> 10%

### Directives nécessaires à la correction
XXXXX

### Commentaires généraux:

### feedback-api

Gestion de la DB : Un problème survient du fait que SQLite est un fichier et non un serveur de base de données.
Donc, on a des soucis pour récupérer tous les feedbacks avec un GET : si on choisit un StatefulSet, chaque réplique a son propre univers séparé des autres.
Le meilleur choix ici serait donc de définir un PVC et un PV avec un Deployment pour que toutes les répliques lisent et écrivent dans le même fichier. Cette solution semble OK dans le cadre du TP : l'administrateur verra toujours la totalité des données, peu importe quel pod répond.

Par contre c'est fragile, mais pourquoi ? Si les 2 réplicas tentent d'écrire dans le fichier SQLite exactement à la même milliseconde, l'un d'eux plantera ou sera bloqué (Database Locked). Si on prend le problème avec le théorème CAP vu en cours, ici on sacrifie la disponibilité. Pour faire mieux, il faudrait changer le type de base de données.

Autre soucis, cette solution implique que nos réplicas tournent sur le même node (dans ce TP on nous propose un node maitre et deux workers donc on déploie nos pods sur 2 nodes différents.) Le risque c'est que si le node échoue alors tous nos pods feedback-api tombent et on fait face à une indisponibilité de service.