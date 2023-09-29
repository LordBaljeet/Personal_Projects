module me.lordbaljeet.projectchess {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens me.lordbaljeet.projectchess to javafx.fxml;
    exports me.lordbaljeet.projectchess;
    exports me.lordbaljeet.projectchess.module;
    opens me.lordbaljeet.projectchess.module to javafx.fxml;
    exports me.lordbaljeet.projectchess.module.Pieces;
    opens me.lordbaljeet.projectchess.module.Pieces to javafx.fxml;
}