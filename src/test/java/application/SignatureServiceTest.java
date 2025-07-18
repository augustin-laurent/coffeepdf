package application;

import com.coffeepdf.coffeepdf.application.SignatureService;
import com.coffeepdf.coffeepdf.application.SignatureServiceImpl;
import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Rectangle;
import com.coffeepdf.coffeepdf.domain.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignatureServiceTest {
    private PDFDocument document;
    private SignatureService signatureService;
    private Signature signature;

    @BeforeEach
    void setUp() {
        document = new PDFDocument(UUID.randomUUID(), "Signed Doc");
        signatureService = new SignatureServiceImpl();
        signature = new Signature(
                UUID.randomUUID(), "Alice", LocalDateTime.now(), new byte[]{1,2,3}, new Rectangle(0,0,10,10)
        );
    }

    @Test
    void testSignDocument() {
        signatureService.signDocument(document, signature);
        assertTrue(document.getSignatures().contains(signature));
    }

    @Test
    void testVerifySignatureReturnsFalseByDefault() {
        signatureService.signDocument(document, signature);
        // returns false since verify() is a stub in Signature
        assertFalse(signatureService.verifySignature(document, signature.getId()));
    }
}
