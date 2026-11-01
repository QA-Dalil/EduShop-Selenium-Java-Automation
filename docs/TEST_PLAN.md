# Test plan

## Goal

Verify the EduShop browser experience against the real Angular UI and ASP.NET Core API. These are functional end-to-end checks, not performance measurements.

## Environment

- Frontend: `http://localhost:4200`
- Backend: `http://localhost:5000`
- Browser: Edge by default; Chrome is supported
- Test data: seeded products and a newly generated customer email for every authenticated test

## Scenarios

| ID | Area | Check | Expected result |
| --- | --- | --- | --- |
| A01 | Registration | Submit empty form | Required-field feedback; no account created |
| A02 | Registration | Mismatched passwords | Clear mismatch error |
| A03 | Login | Unknown credentials | Error shown; user stays on login |
| A04 | Authorization | Guest opens cart URL | Redirect to login |
| A05 | Registration | Invalid email | Email feedback; no account created |
| A06 | Registration | Password shorter than six characters | Form stays on registration and marks password invalid |
| C01 | Catalog | Filter Books and open a product | Matching cards, details, and reviews section |
| C02 | Search | Search for seeded “Laptop” | Matching product card appears |
| C03 | Search | Search for a unique absent term | Zero products found |
| C04 | Catalog | Sort Books by name ascending | Visible names are in ascending order |
| C05 | Catalog | Sort Books by name descending | Visible names are in descending order |
| E01 | Customer journey | Register, sign in, view product, wishlist, search, add to cart, checkout, view order, logout | Every step completes and order is visible |
| S01 | Wishlist | Add a Book, then remove it | Wishlist becomes empty |
| S02 | Cart | Add a Book, increase quantity, then remove it | Total items changes from one to two, then cart becomes empty |
| S03 | Cart | Add a Book, then clear cart | Cart becomes empty |
| S04 | Checkout | Submit a stocked cart without shipping address | Required-address feedback; no order placed |
| S05 | Checkout | Open checkout with an empty cart | Place Order button is disabled |

## Execution rules

- Run against a local, disposable EduShop environment with seeded data.
- E01 creates a real order and reduces Laptop stock by one unit. Keep that product stocked; reset the disposable database or replenish it through the admin API before repeated runs if necessary.
- Each test starts with a fresh browser session. Every authenticated test registers a unique email, so tests do not share carts or wishlists.
- Failures save screenshots in `target/failure-screenshots/`. The category and order tests save evidence in `target/evidence/`. Maven XML reports are in `target/surefire-reports/`. Generated output is excluded from Git.

## Out of scope

Actual card payment processing, visual regression, load testing, and destructive admin changes. The suite runs on local Edge and CI Chrome, but does not test every browser and platform combination.
