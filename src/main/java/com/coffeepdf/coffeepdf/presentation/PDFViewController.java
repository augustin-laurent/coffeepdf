package com.coffeepdf.coffeepdf.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ZoomEvent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.coffeepdf.coffeepdf.domain.PDFDocument;
import com.coffeepdf.coffeepdf.domain.Page;

public class PDFViewController {

    // Static constants for thumbnail dimensions and buffer
    private static final int THUMBNAIL_WIDTH = 80;
    private static final int THUMBNAIL_HEIGHT = 100;

    // Top bar buttons
    @FXML private Button openButton;
    @FXML private Button saveButton;
    @FXML private Button exportButton;

    // Command panel buttons
    @FXML private Button rotateButton;
    @FXML private Button annotateButton;
    @FXML private Button signButton;

    // PDF Preview Area
    @FXML private StackPane pdfPreviewPane;
    @FXML private Label noPdfLabel;

    // Instance fields for thumbnail management
    @FXML private ListView<Integer> thumbnailListView;
    private final ObservableList<Integer> thumbnailPageIndices = FXCollections.observableArrayList();
    private final Map<Integer, Image> thumbnailCache = new HashMap<>();

    private ImageView pdfImageView;
    private PDFController pdfController;
    private Label pageInfoLabel;
    private int currentPageIndex = 0;

    private double zoomLevel = 1.0;
    private static final double MIN_ZOOM = 0.25;
    private static final double MAX_ZOOM = 3.0;
    private static final double ZOOM_STEP = 0.1;

    @FXML
    public void initialize() {
        thumbnailListView.setItems(thumbnailPageIndices);
        thumbnailListView.setCellFactory(listView -> new ThumbnailCell(this));

        thumbnailListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        navigateToPage(newValue);
                    }
                }
        );

        pdfController = new PDFController();

        pdfImageView = new ImageView();
        pdfImageView.setPreserveRatio(true);
        pdfImageView.setFitWidth(350);
        pdfImageView.setFitHeight(450);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pdfImageView);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        pageInfoLabel = new Label();
        pageInfoLabel.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-text-fill: white; -fx-padding: 5px;");
        pageInfoLabel.setVisible(false);

        scrollPane.setStyle("-fx-background-color: transparent;");
        pdfImageView.setClip(new Rectangle());

        pdfPreviewPane.getChildren().addAll(scrollPane, pageInfoLabel);
        pdfImageView.setVisible(false);

        pdfPreviewPane.setOnScroll(this::handleScrollZoom);
        pdfPreviewPane.setOnZoom(this::handleZoomGesture);

        StackPane.setAlignment(pageInfoLabel, Pos.BOTTOM_RIGHT);

        openButton.setOnAction(event -> openPdfFile());
        saveButton.setOnAction(event -> savePdfFile());
        exportButton.setOnAction(event -> exportPdfFile());
        rotateButton.setOnAction(event -> rotatePage());
        annotateButton.setOnAction(event -> annotatePdf());
        signButton.setOnAction(event -> signPdf());

        pdfPreviewPane.setOnKeyPressed(event -> {
            if (event.isControlDown() ||event.isMetaDown()) {
                switch (event.getCode()) {
                    case PLUS, EQUALS:
                        zoomIn();
                        event.consume();
                        break;
                    case MINUS:
                        zoomOut();
                        event.consume();
                        break;
                    case DIGIT0:
                        resetZoom();
                        event.consume();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + event.getCode());
                }
            }
        });

        pdfPreviewPane.setFocusTraversable(true);

        thumbnailListView.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null && newValue.intValue() >= 0) {
                        navigateToPage(newValue.intValue());
                    }
                }
        );
    }

    private void loadPDFThumbnails(PDFDocument document) {
        thumbnailCache.clear();
        thumbnailPageIndices.clear();

        for (int i = 0; i < document.getPages().size(); i++) {
            thumbnailPageIndices.add(i);

            try {
                Image thumbnail = generateThumbnailForPage(i);
                thumbnailCache.put(i, thumbnail);
            } catch (Exception e) {
                // TODO : Replace with proper logging
                System.err.println("Error generating thumbnail for page " + (i + 1) + ": " + e.getMessage());
                thumbnailCache.put(i, createErrorPlaceholder());
            }
        }

        thumbnailListView.refresh();
    }

    public Image generateThumbnailForPage(int pageIndex) {
        PDFDocument currentDoc = pdfController.getCurrentDocument();
        if (currentDoc == null || pageIndex >= currentDoc.getPages().size()) {
            return createErrorPlaceholder();
        }

        Page page = currentDoc.getPages().get(pageIndex);

        try {
            byte[] pageContent = page.getContent();
            if (pageContent != null) {
                Image fullImage = new Image(new ByteArrayInputStream(pageContent));

                return createScaledImage(fullImage);
            }
        } catch (Exception e) {
            // TODO : Replace with proper logging
            System.err.println("Error creating thumbnail: " + e.getMessage());
        }

        return createErrorPlaceholder();
    }

    private Image createScaledImage(Image originalImage) {
        double originalWidth = originalImage.getWidth();
        double originalHeight = originalImage.getHeight();

        double scaleX = THUMBNAIL_WIDTH / originalWidth;
        double scaleY = THUMBNAIL_HEIGHT / originalHeight;
        double scale = Math.min(scaleX, scaleY);

        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);

        javafx.scene.image.WritableImage scaledImage = new javafx.scene.image.WritableImage(
                THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        javafx.scene.image.PixelReader reader = originalImage.getPixelReader();
        javafx.scene.image.PixelWriter writer = scaledImage.getPixelWriter();

        for (int y = 0; y < THUMBNAIL_HEIGHT; y++) {
            for (int x = 0; x < THUMBNAIL_WIDTH; x++) {
                writer.setColor(x, y, javafx.scene.paint.Color.WHITE);
            }
        }

        int offsetX = (THUMBNAIL_WIDTH - scaledWidth) / 2;
        int offsetY = (THUMBNAIL_HEIGHT - scaledHeight) / 2;

        for (int y = 0; y < scaledHeight; y++) {
            for (int x = 0; x < scaledWidth; x++) {
                int sourceX = (int) (x / scale);
                int sourceY = (int) (y / scale);

                if (sourceX < originalWidth && sourceY < originalHeight) {
                    javafx.scene.paint.Color color = reader.getColor(sourceX, sourceY);
                    writer.setColor(x + offsetX, y + offsetY, color);
                }
            }
        }

        return scaledImage;
    }

    private Image createErrorPlaceholder() {
        try {
            return new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/placeholder-thumbnail.png")));
        } catch (Exception e) {
            return createProgrammaticPlaceholder();
        }
    }

    private Image createProgrammaticPlaceholder() {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(javafx.scene.paint.Color.LIGHTGRAY);
        gc.fillRect(0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        gc.setFill(javafx.scene.paint.Color.DARKGRAY);
        gc.fillText("PDF", (double) THUMBNAIL_WIDTH / 2 - 10, (double) THUMBNAIL_HEIGHT / 2);

        return canvas.snapshot(null, null);
    }

    private void navigateToPage(int pageIndex) {
        currentPageIndex = pageIndex;
        displayCurrentPage();
        updatePageInfo();
    }

    private void handleScrollZoom(ScrollEvent event) {
        if (pdfController.getCurrentDocument() != null) {
            if (event.isControlDown()) {
                // Zoom with Ctrl+Scroll
                if (event.getDeltaY() > 0) {
                    zoomIn();
                } else {
                    zoomOut();
                }
                event.consume();
            } else {
                if (event.getDeltaY() > 0) {
                    goToPreviousPage();
                } else {
                    goToNextPage();
                }
                event.consume();
            }
        }
    }

    private void handleZoomGesture(ZoomEvent event) {
        if (pdfController.getCurrentDocument() != null) {
            double zoomFactor = event.getZoomFactor();
            double newZoomLevel = zoomLevel * zoomFactor;

            newZoomLevel = Math.clamp(newZoomLevel, MIN_ZOOM, MAX_ZOOM);

            if (newZoomLevel != zoomLevel) {
                double pivotX = event.getX();
                double pivotY = event.getY();

                double imageX = (pivotX - pdfImageView.getTranslateX()) / zoomLevel;
                double imageY = (pivotY - pdfImageView.getTranslateY()) / zoomLevel;

                zoomLevel = newZoomLevel;

                applyZoom();

                double newTranslateX = pivotX - (imageX * zoomLevel);
                double newTranslateY = pivotY - (imageY * zoomLevel);

                pdfImageView.setTranslateX(newTranslateX);
                pdfImageView.setTranslateY(newTranslateY);
            }

            event.consume();
        }
    }

    private void zoomIn() {
        if (zoomLevel < MAX_ZOOM) {
            zoomLevel = Math.min(MAX_ZOOM, zoomLevel + ZOOM_STEP);
            applyZoom();
        }
    }

    private void zoomOut() {
        if (zoomLevel > MIN_ZOOM) {
            zoomLevel = Math.max(MIN_ZOOM, zoomLevel - ZOOM_STEP);
            applyZoom();
        }
    }

    private void resetZoom() {
        zoomLevel = 1.0;
        applyZoom();
    }

    private void applyZoom() {
        if (pdfImageView.getImage() != null) {
            double baseWidth = 350;
            double baseHeight = 450;

            PDFDocument currentDoc = pdfController.getCurrentDocument();
            if (currentDoc != null && !currentDoc.getPages().isEmpty()) {
                Page currentPage = currentDoc.getPages().get(currentPageIndex);
                int rotation = currentPage.getRotationAngle();

                if (rotation == 90 || rotation == 270) {
                    baseWidth = 450;
                    baseHeight = 350;
                }
            }

            pdfImageView.setFitWidth(baseWidth * zoomLevel);
            pdfImageView.setFitHeight(baseHeight * zoomLevel);

            Rectangle clip = new Rectangle();
            clip.setWidth(pdfPreviewPane.getWidth());
            clip.setHeight(pdfPreviewPane.getHeight());
            pdfImageView.setClip(clip);

            updatePageInfo();
        }
    }

    private void updatePageInfo() {
        PDFDocument currentDoc = pdfController.getCurrentDocument();
        if (currentDoc != null && !currentDoc.getPages().isEmpty()) {
            Page currentPage = currentDoc.getPages().get(currentPageIndex);
            String rotationInfo = currentPage.getRotationAngle() > 0
                    ? String.format(" (Rotated %d°)", currentPage.getRotationAngle())
                    : "";
            String zoomInfo = zoomLevel != 1.0
                    ? String.format(" - Zoom: %.0f%%", zoomLevel * 100)
                    : "";

            pageInfoLabel.setText(String.format("Page %d of %d%s%s",
                    currentPageIndex + 1, currentDoc.getPages().size(), rotationInfo, zoomInfo));
        }
    }

    private void goToNextPage() {
        PDFDocument currentDoc = pdfController.getCurrentDocument();
        if (currentDoc != null && currentPageIndex < currentDoc.getPages().size() - 1) {
            currentPageIndex++;
            displayCurrentPage();
        }
    }

    private void goToPreviousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            displayCurrentPage();
        }
    }

    private void openPdfFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try {
                pdfController.handleOpenPDF(file.getAbsolutePath());
                currentPageIndex = 0;
                displayCurrentPage();
                loadPDFThumbnails(pdfController.getCurrentDocument());
            } catch (Exception e) {
                showAlert("Error", "Failed to open PDF: " + e.getMessage());
            }
        }
    }

    private void displayCurrentPage() {
        PDFDocument currentDoc = pdfController.getCurrentDocument();
        if (currentDoc != null && !currentDoc.getPages().isEmpty()) {
            Page currentPage = currentDoc.getPages().get(currentPageIndex);
            byte[] pageContent = currentPage.getContent();

            if (pageContent != null) {
                try {
                    Image image = new Image(new ByteArrayInputStream(pageContent));
                    pdfImageView.setImage(image);

                    adaptViewToRotation(currentPage);
                    
                    pdfImageView.setVisible(true);
                    noPdfLabel.setVisible(false);

                    updatePageInfo();
                    pageInfoLabel.setVisible(true);
                } catch (Exception e) {
                    showAlert("Error", "Failed to display PDF page: " + e.getMessage());
                }
            }
        }
    }

    private void savePdfFile() {
        PDFDocument currentDoc = pdfController.getCurrentDocument();
        if (currentDoc == null) {
            showAlert("No PDF", "Please open a PDF file first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        if (currentDoc.getName() != null) {
            fileChooser.setInitialFileName(currentDoc.getName());
        }

        File file = fileChooser.showSaveDialog(getStage());
        if (file != null) {
            try {
                pdfController.handleSavePDF(file.getAbsolutePath());
                showAlert("Success", "PDF saved successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to save PDF: " + e.getMessage());
            }
        }
    }

    private void exportPdfFile() {
        // TODO: Implement PDF export logic
        showAlert("Export PDF", "Export PDF functionality not yet implemented.");
    }

    private void rotatePage() {
        PDFDocument currentDoc = pdfController.getCurrentDocument();
        if (currentDoc != null && !currentDoc.getPages().isEmpty()) {
            Page currentPage = currentDoc.getPages().get(currentPageIndex);
            currentPage.setRotationAngle(90);

            adaptViewToRotation(currentPage);

            pageInfoLabel.setText(String.format("Page %d of %d (Rotated %d°)",
                    currentPageIndex + 1, currentDoc.getPages().size(), currentPage.getRotationAngle()));
        } else {
            showAlert("No PDF", "Please open a PDF file first.");
        }
    }

    private void adaptViewToRotation(Page page) {
        int rotation = page.getRotationAngle();
        pdfImageView.setRotate(rotation);

        applyZoom();

        if (rotation == 90 || rotation == 270) {
            pdfImageView.setFitWidth(450);
            pdfImageView.setFitHeight(350);
        } else {
            pdfImageView.setFitWidth(350);
            pdfImageView.setFitHeight(450);
        }

        adaptPreviewPaneSize(rotation);
    }

    private void adaptPreviewPaneSize(int rotation) {
        if (rotation == 90 || rotation == 270) {
            pdfPreviewPane.setPrefWidth(500);
            pdfPreviewPane.setPrefHeight(400);
        } else {
            pdfPreviewPane.setPrefWidth(400);
            pdfPreviewPane.setPrefHeight(500);
        }

        adaptWindowToContent();
    }

    private void adaptWindowToContent() {
        Stage stage = getStage();
        if (stage != null) {
            stage.sizeToScene();

            stage.centerOnScreen();
        }
    }

    private void annotatePdf() {
        // TODO: Open annotation dialog or enable annotation mode
        showAlert("Annotate PDF", "Annotate PDF functionality not yet implemented.");
    }

    private void signPdf() {
        // TODO: Implement signing logic
        showAlert("Sign PDF", "Sign PDF functionality not yet implemented.");
    }

    private Stage getStage() {
        return (Stage) pdfPreviewPane.getScene().getWindow();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public Map<Integer, Image> getThumbnailCache() {
        return thumbnailCache;
    }
}