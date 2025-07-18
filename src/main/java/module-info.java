module com.coffeepdf.coffeepdf {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.apache.pdfbox;
    requires org.slf4j;
    requires java.desktop;

    opens com.coffeepdf.coffeepdf to javafx.fxml, javafx.graphics;
    opens com.coffeepdf.coffeepdf.presentation to javafx.fxml;
    exports com.coffeepdf.coffeepdf;
}