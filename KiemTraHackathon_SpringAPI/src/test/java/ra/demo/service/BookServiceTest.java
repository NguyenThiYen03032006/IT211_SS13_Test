package ra.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ra.demo.exception.BookNotFound;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookResporitory bookResporitory;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {

        book1 = Book.builder()
                .id(1L)
                .title("Java Core")
                .author("Nguyen Van A")
                .category("Programming")
                .quantity(10)
                .build();

        book2 = Book.builder()
                .id(2L)
                .title("Spring Boot")
                .author("Tran Thi B")
                .category("Backend")
                .quantity(5)
                .build();
    }

    @Test
    void getAllBooks_returnList() {

        List<Book> mockBooks = Arrays.asList(book1, book2);

        when(bookResporitory.findAll()).thenReturn(mockBooks);

        List<Book> result = bookService.getBooks();

        assertEquals(2, result.size());
        assertEquals("Java Core", result.get(0).getTitle());
    }

    @Test
    void getBookById_found() {

        when(bookResporitory.findById(1L))
                .thenReturn(Optional.of(book1));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java Core", result.getTitle());
    }

    @Test
    void getBookById_notFound() {

        when(bookResporitory.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFound.class, () -> {
            bookService.getBookById(99L);
        });
    }
}

