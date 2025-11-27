# Description de la remise

### Nom complet: Léopold Andre Gaston Chappuis
### NIP: 537 393 109
### Nom complet: ...
### NIP: ... 
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

### Nginx ingress ! 
attention comme le TP a été fait sur kind pour que tout fonctionne et soit accéssible l'ingress a été installé sur le master node; c'est une mauvaise pratique et peut être changé facilement mais pour que tout fonctionne directement avec une seule commande c'était un passage obligatoire avec l'utilisation de Kind. Il doit être scotché au noeud qui est ouvert vers l'extérieur.

### feedback-api

Gestion de la DB : Un problème survient du fait que SQLite est un fichier et non un serveur de base de données.
Donc, on a des soucis pour récupérer tous les feedbacks avec un GET : si on choisit un StatefulSet, chaque réplique a son propre univers séparé des autres.
Le meilleur choix ici serait donc de définir un PVC et un PV avec un Deployment pour que toutes les répliques lisent et écrivent dans le même fichier. Cette solution semble OK dans le cadre du TP : l'administrateur verra toujours la totalité des données, peu importe quel pod répond.

Par contre c'est fragile, mais pourquoi ? Si les 2 réplicas tentent d'écrire dans le fichier SQLite exactement à la même milliseconde, l'un d'eux plantera ou sera bloqué (Database Locked). Si on prend le problème avec le théorème CAP vu en cours, ici on sacrifie la disponibilité. Pour faire mieux, il faudrait changer le type de base de données.

Autre soucis, cette solution implique que nos réplicas tournent sur le même node (dans ce TP on nous propose un node maitre et deux workers donc on déploie nos pods sur 2 nodes différents.) Le risque c'est que si le node échoue alors tous nos pods feedback-api tombent et on fait face à une indisponibilité de service.

LivenessProbe: quoi tester ? ON voit qu'on peut tester deux choses la route health et la route feedback, la route health vérifie que le serveur est bien lancé et répond 200 OK par contre la route feedback vérifie que le serveur arrive bien à communiquer avec la base de données. Ici on pourrati vérifier /health mais on aurait pas d'information si la db plante, en utilisant feedback on détourne une route métier mais on vérifie que le serveur est up et communique avec la base de données. SI la db plante (par exemple un accès pas relaché) kubernetes tue le pod et le remplaces ce qui permet de relacher la db.

Vérifier le liveness probe avec /feedback parait plus judicieux mais implique plus d'appels réseaux et de travail car on retourne réellement les éléments et si on a beaucoup de choses dans la base de données ça peut devenir problématique.
La db locale est critique pour ce service !
On pourrait aussi partir du principe que comme la db est locale alors on aura moins de soucis.

On va supposer que le route health a été crée pour le liveness probe et utiliser celle-ci même s'il faut nuancer / il faudrait ajouter des tests pour être certain que la base de donnée communique bien avec notre service.