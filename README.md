# ShopStockAlert

## Overview

`ShopStockAlert` is a simple Java application designed to monitor online store stock for specific items.
It fetches the webpage's content, processes it to identify available deals, and provides links to those deals.
This project demonstrates the usage of web scraping and data processing with Java.

## Features

- **Fetches Webpage Content**: Utilizes Jsoup to retrieve the content of the webpage.
- **Extracts Article Elements**: Parses the page to extract article elements.
- **Identifies Relevant Articles**: Searches for articles containing specific keywords (e.g., "deal").
- **Provides Deal Links**: Gathers and displays links to the identified deals.
- **Sends Email Notifications**: Notifies users about new deals via email.
- **Command-Line Authorization**: Configures and authorizes the application through command-line arguments.

## Getting Started

To run the application, you need to provide four command-line arguments:

1. **Service Name** (`zolonda` or `wbw`): The service to check.
1. **Website URL**: The URL of the website you want to monitor.
1. **Sender Email Address**: The email address from which the notification will be sent.
1. **Recipient Email Address**: The email address to which the notification will be sent.

### Example Command

```
java -jar <PATH TO JAR> com.bartholomaeuss.shopstockalert.App "zolonda" "http://example.com" "sender@example.com" "recipient@example.com"
```

## Requirements

* Java 11 or higher
* Maven
* Internet connection for webpage retrieval and email sending
* Valid email addresses for sending and receiving notifications
* Valid Google API Credentials
