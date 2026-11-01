package com.qadalil.edushop.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qadalil.edushop.pages.HomePage;
import com.qadalil.edushop.pages.ProductPage;
import com.qadalil.edushop.support.UiTest;
import org.junit.jupiter.api.Test;

public final class CatalogTests extends UiTest {
  @Test
  void categoryFilterAndProductDetailsWork() {
    HomePage home = new HomePage(driver).open().category("Books");
    captureEvidence("books-category");
    assertTrue(home.cardCount() > 0);
    ProductPage product = home.openFirstProduct();
    assertTrue(!product.name().isBlank());
    assertTrue(product.reviewsVisible());
  }

  @Test
  void searchFindsSeededProduct() {
    HomePage home = new HomePage(driver).open().search("Laptop");
    assertTrue(home.cardCount() > 0);
    assertTrue(home.firstProductName().toLowerCase().contains("laptop"));
  }
}
