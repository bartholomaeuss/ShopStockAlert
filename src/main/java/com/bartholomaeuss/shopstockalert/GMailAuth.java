package com.bartholomaeuss.shopstockalert;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


/**
 * Handles Gmail API authentication and email sending functionality.
 * <p>
 * This class manages the OAuth2 authentication flow required for the Gmail API, allowing the application
 * to send emails on behalf of the authenticated user. It handles the retrieval of stored authorization
 * credentials or initiates a new authorization process if no valid credentials are found. Additionally,
 * the class provides functionality to list all labels in the authenticated user's Gmail account.
 * </p>
 */
public class GMailAuth {
    /**
     * Application name.
     */
    static final String APPLICATION_NAME = "ShopStockAlert";
    /**
     * Global instance of the JSON factory.
     */
    static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    /**
     * Global instance of the scopes required by this application.
     * <p>
     * This application requires permission to send emails on behalf of the user.
     * If you modify the scopes, you need to delete any previously saved tokens
     * in the specified tokens directory to prompt a new authorization flow.
     * </p>
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    /**
     * Path to the credentials file that contains the client secrets.
     */
    private static final String CREDENTIALS_FILE_PATH = "/cs.json";

    /**
     * Creates an authorized {@link Credential} object for accessing the Gmail API.
     *
     * <p>This method loads the client secrets from the specified credentials file,
     * initiates the OAuth2 authorization code flow, and returns a Credential object
     * representing the authenticated user. If valid credentials are already stored,
     * they are loaded and returned. Otherwise, the user is prompted to authorize the
     * application via a web page.</p>
     *
     * @param HTTP_TRANSPORT The network HTTP transport used for communication with the Gmail API.
     * @return An authorized {@link Credential} object.
     * @throws IOException If the credentials file cannot be found or read.
     */
    static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        InputStream in = GMailAuth.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            clientSecrets,
            SCOPES)
            .setDataStoreFactory(
                new FileDataStoreFactory(
                    new java.io.File(TOKENS_DIRECTORY_PATH)
                )
            )
            .setAccessType("offline")
            .build();

        Credential credential = flow.loadCredential("user");
        if (oldCredentialsValid(credential)) {
            return credential;
        }

        GoogleTokenResponse tokenResponse = flow
            .newTokenRequest(getCode(flow))
            .setRedirectUri("urn:ietf:wg:oauth:2.0:oob")
            .execute();

        return flow.createAndStoreCredential(tokenResponse, "user");
    }

    /**
     * Checks if the existing {@link Credential} object is valid.
     *
     * <p>This method verifies that the provided credential is not null, has a valid access token,
     * and the token is either not expired or has more than a minute before it expires.</p>
     *
     * @param credential The {@link Credential} object to validate.
     * @return {@code true} if the credential is valid, {@code false} otherwise.
     */
    private static Boolean oldCredentialsValid(final Credential credential) {
        return credential != null &&
            credential.getAccessToken() != null &&
            (credential.getExpiresInSeconds() == null ||
                credential.getExpiresInSeconds() > 60
            );
    }

    /**
     * Prompts the user to authorize the application and retrieve an authorization code.
     *
     * <p>This method generates an authorization URL, prompts the user to visit it, and then
     * requests them to input the authorization code provided by Google. This code is then
     * used to complete the OAuth2 authorization flow.</p>
     *
     * @param flow The {@link GoogleAuthorizationCodeFlow} used to generate the authorization URL.
     * @return The authorization code entered by the user.
     */
    private static String getCode(GoogleAuthorizationCodeFlow flow){
        GoogleAuthorizationCodeRequestUrl url = flow
            .newAuthorizationUrl()
            .setRedirectUri("urn:ietf:wg:oauth:2.0:oob")
            .setAccessType("offline");

        System.out.println("Please visit the following URL and enter the code provided:");
        System.out.println(url.build());

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the code:");

        return scanner.nextLine();
    }
}
