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


public class seleccionController implements Initializable {

    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    @FXML
    void onActionPrueba(ActionEvent event) {
        int[] arr = {64, 25, 12, 22, 11};
        StringBuilder sb = new StringBuilder();
        sb.append("ESTADO INICIAL: ").append(Arrays.toString(arr)).append("\n\n");

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) minIdx = j;
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
            
            sb.append("Paso ").append(i + 1).append(": ").append(Arrays.toString(arr)).append("\n");
        }
        sb.append("\n¡ORDENAMIENTO COMPLETADO!");
        
        // AQUÍ LLAMAS A LA ALERTA EN LUGAR DEL TEXTAREA FIJO:
        mostrarAlertaPasoAPaso("Paso a paso: Selección Sort", sb.toString());
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

    @FXML
    void onActionVolver(ActionEvent event) {
          try {
             Parent vistaPrincipal = FXMLLoader.load(getClass().getResource("/V.1.fxml"));
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            Scene scene = new Scene(vistaPrincipal);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al regresar a la vista principal S:");
            e.printStackTrace();
        }
    }
    
}
