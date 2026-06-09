package mate.academy.bookstoreappspring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreappspring.dto.book.BookDto;
import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.book.BookSearchParamsDto;
import mate.academy.bookstoreappspring.dto.book.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.mapper.BookMapper;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.book.BookRepository;
import mate.academy.bookstoreappspring.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setIsbn("ISBN-" + id);
        book.setPrice(BigDecimal.valueOf(42.50));
        book.setDescription("A handbook of agile software craftsmanship");
        return book;
    }

    private BookDto bookDto(Long id) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setTitle("Clean Code");
        dto.setAuthor("Robert Martin");
        dto.setIsbn("ISBN-" + id);
        dto.setPrice(BigDecimal.valueOf(42.50));
        dto.setDescription("A handbook of agile software craftsmanship");
        dto.setCategories(Set.of(1L));
        return dto;
    }

    @Test
    @DisplayName("createBook maps, saves and returns the persisted dto")
    void createBook_validDto_returnsSavedDto() {
        // given
        BookDto request = bookDto(null);
        Book entity = book(null);
        Book saved = book(1L);
        BookDto expected = bookDto(1L);

        when(bookMapper.toEntity(request)).thenReturn(entity);
        when(bookRepository.save(entity)).thenReturn(saved);
        when(bookMapper.toDto(saved)).thenReturn(expected);

        // when
        BookDto actual = bookService.createBook(request);

        // then
        assertEquals(expected, actual);
        verify(bookRepository).save(entity);
    }

    @Test
    @DisplayName("findById returns the dto when the book exists")
    void findById_existingId_returnsDto() {
        // given
        Book book = book(1L);
        BookDto expected = bookDto(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        // when
        BookDto actual = bookService.findById(1L);

        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findById throws EntityNotFoundException when the book is missing")
    void findById_nonExistingId_throwsException() {
        // given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        EntityNotFoundException ex =
                assertThrows(EntityNotFoundException.class, () -> bookService.findById(99L));
        assertEquals("Book with id 99 not found", ex.getMessage());
    }

    @Test
    @DisplayName("getAllBooks returns a page of mapped dtos")
    void getAllBooks_returnsMappedPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Book book = book(1L);
        BookDto dto = bookDto(1L);
        when(bookRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(book), pageable, 1));
        when(bookMapper.toDto(book)).thenReturn(dto);

        // when
        Page<BookDto> actual = bookService.getAllBooks(pageable);

        // then
        assertEquals(1, actual.getTotalElements());
        assertEquals(dto, actual.getContent().get(0));
    }

    @Test
    @DisplayName("deleteById deletes the book when it exists")
    void deleteById_existingId_deletesBook() {
        // given
        Book book = book(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // when
        bookService.deleteById(1L);

        // then
        verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("deleteById throws and never deletes when the book is missing")
    void deleteById_nonExistingId_throwsException() {
        // given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(99L));
        verify(bookRepository, never()).delete(any(Book.class));
    }

    @Test
    @DisplayName("updateById applies the changes and returns the updated dto")
    void updateById_existingId_returnsUpdatedDto() {
        // given
        Book book = book(1L);
        BookUpdateRequestDto request = new BookUpdateRequestDto();
        request.setTitle("New Title");
        BookDto expected = bookDto(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        // when
        BookDto actual = bookService.updateById(1L, request);

        // then
        assertEquals(expected, actual);
        verify(bookMapper).updateBook(request, book);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("updateById throws and never saves when the book is missing")
    void updateById_nonExistingId_throwsException() {
        // given
        BookUpdateRequestDto request = new BookUpdateRequestDto();
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(
                EntityNotFoundException.class, () -> bookService.updateById(99L, request));
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("search builds a specification and returns the mapped page")
    void search_returnsMappedPage() {
        // given
        BookSearchParamsDto params =
                new BookSearchParamsDto(new String[] {"Robert Martin"}, null);
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Book> spec = (root, query, cb) -> null;
        Book book = book(1L);
        BookDto dto = bookDto(1L);

        when(bookSpecificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable))
                .thenReturn(new PageImpl<>(List.of(book), pageable, 1));
        when(bookMapper.toDto(book)).thenReturn(dto);

        // when
        Page<BookDto> actual = bookService.search(params, pageable);

        // then
        assertEquals(1, actual.getTotalElements());
        assertEquals(dto, actual.getContent().get(0));
    }

    @Test
    @DisplayName("getBooksByCategoryId returns books without category ids")
    void getBooksByCategoryId_returnsMappedPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Book book = book(1L);
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(1L);
        dto.setTitle("Clean Code");
        when(bookRepository.findAllByCategoriesId(5L, pageable))
                .thenReturn(new PageImpl<>(List.of(book), pageable, 1));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto);

        // when
        Page<BookDtoWithoutCategoryIds> actual = bookService.getBooksByCategoryId(5L, pageable);

        // then
        assertEquals(1, actual.getTotalElements());
        assertEquals(dto, actual.getContent().get(0));
    }
}
