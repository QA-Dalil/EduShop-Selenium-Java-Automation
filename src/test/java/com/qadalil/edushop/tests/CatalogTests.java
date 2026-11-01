package com.qadalil.edushop.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.List;

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

  @Test
  void searchWithUnknownTermShowsNoProducts() {
    HomePage home = new HomePage(driver).open().searchWithoutResults("no-match-4ac91f2e");
    assertEquals(0, home.cardCount());
  }

  @Test
  void booksCanBeSortedByNameAscending() {
    HomePage home = new HomePage(driver).open().category("Books").sortByName(false);
    List<String> names = home.productNames();
    assertTrue(names.size() > 1);
    assertEquals(names.stream().sorted().toList(), names);
  }

  @Test
  void booksCanBeSortedByNameDescending() {
    HomePage home = new HomePage(driver).open().category("Books").sortByName(true);
    List<String> names = home.productNames();
    assertTrue(names.size() > 1);
    assertEquals(names.stream().sorted(Comparator.reverseOrder()).toList(), names);
  }
}
