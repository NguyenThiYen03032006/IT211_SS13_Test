package ra.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ra.demo.exception.BookNotFound;
import ra.demo.model.dto.request.BookDTO;
import ra.demo.model.entity.Book;
import ra.demo.repository.BookResporitory;
import ra.demo.service.BookService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookResporitory bookResporitory;

    @Override
    public List<Book> getBooks() {

        log.debug("Request lấy toàn bộ danh sách sách");

        List<Book> books = bookResporitory.findAll();

        log.info("Lấy danh sách sách thành công. Tổng số sách: {}", books.size());

        return books;
    }

    @Override
    public Book getBookById(Long id) {

        log.debug("Request lấy sách với id: {}", id);

        Book book = bookResporitory.findById(id)
                .orElseThrow(() -> {

                    log.error("Không tìm thấy sách với id: {}", id);

                    return new BookNotFound("Không tồn tại sách có mã " + id);
                });

        log.info("Lấy thông tin sách thành công với id: {}", id);

        return book;
    }

    @Override
    public Book insertBook(BookDTO bookDTO) {

        log.debug("Request thêm mới sách: {}", bookDTO);

        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();

        Book savedBook = bookResporitory.save(book);

        log.info("Thêm mới sách thành công với id: {}", savedBook.getId());

        return savedBook;
    }

    @Override
    public Book updateBook(Long id, BookDTO bookDTO) {

        log.debug("Request cập nhật sách với id: {}", id);

        bookResporitory.findById(id)
                .orElseThrow(() -> {

                    log.error("Không tìm thấy sách để cập nhật với id: {}", id);

                    return new BookNotFound("Không tồn tại sách có mã " + id);
                });

        Book book = Book.builder()
                .id(id)
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .category(bookDTO.getCategory())
                .quantity(bookDTO.getQuantity())
                .build();

        Book updatedBook = bookResporitory.save(book);

        log.info("Cập nhật sách thành công với id: {}", id);

        return updatedBook;
    }

    @Override
    public boolean deleteBook(Long id) {

        log.debug("Request xóa sách với id: {}", id);

        bookResporitory.findById(id)
                .orElseThrow(() -> {

                    log.error("Không tìm thấy sách để xóa với id: {}", id);

                    return new BookNotFound("Không tồn tại sách có mã " + id);
                });

        bookResporitory.deleteById(id);

        log.info("Xóa sách thành công với id: {}", id);

        return true;
    }

    @Override
    public Book updatePartialBook(Long id, BookDTO bookDTO) {

        log.debug("Request cập nhật từng phần sách với id: {}", id);

        Book book = bookResporitory.findById(id)
                .orElseThrow(() -> {

                    log.error("Không tìm thấy sách để patch với id: {}", id);

                    return new BookNotFound("Không tồn tại sách có mã " + id);
                });

        if (bookDTO.getTitle() != null && !bookDTO.getTitle().isBlank()) {
            book.setTitle(bookDTO.getTitle());
        }

        if (bookDTO.getAuthor() != null && !bookDTO.getAuthor().isBlank()) {
            book.setAuthor(bookDTO.getAuthor());
        }

        if (bookDTO.getCategory() != null && !bookDTO.getCategory().isBlank()) {
            book.setCategory(bookDTO.getCategory());
        }

        if (bookDTO.getQuantity() != null && bookDTO.getQuantity() >= 0) {
            book.setQuantity(bookDTO.getQuantity());
        }

        Book updatedBook = bookResporitory.save(book);

        log.info("Patch sách thành công với id: {}", id);

        return updatedBook;
    }
}