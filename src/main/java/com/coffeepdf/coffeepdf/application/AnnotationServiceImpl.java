package com.coffeepdf.coffeepdf.application;

import com.coffeepdf.coffeepdf.domain.Annotation;
import com.coffeepdf.coffeepdf.domain.PDFDocument;

import java.util.UUID;

public class AnnotationServiceImpl implements AnnotationService {

    @Override
    public void addAnnotation(PDFDocument pdf, Annotation annotation) {
        pdf.addAnnotation(annotation);
    }

    @Override
    public void editAnnotation(PDFDocument pdf, UUID annotationId, String newContent) {
        pdf.getAnnotations().stream()
                .filter(annotation -> annotation.getId().equals(annotationId))
                .findFirst()
                .ifPresent(annotation -> annotation.setContent(newContent));
    }

    @Override
    public void removeAnnotation(PDFDocument pdf, UUID annotationId) {
        pdf.removeAnnotation(annotationId);
    }
}
