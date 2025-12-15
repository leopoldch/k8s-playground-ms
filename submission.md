# Description de la remise

### Liste des codes et descriptions des fonctionnalités sélectionnées:
-  (FA1) Sécuriser et encrypter les communications au travers de certificats SSL ==> 10%
- (FA31) Intégration d'un outil de gestion de journaux (Loki, Promtail) ==> 5%
  Implémentation réalisée avec un DaemonSet (Promtail) et Loki, tel que vu dans le cadre du cours. Un dashboard de visualisation est disponible sur Grafana (/admin/grafana). 
  **Note technique :** En raison de l'utilisation de l'environnement *Kind*, nous avons dû attribuer des droits privilégiés (`privileged: true`, `runAsUser: 0`) au DaemonSet. Ces permissions, bien que non recommandées en production, étaient strictement requises dans le cadre de ce TP pour permettre à Promtail de lire correctement les fichiers de logs sur les nœuds virtuels.
- (FA32) Intégration de monitoring des ressources physiques (CPU, Mémoire,...)(Prometheus) ==> 5%
- (FA34) Visualisation (Grafana) ==> 10%

### Directives nécessaires à la correction
Pour appliquer les configs: `kubectl apply -R -f submission` , car pour des soucis de propreté les configurations sont rangées dans différents dossiers.

Grafana est accessible à travers `/admin/grafana`
Monitoring des ressources physiques avec Prometheus.

Attendre que tout soit bien lancé, il y a beaucoup de choses ça peut prendre une à deux minutes. (`PR_END_OF_FILE_ERROR` et `PR_CONNECT_RESET_ERROR`, il faut juste attendre que tout soit bien lancé)

### Commentaires généraux:

### Service Worker (!)

Attention, l'app frontend fait tourner un service worker qui renvoie vers la page de frontend une fois accédée, plus simplement, une fois que l'app ouvre la page de frontend, il est impossible de joindre les autres endpoints pour afficher autre chose. Pour résoudre cette limitation à travers des scripts kubernetes on supprime le fichier du service-worker avant de tout lancer dans le deployment: 

```yaml    
  args: 
    - rm -f /usr/share/nginx/html/service-worker.js && exec nginx -g 'daemon off;'
```

### Nginx ingress ! 
attention comme le TP a été fait sur kind pour que tout fonctionne et soit accessible l'ingress a été installé sur le master node; c'est une mauvaise pratique et peut être changé facilement, mais pour que tout fonctionne directement avec une seule commande c'était un passage obligatoire avec l'utilisation de Kind. Il doit être scotché au noeud qui est ouvert vers l'extérieur.

L'ingress de grafana a été défini dans un autre que l'application, car dans un autre namespace pour la propreté.
Grafana peut mettre un certain temps à se lancer, merci d'être patient et d'attendre au moins 1 minute.

Pour permettre le déploiement de l'Ingress NGINX en une seule commande, nous avons du retirer la configuration du ValidatingWebhook. Ce composant, qui est là dans une installation standard, sert à valider la syntaxe des règles Ingress. Cependant, il crée une condition de concurrence (race condition) pendant un déploiement simultané : Kubernetes tente de valider l'Ingress via le Webhook alors que le Pod du contrôleur n'est pas encore prêt, ce qui bloque le déploiement. Le fait de l'avoir retiré n'est pas idéal pour la sécurité/fiabilité, mais était nécessaire pour respecter la contrainte d'exécution unique du TP

### Certificats et encryption

Ici on aurait pu utiliser un ClusterIssuer, un CRD issu de CertManager, mais la complexité venait d'une race condition pour créer nos kind:ClusterIssuer etc après que le CRD qoit completement défini, la solution la plus simple pour quand même implémenter des routes sécurisées avec des certificats ssl était d'en générer un statique et d'utiliser celui-ci qui est finalement la même chose, un certificat self-signed. Remarque, dans une réelle implémentation on aurait pu utiliser Let's Encrypt qui permet de faire signer des certificats par des autorités de certification vérifiées (On n'a pas choisi cette méthode, car il aurait fallu un nom de domaine etc...). 
Note : C'est normal si une page s'affiche et met un warning comme quoi le certificat est self-signed, il faut juste cliquer sur OK et avancer vers le site.

### feedback-api

Gestion de la DB : Un problème survient du fait que SQLite est un fichier et non un serveur de base de données.
Donc, on a des soucis pour récupérer tous les feedbacks avec un GET : si on choisit un StatefulSet, chaque réplique a son propre univers séparé des autres.
Le meilleur choix ici serait donc de définir un PVC et un PV avec un Deployment pour que toutes les répliques lisent et écrivent dans le même fichier. Cette solution semble OK dans le cadre du TP : l'administrateur verra toujours la totalité des données, peu importe quel pod répond.

Par contre c'est fragile, mais pourquoi ? Si les 2 réplicas tentent d'écrire dans le fichier SQLite exactement à la même milliseconde, l'un d'eux plantera ou sera bloqué (Database Locked). Si on prend le problème avec le théorème CAP vu en cours, ici on sacrifie la disponibilité. Pour faire mieux, il faudrait changer le type de base de données.

Autre soucis, cette solution implique que nos réplicas tournent sur le même node (dans ce TP on nous propose un node maitre et deux workers donc on déploie nos pods sur 2 nodes différents.) Le risque c'est que si le node échoue alors tous nos pods feedback-api tombent et on fait face à une indisponibilité de service.

LivenessProbe: quoi tester ? ON voit qu'on peut tester deux choses la route health et la route feedback, la route health vérifie que le serveur est bien lancé et répond 200 OK par contre la route feedback vérifie que le serveur arrive bien à communiquer avec la base de données. Ici on pourrait vérifier /health , mais on n'aurait pas d'information si la db plante, en utilisant feedback on détourne une route métier, mais on vérifie que le serveur est up et communique avec la base de données. SI la db plante (par exemple un accès pas relâché) kubernetes tue le pod et le remplaces ce qui permet de relâcher la db.

Vérifier le liveness probe avec /feedback parait plus judicieux, mais implique plus d'appels réseaux et de travail, car on retourne réellement les éléments et si on a beaucoup de choses dans la base de données ça peut devenir problématique.
La db locale est critique pour ce service !
On pourrait aussi partir du principe que comme la db est locale alors on aura moins de soucis.

On va supposer que la route health a été créée pour le liveness probe et utiliser celle-ci même s'il faut nuancer / il faudrait ajouter des tests pour être certain que la base de données communique bien avec notre service.

### api-gateway

Remplacement de l'image de base dans le Dockerfile de openjdk:8-jre-alpine par eclipse-temurin:8-jre-alpine, car la version openjdk est plus à jour et n'existe pu, alors que la version par eclipse est disponible et fiable.

### logic-api

Modification du fichier requirements.txt pour pin Werkzeug en version 2.2.2 (requis par la version de Flask) et mis la version de textblob de 0.15.0 à 0.19.0 afin de corriger un bug et la rendre compatible avec le code.Ensuite, le Dockerfile a été mis à jour pour utiliser python:3.9-slim-buster au lieu de 3.8 pour supporter la nouvelle version de textblob. Sources: https://pypi.org/project/textblob/, https://stackoverflow.com/questions/77213053/why-did-flask-start-failing-with-importerror-cannot-import-name-url-quote-fr, https://github.com/sloria/TextBlob/issues/474
