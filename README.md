# Shopify Order Retrieval App

## Overview

This application is designed to make a REST call to the Shopify API, retrieve order data based on specified filter
conditions, and generate an Excel sheet with the fetched data.

## Features

- Utilizes `RestTemplate` for making REST calls to the Shopify API.
- Configuration of API URI, key, and secret is handled through the application properties file.
- Uses `ObjectMapper` and `JsonNode` to process the API response and map it to the corresponding fields in the model
  class.
- Generates an Excel sheet using `XSSFWorkbook` from the Apache POI library.

## Setup

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/dev8523/shopify-order-retrieval.git
    cd shopify-order-retrieval
    ```

2. **Configure Shopify API Credentials:**

   Update the `application.properties` file with your Shopify API details.

    ```properties
    shopify.api.url=https://your-shopify-api-url
    shopify.api.key=your-api-key
    shopify.api.secret=your-api-secret
    ```

3. **Build and Run:**

    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

   The application will be accessible at `http://localhost:8080`.

## Usage

1. Access the application endpoint to retrieve orders:

    ```
    GET http://localhost:8080/orders/generateExcel?filter={your-filter-condition}
    ```

   Replace `{your-filter-condition}` with your desired filter condition.

2. The application will make a REST call to Shopify, retrieve orders, and generate an Excel sheet.

3. Find the generated Excel sheet in the project directory.

## Dependencies

- Spring Boot
- Apache POI
- Jackson (for JSON processing)

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.
