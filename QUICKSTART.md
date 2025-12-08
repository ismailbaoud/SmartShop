# ⚡ Démarrage Rapide - SmartShop

Guide ultra-simple pour démarrer SmartShop en **5 minutes** !

---

## ✅ Ce dont vous avez besoin

- [ ] Java 21 installé
- [ ] Maven installé  
- [ ] PostgreSQL installé et démarré

---

## 🚀 Installation en 5 Minutes

### 1️⃣ Cloner le Projet (30 secondes)

```bash
git clone https://github.com/votre-repo/smartShop.git
cd smartShop
```

### 2️⃣ Créer la Base de Données (1 minute)

```bash
# Ouvrir PostgreSQL
sudo -u postgres psql

# Créer la base
CREATE DATABASE smartshoptest;

# Quitter
\q
```

### 3️⃣ Configurer (30 secondes)

Ouvrir `src/main/resources/application.properties` et modifier si nécessaire :

```properties
spring.datasource.username=happy
spring.datasource.password=1234
```

### 4️⃣ Installer et Démarrer (3 minutes)

```bash
mvn clean install
mvn spring-boot:run
```

### 5️⃣ Tester (30 secondes)

Ouvrir votre navigateur : **http://localhost:8080**

---

## 🎯 Premier Test de l'API

### Créer votre premier client

```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "John Doe",
    "email": "john@test.com",
    "password": "test123"
  }'
```

**✅ Si vous voyez un JSON en réponse, ça marche !**

---

## 📚 Suite

Maintenant que tout fonctionne, consultez :

- **README.md** → Documentation complète
- **USAGE_EXAMPLES.md** → Exemples pratiques

---

## 🆘 Problème ?

### Erreur "Port 8080 already in use"
```bash
# Changer le port dans application.properties
server.port=9090
```

### Erreur "Connection refused"
```bash
# Démarrer PostgreSQL
sudo systemctl start postgresql
```

### Erreur de compilation
```bash
# Nettoyer et recompiler
mvn clean compile
```

---

## 🎉 C'est Parti !

Vous êtes prêt à utiliser SmartShop ! 🚀

**Besoin d'aide ?** → Voir README.md pour plus de détails

