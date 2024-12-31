module ru.rsreu.javafxfirsttry {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens ru.rsreu.javafxfirsttry to javafx.fxml;
    exports ru.rsreu.javafxfirsttry;
}