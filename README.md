# Projet AMIO

Groupe :
- Hind Latrache
- Armand Bouveron

## Sources

GitHub : [pj_amio](https://github.com/ABouveron/pj_amio)

## Description

L'objectif de ce mini-projet est de finaliser une application Android exploitant des données
issues d'un réseau de capteurs et exposées à travers un web service (IoTLab de TELECOM Nancy). Le but
est de détecter les lumières laissées actives dans les bureaux en soirée.

## Fonctionnalités 

- [x] démarrage et arrêt du service via un interrupteur
- [x] lister dans l'activité principale les capteurs actifs et les valeurs de luminosité qu'ils relèvent,
  en mettant en évidence ceux qui indiquent la présence d'une lumière active (affichés en rouge)
- [x] émettre une notification si une nouvelle lumière vient d'être allumée en semaine entre 19h et
  23h, en spécifiant le capteur impliqué (envoi d'une notification si changement de luminosité >= 50)
- [x] envoyer un email si cet événement survient le week-end entre 19h et 23h ou en semaine
  entre 23h et 6h (ouverture de l'application mail avec les données pré-remplies)
- [x] permettre la configuration des plages horaires et de l'adresse email dans un menu dédié de
  l'application (menu de préférences accessible depuis l'activité principale)
- [x] permettre la configuration de l'adresse mail du correspondant dans un menu dédié de l'application
  (menu de préférences accessible depuis l'activité principale)
- [x] démarrage possible de l'application au démarrage du téléphone (à configurer dans les préférences) - non fonctionnel quand l'application n'est pas lancée
