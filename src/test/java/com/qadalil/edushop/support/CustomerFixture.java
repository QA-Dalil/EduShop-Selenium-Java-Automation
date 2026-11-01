package com.qadalil.edushop.support;

import java.util.UUID;
import com.qadalil.edushop.pages.AuthPage;
import org.openqa.selenium.WebDriver;

public final class CustomerFixture {
  private CustomerFixture() {}

  public static String register(WebDriver driver) {
    String email = "selenium." + UUID.randomUUID() + "@example.com";
    AuthPage page = new AuthPage(driver).openRegister();
    page.register("Rahaf", "Automation", email, "TestPass123!", "TestPass123!");
    page.waitForHome();
    return email;
  }
}
