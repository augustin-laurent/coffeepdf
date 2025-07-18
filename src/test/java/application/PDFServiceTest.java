package application;

import com.coffeepdf.coffeepdf.application.PDFService;
import com.coffeepdf.coffeepdf.application.PDFServiceImpl;
import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PDFServiceTest {
    private PDFDocument document;
    private PDFService pdfService;

    @BeforeEach
    void setUp() {
        document = new PDFDocument(UUID.randomUUID(), "Sample PDF");
        document.addPage(new Page(1, new byte[0]));
        document.addPage(new Page(2, new byte[0]));
        pdfService = new PDFServiceImpl();
    }

    @Test
    void rotatePagesAppliesCorrectRotationToSpecifiedPages() {
        Map<Integer, Integer> rotations = Map.of(1, 90, 2, 180);

        pdfService.rotatePages(document, rotations);

        assertEquals(90, document.getPage(1).getRotationAngle());
        assertEquals(180, document.getPage(2).getRotationAngle());
    }

    @Test
    void rotatePagesIgnoresPagesNotInRotationMap() {
        Map<Integer, Integer> rotations = Map.of(1, 90);

        pdfService.rotatePages(document, rotations);

        assertEquals(90, document.getPage(1).getRotationAngle());
        assertEquals(0, document.getPage(2).getRotationAngle());
    }

    @Test
    void rotatePagesHandlesEmptyRotationMap() {
        Map<Integer, Integer> rotations = Map.of();

        pdfService.rotatePages(document, rotations);

        assertEquals(0, document.getPage(1).getRotationAngle());
        assertEquals(0, document.getPage(2).getRotationAngle());
    }

    @Test
    void rotatePagesHandlesInvalidPageContentGracefully() {
        document.getPage(1).setContent(null);
        Map<Integer, Integer> rotations = Map.of(1, 90);

        assertDoesNotThrow(() -> pdfService.rotatePages(document, rotations));
        assertEquals(0, document.getPage(1).getRotationAngle());
    }

    @Test
    void testDeletePages() {
        List<Integer> pagesToDelete = List.of(1);
        pdfService.deletePages(document, pagesToDelete);

        assertNull(document.getPage(1));
        assertNotNull(document.getPage(2));
    }
}
