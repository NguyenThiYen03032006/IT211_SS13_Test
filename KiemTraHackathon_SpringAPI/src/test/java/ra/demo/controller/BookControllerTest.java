package ra.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ra.demo.advice.BookControllerAdvice;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.service.BookService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(BookControllerAdvice.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_return200() throws Exception {

        List<Book> books = List.of(
                Book.builder()
                        .id(1L)
                        .title("Java")
                        .author("Author A")
                        .category("Programming")
                        .quantity(10)
                        .build(),

                Book.builder()
                        .id(2L)
                        .title("Spring")
                        .author("Author B")
                        .category("Backend")
                        .quantity(5)
                        .build()
        );

        when(bookService.getBooks()).thenReturn(books);

        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("Java"));
    }

    @Test
    void getBookById_found() throws Exception {

        Book book = Book.builder()
                .id(1L)
                .title("Java Core")
                .author("Nguyen Van A")
                .category("Programming")
                .quantity(10)
                .build();

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Java Core"));
    }

    @Test
    void getBookById_notFound() throws Exception {

        when(bookService.getBookById(99L))
                .thenThrow(new BookNotFound("Không tồn tại sách"));

        mockMvc.perform(get("/api/v1/books/99")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }
}
