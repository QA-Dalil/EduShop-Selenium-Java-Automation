package com.qadalil.edushop.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qadalil.edushop.pages.CartPage;
import com.qadalil.edushop.pages.CheckoutPage;
import com.qadalil.edushop.pages.HomePage;
import com.qadalil.edushop.pages.Navigation;
import com.qadalil.edushop.pages.ProductPage;
import com.qadalil.edushop.support.CustomerFixture;
import com.qadalil.edushop.support.UiTest;
import org.junit.jupiter.api.Test;

public final class ShoppingTests extends UiTest {
  @Test
  void wishlistItemCanBeRemoved() {
    CustomerFixture.register(driver);
    ProductPage product = new HomePage(driver).open().category("Books").openFirstProduct();
    String name = product.name();
    product.addToWishlist();
    Navigation nav = new Navigation(driver).wishlist();
    assertTrue(nav.wishlistContains(name));
    nav.removeFirstWish();
    assertTrue(nav.wishlistIsEmpty());
  }

  @Test
  void cartQuantityCanBeIncreasedAndItemRemoved() {
    CustomerFixture.register(driver);
    new HomePage(driver).open().category("Books").addFirstAvailableToCart();
    CartPage cart = new Navigation(driver).cart();
    assertTrue(cart.hasItems());
    assertEquals(1, cart.totalItems());
    cart.increaseFirstQuantity();
    assertEquals(2, cart.totalItems());
    cart.removeFirstItem();
    assertFalse(cart.hasItems());
  }

  @Test
  void cartCanBeCleared() {
    CustomerFixture.register(driver);
    new HomePage(driver).open().category("Books").addFirstAvailableToCart();
    CartPage cart = new Navigation(driver).cart();
    assertTrue(cart.hasItems());
    cart.clear();
    assertFalse(cart.hasItems());
  }

  @Test
  void checkoutRequiresShippingAddress() {
    CustomerFixture.register(driver);
    new HomePage(driver).open().category("Books").addFirstAvailableToCart();
    CheckoutPage checkout = new Navigation(driver).cart().checkout().submitWithoutAddress();
    assertTrue(checkout.addressRequired());
    assertTrue(driver.getCurrentUrl().endsWith("/checkout"));
  }

  @Test
  void emptyCartDisablesOrderSubmission() {
    CustomerFixture.register(driver);
    driver.get(System.getProperty("baseUrl", "http://localhost:4200") + "/checkout");
    assertTrue(new CheckoutPage(driver).placeOrderDisabled());
  }
}
