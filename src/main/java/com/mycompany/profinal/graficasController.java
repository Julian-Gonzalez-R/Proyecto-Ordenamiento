package com.mycompany.profinal;

import com.mycompany.profinal.ClasificadorComplejidad.Resultado;
import java.io.IOException;


import java.net.URL;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * FXML Controller class
 *
 * @author frepl
 */
public class graficasController implements Initializable {

    @FXML
    private Button btnVolver;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               

    }    
    @FXML
    private LineChart<String, Number> lineChart;

   public void recibirDatos(String datosRadix, String datosTree, String datosSeleccion, 
                             String compRadix, String compTree, String compSeleccion) {
        
        lineChart.setTitle("Comparativa de Rendimiento");
        lineChart.getXAxis().setLabel("Cantidad de Datos (N)");
        lineChart.getYAxis().setLabel("Tiempo (Nanosegundos)");

        lineChart.getData().clear();

        // Le pasamos la complejidad dinámica al nombre de la serie
        lineChart.getData().add(crearSerie("Radix Sort (" + compRadix + ")", datosRadix));
        lineChart.getData().add(crearSerie("Tree Sort (" + compTree + ")", datosTree));
        lineChart.getData().add(crearSerie("Selección Sort (" + compSeleccion + ")", datosSeleccion));
    }

    // 2. CAMBIO CLAVE: La serie ahora maneja <String, Number>
    private XYChart.Series<String, Number> crearSerie(String nombreAlgoritmo, String datosCsv) {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(nombreAlgoritmo);

        for (String linea : datosCsv.split("\n")) {
            if (linea.trim().isEmpty()) continue;
            
            String[] partes = linea.split(",");
            if (partes.length == 2) {
                // 3. CAMBIO CLAVE: Tomamos la 'N' como texto en lugar de convertirla a entero
                String n = partes[0].trim(); 
                long tiempo = Long.parseLong(partes[1].trim());
                
                // Añadimos el punto a la gráfica
                serie.getData().add(new XYChart.Data<>(n, tiempo));
            }
        }
        return serie;
    }

    @FXML
    private void onActionVolver(ActionEvent event) {
        try {
            // 1. Cargar el FXML de la vista principal desde la raíz de resources
            Parent vistaPrincipal = FXMLLoader.load(getClass().getResource("/V.1.fxml"));
            
            // 2. Obtener el escenario (Stage) actual desde el botón presionado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // 3. Reemplazar la escena actual por la vista principal
            Scene scene = new Scene(vistaPrincipal);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al regresar a la vista principal T:");
            e.printStackTrace();
        }
    }
    
}
