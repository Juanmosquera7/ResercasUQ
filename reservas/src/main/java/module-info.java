module co.edu.uniquindio.reservasuq {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;
    requires org.simplejavamail.core;
    requires org.simplejavamail;

    // Exposición de los paquetes necesarios
    opens co.edu.uniquindio.reservasuq to javafx.fxml;
    exports co.edu.uniquindio.reservasuq;
    exports co.edu.uniquindio.reservasuq.controladores;
    opens co.edu.uniquindio.reservasuq.controladores to javafx.fxml;
    exports co.edu.uniquindio.reservasuq.modelo;
    opens co.edu.uniquindio.reservasuq.modelo to javafx.fxml;
}
