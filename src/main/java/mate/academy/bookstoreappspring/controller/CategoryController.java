package mate.academy.bookstoreappspring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Categories", description = "Endpoints for managing book categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Get all categories",
            description = "Returns paginated list of all categories"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<CategoryDto> getAll(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(
            summary = "Create category",
            description = "Creates a new category (ADMIN only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryDto createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category data to create",
                    required = true
            )
            @RequestBody @Valid CategoryDto dto) {
        return categoryService.save(dto);
    }

    @Operation(
            summary = "Get category by ID",
            description = "Returns a category by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Update category",
            description = "Updates an existing category by ID (ADMIN only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated category data",
                    required = true
            )
            @RequestBody @Valid CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    @Operation(
            summary = "Delete category",
            description = "Deletes category by ID (ADMIN only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(
            summary = "Get books by category ID",
            description = "Returns paginated list of books that belong to a specific category"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookDtoWithoutCategoryIds.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}/books")
    @PreAuthorize("hasRole('USER')")
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return categoryService.getBooksByCategoryId(id, pageable);
    }
}

