package com.example.crud1.testRepositories;

import com.example.crud1.repositories.BookRepository;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BookRepositoryTest {

    @InjectMocks
    private BookRepository bookRepository;

    @Mock
    private DataSource mockDataSource;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);
    }

    @Test
    public void getFilteredBooks_queryAndParameters_OneParam() throws SQLException {

        bookRepository.getFilteredBooks("Testas", null, null, null);
        verify(mockConnection).prepareStatement("SELECT * FROM books WHERE title = ?;");
        verify(mockPreparedStatement, times(1)).setObject(1, "Testas");
    }

    @Test
    public void testGetFilteredBooks_queryAndParameters_AllParams() throws SQLException {

        bookRepository.getFilteredBooks("Testas", 2024, "Testas author", 5);
        verify(mockConnection).prepareStatement
                ("SELECT * FROM books WHERE title = ? AND publishYear = ? AND author = ? AND rating = ?;");

        verify(mockPreparedStatement).setObject(1, "Testas");
        verify(mockPreparedStatement).setObject(2, 2024);
        verify(mockPreparedStatement).setObject(3, "Testas author");
        verify(mockPreparedStatement).setObject(4, 5);
    }

    @Test
    public void testGetFilteredBooks_queryAndParameters_NoParams() throws SQLException {
        bookRepository.getFilteredBooks(null, null, null, null);
        verify(mockConnection).prepareStatement("SELECT * FROM books;");
    }
}
