package com.coffeepdf.coffeepdf.infrastructure;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PDFRepositoryImpl implements PDFRepository {

    public PDFRepositoryImpl() {
        // Constructor for dependency injection
    }

    @Override
    public PDFDocument load(String path) {
        try (PDDocument document = PDDocument.load(new File(path))) {
            String fileName = new File(path).getName();
            PDFDocument pdfDoc = new PDFDocument(UUID.randomUUID(), fileName);

            PDFRenderer renderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                PDPage pdPage = document.getPage(i);

                // 150 DPI is a common resolution for rendering PDF pages, might create a settings later on to adjust it.
                BufferedImage image = renderer.renderImageWithDPI(i, 150);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", baos);
                byte[] pageContent = baos.toByteArray();

                Page page = new Page(i + 1, pdPage.getRotation(), pageContent);
                pdfDoc.addPage(page);
            }
            return pdfDoc;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load PDF from path: " + path, e);
        }
    }

    @Override
    public void save(PDFDocument document, String path) {
        try (PDDocument pdDocument = new PDDocument()) {
            for (Page page : document.getPages()) {
                PDPage pdPage = new PDPage();
                pdPage.setRotation(page.getRotationAngle());
                pdDocument.addPage(pdPage);
            }
            pdDocument.save(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save PDF to path: " + path, e);
        }
    }
}