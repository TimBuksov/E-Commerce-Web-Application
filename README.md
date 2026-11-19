# E-Commerce Web Application (Demo Shop)

A junior-level full-stack demo using **Java 21 + Spring Boot + JPA** for the backend and **React + Vite** for the storefront. Includes a product catalog, search, cart, stock validation, and saved demo orders. **No payments or real fulfilment.**

## Run locally
Requirements: Java 21, Maven 3.9+, Node.js 20.19+ / 22.12+.

Terminal 1 (project root):
```bash
mvn spring-boot:run
```

Terminal 2:
```bash
cd frontend
npm install
npm run dev
```

Visit http://localhost:5173; Vite proxies `/api` to http://localhost:8082. Seed products are created on first run. The database is local H2 and stored under `data/`.

Test backend: `mvn test`. Build frontend: `cd frontend && npm run build`.

## API
- `GET /api/products` — list products (`?search=mouse` supported)
- `POST /api/orders` — create a demo order: `{"customerName":"Sample User","email":"sample@example.com","items":[{"productId":1,"quantity":2}]}`

## Known limitations
This is an **educational demo**. No authentication, admin panel, payment gateway, tax calculation, or shipment logic. Do not use with real customer data or expose checkout publicly. Production improvements: authentication and admin RBAC, migrations, integration tests, inventory locking, idempotency, payments sandbox, PostgreSQL, and CI.
