package com.example.avitoprsr.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Parser {

    public static String getUrl(String req) {
        String messageText = "";
        WebDriver webDriver = new ChromeDriver();
        Document doc;
        webDriver.get("https://www.avito.ru/sankt_peterburg_i_lo?q=" + req); //# Подключаемся к Авито с помощью Selenium
        doc = Jsoup.parse(webDriver.getPageSource()); //# Получаем код страницы
        webDriver.quit();

        Elements links = doc.select("div.iva-item-slider-pYwHo > a:first-child[href]");
        //# Выбираем ссылки объявлений, из div тега

        int iteration = 0;
        for (Element e : links) {
            if (iteration > 10) {
                break; //# Выводим первые 10 объявлений
            }
            messageText += "https://www.avito.ru" + e.attr("href") + "\n";
            iteration++;
        }
        return messageText;
    }
}
