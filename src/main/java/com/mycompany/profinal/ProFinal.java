package com.mycompany.profinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProFinal extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Al llevar "/" busca V.1.fxml en la raíz de src/main/resources
        Parent root = FXMLLoader.load(getClass().getResource("/V.1.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("NONAME");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}