package com.coffeepdf.coffeepdf.presentation;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThumbnailCell extends ListCell<Integer> {
    private static final int THUMBNAIL_WIDTH = 80;
    private static final int THUMBNAIL_HEIGHT = 100;

    private final ImageView imageView;
    private final Label pageLabel;
    private final VBox container;

    private final PDFViewController pdfViewController;

    public ThumbnailCell(final PDFViewController pdfViewControllerArg) {
        pdfViewController = pdfViewControllerArg;

        imageView = new ImageView();
        imageView.setFitWidth(THUMBNAIL_WIDTH);
        imageView.setFitHeight(THUMBNAIL_HEIGHT);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        pageLabel = new Label();
        pageLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

        container = new VBox(2);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(imageView, pageLabel);
    }

    @Override
    protected void updateItem(Integer pageIndex, boolean empty) {
        super.updateItem(pageIndex, empty);

        if (empty || pageIndex == null) {
            setGraphic(null);
        } else {
            pageLabel.setText("Page " + (pageIndex + 1));

            Map<Integer, Image> cache = pdfViewController.getThumbnailCache();

            if (cache.containsKey(pageIndex)) {
                imageView.setImage(cache.get(pageIndex));
                setGraphic(container);
            } else {
                pageLabel.setText("Page " + (pageIndex + 1));

                imageView.setImage(createErrorPlaceholder());
                setGraphic(container);

                Platform.runLater(() -> {
                    try {
                        Image thumbnail = pdfViewController.generateThumbnailForPage(pageIndex);
                        cache.put(pageIndex, thumbnail);

                        if (getItem() != null && getItem().equals(pageIndex)) {
                            imageView.setImage(thumbnail);
                        }
                    } catch (Exception e) {
                        Image placeholder = createErrorPlaceholder();
                        cache.put(pageIndex, placeholder);
                        if (getItem() != null && getItem().equals(pageIndex)) {
                            imageView.setImage(placeholder);
                        }
                    }
                });
            }
        }
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
        Canvas canvas = new Canvas(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        gc.setFill(Color.DARKGRAY);
        gc.fillText("PDF", THUMBNAIL_WIDTH/2.0 - 10, THUMBNAIL_HEIGHT/2.0);

        return canvas.snapshot(null, null);
    }
}
