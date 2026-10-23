import java.util.Random;

public class Juego {
    private static final int TERMINADO = 0;
    private static final int CANCELADO = 1;
    private static final int[] DESPLAZAMIENTO_FILA = {-1, 1, 0, 0, -1, -1, 1, 1};
    private static final int[] DESPLAZAMIENTO_COLUMNA = {0, 0, -1, 1, -1, 1, -1, 1};
    private static final Random ALEATORIO = new Random();

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Juego rápido");
            System.out.println("2. Juego personalizado");
            System.out.println("3. Salir");
            int opcion = Consola.leerEntero("Opción: ", 1, 3);
            if (opcion == 1) {
                juegoRapido();
            } else if (opcion == 2) {
                juegoPersonalizado();
            } else {
                salir = true;
                System.out.println("¡Hasta pronto!");
            }
        }
    }

    private static void mostrarInicio(Tablero tablero, Ejercito uno, Ejercito dos) {
        tablero.mostrar();
        uno.mostrarEstadisticas();
        dos.mostrarEstadisticas();
    }

    private static void juegoRapido() {
        boolean otraVez = true;
        while (otraVez) {
            Ejercito uno = new Ejercito(1, 'A');
            Ejercito dos = new Ejercito(2, 'B');
            Tablero tablero = new Tablero(uno, dos);
            tablero.poblar(uno);
            tablero.poblar(dos);
            mostrarInicio(tablero, uno, dos);
            otraVez = jugarPartida(tablero, uno, dos);
        }
    }

    private static void juegoPersonalizado() {
        Ejercito uno = new Ejercito(1, 'A');
        Ejercito dos = new Ejercito(2, 'B');
        Tablero tablero = new Tablero(uno, dos);
        tablero.poblar(uno);
        tablero.poblar(dos);
        mostrarInicio(tablero, uno, dos);
        boolean jugar = Personalizado.gestionar(uno, dos, tablero);
        if (jugar) {
            tablero.mostrar();
            boolean nuevoJuego = jugarPartida(tablero, uno, dos);
            if (nuevoJuego) {
                juegoRapido();
            }
        }
    }

    // Devuelve true si se desea un juego totalmente nuevo; false para volver al menú principal
    private static boolean jugarPartida(Tablero tablero, Ejercito uno, Ejercito dos) {
        int resultado = partida(tablero, uno, dos);
        if (resultado == CANCELADO) {
            System.out.println("\nJuego cancelado.");
            System.out.println("1. Empezar un juego totalmente nuevo");
            System.out.println("2. Volver al menú principal");
        } else {
            System.out.println("\n1. Volver a jugar");
            System.out.println("2. Volver al menú principal");
        }
        return Consola.leerEntero("Opción: ", 1, 2) == 1;
    }

    private static int partida(Tablero tablero, Ejercito uno, Ejercito dos) {
        boolean turnoDelUno = true;
        while (!uno.estaVacio() && !dos.estaVacio()) {
            boolean cancelado;
            if (turnoDelUno) {
                cancelado = jugarTurno(tablero, uno, dos);
            } else {
                cancelado = jugarTurno(tablero, dos, uno);
            }
            if (cancelado) {
                return CANCELADO;
            }
            tablero.mostrar();
            turnoDelUno = !turnoDelUno;
        }
        if (uno.estaVacio()) {
            System.out.println("¡Gana el Ejército 2!");
        } else {
            System.out.println("¡Gana el Ejército 1!");
        }
        return TERMINADO;
    }

    // Devuelve true si el jugador canceló el juego
    private static boolean jugarTurno(Tablero tablero, Ejercito propio, Ejercito rival) {
        System.out.println("\nTurno del Ejército " + propio.getNumero() + " (" + propio.getSimbolo() + ")  [escriba 0 para cancelar el juego]");
        while (true) {
            String texto = Consola.leerLinea("Coordenada del soldado (ej. B2): ").toUpperCase();
            if (texto.equals("0")) {
                return true;
            }
            int[] posicion = interpretarCoordenada(texto);
            if (posicion == null) {
                System.out.println("Coordenada inválida. Use una letra A-J seguida de un número 1-10 (ej. B2).");
                continue;
            }
            Soldado soldado = propio.buscarEn(posicion[0], posicion[1]);
            if (soldado == null) {
                System.out.println("No hay un soldado de tu ejército en esa posición.");
                continue;
            }
            int direccion = Consola.leerEntero("Dirección (1 Arriba, 2 Abajo, 3 Izquierda, 4 Derecha, 5 Arriba-Izq, 6 Arriba-Der, 7 Abajo-Izq, 8 Abajo-Der, 0 Cancelar): ", 0, 8);
            if (direccion == 0) {
                return true;
            }
            int nuevaFila = posicion[0] + DESPLAZAMIENTO_FILA[direccion - 1];
            int nuevaColumna = posicion[1] + DESPLAZAMIENTO_COLUMNA[direccion - 1];
            if (!tablero.estaDentro(nuevaFila, nuevaColumna)) {
                System.out.println("No puedes moverte fuera del tablero.");
            } else if (propio.buscarEn(nuevaFila, nuevaColumna) != null) {
                System.out.println("Ya hay un soldado de tu ejército en ese cuadrado.");
            } else {
                ejecutarMovimiento(soldado, nuevaFila, nuevaColumna, propio, rival);
                return false;
            }
        }
    }

    private static int[] interpretarCoordenada(String texto) {
        if (texto.length() < 2 || texto.length() > 3) {
            return null;
        }
        char letra = texto.charAt(0);
        if (letra < 'A' || letra >= 'A' + Tablero.TAMANO) {
            return null;
        }
        try {
            int fila = Integer.parseInt(texto.substring(1)) - 1;
            if (fila < 0 || fila >= Tablero.TAMANO) {
                return null;
            }
            return new int[]{fila, letra - 'A'};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void ejecutarMovimiento(Soldado soldado, int nuevaFila, int nuevaColumna, Ejercito propio, Ejercito rival) {
        Soldado soldadoRival = rival.buscarEn(nuevaFila, nuevaColumna);
        if (soldadoRival == null) {
            soldado.setPosicion(nuevaFila, nuevaColumna);
            soldado.avanzar();
        } else {
            resolverBatalla(soldado, soldadoRival, propio, rival);
        }
    }

    // La probabilidad de vencer es proporcional a la vida actual de cada soldado
    private static void resolverBatalla(Soldado atacante, Soldado defensor, Ejercito ejercitoAtacante, Ejercito ejercitoDefensor) {
        int vidaA = atacante.getVidaActual();
        int vidaD = defensor.getVidaActual();
        double probA = 100.0 * vidaA / (vidaA + vidaD);
        double probD = 100.0 - probA;
        double sorteo = ALEATORIO.nextDouble() * 100.0;
        boolean ganaAtacante = sorteo < probA;

        System.out.println("\n¡BATALLA! " + atacante.getNombre() + " (vida " + vidaA + ") vs "
                + defensor.getNombre() + " (vida " + vidaD + ")");
        System.out.println("Probabilidad de vencer: " + atacante.getNombre() + " " + String.format("%.1f", probA) + "%  |  "
                + defensor.getNombre() + " " + String.format("%.1f", probD) + "%");
        System.out.println("Se sortea un número entre 0 y 100: si es menor que " + String.format("%.1f", probA)
                + " gana el atacante, si no gana el defensor. Salió " + String.format("%.2f", sorteo) + ".");

        Soldado ganador;
        Soldado perdedor;
        if (ganaAtacante) {
            ganador = atacante;
            perdedor = defensor;
            atacante.atacar();
            defensor.serAtacado(defensor.getVidaActual());
            ejercitoDefensor.eliminarSoldado(defensor);
            atacante.setPosicion(defensor.getFila(), defensor.getColumna());
        } else {
            ganador = defensor;
            perdedor = atacante;
            defensor.defender();
            atacante.serAtacado(atacante.getVidaActual());
            ejercitoAtacante.eliminarSoldado(atacante);
        }
        ganador.setVidaActual(ganador.getVidaActual() + 1);

        double probGanador = ganaAtacante ? probA : probD;
        String explicacion = "Ganó " + ganador.getNombre() + " porque el número sorteado cayó en su rango de victoria";
        if (probGanador > 50.0) {
            explicacion = explicacion + ", y tenía ventaja por su mayor vida actual.";
        } else if (probGanador < 50.0) {
            explicacion = explicacion + ", venciendo pese a tener menos probabilidad (le favoreció la suerte).";
        } else {
            explicacion = explicacion + " (ambos tenían la misma probabilidad).";
        }
        System.out.println(explicacion);
        System.out.println(perdedor.getNombre() + " desaparece del tablero. " + ganador.getNombre()
                + " ocupa el cuadrado y sube su vida actual a " + ganador.getVidaActual() + ".");
    }
}