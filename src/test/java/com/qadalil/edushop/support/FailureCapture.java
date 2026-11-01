package com.qadalil.edushop.support;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.WebDriver;

public final class FailureCapture implements TestExecutionExceptionHandler {
  private static final ThreadLocal<WebDriver> CURRENT_DRIVER = new ThreadLocal<>();

  static void setDriver(WebDriver driver) { CURRENT_DRIVER.set(driver); }
  static void clearDriver() { CURRENT_DRIVER.remove(); }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable cause) throws Throwable {
    WebDriver driver = CURRENT_DRIVER.get();
    if (driver != null) UiTest.screenshot(driver, context.getDisplayName());
    throw cause;
  }
}
