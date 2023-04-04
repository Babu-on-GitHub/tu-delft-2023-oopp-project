package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.TagService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/tag")
public class TagController {

    static Logger log = Logger.getLogger(TagController.class.getName());

    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        try {
            var tag = tagService.getTagById(id);
            return ResponseEntity.ok(tag);
        } catch (IllegalArgumentException e){
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        throw new UnsupportedOperationException("The operation is not supported");
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {
        throw new UnsupportedOperationException("The operation is not supported");
    }
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Tag> update(@RequestBody Tag tag, @PathVariable("id") long id) {
        throw new UnsupportedOperationException("The operation is not supported");
    }
}
