# Test plan

## Goal

Verify the EduShop browser experience against the real Angular UI and ASP.NET Core API. These are functional end-to-end checks, not performance measurements.

## Environment

- Frontend: `http://localhost:4200`
- Backend: `http://localhost:5000`
- Browser: Edge by default; Chrome is supported
- Test data: seeded products and a newly generated customer email for each purchase run

## Scenarios

| ID | Area | Check | Expected result |
| --- | --- | --- | --- |
| A01 | Registration | Submit empty form | Required-field feedback; no account created |
| A02 | Registration | Mismatched passwords | Clear mismatch error |
| A03 | Login | Unknown credentials | Error shown; user stays on login |
| A04 | Authorization | Guest opens cart URL | Redirect to login |
| C01 | Catalog | Filter Books and open a product | Matching cards, details, and reviews section |
| C02 | Search | Search for seeded “Laptop” | Matching product card appears |
| E01 | Customer journey | Register, sign in, view product, wishlist, search, add to cart, checkout, view order, logout | Every step completes and order is visible |

## Execution rules

- Run against a local, disposable EduShop environment with seeded data.
- The purchase scenario creates a customer and a real order, and reduces stock by one unit.
- Keep a stocked Laptop product available before running E01. Use the EduShop admin API or UI if stock has been depleted.
- Tests run sequentially. Each test starts with a fresh browser session; E01 uses a unique email.
- Failures save screenshots in `target/failure-screenshots/`. The category and order tests save evidence in `target/evidence/`. Maven XML reports are in `target/surefire-reports/`. Generated output is excluded from Git.

## Out of scope

Actual card payment processing, visual regression, cross-browser grid, load testing, and destructive admin changes.
