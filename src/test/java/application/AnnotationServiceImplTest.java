package application;

import com.coffeepdf.coffeepdf.application.AnnotationService;
import com.coffeepdf.coffeepdf.application.AnnotationServiceImpl;
import com.coffeepdf.coffeepdf.domain.Annotation;
import com.coffeepdf.coffeepdf.domain.AnnotationType;
import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationServiceImplTest {
    private PDFDocument document;
    private AnnotationService annotationService;

    @BeforeEach
    void setUp() {
        document = new PDFDocument(UUID.randomUUID(), "Test Document");
        annotationService = new AnnotationServiceImpl();
    }

    @Test
    void testAddAnnotation() {
        Annotation annotation = new Annotation(
                UUID.randomUUID(),
                AnnotationType.HIGHLIGHT,
                1, "Test highlight",
                new Rectangle(10, 10, 100, 20)
        );
        annotationService.addAnnotation(document, annotation);

        assertTrue(document.getAnnotations().contains(annotation));
    }

    @Test
    void testEditAnnotation() {
        UUID id = UUID.randomUUID();
        Annotation annotation = new Annotation(
                id,
                AnnotationType.COMMENT,
                1,
                "Original comment",
                new Rectangle(5, 5, 20, 10)
        );
        annotationService.addAnnotation(document, annotation);

        annotationService.editAnnotation(document, id, "Edited comment");

        assertEquals("Edited comment", document.getAnnotations().getFirst().getContent());
    }

    @Test
    void testRemoveAnnotation() {
        UUID id = UUID.randomUUID();
        Annotation annotation = new Annotation(
                id,
                AnnotationType.SHAPE,
                2,
                "A shape annotation",
                new Rectangle(0, 0, 15, 15)
        );
        annotationService.addAnnotation(document, annotation);

        annotationService.removeAnnotation(document, id);

        assertTrue(document.getAnnotations().isEmpty());
    }
}
