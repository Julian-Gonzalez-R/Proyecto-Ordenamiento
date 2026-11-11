
package com.mycompany.profinal;

import com.mycompany.profinal.ClasificadorComplejidad.Resultado;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class V1Controller implements Initializable {
    @FXML
    private Button iraRadix;
    private Button iraTreeSort;
    private Button iraSelection;
    private Button bRun;
    private final Random random = new Random();
    private static final long PRESUPUESTO_NANOS_POR_N = 100_000_000L; 
    private static final int MAX_REPETICIONES = 5;
    @FXML
    private TextField txtDatos;
    @FXML
    void onActionButtonR(ActionEvent event){
        
        try {            
            
            //pantalla
            Parent nuevaVista = FXMLLoader.load(getClass().getResource("/radix.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(nuevaVista);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla radix.fxml:");
            e.printStackTrace();
        }
        
    }
    @FXML
    void onActionButtonT(ActionEvent event){
        
        try {
            // Carga la nueva pantalla radix.fxml que está en la raíz de resources
            Parent nuevaVista = FXMLLoader.load(getClass().getResource("/tree.fxml"));
            
            // Obtiene la ventana actual a través del evento del botón
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Cambia la escena a la nueva vista
            Scene scene = new Scene(nuevaVista);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla radix.fxml:");
            e.printStackTrace();
        }
        
        
    }
    @FXML
    void onActionButtonS(ActionEvent event) {
                
        try {
            // Carga la nueva pantalla radix.fxml que está en la raíz de resources
            Parent nuevaVista = FXMLLoader.load(getClass().getResource("/seleccion.fxml"));
            
            // Obtiene la ventana actual a través del evento del botón
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Cambia la escena a la nueva vista
            Scene scene = new Scene(nuevaVista);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla radix.fxml:");
            e.printStackTrace();
        }
        
    }

    @FXML
    void onActionButtonbR(ActionEvent event) {  
        try {
            // Leer el límite
            int limiteN = Integer.parseInt(txtDatos.getText().trim());
            
            // 2. CRÍTICO: Cambié 100000 a 1000000 para que coincida con tu alerta
            if (limiteN < 10 || limiteN > 1000000) {
                mostrarAlerta("Límite inválido", "El valor debe estar entre 10 y 1.000.000");
                return; // CRÍTICO: Esto evita que el programa siga si el número es inválido
            }

            System.out.println("Iniciando pruebas hasta N = " + limiteN + "...\n");

            StringBuilder puntosRadix = new StringBuilder();
            StringBuilder puntosTree = new StringBuilder();
            StringBuilder puntosSeleccion = new StringBuilder();

            // Evaluar en 5 fragmentos (ej: si digita 10000, evalúa en 2k, 4k, 6k, 8k, 10k)
            int numPuntos = 5;
            for (int i = 1; i <= numPuntos; i++) {
                int nActual = (limiteN / numPuntos) * i;
                
                // Medición
                puntosRadix.append(nActual).append(",").append(medirAlgoritmo(this::radixSort, nActual)).append("\n");
                puntosTree.append(nActual).append(",").append(medirAlgoritmo(this::treeSort, nActual)).append("\n");
                puntosSeleccion.append(nActual).append(",").append(medirAlgoritmo(this::seleccionSort, nActual)).append("\n");
            }

            // Clasificar usando tu clase ClasificadorComplejidad
            Resultado resRadix = ClasificadorComplejidad.clasificar(puntosRadix.toString());
            Resultado resTree = ClasificadorComplejidad.clasificar(puntosTree.toString());
            Resultado resSeleccion = ClasificadorComplejidad.clasificar(puntosSeleccion.toString());

            // Mostrar resultados en consola
            System.out.println("--- RADIX SORT ---");
            System.out.println(ClasificadorComplejidad.formatearResultado(resRadix));

            System.out.println("--- TREE SORT ---");
            System.out.println(ClasificadorComplejidad.formatearResultado(resTree));

            System.out.println("--- SELECCIÓN SORT ---");
            System.out.println(ClasificadorComplejidad.formatearResultado(resSeleccion));
        
            // Pantalla graficas.fxml
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/graficas.fxml"));
        Parent nuevaVista = loader.load();

        // Obtener el controlador e INYECTAR los datos y las clasificaciones
        graficasController controlador = loader.getController();
        controlador.recibirDatos(
            puntosRadix.toString(), 
            puntosTree.toString(), 
            puntosSeleccion.toString(),
            resRadix.mejorModelo,      // Nivel detectado para Radix
            resTree.mejorModelo,       // Nivel detectado para Tree
            resSeleccion.mejorModelo   // Nivel detectado para Selección
        );

        // Mostrar la ventana
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(nuevaVista);
        stage.setScene(scene);
        stage.show();
        } catch (NumberFormatException e) {
            // 3. CRÍTICO: Atrapa el error si el usuario escribe letras o deja vacío
            mostrarAlerta("Entrada inválida", "Por favor ingresa únicamente números enteros.");
            
        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla graficas.fxml:");
            e.printStackTrace();
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private long medirAlgoritmo(Consumer<int[]> algoritmo, int n) {
        int[] datosCalentamiento = generarArregloAleatorio(n);
        algoritmo.accept(datosCalentamiento);

        long tiempoAcumulado = 0;
        int repeticiones = 0;
        long inicioTotal = System.nanoTime();

        while ((System.nanoTime() - inicioTotal) < PRESUPUESTO_NANOS_POR_N && repeticiones < MAX_REPETICIONES) {
            int[] datosGenerados = generarArregloAleatorio(n);
            long t0Interno = System.nanoTime();
            algoritmo.accept(datosGenerados);
            long t1Interno = System.nanoTime();
            
            tiempoAcumulado += (t1Interno - t0Interno);
            repeticiones++;
        }
        return tiempoAcumulado / Math.max(1, repeticiones);
    }

    private int[] generarArregloAleatorio(int n) {
        int[] arreglo = new int[n];
        for (int i = 0; i < n; i++) {
            arreglo[i] = random.nextInt(100000); 
        }
        return arreglo;
    }
    // ==========================================
    // 5. ALGORITMOS DE ORDENAMIENTO
    // ==========================================

    // --- 1. SELECCIÓN SORT ---
    private void seleccionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    // --- 2. RADIX SORT ---
    private void radixSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortParaRadix(arr, exp);
        }
    }

    private void countingSortParaRadix(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        // Llenar el conteo
        for (int i = 0; i < n; i++) {
            int digito = (arr[i] / exp) % 10;
            count[digito]++;
        }

        // Acumular posiciones
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Construir el arreglo de salida (recorriendo de atrás hacia adelante)
        for (int i = n - 1; i >= 0; i--) {
            int digito = (arr[i] / exp) % 10;
            output[count[digito] - 1] = arr[i];
            count[digito]--;
        }

        // Copiar los datos ordenados al arreglo original
        System.arraycopy(output, 0, arr, 0, n);
    }

    // --- 3. TREE SORT ---
    private static class NodoTree {
        int valor;
        NodoTree izq, der;

        NodoTree(int item) {
            valor = item;
            izq = der = null;
        }
    }

    private void treeSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        NodoTree raiz = new NodoTree(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            insertarEnTree(raiz, arr[i]);
        }
        
        int[] index = {0}; 
        inordenTree(raiz, arr, index);
    }

    private void insertarEnTree(NodoTree nodo, int valor) {
        while (true) {
            if (valor < nodo.valor) {
                if (nodo.izq == null) {
                    nodo.izq = new NodoTree(valor);
                    break;
                } else {
                    nodo = nodo.izq;
                }
            } else {
                if (nodo.der == null) {
                    nodo.der = new NodoTree(valor);
                    break;
                } else {
                    nodo = nodo.der;
                }
            }
        }
    }

    private void inordenTree(NodoTree nodo, int[] arr, int[] index) {
        if (nodo != null) {
            inordenTree(nodo.izq, arr, index);
            arr[index[0]++] = nodo.valor;
            inordenTree(nodo.der, arr, index);
        }
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ts
    }
}
