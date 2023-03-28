package server.services;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.List;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    SynchronizationService synchronizationService;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(long id) {
        if (id < 0 || tagRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Trying to get non existing tag");
        }
        return tagRepository.findById(id).orElse(null);
    }

    public Tag updateTag(Tag tag, long id) {
        throw new UnsupportedOperationException();
    }

    public Tag saveTag(Tag tag, long id) {
        throw new UnsupportedOperationException();
    }

    public void removeTag(long id) {
        throw new UnsupportedOperationException();
    }
}
