package com.mycompany.profinal;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

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


public class treeController implements Initializable {

    @FXML
    private Button btnVolver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    private static class NodoTree {
        int valor; 
        NodoTree izq, der;
        NodoTree(int item) { valor = item; izq = der = null; }
    }
    @FXML
    void onActionPrueba(ActionEvent event) {
        int[] arr = {50, 30, 70, 20, 40, 90, 80};
        StringBuilder sb = new StringBuilder();
        sb.append("ESTADO INICIAL: ").append(Arrays.toString(arr)).append("\n\n");
        sb.append("1. CONSTRUYENDO EL ÁRBOL BINARIO...\n");

        NodoTree raiz = new NodoTree(arr[0]);
        sb.append("   -> Raíz creada con el valor: ").append(arr[0]).append("\n");
        
        for (int i = 1; i < arr.length; i++) {
            insertarSimulado(raiz, arr[i], sb);
        }

        sb.append("\n2. EXTRAYENDO DATOS (Recorrido Inorden de Izq a Der):\n   ");
        int[] index = {0};
        inordenSimulado(raiz, arr, index, sb);
        
        sb.append("\n\nESTADO FINAL: ").append(Arrays.toString(arr));
        
        mostrarAlertaPasoAPaso("Paso a paso: Tree Sort", sb.toString());
    }

    private void insertarSimulado(NodoTree nodo, int valor, StringBuilder sb) {
        while (true) {
            if (valor < nodo.valor) {
                if (nodo.izq == null) {
                    nodo.izq = new NodoTree(valor);
                    sb.append("   -> ").append(valor).append(" insertado a la IZQUIERDA de ").append(nodo.valor).append("\n");
                    break;
                } else {
                    nodo = nodo.izq;
                }
            } else {
                if (nodo.der == null) {
                    nodo.der = new NodoTree(valor);
                    sb.append("   -> ").append(valor).append(" insertado a la DERECHA de ").append(nodo.valor).append("\n");
                    break;
                } else {
                    nodo = nodo.der;
                }
            }
        }
    }

    private void inordenSimulado(NodoTree nodo, int[] arr, int[] index, StringBuilder sb) {
        if (nodo != null) {
            inordenSimulado(nodo.izq, arr, index, sb);
            arr[index[0]++] = nodo.valor;
            sb.append("[").append(nodo.valor).append("] ");
            inordenSimulado(nodo.der, arr, index, sb);
        }
    }
    

    @FXML
    void onActionVolver(ActionEvent event) {
        
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
