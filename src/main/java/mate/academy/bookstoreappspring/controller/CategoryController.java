package mate.academy.bookstoreappspring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.category.CategoryDto;
import mate.academy.bookstoreappspring.service.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Operations for categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get all categories with pagination")
    @GetMapping
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Create new category")
    @ApiResponse(responseCode = "201", description = "Category created successfully",
            content = @Content(schema = @Schema(implementation = CategoryDto.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryDto create(
            @RequestBody
            @Valid
            CategoryDto dto) {
        return categoryService.save(dto);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public CategoryDto getById(
            @Parameter(description = "ID of the category") @PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category by ID")
    @PutMapping("/{id}")
    public CategoryDto update(
            @Parameter(description = "ID of the category") @PathVariable Long id,
            @RequestBody CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category by ID")
    @DeleteMapping("/{id}")
    public void deleteById(@Parameter(description = "ID of the category") @PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get books by category ID with pagination")
    @GetMapping("/{id}/books")
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @Parameter(description = "ID of the category") @PathVariable Long id,
            Pageable pageable) {
        return categoryService.getBooksByCategoryId(id, pageable);
    }
}
