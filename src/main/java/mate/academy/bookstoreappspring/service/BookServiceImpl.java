package mate.academy.bookstoreappspring.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.mapper.BookMapper;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    public BookDto createBook(Book book) {
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book is not found")));
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.getAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
