package com.bartholomaeuss.shopstockalert;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code WBWChecker} class is responsible for checking the availability of products on a specific website.
 * <p>
 * It fetches the webpage content, retrieves article elements related to "wohnungen", filters for specific locations of interest,
 * and extracts the href attributes from the relevant HTML elements.
 * </p>
 */
public class WBWChecker {
    private Website website = null;
    private String title;
    private Elements articles;
    private List<Element> locationsOfInterest;
    private List<String> hrefs;

    /**
     * Constructs a {@code WBWChecker} object for a specified website URL.
     * <p>
     * This constructor initializes the {@link Website} object by fetching the webpage content. It also extracts
     * the page title, retrieves all relevant article elements, filters for specific locations of interest,
     * and extracts the href attributes from these elements.
     * </p>
     *
     * @param url The URL of the website to be checked.
     */
    public WBWChecker(String url) {

        try {
            this.website = new Website(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred, the program will now exit.");
            System.exit(4);
        }
        this.title = getTitle();
        this.articles = getArticles();
        this.locationsOfInterest = getLocationsOfInterest();
        this.hrefs = getHrefs();

    }

    /**
     * Retrieves the Website object associated with this AvailabilityChecker.
     *
     * @return The {@link Website} object.
     */
    public Website getWebsite() {
        return website;
    }

    /**
     * Retrieves the title of the webpage.
     * <p>
     * This method returns the title of the webpage fetched from the URL provided in the constructor.
     * </p>
     *
     * @return The title of the webpage.
     */
    public String getTitle() {
        return website.getDOM().title();
    }

    /**
     * Retrieves all article elements within the webpage that belong to the "section-wohnung container" class.
     * <p>
     * This method selects and returns all HTML elements that have the class "section-wohnung container".
     * These elements typically represent articles related to "wohnungen" on the webpage.
     * </p>
     *
     * @return An {@link Elements} object containing all article elements with the class "section-wohnung container".
     */
    public Elements getArticles() {
        return website.getDOM().getElementsByClass("section-wohnung container");
    }

    /**
     * Filters the retrieved articles to include only those containing a specific keyword.
     * <p>
     * This method iterates over the articles and selects only those that have an {@code <h2>} tag
     * containing the keyword "rath" in its text. The filtered articles represent specific locations of interest.
     * </p>
     *
     * @return A {@link List} of {@link Element} objects representing the filtered articles of interest.
     */
    public List<Element> getLocationsOfInterest() {
        List<Element> filteredArticles = new ArrayList<>();
        for (Element article : articles) {
            if (article.getElementsByTag("h2").text().toLowerCase().contains("rath")) {
                filteredArticles.add(article);
            }
        }
        return filteredArticles;
    }

    /**
     * Extracts the {@code href} attributes from the filtered articles.
     * <p>
     * This method retrieves all {@code href} attributes from the {@code <a>} tags within the filtered articles.
     * These hrefs typically link to the detailed pages of the locations of interest.
     * </p>
     *
     * @return A {@link List} of {@link String} objects representing the href attributes of the filtered articles.
     */
    public List<String> getHrefs() {
        List<String> hrefs = new ArrayList<>();
        for (Element location : locationsOfInterest) {
            String href = location.getElementsByTag("a").attr("href");
            if (!href.isEmpty()) {
                hrefs.add(href);
            }
        }
        return hrefs;
    }
}
