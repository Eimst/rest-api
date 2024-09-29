 # Book REST API

This is a simple RESTful API for managing books. It allows users to retrieve books with optional filters and update the rating of a specific book.

## Prerequisites

- **Java 17 or higher**: The application is built using Java.
- **Maven**: For building and managing project dependencies.
- **SQLite**: The application uses SQLite as the database.

## Preloaded Database

The application includes a preloaded SQLite database located in the `src/main/resources` folder. This database contains some initial data for testing purposes.

### How to Use

- The preloaded database will be automatically used when you run the application.
- You can find the SQLite file in `src/main/resources/db`
- Ensure that the database connection in your `application.properties` points to this file:

```properties
spring.datasource.url=jdbc:sqlite:./src/main/resources/db/books.db
spring.datasource.driver-class-name=org.sqlite.JDBC
```
---

## API Endpoints


### GET /books

Retrieve a list of books with optional filters.

- **URL**: `/books`
- **Method**: `GET`
- **URL Query Parameters**:

  | Parameter     | Type    | Required | Description                          |
  |---------------|---------|----------|--------------------------------------|
  | `title`       | String  | No       | Filter books by title                |
  | `publishYear` | Integer | No       | Filter books by publication year     |
  | `author`      | String  | No       | Filter books by author               |
  | `rating`      | Integer | No       | Filter books by rating (1 to 5)      |
  
**Note**: Additional parameters that are not specified in the table above will be ignored and will not cause an error.

#### Success Response

- **Status**: `200 OK`
- **Content**:

    ```json
    {
      "books": [
        {
          "id": 1,
          "title": "The Road",
          "publishYear": 2006,
          "author": "Cormac McCarthy",
          "rating": 5
        },
        {
          "id": 2,
          "title": "The Goldfinch",
          "publishYear": 2013,
          "author": "Donna Tartt",
          "rating": 4
        }
        // ... other books
      ]
    }
    ```

#### Example Request

```http
GET /books?publishYear=2003&rating=5 HTTP/1.1
Host: localhost:8080
```

#### Error Response

  - **Status**: `400 Bad Request` (Occurs only if a data type mismatch happens, such as incorrect publishYear or rating input).

---

### PATCH /books/update-rating/{id}

Update the rating of a specific book.

- **URL**: `/books/update-rating/{id}`
- **Method**: `PATCH`
- **URL Path Parameter**:

  | Parameter | Type    | Required | Description                |
  |-----------|---------|----------|----------------------------|
  | `id`      | Integer | Yes      | ID of the book to update    |

- **URL Query Parameters**:

  | Parameter     | Type    | Required | Description                          |  
  |---------------|---------|----------|--------------------------------------|
  | `new-rating`  | Integer | Yes      | New rating value (between 1 and 5)   |

**Note**: Additional parameters not listed above will be ignored and will not cause an error in the request.


#### Success Response

- **Status**: `200 OK`
- **Content**:

  ```json
  {
    "message": "Successfully updated rating."
  }
  ```


#### Error Responses

- **Status**: `404 Not Found`

  ```json
  {
    "message": "The provided book doesn't exist."
  }
  ```

- **Status**: `400 Bad Request` (If new-rating is out of bounds)

  ```json
  {
    "message": "Validation failed for argument [newRating]"
  }
  ```


## Testing

### Running Unit Tests

The project includes unit tests for the `BookRepository`. To run the tests, execute the following command:

```bash
mvn test
```



### Running Integration Tests

Integration tests are included to test the API endpoints. To run the integration tests, execute the following command:

```bash
mvn verify
```


### Test Coverage

- **Repository Tests**: Verifies SQL queries and parameters.
- **Controller Tests**: Validates API endpoints, request parameters, and responses.

---

