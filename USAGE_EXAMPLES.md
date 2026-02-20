# 📖 Guide d'Utilisation Pratique - SmartShop

Ce guide contient des **exemples concrets** pour utiliser SmartShop au quotidien.

---

## 🎯 Scénarios d'Utilisation

### Scénario 1 : Inscription d'un Nouveau Client

**Objectif** : Créer un compte client et se connecter

#### Étape 1 : Créer le compte
```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Marie Dupont",
    "email": "marie.dupont@email.com",
    "password": "MotDePasseSecurise123"
  }'
```

**Résultat** :
```json
{
  "id": 1,
  "nom": "Marie Dupont",
  "email": "marie.dupont@email.com",
  "totalCommandes": 0,
  "totalDepense": 0.0,
  "niveauDeFidelite": "BASIC",
  "firstOrderDate": null,
  "lastOrderDate": null
}
```

#### Étape 2 : Se connecter
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "marie.dupont@email.com",
    "password": "MotDePasseSecurise123"
  }'
```

---

### Scénario 2 : Créer un Catalogue de Produits

**Objectif** : Ajouter plusieurs produits au catalogue

#### Produit 1 : Laptop
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Laptop HP Pavilion",
    "prixUnit": 799.99,
    "stockQuantitie": 15
  }'
```

#### Produit 2 : Souris
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Souris Logitech MX Master",
    "prixUnit": 89.99,
    "stockQuantitie": 50
  }'
```

#### Produit 3 : Clavier
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Clavier Mécanique Corsair",
    "prixUnit": 129.99,
    "stockQuantitie": 30
  }'
```

---

### Scénario 3 : Passer une Première Commande

**Objectif** : Client BASIC achète pour la première fois

#### Étape 1 : Créer la commande
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": 1,
    "promo": null,
    "tva": 20,
    "items": [
      {
        "product_id": 1,
        "quantite": 1
      },
      {
        "product_id": 2,
        "quantite": 2
      }
    ]
  }'
```

**Calcul automatique** :
- Laptop : 799.99€ × 1 = 799.99€
- Souris : 89.99€ × 2 = 179.98€
- **Sous-total** : 979.97€
- **Réduction fidélité** : 0€ (BASIC, pas de réduction)
- **Réduction promo** : 0€ (pas de code)
- **HT après réductions** : 979.97€
- **TVA (20%)** : 195.99€
- **Total TTC** : 1175.96€

#### Étape 2 : Payer la commande
```bash
curl -X POST http://localhost:8080/api/orders/1/payments \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 1175.96,
    "method": "VIREMENT",
    "banque": "Crédit Agricole"
  }'
```

**Résultat** : La commande est automatiquement **CONFIRMÉE** car payée intégralement.

---

### Scénario 4 : Atteindre le Niveau SILVER

**Objectif** : Passer du niveau BASIC à SILVER

Le client doit faire **3 commandes** OU dépenser **1000€**.

#### Commande 2
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": 1,
    "items": [
      {"product_id": 3, "quantite": 1}
    ]
  }'

# Payer 155.99€ (129.99 + TVA)
curl -X POST http://localhost:8080/api/orders/2/payments \
  -H "Content-Type: application/json" \
  -d '{"amount": 155.99, "method": "ESPECES"}'
```

#### Commande 3
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": 1,
    "items": [
      {"product_id": 2, "quantite": 1}
    ]
  }'

# Payer 107.99€ (89.99 + TVA)
curl -X POST http://localhost:8080/api/orders/3/payments \
  -H "Content-Type: application/json" \
  -d '{"amount": 107.99, "method": "CHEQUE"}'
```

**✅ Après la 3ème commande, le client passe automatiquement SILVER !**

---

### Scénario 5 : Profiter de la Réduction SILVER

**Objectif** : Bénéficier de -5% sur une commande ≥ 500€

#### Commande avec réduction
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": 1,
    "items": [
      {"product_id": 1, "quantite": 1}
    ]
  }'
```

**Calcul avec réduction SILVER** :
- Sous-total : 799.99€
- **Réduction fidélité (-5%)** : -40.00€
- HT après réduction : 759.99€
- TVA (20%) : 152.00€
- **Total TTC** : 911.99€ ✅ Économie de 48€ !

---

### Scénario 6 : Utiliser un Code Promo

**Objectif** : Créer et utiliser un code promo

#### Étape 1 : Créer le code promo (Admin)
```bash
curl -X POST http://localhost:8080/api/promos \
  -H "Content-Type: application/json" \
  -d '{
    "discountPercent": 10,
    "expiresAt": "2025-12-31T23:59:59"
  }'
```

**Résultat** :
```json
{
  "id": 1,
  "code": "PROMO-A7B3",
  "discountPercent": 10,
  "expiresAt": "2025-12-31T23:59:59",
  "usedTimes": 0
}
```

#### Étape 2 : Utiliser le code
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": 1,
    "promo": "PROMO-A7B3",
    "items": [
      {"product_id": 1, "quantite": 1}
    ]
  }'
```

**Calcul avec SILVER + Code Promo** :
- Sous-total : 799.99€
- **Réduction fidélité (-5%)** : -40.00€ = 759.99€
- **Réduction promo (-10%)** : -76.00€ = 683.99€
- TVA (20%) : 136.80€
- **Total TTC** : 820.79€ ✅ Économie totale de 164.17€ !

---

### Scénario 7 : Paiement en Plusieurs Fois

**Objectif** : Payer une commande en 3 versements

#### Commande de 1200€
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": 1,
    "items": [
      {"product_id": 1, "quantite": 1},
      {"product_id": 3, "quantite": 3}
    ]
  }'
```

Total : 1189.96€

#### Paiement 1 : Acompte
```bash
curl -X POST http://localhost:8080/api/orders/5/payments \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 400.00,
    "method": "ESPECES",
    "banque": ""
  }'
```

**État** : PANDING (montant restant : 789.96€)

#### Paiement 2 : Deuxième versement
```bash
curl -X POST http://localhost:8080/api/orders/5/payments \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 400.00,
    "method": "CHEQUE",
    "banque": "BNP Paribas"
  }'
```

**État** : PANDING (montant restant : 389.96€)

#### Paiement 3 : Solde
```bash
curl -X POST http://localhost:8080/api/orders/5/payments \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 389.96,
    "method": "VIREMENT",
    "banque": "Société Générale"
  }'
```

**État** : ✅ CONFIRMED (montant restant : 0€)

---

### Scénario 8 : Annuler une Commande

**Objectif** : Annuler une commande et restituer le stock

```bash
curl -X PUT http://localhost:8080/api/orders/3/cancel
```

**Ce qui se passe** :
- Le statut passe à **CANCELED**
- Le stock des produits est **restitué automatiquement**
- Le client peut refaire une commande

---

### Scénario 9 : Gérer le Stock

**Objectif** : Augmenter le stock d'un produit

#### Voir le stock actuel
```bash
curl -X GET http://localhost:8080/api/products/1
```

Résultat : `stockQuantitie: 10`

#### Ajouter du stock
Il faut créer une méthode dans le controller (non implémentée par défaut).

**Alternative** : Mettre à jour le produit
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Laptop HP Pavilion",
    "prixUnit": 799.99,
    "stockQuantitie": 50
  }'
```

Nouveau stock : 50 unités ✅

---

### Scénario 10 : Atteindre le Niveau GOLD

**Objectif** : Passer de SILVER à GOLD

Le client SILVER doit faire **10 commandes** OU dépenser **5000€**.

#### Méthode rapide : 1 grosse commande
```bash
# Commander pour 5000€
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "client_id": 1,
    "items": [
      {"product_id": 1, "quantite": 6}
    ]
  }'

# Payer
curl -X POST http://localhost:8080/api/orders/10/payments \
  -H "Content-Type: application/json" \
  -d '{"amount": 5279.94, "method": "VIREMENT"}'
```

**✅ Le client passe automatiquement GOLD !**

Maintenant il bénéficie de **-10% sur les commandes ≥ 800€**.

---

## 🔍 Vérifier le Statut d'un Client

```bash
curl -X GET http://localhost:8080/api/clients/1
```

**Résultat** :
```json
{
  "id": 1,
  "nom": "Marie Dupont",
  "email": "marie.dupont@email.com",
  "totalCommandes": 10,
  "totalDepense": 8724.82,
  "niveauDeFidelite": "GOLD",
  "firstOrderDate": "2024-12-01T10:30:00",
  "lastOrderDate": "2024-12-06T15:45:00"
}
```

---

## 📊 Consulter l'Historique

### Toutes les commandes d'un client
```bash
curl -X GET http://localhost:8080/api/orders/client/1
```

### Tous les paiements d'une commande
```bash
curl -X GET http://localhost:8080/api/orders/1/payments
```

### Tous les produits
```bash
curl -X GET http://localhost:8080/api/products
```

### Tous les clients
```bash
curl -X GET http://localhost:8080/api/clients
```

---

## 💡 Conseils d'Utilisation

### 1. Toujours vérifier le stock
Avant de passer commande, vérifiez qu'il y a assez de stock :
```bash
curl -X GET http://localhost:8080/api/products/1
```

### 2. Utiliser les codes promo intelligemment
- Créez des codes avec dates d'expiration
- Suivez l'utilisation avec `usedTimes`

### 3. Profiter du système de fidélité
- Encouragez les clients à commander régulièrement
- Les réductions s'appliquent automatiquement

### 4. Gérer les paiements multiples
- Acceptez des acomptes
- Le système gère le montant restant automatiquement

### 5. Annuler si nécessaire
- L'annulation restitue le stock automatiquement
- Aucune perte de produits

---

## 🎓 Résumé des Chemins de Fidélité

```
BASIC (🥉)
   ↓ 3 commandes OU 1000€
SILVER (🥈) → -5% si commande ≥ 500€
   ↓ 10 commandes OU 5000€
GOLD (🥇) → -10% si commande ≥ 800€
   ↓ 20 commandes OU 15000€
PLATINIUM (💎) → -15% si commande ≥ 1200€
```

---

## 📞 Besoin d'Aide ?

Consultez le fichier `README.md` principal pour :
- Installation complète
- Configuration
- API Endpoints détaillés
- Troubleshooting

---

**Bon shopping avec SmartShop ! 🛍️✨**

