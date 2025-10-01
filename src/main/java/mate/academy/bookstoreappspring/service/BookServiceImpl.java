package mate.academy.bookstoreappspring.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.dto.UpdateBookRequestDto;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.mapper.BookMapper;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.dto.CreateBookRequestDto;
import mate.academy.bookstoreappspring.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    public BookDto createBook(CreateBookRequestDto createBookRequestDto) {
        Book model = bookMapper.toModel(createBookRequestDto);
        return bookMapper.toDto(bookRepository.save(model));
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book is not found")));
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, UpdateBookRequestDto updateBookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        bookMapper.updateModelFromDto(updateBookRequestDto, book);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);

    }
}
