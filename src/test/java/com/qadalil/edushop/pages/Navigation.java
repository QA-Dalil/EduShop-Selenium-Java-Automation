package com.qadalil.edushop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class Navigation extends BasePage {
  public Navigation(WebDriver driver) { super(driver); }

  public CartPage cart() {
    click(By.cssSelector("nav a[href='/cart']"));
    return new CartPage(driver);
  }

  public Navigation wishlist() {
    click(By.cssSelector("nav a[href='/wishlist']"));
    pathIs("/wishlist");
    visible(By.xpath("//h2[contains(.,'My Wishlist')]"));
    return this;
  }

  public boolean wishlistContains(String productName) {
    return wait.until(ExpectedConditions.textToBePresentInElementLocated(
        By.cssSelector(".product-card .card-title"), productName));
  }

  public Navigation logout() {
    click(By.cssSelector("nav .dropdown-toggle"));
    click(By.cssSelector("nav .dropdown-menu .text-danger"));
    pathIs("/login");
    return this;
  }

  public boolean guestLinksVisible() {
    return visible(By.cssSelector("nav a[href='/login']")).isDisplayed();
  }
}
