module tech.octopusdragon.intervaltimer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens tech.octopusdragon.intervaltimer to javafx.fxml;
    opens tech.octopusdragon.intervaltimer.application.scenes to javafx.fxml;
    opens tech.octopusdragon.intervaltimer.application.controls to javafx.fxml;
    opens tech.octopusdragon.intervaltimer.application.windows to javafx.fxml;
    exports tech.octopusdragon.intervaltimer;
    exports tech.octopusdragon.intervaltimer.application;
}