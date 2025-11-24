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
Pour appliquer les configs: `kubectl apply -R -f submission` car pour des soucis de propreté les configurations sont rangés dans différents dossiers.

Grafana est accéssible à travers `/admin/grafana`


### Commentaires généraux:
L'ingress de grafana a été défini dans un autre que l'application car dans un autre namespace pour la propreté.
Grafana peut mettre un certain temps à se lancer, merci d'être patient et d'attendre au moins 1 minute.

Pour permettre le déploiement de l'Ingress NGINX en une seule commande, nous avons du retirer la configuration du ValidatingWebhook. Ce composant, qui est là dans une installation standard, sert à valider la syntaxe des règles Ingress. Cependant, il crée une condition de concurrence (race condition) pendant un déploiement simultané : Kubernetes tente de valider l'Ingress via le Webhook alors que le Pod du contrôleur n'est pas encore prêt, ce qui bloque le déploiement. Le fait de l'avoir retiré n'est pas idéal pour la sécurité/fiabilité, mais était nécessaire pour respecter la contrainte d'exécution unique du TP