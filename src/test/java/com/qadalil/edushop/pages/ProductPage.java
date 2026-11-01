package com.qadalil.edushop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class ProductPage extends BasePage {
  public ProductPage(WebDriver driver) { super(driver); }

  public String name() { return visible(By.cssSelector(".container h2")).getText(); }
  public boolean reviewsVisible() { return visible(By.xpath("//h4[normalize-space()='Customer Reviews']")).isDisplayed(); }

  public ProductPage addToWishlist() {
    click(By.xpath("//button[contains(normalize-space(.),'Wishlist')]"));
    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".alert-success"), "Added to wishlist"));
    return this;
  }

  public ProductPage addToCart() {
    click(By.xpath("//button[contains(normalize-space(.),'Add to Cart')]"));
    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".alert-success"), "Added to cart"));
    return this;
  }
}
