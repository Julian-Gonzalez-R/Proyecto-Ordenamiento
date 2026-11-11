/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.profinal;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
    import javafx.scene.control.TextArea;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.Priority;

/**
 * FXML Controller class
 *
 * @author frepl
 */
public class radixController implements Initializable {

    @FXML
    private Button btnVolver;
    

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    @FXML
    
    void onActionPrueba(ActionEvent event) {
        int[] arr = {170, 45, 75, 90, 802, 24, 2, 66};
        StringBuilder sb = new StringBuilder();
        sb.append("ESTADO INICIAL: ").append(Arrays.toString(arr)).append("\n\n");

        int max = Arrays.stream(arr).max().getAsInt();
        int paso = 1;

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortSimulado(arr, exp);
            String digitoNombre = (exp == 1) ? "Unidades" : (exp == 10) ? "Decenas" : (exp == 100) ? "Centenas" : "Miles";
            sb.append("Paso ").append(paso++).append(" (Ordenando por ").append(digitoNombre).append("): \n")
              .append(Arrays.toString(arr)).append("\n\n");
        }
        sb.append("¡ORDENAMIENTO COMPLETADO!");
        
        mostrarAlertaPasoAPaso("Paso a paso: Radix Sort", sb.toString());
    }
    private void countingSortSimulado(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int i = 0; i < n; i++) count[(arr[i] / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        System.arraycopy(output, 0, arr, 0, n);
    }
    
    @FXML
    private void onActionVolver(ActionEvent event) {
        
        try {
            Parent vistaPrincipal = FXMLLoader.load(getClass().getResource("/V.1.fxml"));
           
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       
            Scene scene = new Scene(vistaPrincipal);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al regresar a la vista principal R:");
            e.printStackTrace();
        }
        
    }
    private void mostrarAlertaPasoAPaso(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Traza de Ejecución");
        alerta.setHeaderText(titulo);

        // Crear un TextArea para que el texto sea scrolleable y no se desborde
        TextArea textArea = new TextArea(contenido);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        
        // Configurar para que ocupe buen espacio
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        
        alerta.getDialogPane().setContent(textArea);
        alerta.showAndWait();
    }
    
}
