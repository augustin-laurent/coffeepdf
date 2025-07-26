package domain;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.UUID;


class PDFDocumentTest {
    @TempDir
    Path tempDir;

    private Path testPdfPath;
    private PDFDocument pdfDocument;

    @BeforeEach
    void setUp() throws IOException {
        testPdfPath = tempDir.resolve("test.pdf");
        Files.createFile(testPdfPath);
        pdfDocument = new PDFDocument(UUID.randomUUID(), "test.pdf", testPdfPath.toString());
    }

    @Test
    void testConstructorWithPath() {
        assertNotNull(pdfDocument);
        assertEquals(testPdfPath.toString(), pdfDocument.getSourcePath());
    }

    @Test
    void testGetId() {
        String id = String.valueOf(pdfDocument.getId());
        assertNotNull(id);
        assertFalse(id.isEmpty());
    }

    @Test
    void testGetName() {
        String name = pdfDocument.getName();
        assertEquals("test.pdf", name);
    }

    @Test
    void testGetSourcePath() {
        assertEquals(testPdfPath.toString(), pdfDocument.getSourcePath());
    }

    @Test
    void testRemovePage() {
        Page page1 = new Page(1, "content1".getBytes());
        Page page2 = new Page(2, "content2".getBytes());
        pdfDocument.addPage(page1);
        pdfDocument.addPage(page2);

        pdfDocument.removePage(1);

        assertNull(pdfDocument.getPage(1));
        assertNotNull(pdfDocument.getPage(2));
    }

    @Test
    void testInMemoryDocumentMethods() throws IOException {
        PDDocument testDocument = new PDDocument();

        assertFalse(pdfDocument.hasInMemoryDocument());

        pdfDocument.setInMemoryDocument(testDocument);

        assertTrue(pdfDocument.hasInMemoryDocument());
        assertEquals(testDocument, pdfDocument.getInMemoryDocument());

        testDocument.close();
    }
}
