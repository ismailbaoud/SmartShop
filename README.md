# 🛒 SmartShop - Plateforme de Gestion de Boutique

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.6-blue)
![Maven](https://img.shields.io/badge/Maven-3.x-red)
![Tests](https://img.shields.io/badge/Tests-64%20unitaires-success)

**SmartShop** est une application de gestion de boutique en ligne moderne développée avec Spring Boot. Elle permet de gérer les clients, les produits, les commandes, les paiements et un système de fidélité automatique.

---

## 📋 Table des Matières

- [Fonctionnalités](#-fonctionnalités)
- [Technologies Utilisées](#-technologies-utilisées)
- [Architecture](#-architecture)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Démarrage](#-démarrage)
- [API Endpoints](#-api-endpoints)
- [Système de Fidélité](#-système-de-fidélité)
- [Tests](#-tests)
- [Structure du Projet](#-structure-du-projet)
- [Modèles de Données](#-modèles-de-données)
- [Contribuer](#-contribuer)

---

## 🎯 Fonctionnalités

### Gestion des Utilisateurs et Authentification
- ✅ **Inscription** : Création de compte client avec mot de passe sécurisé (BCrypt)
- ✅ **Connexion** : Authentification avec session HTTP
- ✅ **Déconnexion** : Invalidation de session
- ✅ **Profil** : Consultation du profil utilisateur

### Gestion des Clients
- ✅ **CRUD complet** : Créer, Lire, Mettre à jour, Supprimer
- ✅ **Système de fidélité** : 4 niveaux (BASIC, SILVER, GOLD, PLATINIUM)
- ✅ **Upgrade automatique** : Basé sur le nombre de commandes et montant dépensé
- ✅ **Historique** : Première et dernière date de commande

### Gestion des Produits
- ✅ **CRUD complet** : Gestion complète des produits
- ✅ **Gestion du stock** : Ajout et retrait automatique
- ✅ **Validation** : Vérification de la disponibilité avant commande
- ✅ **Timestamps** : Dates de création, modification et suppression

### Gestion des Commandes
- ✅ **Création** : Commande avec plusieurs produits
- ✅ **Calculs automatiques** : Sous-total, TVA, réductions
- ✅ **Réduction fidélité** : Selon le niveau du client
- ✅ **Code promo** : Application de réductions supplémentaires
- ✅ **États** : PENDING, CONFIRMED, CANCELED
- ✅ **Annulation** : Restitution automatique du stock

### Gestion des Paiements
- ✅ **Paiements multiples** : Par commande
- ✅ **Méthodes** : ESPECES, CHEQUE, VIREMENT
- ✅ **Suivi** : Date de paiement et d'encaissement
- ✅ **Validation** : Montants et confirmation automatique
- ✅ **États** : PENDING, ENCAISSE, REJETÉ

### Codes Promo
- ✅ **Génération** : Codes automatiques format "PROMO-XXXX"
- ✅ **Validation** : Vérification date d'expiration
- ✅ **Pourcentage** : Réduction configurable
- ✅ **Statistiques** : Nombre d'utilisations

---

## 🛠️ Technologies Utilisées

### Backend
- **Java 21** - Langage de programmation
- **Spring Boot 4.0.0** - Framework principal
- **Spring Web** - API REST
- **Spring Data JPA** - Accès aux données
- **Hibernate** - ORM

### Base de Données
- **PostgreSQL 17.6** - Base de données relationnelle

### Sécurité
- **BCrypt** - Hashing des mots de passe
- **Spring Session** - Gestion des sessions HTTP

### Outils
- **Lombok** - Réduction du code boilerplate
- **MapStruct** - Mapping Entity ↔ DTO
- **Maven** - Gestion des dépendances
- **Liquibase** - Migrations de base de données

### Tests
- **JUnit 5** - Framework de test
- **Mockito** - Mocking
- **Spring Boot Test** - Tests d'intégration
- **64 tests unitaires** - Couverture complète

---

## 🏗️ Architecture

SmartShop suit une **architecture en couches** :

```
┌─────────────────────────────────────┐
│         Controllers                 │  ← API REST
│  (AuthController, ClientController) │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│           Services                  │  ← Logique métier
│  (AuthService, ClientService, ...)  │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│         Repositories                │  ← Accès données
│  (ClientRepository, OrderRepo, ...) │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       PostgreSQL Database           │
└─────────────────────────────────────┘
```

### Composants Principaux

- **Controllers** : Gèrent les requêtes HTTP
- **Services** : Contiennent la logique métier
- **Repositories** : Interagissent avec la base de données
- **DTOs** : Objets de transfert de données
- **Mappers** : Conversion Entity ↔ DTO
- **Models** : Entités JPA
- **Exceptions** : Gestion personnalisée des erreurs

---

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

### Obligatoire
- ✅ **Java 21** ou supérieur
  ```bash
  java -version
  # Doit afficher : java version "21.x.x"
  ```

- ✅ **Maven 3.x**
  ```bash
  mvn -version
  # Doit afficher : Apache Maven 3.x.x
  ```

- ✅ **PostgreSQL 12+** (recommandé 17.6)
  ```bash
  psql --version
  # Doit afficher : psql (PostgreSQL) 17.x
  ```

### Optionnel
- 💡 **Git** - Pour cloner le projet
- 💡 **IntelliJ IDEA** ou **Eclipse** - IDE recommandé
- 💡 **Postman** ou **cURL** - Pour tester l'API

---

## 🚀 Installation

### Étape 1 : Cloner le Projet

```bash
# Cloner le repository
git clone https://github.com/votre-repo/smartShop.git

# Aller dans le dossier
cd smartShop
```

### Étape 2 : Créer la Base de Données

```bash
# Se connecter à PostgreSQL
sudo -u postgres psql

# Créer la base de données
CREATE DATABASE smartshoptest;

# Créer un utilisateur (optionnel)
CREATE USER happy WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE smartshoptest TO happy;

# Quitter
\q
```

### Étape 3 : Configurer l'Application

Modifier le fichier `src/main/resources/application.properties` :

```properties
# Nom de l'application
spring.application.name=smartShoptest

# Configuration base de données
spring.datasource.url=jdbc:postgresql://localhost:5432/smartshoptest
spring.datasource.username=happy
spring.datasource.password=1234
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuration Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

**⚠️ Important** : Remplacez `happy` et `1234` par vos propres identifiants PostgreSQL.

### Étape 4 : Installer les Dépendances

```bash
mvn clean install
```

Cette commande va :
- Télécharger toutes les dépendances
- Compiler le projet
- Exécuter les tests
- Créer le fichier WAR

---

## ⚙️ Configuration

### Configuration de la Base de Données

**Modes de création de schéma** :

```properties
# Option 1 : Créer les tables automatiquement (développement)
spring.jpa.hibernate.ddl-auto=create

# Option 2 : Mettre à jour les tables (recommandé)
spring.jpa.hibernate.ddl-auto=update

# Option 3 : Valider seulement (production)
spring.jpa.hibernate.ddl-auto=validate

# Option 4 : Ne rien faire
spring.jpa.hibernate.ddl-auto=none
```

### Configuration du Port

Par défaut, l'application démarre sur le port **8080**. Pour changer :

```properties
server.port=9090
```

### Configuration des Logs

```properties
# Niveau de log
logging.level.com.ismail.smartShop=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## 🎬 Démarrage

### Méthode 1 : Avec Maven

```bash
# Compiler et démarrer
mvn spring-boot:run
```

### Méthode 2 : Avec Java

```bash
# Compiler d'abord
mvn clean package

# Démarrer l'application
java -jar target/smartShop-0.0.1-SNAPSHOT.war
```

### Méthode 3 : Avec votre IDE

1. Ouvrir le projet dans IntelliJ IDEA ou Eclipse
2. Trouver la classe `SmartShopApplication.java`
3. Clic droit → Run 'SmartShopApplication'

### Vérifier que ça fonctionne

L'application démarre sur : **http://localhost:8080**

Vous devriez voir dans les logs :
```
Started SmartShopApplication in X.XXX seconds
```

---

## 🌐 API Endpoints

### 🔐 Authentification

#### Connexion
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "client@test.com",
  "password": "password123"
}
```

**Réponse** :
```json
{
  "id": 1,
  "userName": "client@test.com",
  "role": "CLIENT"
}
```

#### Déconnexion
```http
POST /api/auth/logout
```

---

### 👤 Clients

#### Créer un client
```http
POST /api/clients
Content-Type: application/json

{
  "nom": "John Doe",
  "email": "john@example.com",
  "password": "secure123"
}
```

#### Obtenir tous les clients
```http
GET /api/clients
```

#### Obtenir un client par ID
```http
GET /api/clients/1
```

#### Mettre à jour un client
```http
PUT /api/clients/1
Content-Type: application/json

{
  "nom": "Jane Doe",
  "email": "jane@example.com"
}
```

#### Supprimer un client
```http
DELETE /api/clients/1
```

#### Changer le niveau de fidélité
```http
PUT /api/clients/1/fidelite
Content-Type: application/json

{
  "niveauDeFidelite": "GOLD"
}
```

---

### 📦 Produits

#### Créer un produit
```http
POST /api/products
Content-Type: application/json

{
  "nom": "Laptop Dell",
  "prixUnit": 999.99,
  "stockQuantitie": 50
}
```

#### Obtenir tous les produits
```http
GET /api/products
```

#### Obtenir un produit par ID
```http
GET /api/products/1
```

#### Mettre à jour un produit
```http
PUT /api/products/1
Content-Type: application/json

{
  "nom": "Laptop Dell XPS",
  "prixUnit": 1299.99,
  "stockQuantitie": 30
}
```

#### Supprimer un produit
```http
DELETE /api/products/1
```

---

### 🛍️ Commandes

#### Créer une commande
```http
POST /api/orders
Content-Type: application/json

{
  "client_id": 1,
  "promo": "PROMO-ABC1",
  "tva": 20,
  "items": [
    {
      "product_id": 1,
      "quantite": 2
    },
    {
      "product_id": 2,
      "quantite": 1
    }
  ]
}
```

#### Obtenir toutes les commandes
```http
GET /api/orders
```

#### Obtenir les commandes d'un client
```http
GET /api/orders/client/1
```

#### Confirmer une commande
```http
PUT /api/orders/1/confirm
```

#### Annuler une commande
```http
PUT /api/orders/1/cancel
```

---

### 💳 Paiements

#### Créer un paiement
```http
POST /api/orders/1/payments
Content-Type: application/json

{
  "amount": 500.00,
  "method": "ESPECES",
  "banque": "BNP Paribas"
}
```

#### Obtenir les paiements d'une commande
```http
GET /api/orders/1/payments
```

#### Mettre à jour le statut d'un paiement
```http
PUT /api/orders/1/payments/1/status
Content-Type: application/json

{
  "status": "ENCAISSE"
}
```

---

### 🎟️ Codes Promo

#### Créer un code promo
```http
POST /api/promos
Content-Type: application/json

{
  "discountPercent": 15,
  "expiresAt": "2025-12-31T23:59:59"
}
```

**Réponse** :
```json
{
  "id": 1,
  "code": "PROMO-A3B7",
  "discountPercent": 15,
  "expiresAt": "2025-12-31T23:59:59",
  "usedTimes": 0
}
```

#### Valider un code promo
```http
GET /api/promos/validate?code=PROMO-A3B7
```

---

## 🏆 Système de Fidélité

SmartShop propose un **système de fidélité automatique** avec 4 niveaux :

### Niveaux de Fidélité

| Niveau | Badge | Conditions d'Upgrade | Réduction |
|--------|-------|---------------------|-----------|
| **BASIC** | 🥉 | Nouveau client | Aucune |
| **SILVER** | 🥈 | 3 commandes OU 1000€ dépensés | -5% si commande ≥ 500€ |
| **GOLD** | 🥇 | 10 commandes OU 5000€ dépensés | -10% si commande ≥ 800€ |
| **PLATINIUM** | 💎 | 20 commandes OU 15000€ dépensés | -15% si commande ≥ 1200€ |

### Comment ça marche ?

1. **Inscription** : Tout nouveau client commence au niveau **BASIC**
2. **Commandes** : À chaque commande validée, le système compte :
   - Nombre total de commandes
   - Montant total dépensé
3. **Upgrade automatique** : Si les conditions sont remplies, le niveau monte automatiquement
4. **Réductions** : Les réductions s'appliquent automatiquement sur les commandes éligibles

### Exemple

Un client avec le niveau **GOLD** qui passe une commande de **1000€** :
- Sous-total : 1000€
- Réduction fidélité (10%) : -100€
- Code promo éventuel : -X€
- TVA (20%) : +(total après réductions × 0.20)
- **Total TTC** : Montant final

---

## 🧪 Tests

SmartShop dispose d'une suite complète de **64 tests unitaires**.

### Exécuter tous les tests

```bash
mvn test
```

### Exécuter un service spécifique

```bash
mvn -Dtest=ProductServiceTest test
```

### Services Testés

| Service | Tests | Couverture |
|---------|-------|------------|
| AuthService | 3 | 100% |
| UserService | 5 | 100% |
| ClientService | 18 | 100% |
| ProductService | 10 | 100% |
| OrderService | 10 | 100% |
| OrderItemService | 4 | 100% |
| PaymentService | 8 | 100% |
| PromoService | 6 | 100% |

### Voir les rapports

Les rapports de tests sont générés dans :
```
target/surefire-reports/
```

---

## 📁 Structure du Projet

```
smartShop/
├── src/
│   ├── main/
│   │   ├── java/com/ismail/smartShop/
│   │   │   ├── annotation/           # Annotations personnalisées
│   │   │   │   ├── RequireAdmin.java
│   │   │   │   ├── RequireAuth.java
│   │   │   │   └── RequireClient.java
│   │   │   ├── config/               # Configuration Spring
│   │   │   │   ├── AuthInterceptor.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/           # Contrôleurs REST
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ClientController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── promoController.java
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   │   ├── auth/
│   │   │   │   ├── client/
│   │   │   │   ├── order/
│   │   │   │   ├── orderItem/
│   │   │   │   ├── payment/
│   │   │   │   ├── product/
│   │   │   │   ├── promo/
│   │   │   │   └── user/
│   │   │   ├── exception/            # Gestion des exceptions
│   │   │   │   ├── AppException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── client/
│   │   │   │   └── product/
│   │   │   ├── helper/               # Classes utilitaires
│   │   │   │   ├── CodeGenerater.java
│   │   │   │   └── passwordHasher.java
│   │   │   ├── mapper/               # MapStruct mappers
│   │   │   │   ├── ClientMapper.java
│   │   │   │   ├── OrderMapper.java
│   │   │   │   ├── OrderItemMapper.java
│   │   │   │   ├── PaymentMapper.java
│   │   │   │   ├── ProductMapper.java
│   │   │   │   ├── PromoMapper.java
│   │   │   │   └── UserMapper.java
│   │   │   ├── model/                # Entités JPA
│   │   │   │   ├── Client.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Promo.java
│   │   │   │   ├── User.java
│   │   │   │   └── enums/
│   │   │   ├── repository/           # Repositories JPA
│   │   │   │   ├── ClientRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── OrderItemRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── PromoRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/              # Services métier
│   │   │   │   ├── implementation/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── ClientService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── OrderItemService.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── PromoService.java
│   │   │   │   └── UserService.java
│   │   │   ├── ServletInitializer.java
│   │   │   └── SmartShopApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                         # Tests unitaires
│       └── java/com/ismail/smartShop/
│           └── service/
│               ├── AuthServiceTest.java
│               ├── ClientServiceTest.java
│               ├── OrderServiceTest.java
│               ├── OrderItemServiceTest.java
│               ├── PaymentServiceTest.java
│               ├── ProductServiceTest.java
│               ├── PromoServiceTest.java
│               └── UserServiceTest.java
├── pom.xml                           # Configuration Maven
├── run-tests.sh                      # Script d'exécution des tests
└── README.md                         # Ce fichier
```

---

## 📊 Modèles de Données

### Client
```java
Client {
  id: Long
  nom: String
  email: String
  niveauDeFidelite: NiveauFidelite (BASIC, SILVER, GOLD, PLATINIUM)
  totalCommandes: Integer
  totalDepense: Double
  firstOrderDate: LocalDateTime
  lastOrderDate: LocalDateTime
  user: User (OneToOne)
  orders: List<Order> (OneToMany)
}
```

### Product
```java
Product {
  id: Long
  nom: String
  prixUnit: Double
  stockQuantitie: Integer
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
  deletedAt: LocalDateTime
}
```

### Order
```java
Order {
  id: Long
  client: Client (ManyToOne)
  promo: String
  dateOrder: LocalDateTime
  subTotal: Double
  tva: Double
  totalTTC: Double
  status: OrderStatus (PANDING, CONFIRMED, CANCELED)
  montant_restant: Double
  orderItems: List<OrderItem> (OneToMany)
  payments: List<Payment> (OneToMany)
}
```

### Payment
```java
Payment {
  id: Long
  amount: Double
  method: PaymentMethod (ESPECES, CHEQUE, VIREMENT)
  banque: String
  datePaiement: LocalDateTime
  dateEncaissement: LocalDateTime
  status: PaymentStatus (PENDING, ENCAISSE, REJETÉ)
  order: Order (ManyToOne)
}
```

### Promo
```java
Promo {
  id: Long
  code: String (format: PROMO-XXXX)
  discountPercent: Integer
  usedTimes: Integer
  expiresAt: LocalDateTime
}
```

---

## 🤝 Contribuer

Les contributions sont les bienvenues ! Voici comment contribuer :

### 1. Fork le projet
```bash
# Cliquer sur "Fork" sur GitHub
```

### 2. Créer une branche
```bash
git checkout -b feature/ma-nouvelle-fonctionnalite
```

### 3. Faire les modifications
```bash
# Modifier le code
# Ajouter des tests
# Vérifier que les tests passent
mvn test
```

### 4. Commit et Push
```bash
git add .
git commit -m "Ajout de ma nouvelle fonctionnalité"
git push origin feature/ma-nouvelle-fonctionnalite
```

### 5. Créer une Pull Request
- Aller sur GitHub
- Cliquer sur "New Pull Request"
- Décrire vos modifications

---

## 📝 Conventions de Code

- **Java** : Suivre les conventions Java standard
- **Naming** : 
  - Classes : `PascalCase`
  - Méthodes : `camelCase`
  - Variables : `camelCase`
  - Constantes : `UPPER_SNAKE_CASE`
- **Tests** : Format `methode_ShouldExpectedBehavior_WhenCondition`
- **Commits** : Messages clairs et descriptifs

---

## 🐛 Problèmes Courants

### Erreur : "Port 8080 already in use"
**Solution** : Changer le port dans `application.properties`
```properties
server.port=9090
```

### Erreur : "Connection to database failed"
**Solution** : Vérifier que PostgreSQL est démarré
```bash
sudo systemctl start postgresql
sudo systemctl status postgresql
```

### Erreur : "Table doesn't exist"
**Solution** : Changer le mode de création
```properties
spring.jpa.hibernate.ddl-auto=create
```

### Erreur de compilation MapStruct
**Solution** : Nettoyer et recompiler
```bash
mvn clean install
```

---

## 📞 Support

Pour toute question ou problème :

1. **Issues GitHub** : Créer une issue sur le repository
2. **Documentation** : Consulter ce README
3. **Tests** : Voir les exemples dans les fichiers de test

---

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

---

## 👨‍💻 Auteur

**Ismail**
- GitHub: [@votre-github](https://github.com/votre-github)
- Email: contact@example.com

---

## 🎉 Remerciements

Merci d'utiliser SmartShop ! N'hésitez pas à ⭐ le projet si vous le trouvez utile.

---

## 📚 Ressources Supplémentaires

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [MapStruct Documentation](https://mapstruct.org/)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

**Version** : 0.0.1-SNAPSHOT  
**Date** : Décembre 2024  
**Status** : En développement actif 🚀

