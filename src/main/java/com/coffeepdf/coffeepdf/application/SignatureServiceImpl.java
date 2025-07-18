package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Signature;

import java.util.UUID;

public class SignatureServiceImpl implements  SignatureService {
    @Override
    public void signDocument(PDFDocument pdf, Signature signature) {
        pdf.addSignature(signature);
    }

    @Override
    public boolean verifySignature(PDFDocument pdf, UUID signatureId) {
        return pdf.getSignatures().stream()
                .filter(sig -> sig.getId().equals(signatureId))
                .findFirst()
                .map(Signature::verify)
                .orElse(false);
    }
}
