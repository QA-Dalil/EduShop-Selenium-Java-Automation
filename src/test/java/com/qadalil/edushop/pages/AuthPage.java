package com.qadalil.edushop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class AuthPage extends BasePage {
  public AuthPage(WebDriver driver) { super(driver); }

  public AuthPage openRegister() {
    driver.get(System.getProperty("baseUrl", "http://localhost:4200") + "/register");
    visible(By.cssSelector("form [formcontrolname='firstName']"));
    return this;
  }

  public AuthPage openLogin() {
    driver.get(System.getProperty("baseUrl", "http://localhost:4200") + "/login");
    visible(By.cssSelector("form [formcontrolname='email']"));
    return this;
  }

  public AuthPage register(String first, String last, String email, String password, String confirm) {
    type(By.cssSelector("[formcontrolname='firstName']"), first);
    type(By.cssSelector("[formcontrolname='lastName']"), last);
    type(By.cssSelector("[formcontrolname='email']"), email);
    type(By.cssSelector("[formcontrolname='password']"), password);
    type(By.cssSelector("[formcontrolname='confirmPassword']"), confirm);
    click(By.cssSelector("form button[type='submit']"));
    return this;
  }

  public AuthPage login(String email, String password) {
    type(By.cssSelector("[formcontrolname='email']"), email);
    type(By.cssSelector("[formcontrolname='password']"), password);
    click(By.cssSelector("form button[type='submit']"));
    return this;
  }

  public String error() { return visible(By.cssSelector(".alert-danger")).getText(); }
  public String validation() { return visible(By.cssSelector(".invalid-feedback")).getText(); }
  public void waitForHome() { pathIs(""); }
}
