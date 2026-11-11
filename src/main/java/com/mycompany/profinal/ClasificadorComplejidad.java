package com.mycompany.profinal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;


public class ClasificadorComplejidad {

    private static class Candidato {
        final String nombre;
        final DoubleUnaryOperator f;
        Candidato(String nombre, DoubleUnaryOperator f) {
            this.nombre = nombre;
            this.f = f;
        }
    }

    // Ordenadas de la más simple a la más costosa.
    private static final List<Candidato> CANDIDATOS = List.of(
        new Candidato("O(1)",       n -> 1.0),
        new Candidato("O(log n)",   n -> Math.log(n) / Math.log(2)),
        new Candidato("O(n)",       n -> n),
        new Candidato("O(n log n)", n -> n * (Math.log(n) / Math.log(2))),
        new Candidato("O(n^2)",     n -> n * n),
        new Candidato("O(n^3)",     n -> n * n * n),
        new Candidato("O(2^n)",     n -> Math.pow(2, n))
    );

    public static class Resultado {
        public final String mejorModelo;
        public final double mejorR2;
        public final Map<String, Double> r2PorModelo;

        Resultado(String mejorModelo, double mejorR2, Map<String, Double> r2PorModelo) {
            this.mejorModelo = mejorModelo;
            this.mejorR2 = mejorR2;
            this.r2PorModelo = r2PorModelo;
        }
    }

    public static Resultado clasificar(String salidaConsola) {
        List<double[]> puntos = parsearPuntos(salidaConsola);
        if (puntos.size() < 3) {
            return new Resultado("Datos insuficientes para clasificar", 0.0, Map.of());
        }

        double[] ns = new double[puntos.size()];
        double[] ts = new double[puntos.size()];
        for (int i = 0; i < puntos.size(); i++) {
            ns[i] = puntos.get(i)[0];
            ts[i] = puntos.get(i)[1];
        }

        Map<String, Double> r2PorModelo = new LinkedHashMap<>();
        String mejorNombre = null;
        double mejorR2 = -Double.MAX_VALUE;

        for (Candidato candidato : CANDIDATOS) {
            double[] xs = new double[ns.length];
            boolean valido = true;
            for (int i = 0; i < ns.length; i++) {
                double valor = candidato.f.applyAsDouble(ns[i]);
                if (!Double.isFinite(valor)) {
                    valido = false;
                    break;
                }
                xs[i] = valor;
            }
            if (!valido) continue;

            double r2 = calcularR2(xs, ts);
            r2PorModelo.put(candidato.nombre, r2);

            if (r2 > mejorR2) {
                mejorR2 = r2;
                mejorNombre = candidato.nombre;
            }
        }

        if (mejorNombre == null) {
            return new Resultado("No se pudo determinar", 0.0, r2PorModelo);
        }
        return new Resultado(mejorNombre, mejorR2, r2PorModelo);
    }

    private static List<double[]> parsearPuntos(String salidaConsola) {
        List<double[]> puntos = new ArrayList<>();
        if (salidaConsola == null) return puntos;
        for (String linea : salidaConsola.split("\n")) {
            linea = linea.trim();
            if (!linea.contains(",")) continue;
            String[] partes = linea.split(",");
            if (partes.length != 2) continue;
            try {
                double n = Double.parseDouble(partes[0].trim());
                double t = Double.parseDouble(partes[1].trim());
                puntos.add(new double[]{n, t});
            } catch (NumberFormatException ignorado) {
                // línea de depuración del programa del usuario (no es un punto n,tiempo)
            }
        }
        return puntos;
    }

    /** Regresión lineal simple t = a*x + b; devuelve el R² del ajuste. */
    private static double calcularR2(double[] xs, double[] ys) {
        int n = xs.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
        for (int i = 0; i < n; i++) {
            sumX += xs[i];
            sumY += ys[i];
            sumXY += xs[i] * ys[i];
            sumXX += xs[i] * xs[i];
        }
        double mediaX = sumX / n;
        double mediaY = sumY / n;

        double denominador = sumXX - n * mediaX * mediaX;
        double a, b;
        if (Math.abs(denominador) < 1e-12) {
            a = 0;
            b = mediaY;
        } else {
            a = (sumXY - n * mediaX * mediaY) / denominador;
            b = mediaY - a * mediaX;
        }

        double ssRes = 0, ssTot = 0;
        for (int i = 0; i < n; i++) {
            double prediccion = a * xs[i] + b;
            ssRes += Math.pow(ys[i] - prediccion, 2);
            ssTot += Math.pow(ys[i] - mediaY, 2);
        }

        if (ssTot < 1e-12) return 1.0;
        return 1.0 - (ssRes / ssTot);
    }

    public static String formatearResultado(Resultado resultado) {
        StringBuilder sb = new StringBuilder();
        sb.append("Clasificación estimada: ").append(resultado.mejorModelo)
          .append(String.format(" (R² = %.4f)", resultado.mejorR2)).append("\n\n");
        if (!resultado.r2PorModelo.isEmpty()) {
            sb.append("Ajuste por modelo:\n");
            for (Map.Entry<String, Double> entrada : resultado.r2PorModelo.entrySet()) {
                sb.append(String.format("  %-12s R\u00b2 = %.4f%n", entrada.getKey(), entrada.getValue()));
            }
        }
        return sb.toString();
    }
}
