package com.myblog.backend.article;

import com.myblog.backend.article.dto.TagDto;
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
@RequestMapping("/api/admin/tags")
public class TagController {

    private final TagRepository repository;

    public TagController(TagRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResponse<List<TagDto>> list() {
        return ApiResponse.ok(repository.findAll().stream()
                .map(t -> new TagDto(t.getId(), t.getName()))
                .toList());
    }

    @PostMapping
    public ApiResponse<TagDto> create(@RequestBody Tag t) {
        Tag saved = repository.save(new Tag(t.getName()));
        return ApiResponse.ok(new TagDto(saved.getId(), saved.getName()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        repository.deleteById(id);
        return ApiResponse.ok(null);
    }
}
