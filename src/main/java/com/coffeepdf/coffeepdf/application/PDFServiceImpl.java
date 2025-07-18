package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class PDFServiceImpl implements PDFService {

    private static final Logger logger = getLogger(PDFServiceImpl.class);

    @Override
    public PDFDocument convertImagesToPDF(List<String> imagePaths) {
        try (PDDocument document = new PDDocument()) {
            imagePaths.forEach(imagePath -> {
                try {
                    BufferedImage image = ImageIO.read(new File(imagePath));
                    PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);

                    PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
                    document.addPage(page);

                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.drawImage(pdImage, 0, 0);
                    }
                } catch (IOException e) {
                    logger.error("Failed to add image to PDF: {}", imagePath, e);
                }
            });

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);

            PDFDocument pdfDoc = new PDFDocument(UUID.randomUUID(), "converted_images.pdf");
            PDFRenderer renderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage pageImage = renderer.renderImageWithDPI(i, 150);
                ByteArrayOutputStream pageContentStream = new ByteArrayOutputStream();
                ImageIO.write(pageImage, "PNG", pageContentStream);

                Page page = new Page(i + 1, 0, pageContentStream.toByteArray());
                pdfDoc.addPage(page);
            }

            return pdfDoc;
        } catch (IOException e) {
            logger.error("Failed to convert images to PDF", e);
            throw new RuntimeException("Failed to convert images to PDF", e);
        }
    }

    @Override
    public PDFDocument deletePages(PDFDocument pdf, List<Integer> pages) {
        List<Page> remainingPages = pdf.getPages().stream()
                .filter(page -> !pages.contains(page.getPageNumber()))
                .toList();

        PDFDocument newPdf = new PDFDocument(UUID.randomUUID(), pdf.getName());
        remainingPages.forEach(newPdf::addPage);

        return newPdf;
    }

    @Override
    public PDFDocument rotatePages(PDFDocument pdf, Map<Integer, Integer> rotations) {
        pdf.getPages().stream()
                .filter(page -> rotations.containsKey(page.getPageNumber()))
                .forEach(page -> {
                    int angle = rotations.get(page.getPageNumber());
                    try (PDDocument pdDoc = PDDocument.load(page.getContent())) {
                        PDPage pdPage = pdDoc.getPage(0);
                        pdPage.setRotation(angle);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        pdDoc.save(baos);
                        page.setContent(baos.toByteArray());
                        page.setRotationAngle(angle);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                });
        return pdf;
    }

    @Override
    public PDFDocument exportPages(PDFDocument pdf, List<Integer> pages) {
        List<Page> exportedPages = pdf.getPages().stream()
                .filter(page -> pages.contains(page.getPageNumber()))
                .toList();

        PDFDocument exportedPdf = new PDFDocument(UUID.randomUUID(), "exported_" + pdf.getName());
        exportedPages.forEach(exportedPdf::addPage);

        return exportedPdf;
    }
}
