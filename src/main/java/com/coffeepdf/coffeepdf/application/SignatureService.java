package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Signature;

import java.util.UUID;

/**
 * Service interface for managing digital signatures in PDF documents.
 * Provides methods to sign and verify signatures on PDF documents.
 */
public interface SignatureService {
    /**
     * Signs a PDF document with a digital signature.
     *
     * @param pdf        the PDF document to be signed
     * @param signature  the digital signature to be applied
     */
    void signDocument(PDFDocument pdf, Signature signature);
    /**
     * Verifies a digital signature on a PDF document.
     *
     * @param pdf         the PDF document containing the signature
     * @param signatureId the unique identifier of the signature to be verified
     * @return true if the signature is valid, false otherwise
     */
    boolean verifySignature(PDFDocument pdf, UUID signatureId);
}
