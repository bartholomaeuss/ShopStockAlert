package com.bartholomaeuss.shopstockalert;

/**
 * The entry point of the ShopStockAlert application.
 * <p>
 * This class contains the main method that initializes the {@link AvailabilityChecker},
 * retrieves and prints information about the webpage's title, articles, deals, and links to deals.
 * </p>
 */
public class App {
    /**
     * The main method that runs the application.
     * <p>
     * This method creates an instance of {@link AvailabilityChecker}, then retrieves and prints
     * the webpage title, the number of articles, the number of deals, and the links to the deals.
     * </p>
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        AvailabilityChecker availabilityChecker = new AvailabilityChecker();
        System.out.println("Title: " + availabilityChecker.getTitle());
        System.out.println("Number of Articles: " + availabilityChecker.getArticles().size());
        System.out.println("Number of Deals: " + availabilityChecker.getDeals().size());
        System.out.println("Links to Deals: " + availabilityChecker.getHrefs());
    }
}
