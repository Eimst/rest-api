package com.example.crud1.repositories;

import com.example.crud1.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.example.crud1.utils.Pair;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final DataSource dataSource;

    @Autowired
    public BookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Pair buildQueryAndParameters(String title, Integer year, String author, Integer rating) {

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM books");
        List<Object> parameters = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (title != null) {
            conditions.add("title = ?");
            parameters.add(title);
        }
        if (year != null) {
            conditions.add("publishYear = ?");
            parameters.add(year);
        }
        if (author != null) {
            conditions.add("author = ?");
            parameters.add(author);
        }
        if (rating != null) {
            conditions.add("rating = ?");
            parameters.add(rating);
        }

        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(String.join(" AND ", conditions));
        }

        queryBuilder.append(";");
        return new Pair(queryBuilder.toString(), parameters);
    }


    private Optional<List<Book>> executeQuery(String query, List<Object> parameters){
        Optional<List<Book>> optionalBooks = Optional.empty();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();

            while(resultSet.next()){
                books.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getInt("publishYear"),
                        resultSet.getString("author"),
                        resultSet.getInt("rating")
                ));
            }
            optionalBooks = Optional.of(books);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return optionalBooks;
    }

    public Optional<List<Book>> getFilteredBooks(String title, Integer year, String author, Integer rating)  {
        Pair queryAndParameters = buildQueryAndParameters(title, year, author, rating);

        String query = queryAndParameters.query();
        List<Object> parameters = queryAndParameters.parameters();

        return executeQuery(query, parameters);
    }


    public boolean updateRating(int id, int newRating){
        try (Connection connection = dataSource.getConnection()) {
            String query = "UPDATE books SET rating=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, newRating);
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }
}
