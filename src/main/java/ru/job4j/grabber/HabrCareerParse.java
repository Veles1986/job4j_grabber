package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HabrCareerParse {

    private static final int PAGE_NUMBERS = 1;
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= PAGE_NUMBERS; i++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, i, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateElement = row.select(".vacancy-card__date").first().child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String description;
                try {
                    description = retrieveDescription(link);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String date = dateElement.attr("datetime");
                System.out.printf("%s %s %s%n %s", vacancyName, date, link, description);
            });
        }
    }

    private static String retrieveDescription(String link) throws IOException {
        StringBuilder result = new StringBuilder();
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element description = document.selectFirst(".vacancy-description__text");
        for (Element element : description.children()) {
            if (!element.children().isEmpty()) {
                for (Element child : element.children()) {
                    result.append(child.text());
                    result.append(System.lineSeparator());
                }
            } else {
                result.append(element.text());
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }


}