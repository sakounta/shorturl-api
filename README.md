# Short URL - API de Raccourcissement d'URL

## Description

Cette application est une API REST qui permet de raccourcir des URLs. Elle offre les fonctionnalités suivantes :
- Raccourcir une URL longue en une URL courte.
- Rediriger vers l'URL originale à partir d'une URL courte.
- Récupérer le nombre de visites d'une URL courte.
- Récupérer toutes les URLs courtes associées à une URL originale.

## Technologies Utilisées

- **Java 17**
- **Spring Boot**
- **PostgreSQL** (base de données)

---

## Prérequis

Avant de commencer, assurez-vous d'avoir installé les éléments suivants :

- **Java 17** : [Télécharger Java](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- **PostgreSQL** : [Télécharger PostgreSQL](https://www.postgresql.org/download/)

---

## Configuration

### 1. Cloner le Projet

Clonez ce dépôt sur votre machine locale :

```bash
git clone https://github.com/sakounta/shorturl-api.git
cd short-url-api
