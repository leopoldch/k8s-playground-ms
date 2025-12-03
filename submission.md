# Description de la remise

### Nom complet: Léopold Andre Gaston Chappuis
### NI: 537 393 109

### Nom complet: Pascal de Le Rue
### NI: 111 187 065

### Nom complet: Audrey Eliane Andrée Dive
### NI: 537 393 135

### Nom complet: Christopher Bilodeau
### NI: 536 785 193


### Liste des codes et descriptions des fonctionnalités sélectionnées:
Exemple:
- (FA31) Intégration d'un outil de gestion de journaux (Loki, Promtail) ==> 5%
  Implémentation réalisée avec un DaemonSet (Promtail) et Loki, tel que vu dans le cadre du cours. Un dashboard de visualisation est disponible sur Grafana (/admin/grafana). 
  **Note technique :** En raison de l'utilisation de l'environnement *Kind*, nous avons dû attribuer des droits privilégiés (`privileged: true`, `runAsUser: 0`) au DaemonSet. Ces permissions, bien que non recommandées en production, étaient strictement requises dans le cadre de ce TP pour permettre à Promtail de lire correctement les fichiers de logs sur les nœuds virtuels.
- (FA32) Intégration de monitoring des ressources physiques (CPU, Mémoire,...)(Prometheus) ==> 5%
- (FA34) Visualisation (Grafana) ==> 10%

### Directives nécessaires à la correction
Pour appliquer les configs: `kubectl apply -R -f submission` car pour des soucis de propreté les configurations sont rangés dans différents dossiers.

Grafana est accéssible à travers `/admin/grafana`
Monitoring des ressources physiques avec Prometheus.


### Commentaires généraux:

### Nginx ingress ! 
attention comme le TP a été fait sur kind pour que tout fonctionne et soit accéssible l'ingress a été installé sur le master node; c'est une mauvaise pratique et peut être changé facilement mais pour que tout fonctionne directement avec une seule commande c'était un passage obligatoire avec l'utilisation de Kind. Il doit être scotché au noeud qui est ouvert vers l'extérieur.

L'ingress de grafana a été défini dans un autre que l'application car dans un autre namespace pour la propreté.
Grafana peut mettre un certain temps à se lancer, merci d'être patient et d'attendre au moins 1 minute.

Pour permettre le déploiement de l'Ingress NGINX en une seule commande, nous avons du retirer la configuration du ValidatingWebhook. Ce composant, qui est là dans une installation standard, sert à valider la syntaxe des règles Ingress. Cependant, il crée une condition de concurrence (race condition) pendant un déploiement simultané : Kubernetes tente de valider l'Ingress via le Webhook alors que le Pod du contrôleur n'est pas encore prêt, ce qui bloque le déploiement. Le fait de l'avoir retiré n'est pas idéal pour la sécurité/fiabilité, mais était nécessaire pour respecter la contrainte d'exécution unique du TP

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
