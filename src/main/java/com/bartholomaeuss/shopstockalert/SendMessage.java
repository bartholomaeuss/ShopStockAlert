package com.bartholomaeuss.shopstockalert;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;

import static com.bartholomaeuss.shopstockalert.GMailAuth.*;

/**
 * Provides functionality to send an email using the Gmail API.
 * <p>
 * This class demonstrates how to create and send an email message through the Gmail API.
 * It handles the construction of the email message, encodes it in the appropriate format,
 * and interacts with the Gmail API to send the email.
 * </p>
 */public class SendMessage {
    /**
     * Sends an email from the user's Gmail account to the specified recipient.
     * <p>
     * This method initializes the Gmail API client, constructs a MIME message from the provided
     * email details, encodes it, and sends it using the Gmail API. It returns the sent message
     * object if the email was successfully sent, or {@code null} if an error occurred.
     * </p>
     *
     * @param fromEmailAddress The email address that will appear in the "From" header of the email.
     * @param toEmailAddress   The email address of the recipient.
     * @param messageSubject   The subject of the email.
     * @param bodyText         The body text of the email.
     * @return The sent {@link Message} object if the email was successfully sent; {@code null} otherwise.
     * @throws MessagingException If there is an error with the email format or addressing.
     * @throws IOException        If an error occurs while accessing the service account credentials or sending the email.
     * @throws GeneralSecurityException If there is an error with the security configuration or credentials.
     */
    public static Message sendEmail(String fromEmailAddress,
                                    String toEmailAddress,
                                    String messageSubject,
                                    String bodyText)
        throws MessagingException, IOException, GeneralSecurityException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(new NetHttpTransport(),
            JSON_FACTORY,
            getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(fromEmailAddress));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO,
            new InternetAddress(toEmailAddress));
        email.setSubject(messageSubject);
        email.setText(bodyText);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        try {
            message = service.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
        return null;
    }
}
