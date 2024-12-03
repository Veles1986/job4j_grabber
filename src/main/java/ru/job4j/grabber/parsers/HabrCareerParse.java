package ru.job4j.grabber.parsers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final int PAGE_NUMBERS = 5;
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    private String retrieveDescription(String link) throws IOException {
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

    private Post createPost(Element element) {
        Element titleElement = element.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        Element dateElement = element.select(".vacancy-card__date").first().child(0);
        String vacancyName = titleElement.text();
        String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        String description;
        try {
            description = retrieveDescription(link);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String date = dateElement.attr("datetime");
        HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
        return new Post(1, vacancyName, link, description, timeParser.parse(date));
    }

    private Elements getElements(int page) throws IOException {
        String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, page, SUFFIX);
        Connection connection = Jsoup.connect(fullLink);
        Document document = connection.get();
        return document.select(".vacancy-card__inner");
    }

    @Override
    public List<Post> fetch() throws IOException {
        List<Post> result = new ArrayList<>();
        for (int i = 1; i <= PAGE_NUMBERS; i++) {
            for (Element element : getElements(i)) {
                result.add(createPost(element));
            }
        }
        return result;
    }
}