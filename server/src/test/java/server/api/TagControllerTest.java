package server.api;

import commons.Tag;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.TagService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Tests for TagController")
class TagControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Get all tags")
    public void testGetAll() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "tag1"));
        tags.add(new Tag(2, "tag2"));
        when(tagService.getAllTags()).thenReturn(tags);
        List<Tag> result = tagController.getAll();
        assertEquals(tags, result);
    }

    @Test
    @DisplayName("Get tag by id - success")
    public void testGetByIdSuccess() {
        long id = 1;
        Tag tag = new Tag(id, "tag1");
        when(tagService.getTagById(id)).thenReturn(tag);
        ResponseEntity<Tag> result = tagController.getById(id);
        assertEquals(tag, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Get tag by id - failure")
    public void testGetByIdFailure() {
        long id = 1;
        String message = "Tag with id " + id + " not found";
        when(tagService.getTagById(id)).thenThrow(new IllegalArgumentException(message));
        ResponseEntity<Tag> result = tagController.getById(id);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }


    @Test
    public void testAddWithException() {
        Tag tag = new Tag(1, "tag1");
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            tagController.add(tag);
        });

        String expectedMessage = "The operation is not supported";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testRemoveWithException() {
        Tag tag = new Tag(1, "tag1");
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            tagController.remove(1);
        });

        String expectedMessage = "The operation is not supported";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testUpdateWithException() {
        Tag tag = new Tag(1, "tag1");
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            tagController.update(tag,1);
        });

        String expectedMessage = "The operation is not supported";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

