package com.qadalil.edushop.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qadalil.edushop.pages.AuthPage;
import com.qadalil.edushop.support.UiTest;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class AuthTests extends UiTest {
  @Test
  void registerRequiresNamesAndEmail() {
    AuthPage page = new AuthPage(driver).openRegister();
    page.register("", "", "", "", "");
    assertTrue(page.validation().contains("First name is required"));
    assertTrue(driver.getCurrentUrl().endsWith("/register"));
  }

  @Test
  void registerRejectsPasswordMismatch() {
    AuthPage page = new AuthPage(driver).openRegister();
    page.register("Rahaf", "Test", "mismatch@example.com", "Pass123!", "Different123!");
    assertEquals("Passwords do not match.", page.error());
  }

  @Test
  void registerRejectsInvalidEmail() {
    AuthPage page = new AuthPage(driver).openRegister();
    page.register("Rahaf", "Test", "not-an-email", "TestPass123!", "TestPass123!");
    assertTrue(page.validation().contains("Valid email is required"));
    assertTrue(driver.getCurrentUrl().endsWith("/register"));
  }

  @Test
  void registerRejectsShortPassword() {
    AuthPage page = new AuthPage(driver).openRegister();
    page.register("Rahaf", "Test", "short@example.com", "123", "123");
    assertTrue(driver.getCurrentUrl().endsWith("/register"));
    assertTrue(driver.findElement(org.openqa.selenium.By.cssSelector("[formcontrolname='password']"))
        .getAttribute("class").contains("is-invalid"));
  }

  @Test
  void invalidLoginShowsError() {
    AuthPage page = new AuthPage(driver).openLogin();
    page.login("missing.user@example.com", "WrongPass123!");
    assertTrue(page.error().contains("Login failed") || page.error().contains("Invalid"));
    assertTrue(driver.getCurrentUrl().endsWith("/login"));
  }

  @Test
  void protectedCartRedirectsGuestToLogin() {
    driver.get(System.getProperty("baseUrl", "http://localhost:4200") + "/cart");
    new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.urlContains("/login"));
    assertTrue(driver.getCurrentUrl().endsWith("/login"));
  }
}
