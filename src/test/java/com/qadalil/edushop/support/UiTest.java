package com.qadalil.edushop.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

@ExtendWith(FailureCapture.class)
public abstract class UiTest {
  protected WebDriver driver;

  @BeforeEach
  void startBrowser() {
    driver = BrowserSession.create();
    FailureCapture.setDriver(driver);
  }

  @AfterEach
  void stopBrowser() {
    if (driver != null) driver.quit();
    FailureCapture.clearDriver();
  }

  static void screenshot(WebDriver driver, String testName) {
    saveScreenshot(driver, "failure-screenshots", testName + "-" + Instant.now().toEpochMilli());
  }

  protected void captureEvidence(String name) {
    saveScreenshot(driver, "evidence", name);
  }

  private static void saveScreenshot(WebDriver driver, String folder, String testName) {
    try {
      Path directory = Path.of("target", folder);
      Files.createDirectories(directory);
      String safeName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
      Files.write(directory.resolve(safeName + ".png"),
          ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
    } catch (IOException | RuntimeException exception) {
      System.err.println("Could not save failure screenshot: " + exception.getMessage());
    }
  }
}
