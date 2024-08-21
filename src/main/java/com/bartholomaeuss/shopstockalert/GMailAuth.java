package com.bartholomaeuss.shopstockalert;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
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
     * <p>
     * This method loads the client secrets from the specified credentials file, initiates the
     * OAuth2 authorization code flow, and returns a Credential object representing the authenticated user.
     * </p>
     *
     * @param HTTP_TRANSPORT The network HTTP Transport used for communication with the Gmail API.
     * @return An authorized {@link Credential} object.
     * @throws IOException If the credentials file cannot be found or read.
     */
    static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
        throws IOException {

        InputStream in = GMailAuth.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return credential;
    }
}
