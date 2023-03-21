package com.example.SpringBot.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.ArrayList;
import java.util.List;

public class YandexMarketSearch {
    public static List<String> search(String query) throws InterruptedException {
        // устанавливаем путь к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", "C:\\driversss2\\chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        ChromeDriver driver = new ChromeDriver(chromeOptions);

        ;



        // создаем экземпляр драйвера Chrome


        // открываем страницу Яндекс.Маркета
        driver.get("https://market.yandex.ru/");

        // находим поле ввода запроса
        WebElement searchField = driver.findElement(By.id("header-search"));


        // вводим запрос
        searchField.sendKeys(query);

        // находим кнопку поиска и нажимаем на нее
        WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));
        searchButton.click();

        // находим первые 4 товара
        WebElement resultTable = driver.findElement(By.cssSelector("div.n-snippet-list"));
        List<WebElement> resultItems = resultTable.findElements(By.className("n-snippet-card2"));
        List<String> links = new ArrayList<>();
        for (int i = 0; i < 4 && i < resultItems.size(); i++) {
            Thread.sleep(100);
            WebElement item = resultItems.get(i);
            WebElement link = item.findElement(By.cssSelector("a.n-link_theme_blue"));
            String href = link.getAttribute("href");
            links.add(href);
        }

        // выводим найденные ссылки на консоль


        // закрываем драйвер
        driver.quit();
        return links;
    }
}