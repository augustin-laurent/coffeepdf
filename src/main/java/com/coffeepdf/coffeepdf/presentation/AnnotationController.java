package com.coffeepdf.coffeepdf.presentation;

import com.coffeepdf.coffeepdf.application.AnnotationService;
import com.coffeepdf.coffeepdf.application.AnnotationServiceImpl;
import com.coffeepdf.coffeepdf.domain.Annotation;
import com.coffeepdf.coffeepdf.domain.PDFDocument;

import java.util.UUID;

public class AnnotationController {
    private final AnnotationService annotationService;

    public AnnotationController() {
        annotationService = new AnnotationServiceImpl();
    }

    public AnnotationController(AnnotationService annotationServiceArg) {
        annotationService = annotationServiceArg;
    }

    public void addAnnotation(PDFDocument doc, Annotation annotation) {
        annotationService.addAnnotation(doc, annotation);
    }

    public void editAnnotation(PDFDocument doc, UUID annotationId, String newContent) {
        annotationService.editAnnotation(doc, annotationId, newContent);
    }

    public void removeAnnotation(PDFDocument doc, UUID annotationId) {
        annotationService.removeAnnotation(doc, annotationId);
    }
}
