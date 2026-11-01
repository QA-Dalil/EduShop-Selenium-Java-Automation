package com.qadalil.edushop.support;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public final class BrowserSession {
  private BrowserSession() {}

  public static WebDriver create() {
    String browser = System.getProperty("browser", "edge").toLowerCase();
    boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
    WebDriver driver;
    if ("chrome".equals(browser)) {
      ChromeOptions options = new ChromeOptions();
      if (headless) options.addArguments("--headless=new");
      options.addArguments("--window-size=1440,1000");
      driver = new ChromeDriver(options);
    } else if ("edge".equals(browser)) {
      EdgeOptions options = new EdgeOptions();
      if (headless) options.addArguments("--headless=new");
      options.addArguments("--window-size=1440,1000");
      driver = new EdgeDriver(options);
    } else {
      throw new IllegalArgumentException("Supported browsers: edge, chrome. Got: " + browser);
    }
    driver.manage().window().setSize(new Dimension(1440, 1200));
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    return driver;
  }
}
