import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BombermanGame {
    // Constantes
    // Definimos el valor mínimo y máximo que pueden tener los valores en la matriz de juego.
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;

    // Variables del programa
    // Creamos una matriz 'gameBoard' que representará el campo de juego.
    // 'explosionScores' almacenará las puntuaciones de las explosiones realizadas.
    private static int[][] gameBoard;
    private static ArrayList<Integer> explosionScores = new ArrayList<>(); // Para almacenar las puntuaciones de explosión

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Se piden las dimensiones del campo de juego al usuario (filas y columnas).
        // 'getValidDimension' asegura que el valor introducido sea positivo.
        int rows = getValidDimension(scanner, "filas");
        int columns = getValidDimension(scanner, "columnas");


        // Inicializamos la matriz de juego con valores aleatorios entre MIN_VALUE y MAX_VALUE.
        gameBoard = generateRandomMatrix(rows, columns);

        // Ciclo principal del juego donde se presentan las opciones del menú y se gestionan las acciones.
        while (true) {
            showMenu();
            int option = getIntInput(scanner, "Selecciona una opción del menú: ");
            switch (option) {
                // Opción para salir del juego
                case 0:
                    System.out.println("Saliendo del juego...");
                    return;
                // Opción para mostrar la matriz del juego
                case 1:
                    displayMatrix();
                    break;
                // Opción para colocar una bomba
                case 2:
                    placeBomb(scanner);
                    break;
                // Opción para mostrar el ranking de explosiones
                case 3:
                    showRanking();
                    break;
                // Opción no válida
                default:
                    System.out.println("Opción no valida. Intentalo de nuevo.");
            }
        }
    }

    // Método para obtener una dimensión válida (filas o columnas) del usuario
    private static int getValidDimension(Scanner scanner, String dimensionName) {
        int dimension;
        do {
            // Llamamos a 'getIntInput' para pedir un número entero al usuario con el mensaje correspondiente.
            dimension = getIntInput(scanner, "Inserte el número de " + dimensionName + ": ");
            // Comprobamos que la dimensión sea positiva
            if (dimension <= 0) {
                System.out.println("Valor incorrecto. Debe ser un número positivo.");
            }
        } while (dimension <= 0);
        // Continuamos pidiendo la dimensión hasta que sea válida
        return dimension;
    }

    // Método para generar una matriz con valores aleatorios entre MIN_VALUE y MAX_VALUE
    private static int[][] generateRandomMatrix(int rows, int columns) {
        int[][] matrix = new int[rows][columns];
        // Creamos una matriz de tamaño 'rows' x 'columns'
        Random random = new Random();
        // Instanciamos un objeto Random para generar valores aleatorios
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Asignamos un valor aleatorio entre MIN_VALUE y MAX_VALUE a cada celda de la matriz
                matrix[i][j] = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
            }
        }
        // Volvemos a la matriz generada
        return matrix;
    }

    // Método para mostrar el menú de opciones
    private static void showMenu() {
        System.out.println("\n[2] Poner bomba");
        System.out.println("[1] Mostrar matriz");
        System.out.println("[0] Salir");
        System.out.println("[3] Ranking de explosiones");
    }

    // Método para mostrar la matriz del juego en consola
    private static void displayMatrix() {
        System.out.println("Estado actual de la matriz:");
        // Recorremos la matriz y mostramos cada fila en consola
        for (int[] row : gameBoard) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    // Método para colocar una bomba en una posición específica de la matriz
    private static void placeBomb(Scanner scanner) {
        // Solicitamos las coordenadas de la bomba al usuario
        int x = getIntInput(scanner, "Ingrese la coordenada X de la bomba : ");
        int y = getIntInput(scanner, "Ingrese la coordenada Y de la bomba : ");

        // Ajustamos las coordenadas restando 1, ya que las matrices en Java usan índices de 0
        x -= 1;
        y -= 1;

        // Verificamos si las coordenadas son válidas dentro de los límites de la matriz
        if (x < 0 || x >= gameBoard.length || y < 0 || y >= gameBoard[0].length) {
            System.out.println("Coordenadas fuera de rango. Regresando al menú.");
            // Si las coordenadas son inválidas, salimos del método
            return;
        }

        // Calculamos el puntaje de la explosión y lo sumamos al ranking de explosiones
        int explosionScore = calculateExplosion(x, y);
        explosionScores.add(explosionScore);
        System.out.println("Explosión en (" + (x + 1) + ", " + (y + 1) + ") con valor: " + explosionScore);

        // Verificamos si la matriz está completamente despejada (todos los valores son 0)
        if (isBoardCleared()) {
            System.out.println("¡Felicidades! Todos los valores han sido eliminados. Fin del juego.");
            // Finalizamos el programa si el jugador ha ganado
            System.exit(0);
        }
    }


    // Método para calcular el puntaje de la explosión en una celda (y sus filas y columnas)
    private static int calculateExplosion(int x, int y) {
        int explosionScore = 0;

        // Sumamos todos los valores de la fila 'x' y luego los ponemos a 0 (destruidos por la explosión)
        for (int i = 0; i < gameBoard[0].length; i++) {
            explosionScore += gameBoard[x][i];
            gameBoard[x][i] = 0;
        }

        // Sumamos todos los valores de la columna 'y' y los ponemos a 0
        for (int i = 0; i < gameBoard.length; i++) {
            explosionScore += gameBoard[i][y];
            gameBoard[i][y] = 0;
        }
        // Mostramos en consola el puntaje de la explosión
        return explosionScore;
    }

    // Método para verificar si todos los valores de la matriz son 0 (si el juego ha terminado)
    private static boolean isBoardCleared() {
        for (int[] row : gameBoard) {
            for (int cell : row) {
                // Si encontramos un valor distinto de 0, la matriz no está completamente destruida
                if (cell != 0) {
                    return false;
                }
            }
        }
        // Si todos los valores son 0, la partida ha terminado
        return true;
    }

    // Método para mostrar el ranking de las explosiones realizadas durante la partida
    private static void showRanking() {
        System.out.println("Ranking de explosiones:");
        // Mostramos todas las puntuaciones de las explosiones
        for (int i = 0; i < explosionScores.size(); i++) {
            System.out.println("Explosión " + (i + 1) + ": " + explosionScores.get(i));
        }
    }

    // Método para obtener un número entero válido desde la entrada del usuario
    private static int getIntInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            // Mostramos el mensaje que indica lo que debe ingresar el usuario
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                // Si la entrada es un número entero, lo leemos
                value = scanner.nextInt();
                // Limpiamos el buffer de entrada para evitar problemas posteriores
                scanner.nextLine();
                return value;
            } else {
                System.out.println("Numero no valido. Intentalo de nuevo.");
                // Limpiamos el buffer en caso de que la entrada no sea válida
                scanner.nextLine();
            }
        }
    }
}