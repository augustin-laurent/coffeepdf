package application;

import com.coffeepdf.coffeepdf.application.OCRService;
import com.coffeepdf.coffeepdf.application.OCRServiceImpl;
import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OCRServiceTest {
    private PDFDocument document;
    private OCRService ocrService;

    @BeforeEach
    void setUp() {
        document = new PDFDocument(UUID.randomUUID(), "OCR Doc");
        document.addPage(new Page(1, new byte[0]));
        document.addPage(new Page(2, new byte[0]));
        ocrService = new OCRServiceImpl();
    }

    @Test
    void testExtractTextFromPageReturnsEmptyString() {
        String text = ocrService.extractTextFromPage(document.getPage(1));
        assertEquals("", text);
    }

    @Test
    void testExtractTextFromDocumentReturnsMapWithPageNumbers() {
        Map<Integer, String> result = ocrService.extractText(document);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(1));
        assertTrue(result.containsKey(2));
    }
}
