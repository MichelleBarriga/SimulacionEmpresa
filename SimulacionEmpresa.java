import java.util.Random;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class SimulacionEmpresa {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el número de iteraciones que desea: \n");

            int numIteraciones = scanner.nextInt();
            double[] beneficios = new double[numIteraciones];

            for (int i = 0; i < numIteraciones; i++) {
                double precio_unidad = generarDistribucionNormal(100, 20);
                double coste_fijo = generarDistribucionUniforme(120000, 160000);
                double coste_unitario = generarDistribucionTriangular(50, 70, 60);
                double cantidad_producida = generarDistribucionLognormal(10000, 1.2);
                double cantidad_vendida = calcularCantidadVendida(cantidad_producida);
                double ingresos = precio_unidad * cantidad_vendida;
                double costes_variables = coste_unitario * cantidad_producida;
                double beneficio = ingresos - (coste_fijo + costes_variables);
                System.out.println("\n\nIteracion: " + i + "\nBeneficio: " + beneficio);
                beneficios[i] = beneficio;
            }
            
            System.out.println("\nGráfico de beneficios vs iteraciones:");
            imprimirGrafico(beneficios);

            System.out.println("\nIngrese la opción que desea: \n 1: Grafico de barras\n 2: Grafico Lineal \n otro: salir");
            int opcion = scanner.nextInt();
            if(opcion == 1){
                // Crear y configurar el conjunto de datos
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                for (int i = 0; i < numIteraciones; i++) {
                    dataset.addValue(beneficios[i], "Beneficio", String.valueOf(i + 1));
                }

                // Crear el gráfico
                JFreeChart chart = ChartFactory.createBarChart(
                        "Beneficios vs. Iteraciones",
                        "Iteraciones",
                        "Beneficio",
                        dataset
                );

                // Mostrar el gráfico en una ventana
                JFrame frame = new JFrame("Gráfico de Beneficios vs. Iteraciones");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new ChartPanel(chart));
                frame.pack();
                frame.setVisible(true);
            }else if(opcion == 2){
                // Crear y configurar la serie de datos
                XYSeries serie = new XYSeries("Beneficios vs. Iteraciones");
                for (int i = 0; i < numIteraciones; i++) {
                    serie.add(i + 1, beneficios[i]);
                }

                // Crear y configurar el conjunto de datos
                XYSeriesCollection dataset = new XYSeriesCollection();
                dataset.addSeries(serie);

                // Crear el gráfico
                JFreeChart chart = ChartFactory.createXYLineChart(
                        "Beneficios vs. Iteraciones",
                        "Iteraciones",
                        "Beneficio",
                        dataset
                );

                // Mostrar el gráfico en una ventana
                JFrame frame = new JFrame("Gráfico de Beneficios vs. Iteraciones");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new ChartPanel(chart));
                frame.pack();
                frame.setVisible(true);
            }else{
                scanner.close();
            }
            
        }
    }

    public static double calcularCantidadVendida(double cantidad_producida) {
        return cantidad_producida * generarDistribucionUniforme(0.9, 1.0);
    }

    // Funciones para generar valores aleatorios según distribuciones específicas
    public static double generarDistribucionNormal(double media, double desviacionEstandar) {
        Random rand = new Random();
        double u1, u2;
        do {
            u1 = rand.nextDouble();
            u2 = rand.nextDouble();
        } while (u1 <= 0); // Asegurar que u1 no sea cero para evitar el logaritmo de cero

        double z = Math.sqrt(-2 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
        return media + (desviacionEstandar * z);
    }

    public static double generarDistribucionUniforme(double minimo, double maximo) {
        Random rand = new Random();
        return minimo + (maximo - minimo) * rand.nextDouble();
    }

    public static double generarDistribucionTriangular(double minimo, double maximo, double moda) {
        Random rand = new Random();
        double u = rand.nextDouble();
        double F = (moda - minimo) / (maximo - minimo);
        if (u <= F) {
            return minimo + Math.sqrt(u * (maximo - minimo) * (moda - minimo));
        } else {
            return maximo - Math.sqrt((1 - u) * (maximo - minimo) * (maximo - moda));
        }
    }

    public static double generarDistribucionLognormal(double media, double desviacionEstandar) {
        Random rand = new Random();
        double u = rand.nextDouble();
        double v = rand.nextDouble();
        double z = Math.sqrt(-2 * Math.log(u)) * Math.cos(2 * Math.PI * v);
        double lognormal = Math.exp(Math.log(media) + (desviacionEstandar * z));
        return lognormal;
    }

    public static void imprimirGrafico(double[] beneficios) {
        double maxBeneficio = encontrarMaximo(beneficios);

        for (int i = 0; i < beneficios.length; i++) {
            int barraLength = (int) (beneficios[i] / maxBeneficio * 50);
            System.out.printf("Iteración %d: ", i + 1);
            for (int j = 0; j < barraLength; j++) {
                System.out.print("*");
            }
            for(int l = 0; l < (50 - barraLength); l++){
                System.out.print(" ");
            }
            System.out.print("(" + beneficios[i] + ")");
            System.out.println();
        }
    }
    
    public static double encontrarMaximo(double[] arr) {
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
    
}
