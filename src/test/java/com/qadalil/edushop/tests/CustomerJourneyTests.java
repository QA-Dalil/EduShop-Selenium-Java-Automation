package com.qadalil.edushop.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import com.qadalil.edushop.pages.AuthPage;
import com.qadalil.edushop.pages.CartPage;
import com.qadalil.edushop.pages.CheckoutPage;
import com.qadalil.edushop.pages.HomePage;
import com.qadalil.edushop.pages.Navigation;
import com.qadalil.edushop.pages.ProductPage;
import com.qadalil.edushop.support.UiTest;
import org.junit.jupiter.api.Test;

public final class CustomerJourneyTests extends UiTest {
  @Test
  void newCustomerCanShopAndPlaceOrder() {
    String email = "selenium." + UUID.randomUUID() + "@example.com";
    AuthPage auth = new AuthPage(driver).openRegister();
    auth.register("Rahaf", "Automation", email, "TestPass123!", "TestPass123!");
    auth.waitForHome();

    Navigation nav = new Navigation(driver);
    nav.logout();
    auth.openLogin().login(email, "TestPass123!");
    auth.waitForHome();

    HomePage home = new HomePage(driver).open();
    ProductPage product = home.openFirstProduct();
    String productName = product.name();
    assertTrue(product.reviewsVisible());
    product.addToWishlist();

    nav.wishlist();
    assertTrue(nav.wishlistContains(productName));

    new HomePage(driver).open().search("Laptop").addFirstAvailableToCart();
    CartPage cart = nav.cart();
    assertTrue(cart.hasItems());

    CheckoutPage checkout = cart.checkout().placeOrder("12 University Street, Amman, Jordan", "CashOnDelivery");
    captureEvidence("order-confirmation");
    assertTrue(checkout.confirmation().contains("Your order #"));
    assertTrue(checkout.openOrders().orderListed());

    nav.logout();
    assertTrue(nav.guestLinksVisible());
  }
}
