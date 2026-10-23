import java.util.Random;

public class Tablero {
    public static final int TAMANO = 10;
    private Ejercito ejercitoUno;
    private Ejercito ejercitoDos;
    private Random aleatorio;

    public Tablero(Ejercito ejercitoUno, Ejercito ejercitoDos) {
        this.ejercitoUno = ejercitoUno;
        this.ejercitoDos = ejercitoDos;
        this.aleatorio = new Random();
    }

    public boolean estaDentro(int fila, int columna) {
        return fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO;
    }

    public boolean estaOcupada(int fila, int columna) {
        return ejercitoUno.buscarEn(fila, columna) != null || ejercitoDos.buscarEn(fila, columna) != null;
    }

    // Asigna al soldado una casilla libre al azar (no puede haber 2 soldados en el mismo cuadrado)
    public void ubicarAleatorio(Soldado soldado) {
        int fila = aleatorio.nextInt(TAMANO);
        int columna = aleatorio.nextInt(TAMANO);
        while (estaOcupada(fila, columna)) {
            fila = aleatorio.nextInt(TAMANO);
            columna = aleatorio.nextInt(TAMANO);
        }
        soldado.setPosicion(fila, columna);
    }

    public void poblar(Ejercito ejercito) {
        int cantidad = aleatorio.nextInt(10) + 1;
        int indice = 0;
        while (indice < cantidad) {
            String nombre = "Soldado" + indice + "X" + ejercito.getNumero();
            int vida = aleatorio.nextInt(5) + 1;
            int ataque = aleatorio.nextInt(5) + 1;
            int defensa = aleatorio.nextInt(5) + 1;
            Soldado soldado = new Soldado(nombre, ejercito.getNumero(), 0, 0, vida, ataque, defensa);
            ubicarAleatorio(soldado);
            ejercito.agregarSoldado(soldado);
            indice = indice + 1;
        }
    }

    private String contenidoCelda(int fila, int columna) {
        Soldado soldadoUno = ejercitoUno.buscarEn(fila, columna);
        if (soldadoUno != null) {
            return "" + ejercitoUno.getSimbolo() + String.format("%2d", soldadoUno.getVidaActual());
        }
        Soldado soldadoDos = ejercitoDos.buscarEn(fila, columna);
        if (soldadoDos != null) {
            return "" + ejercitoDos.getSimbolo() + String.format("%2d", soldadoDos.getVidaActual());
        }
        return "___";
    }

    public void mostrar() {
        String encabezado = "   ";
        char letra = 'A';
        int columna = 0;
        while (columna < TAMANO) {
            encabezado = encabezado + " " + letra + "  ";
            letra = (char) (letra + 1);
            columna = columna + 1;
        }
        System.out.println();
        System.out.println(encabezado);
        int fila = 0;
        while (fila < TAMANO) {
            String linea = String.format("%2d ", fila + 1);
            columna = 0;
            while (columna < TAMANO) {
                linea = linea + "|" + contenidoCelda(fila, columna);
                columna = columna + 1;
            }
            System.out.println(linea + "|");
            fila = fila + 1;
        }
        System.out.println("Ejército 1 = " + ejercitoUno.getSimbolo() + " | Ejército 2 = " + ejercitoDos.getSimbolo()
                + " | El número es la vida actual");
    }
}