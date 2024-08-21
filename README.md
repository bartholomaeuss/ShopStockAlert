# ShopStockAlert

ShopStockAlert is a simple Java application designed to monitor online store stock for specific items.
It fetches the webpage's content, processes it to identify available deals, and provides links to those deals.
This project demonstrates the usage of web scraping and data processing with Java.

## Features

- Fetches webpage content using Jsoup.
- Extracts article elements from the page.
- Identifies articles containing specific keywords (e.g., "deal").
- Provides links to the identified deals.

## Requirements

- Java 11 or higher
- Google API Credentials: You need a cs.json file with Gmail API credentials for sending emails
- Maven (for managing dependencies and building the project)
