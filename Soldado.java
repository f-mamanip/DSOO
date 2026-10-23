public class Soldado {
    private String nombre;
    private int nivelAtaque;
    private int nivelDefensa;
    private int nivelVida;
    private int vidaActual;
    private int velocidad;
    private String actitud;
    private boolean vive;
    private int fila;
    private int columna;
    private int numeroEjercito;

    // Constructor 1: sin parámetros
    public Soldado() {
        this("Soldado", 0);
    }

    // Constructor 2: nombre y ejército
    public Soldado(String nombre, int numeroEjercito) {
        this(nombre, numeroEjercito, 0, 0, 1, 1, 1);
    }

    // Constructor 3: completo
    public Soldado(String nombre, int numeroEjercito, int fila, int columna, int nivelVida, int nivelAtaque, int nivelDefensa) {
        this.nombre = nombre;
        this.numeroEjercito = numeroEjercito;
        this.fila = fila;
        this.columna = columna;
        this.nivelVida = nivelVida;
        this.vidaActual = nivelVida;
        this.nivelAtaque = nivelAtaque;
        this.nivelDefensa = nivelDefensa;
        this.velocidad = 0;
        this.actitud = "defensiva";
        this.vive = true;
    }

    public void atacar() {
        actitud = "ofensiva";
        avanzar();
    }

    public void defender() {
        velocidad = 0;
        actitud = "defensiva";
    }

    public void avanzar() {
        velocidad = velocidad + 1;
    }

    public void retroceder() {
        if (velocidad > 0) {
            defender();
        } else {
            velocidad = velocidad - 1;
        }
    }

    public void huir() {
        actitud = "fuga";
        velocidad = velocidad + 2;
    }

    public void serAtacado(int dano) {
        vidaActual = vidaActual - dano;
        if (vidaActual <= 0) {
            morir();
        }
    }

    public void morir() {
        vidaActual = 0;
        vive = false;
    }

    // Copia exacta (la posición se copia, pero el tablero asigna una libre al añadirla)
    public Soldado clonar() {
        Soldado copia = new Soldado(nombre, numeroEjercito, fila, columna, nivelVida, nivelAtaque, nivelDefensa);
        copia.vidaActual = vidaActual;
        copia.velocidad = velocidad;
        copia.actitud = actitud;
        copia.vive = vive;
        return copia;
    }

    // Compara nombre, nivelAtaque, nivelDefensa, vidaActual y vive
    public boolean comparar(Soldado otro) {
        return nombre.equals(otro.nombre)
                && nivelAtaque == otro.nivelAtaque
                && nivelDefensa == otro.nivelDefensa
                && vidaActual == otro.vidaActual
                && vive == otro.vive;
    }

    // Devuelve un soldado NUEVO con las sumas; ninguno de los dos cambia (permite Method-Call Chaining)
    public Soldado sumar(Soldado otro) {
        Soldado suma = new Soldado("Suma", numeroEjercito, 0, 0,
                nivelVida + otro.nivelVida,
                nivelAtaque + otro.nivelAtaque,
                nivelDefensa + otro.nivelDefensa);
        suma.vidaActual = vidaActual + otro.vidaActual;
        suma.velocidad = velocidad + otro.velocidad;
        return suma;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setNivelAtaque(int nivelAtaque) {
        this.nivelAtaque = nivelAtaque;
    }

    public int getNivelAtaque() {
        return nivelAtaque;
    }

    public void setNivelDefensa(int nivelDefensa) {
        this.nivelDefensa = nivelDefensa;
    }

    public int getNivelDefensa() {
        return nivelDefensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNivelVida() {
        return nivelVida;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getNumeroEjercito() {
        return numeroEjercito;
    }

    public boolean isVive() {
        return vive;
    }

    public String getCoordenada() {
        return "" + (char) ('A' + columna) + (fila + 1);
    }

    public String toString() {
        return nombre + " | Posición: " + getCoordenada() + " | Vida: " + vidaActual + "/" + nivelVida
                + " | Ataque: " + nivelAtaque + " | Defensa: " + nivelDefensa
                + " | Velocidad: " + velocidad + " | Actitud: " + actitud + " | Vive: " + vive;
    }
}