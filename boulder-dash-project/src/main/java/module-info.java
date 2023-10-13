module g.boulder_dash {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.datatransfer;
    requires java.desktop;

    opens g58744.boulder_dash to javafx.fxml;
    exports g58744.boulder_dash;
}