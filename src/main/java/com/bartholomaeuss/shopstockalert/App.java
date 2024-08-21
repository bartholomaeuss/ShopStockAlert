package com.bartholomaeuss.shopstockalert;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

/**
 * The entry point of the ShopStockAlert application.
 * <p>
 * This class handles the main execution flow of the ShopStockAlert application.
 * Based on the command-line arguments provided, it determines which service checker to use
 * ({@link ZolondaChecker} or {@link WBWChecker}) and processes the specified website URL.
 * If any deals are found, the application sends an email notification containing details
 * such as the webpage title and the links to the available deals.
 * </p>
 */
public class App {
    /**
     * The main method that runs the ShopStockAlert application.
     * <p>
     * This method expects four command-line arguments: the service name ("zolonda" or "wbw"),
     * the URL of the website to be checked, the sender's email address, and the recipient's email address.
     * Depending on the specified service, it instantiates the appropriate checker class
     * ({@link ZolondaChecker} or {@link WBWChecker}) and retrieves relevant information from the webpage.
     * If deals are found on the webpage, an email notification is sent to the recipient.
     * If the service name is invalid or the arguments are incorrect, the program exits with an error code.
     * </p>
     *
     * @param args Command-line arguments:
     *             <ul>
     *                 <li><b>args[0]</b>: The service name ("zolonda" or "wbw")</li>
     *                 <li><b>args[1]</b>: The URL of the website to be checked</li>
     *                 <li><b>args[2]</b>: The sender's email address</li>
     *                 <li><b>args[3]</b>: The recipient's email address</li>
     *             </ul>
     * @throws MessagingException If there is an error sending the email.
     * @throws GeneralSecurityException If a security exception occurs while processing the email.
     * @throws IOException If an I/O error occurs during webpage retrieval.
     */
    public static void main(String[] args) throws MessagingException, GeneralSecurityException, IOException {
        if (args.length == 4) {
            String service = args[0];
            String url = args[1];
            String fromEmailAddress = args[2];
            String toEmailAddress = args[3];
            if (Objects.equals(service, "zolonda")) {
                ZolondaChecker zolondaChecker = new ZolondaChecker(url);
                if (!zolondaChecker.getHrefs().isEmpty()){
                    SendMessage.sendEmail(fromEmailAddress, toEmailAddress, zolondaChecker.getTitle(), zolondaChecker.getHrefs().toString());
                }
            } else if (Objects.equals(service, "wbw")) {
                WBWChecker wbwChecker = new WBWChecker(url);
                if (!wbwChecker.getHrefs().isEmpty()) {
                    SendMessage.sendEmail(fromEmailAddress, toEmailAddress, wbwChecker.getTitle(), wbwChecker.getHrefs().toString());
                }
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
