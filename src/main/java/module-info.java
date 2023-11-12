module uk.adamcz.patryk.rezerwacja {
    requires javafx.controls;
    requires javafx.fxml;


    opens uk.adamcz.patryk.rezerwacja to javafx.fxml;
    exports uk.adamcz.patryk.rezerwacja;
}