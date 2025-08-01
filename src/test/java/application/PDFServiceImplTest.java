package application;

import com.coffeepdf.coffeepdf.application.PDFService;
import com.coffeepdf.coffeepdf.application.PDFServiceImpl;
import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PDFServiceImplTest {
    private PDFDocument document;
    private PDFService pdfService;

    private static final String FALLBACK_PDF_CONTENT = "%PDF-1.4\n1 0 obj<</Type/Catalog/Pages 2 0 R>>endobj 2 0 obj<</Type/Pages/Kids[3 0 R]/Count 1>>endobj 3 0 obj<</Type/Page/Parent 2 0 R>>endobj xref 0 4 0000000000 65535 f 0000000010 00000 n 0000000053 00000 n 0000000125 00000 n trailer<</Size 4/Root 1 0 R>> startxref 173 %%EOF";

    @BeforeEach
    void setUp() {
        document = new PDFDocument(UUID.randomUUID(), "Sample PDF");

        byte[] validPdfContent1 = createValidPdfContent();
        byte[] validPdfContent2 = createValidPdfContent();

        document.addPage(new Page(1, validPdfContent1));
        document.addPage(new Page(2, validPdfContent2));
        pdfService = new PDFServiceImpl();
    }

    private byte[] createValidPdfContent() {
        try {
            PDDocument doc = new PDDocument();
            doc.addPage(new PDPage());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            doc.close();
            return baos.toByteArray();
        } catch (IOException e) {
            return FALLBACK_PDF_CONTENT.getBytes();
        }
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

    @Test
    void testConvertImagesToPDF() {
        String imagePath1 = "src/test/resources/image1.png";
        String imagePath2 = "src/test/resources/image2.png";
        List<String> imagePaths = List.of(imagePath1, imagePath2);

        PDFDocument result = pdfService.convertImagesToPDF(imagePaths);

        assertNotNull(result);
        assertEquals(2, result.getPages().size());
        assertEquals("converted_images.pdf", result.getName());
        assertNotNull(result.getPage(1));
        assertNotNull(result.getPage(2));
    }

    @Test
    void testConvertImagesToPDFWithNonExistentFiles() {
        List<String> invalidImagePaths = List.of("nonexistent1.png", "nonexistent2.jpg");

        PDFDocument result = pdfService.convertImagesToPDF(invalidImagePaths);

        assertNotNull(result);
        assertEquals("converted_images.pdf", result.getName());
        assertEquals(0, result.getPages().size());
    }

    @Test
    void testConvertImagesToPDFWithMixedValidAndInvalidFiles() {
        List<String> mixedPaths = List.of(
                "src/test/resources/image1.png",
                "nonexistent.jpg",
                "invalid_path.png"
        );

        PDFDocument result = pdfService.convertImagesToPDF(mixedPaths);

        assertNotNull(result);
        assertEquals("converted_images.pdf", result.getName());
    }

    @Test
    void testConvertImagesToPDFWithEmptyList() {
        List<String> emptyList = List.of();

        PDFDocument result = pdfService.convertImagesToPDF(emptyList);

        assertNotNull(result);
        assertEquals("converted_images.pdf", result.getName());
        assertEquals(0, result.getPages().size());
    }

    @Test
    void testExportPagesWithNonExistentPageNumbers() {
        List<Integer> nonExistentPages = List.of(99, 100);

        PDFDocument exportedDocument = pdfService.exportPages(document, nonExistentPages);

        assertNotNull(exportedDocument);
        assertEquals(0, exportedDocument.getPages().size());
        assertEquals("exported_Sample PDF", exportedDocument.getName());
    }

    @Test
    void testExportPagesWithEmptyList() {
        List<Integer> emptyList = List.of();

        PDFDocument exportedDocument = pdfService.exportPages(document, emptyList);

        assertNotNull(exportedDocument);
        assertEquals(0, exportedDocument.getPages().size());
        assertEquals("exported_Sample PDF", exportedDocument.getName());
    }

    @Test
    void testExportPages() {
        List<Integer> pagesToExport = List.of(1);

        PDFDocument exportedDocument = pdfService.exportPages(document, pagesToExport);

        assertNotNull(exportedDocument);
        assertEquals(1, exportedDocument.getPages().size());
        assertEquals("exported_Sample PDF", exportedDocument.getName());
        assertNotNull(exportedDocument.getPage(1));
        assertNull(exportedDocument.getPage(2));
    }
}
