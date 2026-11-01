package com.qadalil.edushop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class CheckoutPage extends BasePage {
  public CheckoutPage(WebDriver driver) { super(driver); }

  public CheckoutPage placeOrder(String address, String paymentMethod) {
    visible(By.xpath("//h2[contains(.,'Checkout')]"));
    type(By.cssSelector("[formcontrolname='shippingAddress']"), address);
    new Select(visible(By.cssSelector("[formcontrolname='paymentMethod']"))).selectByValue(paymentMethod);
    click(By.xpath("//button[normalize-space()='Place Order']"));
    visible(By.xpath("//h3[normalize-space()='Order Placed Successfully!']"));
    return this;
  }

  public String confirmation() { return visible(By.xpath("//p[contains(.,'Your order #')]")).getText(); }

  public CheckoutPage openOrders() {
    click(By.cssSelector("a[href='/orders']"));
    pathIs("/orders");
    visible(By.xpath("//h2[contains(.,'My Orders')]"));
    return this;
  }

  public boolean orderListed() {
    return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".order-card"))).isEmpty();
  }
}
