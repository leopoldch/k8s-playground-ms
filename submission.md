# Description de la remise

### Nom complet: XXXXX
### NIP: 111 111 111
### Liste des codes et descriptions des fonctionnalités sélectionnées:
Exemple:
- (FA31) Intégration d'un outil de gestion de journaux (Loki, Promtail) ==> 5%
  Implémentation réalisée avec un DaemonSet (Promtail) et Loki, tel que vu dans le cadre du cours. Un dashboard de visualisation est disponible sur Grafana (/admin/grafana). 
  **Note technique :** En raison de l'utilisation de l'environnement *Kind*, nous avons dû attribuer des droits privilégiés (`privileged: true`, `runAsUser: 0`) au DaemonSet. Ces permissions, bien que non recommandées en production, étaient strictement requises dans le cadre de ce TP pour permettre à Promtail de lire correctement les fichiers de logs sur les nœuds virtuels.

### Directives nécessaires à la correction
Pour appliquer les configs: `kubectl apply -R -f submission` car pour des soucis de propreté les configurations sont rangés dans différents dossiers.

Grafana est accéssible à travers `/admin/grafana`


### Commentaires généraux:
L'ingress de grafana a été défini dans un autre que l'application car dans un autre namespace pour la propreté.
Grafana peut mettre un certain temps à se lancer, merci d'être patient et d'attendre au moins 1 minute.

Pour permettre le déploiement de l'Ingress NGINX en une seule commande, nous avons du retirer la configuration du ValidatingWebhook. Ce composant, qui est là dans une installation standard, sert à valider la syntaxe des règles Ingress. Cependant, il crée une condition de concurrence (race condition) pendant un déploiement simultané : Kubernetes tente de valider l'Ingress via le Webhook alors que le Pod du contrôleur n'est pas encore prêt, ce qui bloque le déploiement. Le fait de l'avoir retiré n'est pas idéal pour la sécurité/fiabilité, mais était nécessaire pour respecter la contrainte d'exécution unique du TP