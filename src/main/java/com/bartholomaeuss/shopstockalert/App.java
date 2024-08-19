package com.bartholomaeuss.shopstockalert;

import java.util.Objects;

/**
 * The entry point of the ShopStockAlert application.
 * <p>
 * This class contains the main method that initializes the appropriate checker class
 * ({@link ZolondaChecker} or {@link WBWChecker}) based on the provided service name.
 * It retrieves and prints information about the webpage's title, the number of articles,
 * the number of deals, and the links to the deals.
 * </p>
 */
public class App {
    /**
     * The main method that runs the application.
     * <p>
     * This method processes command-line arguments to determine the service (either "zolonda" or "wbw")
     * and the URL to be checked. Depending on the service specified, it creates an instance of
     * {@link ZolondaChecker} or {@link WBWChecker}. The method then retrieves and prints
     * the webpage title, the number of articles, the number of deals, and the links to the deals.
     * If the service is not recognized or the arguments are incorrect, the program exits with an error.
     * </p>
     *
     * @param args Command-line arguments: the first argument is the service name ("zolonda" or "wbw"),
     *             and the second argument is the URL of the website to be checked.
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            String service = args[0];
            String url = args[1];
            if (Objects.equals(service, "zolonda")) {
                ZolondaChecker zolondaChecker = new ZolondaChecker(url);
                System.out.println("Title: " + zolondaChecker.getTitle());
                System.out.println("Number of Articles: " + zolondaChecker.getArticles().size());
                System.out.println("Number of Deals: " + zolondaChecker.getDeals().size());
                System.out.println("Links to Deals: " + zolondaChecker.getHrefs());
            } else if (Objects.equals(service, "wbw")) {
                WBWChecker wbwChecker = new WBWChecker(url);
                System.out.println("Title: " + wbwChecker.getTitle());
                System.out.println("Number of Articles: " + wbwChecker.getArticles().size());
                System.out.println("Number of Deals: " + wbwChecker.getLocationsOfInterest().size());
                System.out.println("Links to Deals: " + wbwChecker.getHrefs());
            } else {
                System.out.println("An error occurred, the program will now exit.");
                System.exit(4);
            }
        } else {
            System.out.println("An error occurred, the program will now exit.");
            System.exit(4);
        }
    }
}
