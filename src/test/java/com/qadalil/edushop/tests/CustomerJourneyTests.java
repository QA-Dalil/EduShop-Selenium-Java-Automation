package com.qadalil.edushop.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.Map;
import com.qadalil.edushop.pages.AuthPage;
import com.qadalil.edushop.pages.CartPage;
import com.qadalil.edushop.pages.CheckoutPage;
import com.qadalil.edushop.pages.HomePage;
import com.qadalil.edushop.pages.Navigation;
import com.qadalil.edushop.pages.ProductPage;
import com.qadalil.edushop.support.UiTest;
import com.qadalil.edushop.support.EduShopApi;
import org.junit.jupiter.api.Test;

public final class CustomerJourneyTests extends UiTest {
  @Test
  void newCustomerCanShopAndPlaceOrder() {
    EduShopApi api = new EduShopApi();
    api.ensureLaptopStock(5, 25);
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

    String address = "12 University Street, Amman, Jordan";
    CheckoutPage checkout = cart.checkout().placeOrder(address, "CashOnDelivery");
    captureEvidence("order-confirmation");
    assertTrue(checkout.confirmation().contains("Your order #"));
    String customerToken = api.login(email, "TestPass123!");
    Map<String, Object> order = api.order(checkout.orderId(), customerToken);
    assertEquals(checkout.orderId(), ((Number) order.get("id")).intValue());
    Map<String, Object> customer = api.currentUser(customerToken);
    assertEquals(email, customer.get("email"));
    assertEquals(customer.get("id"), order.get("userId"));
    assertEquals(address, order.get("shippingAddress"));
    assertEquals("CashOnDelivery", order.get("paymentMethod"));
    assertTrue(((java.util.List<?>) order.get("items")).size() > 0);
    assertTrue(checkout.openOrders().orderListed());

    nav.logout();
    assertTrue(nav.guestLinksVisible());
  }
}
