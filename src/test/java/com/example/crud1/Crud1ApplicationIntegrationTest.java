package com.example.crud1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class Crud1ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testGetBooks_WithoutFilter_ShouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/books")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", hasSize(17)))
                .andExpect(jsonPath("$.books[0].title").value("The Road"))
                .andExpect(jsonPath("$.books[1].title").value("The Goldfinch"));
    }

    @Test
    void testGetBooks_WithFilter_ShouldReturnFilteredBooks() throws Exception {
        mockMvc.perform(get("/books")
                        .param("publishYear", "2003")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", hasSize(2)))
                .andExpect(jsonPath("$.books[0].title").value("The Kite Runner"))
                .andExpect(jsonPath("$.books[1].title").value("The Da Vinci Code"));
    }

    @Test
    void testGetBooks_WithIncorrectTypeParams_ShouldReturnBadRequest400() throws Exception {
        mockMvc.perform(get("/books")
                        .param("publishYear", "2003a")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBooks_WithNonExistingParams_ShouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/books")
                        .param("publishYearYear", "2003")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books", hasSize(17)))
                .andExpect(jsonPath("$.books[0].title").value("The Road"))
                .andExpect(jsonPath("$.books[1].title").value("The Goldfinch"));
    }

    @Test
    void testChangeRating_Correct_ShouldChangeRating() throws Exception {
        mockMvc.perform(patch("/books/update-rating/{id}", 1)
                        .param("new-rating", "5")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully updated rating."));
    }

    @Test
    void testChangeRating_IncorrectId_ShouldReturnNotFound404() throws Exception {
        mockMvc.perform(patch("/books/update-rating/{id}", -1)
                        .param("new-rating", "5")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The provided book doesn't exist."));
    }

    @Test
    void testChangeRating_NewRatingOutOfBounds_ShouldReturnBadRequest400() throws Exception {
        mockMvc.perform(patch("/books/update-rating/{id}", 1)
                        .param("new-rating", "6")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}
