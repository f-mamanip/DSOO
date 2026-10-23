import java.util.ArrayList;

public class Ejercito {
    public static final int MAXIMO = 10;

    private int numero;
    private char simbolo;
    private ArrayList<Soldado> soldados;

    public Ejercito(int numero, char simbolo) {
        this.numero = numero;
        this.simbolo = simbolo;
        this.soldados = new ArrayList<Soldado>();
    }

    public int getNumero() {
        return numero;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public int cantidad() {
        return soldados.size();
    }

    public boolean estaLleno() {
        return soldados.size() >= MAXIMO;
    }

    public boolean estaVacio() {
        return soldados.isEmpty();
    }

    public Soldado getSoldado(int indice) {
        return soldados.get(indice);
    }

    public void agregarSoldado(Soldado soldado) {
        soldados.add(soldado);
    }

    public void eliminarSoldado(Soldado soldado) {
        soldados.remove(soldado);
    }

    public void eliminarPosicion(int indice) {
        soldados.remove(indice);
    }

    public void intercambiarSoldados(int primero, int segundo) {
        intercambiar(soldados, primero, segundo);
    }

    public Soldado buscarEn(int fila, int columna) {
        int indice = 0;
        while (indice < soldados.size()) {
            Soldado soldado = soldados.get(indice);
            if (soldado.getFila() == fila && soldado.getColumna() == columna) {
                return soldado;
            }
            indice = indice + 1;
        }
        return null;
    }

    public ArrayList<Soldado> buscarPorNombre(String nombre) {
        ArrayList<Soldado> encontrados = new ArrayList<Soldado>();
        int indice = 0;
        while (indice < soldados.size()) {
            if (soldados.get(indice).getNombre().equalsIgnoreCase(nombre)) {
                encontrados.add(soldados.get(indice));
            }
            indice = indice + 1;
        }
        return encontrados;
    }

    public Soldado soldadoMayorVida() {
        Soldado mayor = soldados.get(0);
        int indice = 1;
        while (indice < soldados.size()) {
            if (soldados.get(indice).getNivelVida() > mayor.getNivelVida()) {
                mayor = soldados.get(indice);
            }
            indice = indice + 1;
        }
        return mayor;
    }

    public double promedioVida() {
        int suma = 0;
        int indice = 0;
        while (indice < soldados.size()) {
            suma = suma + soldados.get(indice).getNivelVida();
            indice = indice + 1;
        }
        return (double) suma / soldados.size();
    }

    public Soldado sumarNiveles() {
        Soldado total = new Soldado("Total", numero, 0, 0, 0, 0, 0);
        int indice = 0;
        while (indice < soldados.size()) {
            total = total.sumar(soldados.get(indice));
            indice = indice + 1;
        }
        return total;
    }

    private void intercambiar(ArrayList<Soldado> lista, int primero, int segundo) {
        Soldado temporal = lista.get(primero);
        lista.set(primero, lista.get(segundo));
        lista.set(segundo, temporal);
    }

    // Ranking por nivel de vida (mayor a menor) - burbuja
    public ArrayList<Soldado> rankingBurbuja() {
        ArrayList<Soldado> copia = new ArrayList<Soldado>(soldados);
        int limite = copia.size() - 1;
        while (limite > 0) {
            int indice = 0;
            while (indice < limite) {
                if (copia.get(indice).getNivelVida() < copia.get(indice + 1).getNivelVida()) {
                    intercambiar(copia, indice, indice + 1);
                }
                indice = indice + 1;
            }
            limite = limite - 1;
        }
        return copia;
    }

    // Ranking por nivel de vida (mayor a menor) - selección
    public ArrayList<Soldado> rankingSeleccion() {
        ArrayList<Soldado> copia = new ArrayList<Soldado>(soldados);
        int inicio = 0;
        while (inicio < copia.size() - 1) {
            int indiceMayor = inicio;
            int indice = inicio + 1;
            while (indice < copia.size()) {
                if (copia.get(indice).getNivelVida() > copia.get(indiceMayor).getNivelVida()) {
                    indiceMayor = indice;
                }
                indice = indice + 1;
            }
            intercambiar(copia, inicio, indiceMayor);
            inicio = inicio + 1;
        }
        return copia;
    }

    public void mostrarNumerados() {
        int indice = 0;
        while (indice < soldados.size()) {
            System.out.println((indice + 1) + ") " + soldados.get(indice));
            indice = indice + 1;
        }
    }

    private void imprimirLista(String titulo, ArrayList<Soldado> lista) {
        System.out.println("--- " + titulo + " ---");
        int indice = 0;
        while (indice < lista.size()) {
            System.out.println(lista.get(indice));
            indice = indice + 1;
        }
    }

    public void mostrarEstadisticas() {
        System.out.println("========== Ejército " + numero + " (" + simbolo + ") ==========");
        System.out.println("Soldado con mayor vida: " + soldadoMayorVida());
        System.out.println("Promedio de vida: " + String.format("%.2f", promedioVida()));
        imprimirLista("Soldados en orden de creación", soldados);
        imprimirLista("Ranking de poder ", rankingBurbuja());
        imprimirLista("Ranking de poder ", rankingSeleccion());
    }
}