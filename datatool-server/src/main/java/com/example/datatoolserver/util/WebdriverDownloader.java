package com.example.datatoolserver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.util.PropertyResourceBundle;
import java.util.concurrent.TimeUnit;

public class WebdriverDownloader {
    static {
        //phantomJsPath=E:/dev/phantomjs/bin/phantomjs.exe
        String phantomJsPath = PropertyResourceBundle.getBundle("webdriver").getString("phantomJsPath");
        System.setProperty("phantomjs.binary.path", phantomJsPath);
    }

    /**
     * download html
     *
     * @param webDriver
     * @param url
     * @return
     */
    public static String download(WebDriver webDriver, String url) {
        webDriver.get(url);
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        return webElement.getAttribute("outerHTML");
    }

    public static WebDriver create() {
        WebDriver webDriver = new PhantomJSDriver();
        /**Specifies the amount of time the driver should wait when searching for an element if it is
         * not immediately present.*/
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return webDriver;
    }

    /**
     * close window，must quit first，then close
     */
    public static void close(WebDriver driver) {
        driver.quit();
        driver.close();
    }

    public static void main(String[] args) {
        String url = "https://v.douyin.com/W43jgB/";
        WebDriver webDriver = WebdriverDownloader.create();
        String html = WebdriverDownloader.download(webDriver, url);
        // WebdriverDownloader.close(webDriver);
        System.out.println(html);
    }
}