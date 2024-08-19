package com.bartholomaeuss.shopstockalert;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks the availability of products on a website and processes the relevant information.
 * <p>
 * This class fetches the webpage content, retrieves articles, filters for deals, and extracts href attributes
 * from the deal elements.
 * </p>
 */
public class ZolondaChecker {
    private Website website = null;
    private String title;
    private Elements articles;
    private List<Element> deals;
    private List<String> hrefs;

    /**
     * Constructs an AvailabilityChecker object for a predefined website URL.
     * <p>
     * The constructor initializes the Website object, retrieves the title, articles, deals and hrefs.
     * </p>
     */
    public ZolondaChecker(String url) {

        try {
            this.website = new Website(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred, the program will now exit.");
            System.exit(4);
        }
        this.title = getTitle();
        this.articles = getArticles();
        this.deals = getDeals();
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
     * Retrieves all article elements from the hidden sections of the webpage.
     * <p>
     * This method selects all article elements within hidden divs in the webpage's DOM.
     * </p>
     *
     * @return An {@link Elements} object containing all article elements.
     */
    public Elements getArticles() {
        return website.getDOM().select("div[hidden]").select("article");
    }

    /**
     * Filters the articles to include only those that contain the keyword "deal".
     * <p>
     * This method iterates through all articles and keeps only those whose text contains the keyword "deal".
     * </p>
     *
     * @return A {@link List} of {@link Element} objects that represent the filtered deal articles.
     */
    public List<Element> getDeals() {
        List<Element> filteredArticles = new ArrayList<>();
        for (Element article : articles) {
            if (article.text().toLowerCase().contains("deal")) {
                filteredArticles.add(article);
            }
        }
        return filteredArticles;
    }

    /**
     * Extracts all href attributes from the deal articles.
     * <p>
     * This method retrieves the href attribute from each <a> tag found within the filtered deal articles.
     * </p>
     *
     * @return A {@link List} of {@link String} objects representing the href attributes of the deal articles.
     */
    public List<String> getHrefs() {
        List<String> hrefs = new ArrayList<>();
        for (Element deal : deals) {
            String href = deal.getElementsByTag("a").attr("href");
            if (!href.isEmpty()) {
                hrefs.add(href);
            }
        }
        return hrefs;
    }
}
