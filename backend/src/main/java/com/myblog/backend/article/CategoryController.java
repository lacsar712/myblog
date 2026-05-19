package com.myblog.backend.article;

import com.myblog.backend.article.dto.CategoryDto;
import com.myblog.backend.common.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final CategoryRepository repository;

    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResponse<List<CategoryDto>> list() {
        return ApiResponse.ok(repository.findAll().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getSlug()))
                .toList());
    }

    @PostMapping
    public ApiResponse<CategoryDto> create(@RequestBody Category c) {
        Category saved = repository.save(new Category(c.getName(), c.getSlug()));
        return ApiResponse.ok(new CategoryDto(saved.getId(), saved.getName(), saved.getSlug()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        repository.deleteById(id);
        return ApiResponse.ok(null);
    }
}
