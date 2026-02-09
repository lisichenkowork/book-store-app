package mate.academy.bookstoreappspring.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.book.BookCreateRequestDto;
import mate.academy.bookstoreappspring.dto.book.BookDto;
import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.book.BookSearchParamsDto;
import mate.academy.bookstoreappspring.dto.book.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.mapper.BookMapper;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.book.BookRepository;
import mate.academy.bookstoreappspring.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto createBook(BookCreateRequestDto dto) {

        Book entity = bookMapper.toEntity(dto);

        return bookMapper.toDto(bookRepository.save(entity));
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(getEntityByIdOrThrow(id));
    }

    @Override
    public List<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        Book book = getEntityByIdOrThrow(id);

        bookRepository.delete(book);
    }

    @Override
    public BookDto updateById(Long id, BookUpdateRequestDto updateBookRequestDto) {
        Book entity = getEntityByIdOrThrow(id);

        bookMapper.updateBook(updateBookRequestDto, entity);

        bookRepository.save(entity);

        return bookMapper.toDto(entity);
    }

    @Override
    public List<BookDto> search(BookSearchParamsDto bookSearchParams, Pageable pageable) {
        Specification<Book> spec = bookSpecificationBuilder.build(bookSearchParams);

        Page<Book> bookPage = bookRepository.findAll(spec, pageable);

        return bookPage.getContent().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {

        return bookRepository.findAllByCategories_id(id, pageable)
                .stream()
                .map(bookMapper::toDtoWithoutCategoryIds)
                .toList();
    }

    private Book getEntityByIdOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }
}
