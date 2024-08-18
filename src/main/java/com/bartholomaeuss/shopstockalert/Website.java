package com.bartholomaeuss.shopstockalert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Represents a website that can be accessed and fetched using a URL.
 * <p>
 * This class is responsible for initializing a connection to a given URL and fetching the Document Object Model (DOM)
 * of the webpage. The DOM is then stored and can be retrieved for further processing.
 * </p>
 */
public class Website {
    private final String url;
    private final Document DOM;

    /**
     * Constructs a new {@code Website} instance with the specified URL.
     * <p>
     * This constructor initializes the website by connecting to the provided URL and fetching the DOM of the webpage.
     * </p>
     *
     * @param url The URL of the website to be initialized.
     * @throws IOException If an error occurs while connecting to the URL or fetching the DOM.
     */
    public Website(String url) throws IOException {
        this.url = url;
        this.DOM = getDOM(url);
    }

    /**
     * Fetches the Document Object Model (DOM) from the specified URL.
     * <p>
     * This method uses Jsoup to connect to the given URL and retrieve the DOM of the webpage.
     * </p>
     *
     * @param url The URL from which to fetch the DOM.
     * @return The {@link Document} object representing the DOM of the page.
     * @throws IOException If an error occurs while fetching the page, such as connectivity issues or an invalid URL.
     */
    private Document getDOM(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    /**
     * Returns the URL of the website.
     * <p>
     * This method provides access to the URL associated with this {@code Website} instance.
     * </p>
     *
     * @return The URL of the website.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the Document object representing (the DOM) of the webpage.
     * <p>
     * This method provides access to the DOM of the website that was fetched during initialization.
     * </p>
     *
     * @return The {@link Document} object representing the DOM of the webpage.
     */
    public Document getDOM() {
        return DOM;
    }
}
