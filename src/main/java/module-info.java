module org.openjfx.ballfx {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens org.openjfx.ballfx to javafx.fxml;
    exports org.openjfx.ballfx;
}