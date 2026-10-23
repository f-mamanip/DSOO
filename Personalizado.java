import java.util.ArrayList;

public class Personalizado {

    // Devuelve true si el usuario eligió "Jugar" y false si eligió "Volver"
    public static boolean gestionar(Ejercito uno, Ejercito dos, Tablero tablero) {
        while (true) {
            System.out.println("\n¿Qué ejército desea gestionar?");
            System.out.println("1. Ejército 1 (" + uno.getSimbolo() + ")");
            System.out.println("2. Ejército 2 (" + dos.getSimbolo() + ")");
            int numero = Consola.leerEntero("Ejército: ", 1, 2);
            Ejercito ejercito = (numero == 1) ? uno : dos;

            int opcion = pedirOpcion(ejercito);
            if (opcion == 10) {
                return true;
            } else if (opcion == 11) {
                return false;
            }
            ejecutar(opcion, ejercito, tablero);
        }
    }

    private static int pedirOpcion(Ejercito ejercito) {
        System.out.println("\n--- Gestión del Ejército " + ejercito.getNumero() + " (" + ejercito.cantidad() + "/" + Ejercito.MAXIMO + " soldados) ---");
        System.out.println("1) Crear soldado");
        System.out.println("2) Eliminar soldado");
        System.out.println("3) Clonar soldado");
        System.out.println("4) Modificar soldado");
        System.out.println("5) Comparar soldados");
        System.out.println("6) Intercambiar soldados");
        System.out.println("7) Ver soldado (por nombre)");
        System.out.println("8) Ver ejército");
        System.out.println("9) Sumar niveles");
        System.out.println("10) Jugar");
        System.out.println("11) Volver");
        return Consola.leerEntero("Opción: ", 1, 11);
    }

    private static void ejecutar(int opcion, Ejercito ejercito, Tablero tablero) {
        if (opcion == 1) {
            crear(ejercito, tablero);
        } else if (opcion == 2) {
            eliminar(ejercito);
        } else if (opcion == 3) {
            clonar(ejercito, tablero);
        } else if (opcion == 4) {
            modificar(ejercito);
        } else if (opcion == 5) {
            comparar(ejercito);
        } else if (opcion == 6) {
            intercambiar(ejercito);
        } else if (opcion == 7) {
            verSoldado(ejercito);
        } else if (opcion == 8) {
            verEjercito(ejercito, tablero);
        } else if (opcion == 9) {
            sumar(ejercito);
        }
    }

    private static int elegirSoldado(Ejercito ejercito, String mensaje) {
        ejercito.mostrarNumerados();
        return Consola.leerEntero(mensaje, 1, ejercito.cantidad()) - 1;
    }

    private static void crear(Ejercito ejercito, Tablero tablero) {
        if (ejercito.estaLleno()) {
            System.out.println("El ejército ya tiene " + Ejercito.MAXIMO + " soldados (límite).");
            return;
        }
        String nombre = Consola.leerNoVacio("Nombre: ");
        int vida = Consola.leerEntero("Nivel de vida [1..10]: ", 1, 10);
        int ataque = Consola.leerEntero("Nivel de ataque [1..10]: ", 1, 10);
        int defensa = Consola.leerEntero("Nivel de defensa [1..10]: ", 1, 10);
        Soldado nuevo = new Soldado(nombre, ejercito.getNumero(), 0, 0, vida, ataque, defensa);
        tablero.ubicarAleatorio(nuevo);
        ejercito.agregarSoldado(nuevo);
        System.out.println("Soldado creado y añadido al final: " + nuevo);
    }

    private static void eliminar(Ejercito ejercito) {
        if (ejercito.cantidad() <= 1) {
            System.out.println("No se puede eliminar: el ejército no puede quedar vacío.");
            return;
        }
        int indice = elegirSoldado(ejercito, "Soldado a eliminar: ");
        Soldado eliminado = ejercito.getSoldado(indice);
        ejercito.eliminarPosicion(indice);
        System.out.println("Eliminado: " + eliminado.getNombre());
    }

    private static void clonar(Ejercito ejercito, Tablero tablero) {
        if (ejercito.estaLleno()) {
            System.out.println("El ejército ya tiene " + Ejercito.MAXIMO + " soldados (límite).");
            return;
        }
        int indice = elegirSoldado(ejercito, "Soldado a clonar: ");
        Soldado copia = ejercito.getSoldado(indice).clonar();
        tablero.ubicarAleatorio(copia); // misma estadística, pero una casilla libre
        ejercito.agregarSoldado(copia);
        System.out.println("Clon añadido al final: " + copia);
    }

    private static void modificar(Ejercito ejercito) {
        int indice = elegirSoldado(ejercito, "Soldado a modificar: ");
        Soldado soldado = ejercito.getSoldado(indice);
        System.out.println("1) nivelAtaque  2) nivelDefensa  3) vidaActual  4) Cancelar");
        int atributo = Consola.leerEntero("Atributo: ", 1, 4);
        if (atributo == 1) {
            soldado.setNivelAtaque(Consola.leerEntero("Nuevo nivelAtaque [1..10]: ", 1, 10));
        } else if (atributo == 2) {
            soldado.setNivelDefensa(Consola.leerEntero("Nuevo nivelDefensa [1..10]: ", 1, 10));
        } else if (atributo == 3) {
            soldado.setVidaActual(Consola.leerEntero("Nueva vidaActual [1..99]: ", 1, 99));
        } else {
            return;
        }
        System.out.println("Resultado: " + soldado);
    }

    private static void comparar(Ejercito ejercito) {
        int primero = elegirSoldado(ejercito, "Primer soldado: ");
        int segundo = elegirSoldado(ejercito, "Segundo soldado: ");
        Soldado a = ejercito.getSoldado(primero);
        Soldado b = ejercito.getSoldado(segundo);
        System.out.println("nombre:       " + a.getNombre() + " / " + b.getNombre() + " -> " + (a.getNombre().equals(b.getNombre()) ? "igual" : "distinto"));
        System.out.println("nivelAtaque:  " + a.getNivelAtaque() + " / " + b.getNivelAtaque() + " -> " + (a.getNivelAtaque() == b.getNivelAtaque() ? "igual" : "distinto"));
        System.out.println("nivelDefensa: " + a.getNivelDefensa() + " / " + b.getNivelDefensa() + " -> " + (a.getNivelDefensa() == b.getNivelDefensa() ? "igual" : "distinto"));
        System.out.println("vidaActual:   " + a.getVidaActual() + " / " + b.getVidaActual() + " -> " + (a.getVidaActual() == b.getVidaActual() ? "igual" : "distinto"));
        System.out.println("vive:         " + a.isVive() + " / " + b.isVive() + " -> " + (a.isVive() == b.isVive() ? "igual" : "distinto"));
        if (a.comparar(b)) {
            System.out.println("Los soldados son IGUALES.");
        } else {
            System.out.println("Los soldados son DIFERENTES.");
        }
    }

    private static void intercambiar(Ejercito ejercito) {
        if (ejercito.cantidad() < 2) {
            System.out.println("Se necesitan al menos 2 soldados.");
            return;
        }
        int primero = elegirSoldado(ejercito, "Primer soldado: ");
        int segundo = Consola.leerEntero("Segundo soldado: ", 1, ejercito.cantidad()) - 1;
        ejercito.intercambiarSoldados(primero, segundo);
        System.out.println("Posiciones intercambiadas en el ejército:");
        ejercito.mostrarNumerados();
    }

    private static void verSoldado(Ejercito ejercito) {
        String nombre = Consola.leerNoVacio("Nombre a buscar: ");
        ArrayList<Soldado> encontrados = ejercito.buscarPorNombre(nombre);
        if (encontrados.isEmpty()) {
            System.out.println("No existe un soldado con ese nombre en este ejército.");
        } else {
            int indice = 0;
            while (indice < encontrados.size()) {
                System.out.println(encontrados.get(indice));
                indice = indice + 1;
            }
        }
    }

    private static void verEjercito(Ejercito ejercito, Tablero tablero) {
        tablero.mostrar();
        ejercito.mostrarEstadisticas();
    }

    private static void sumar(Ejercito ejercito) {
        Soldado total = ejercito.sumarNiveles();
        System.out.println("Sumatorias del Ejército " + ejercito.getNumero() + ":");
        System.out.println("  nivelVida:    " + total.getNivelVida());
        System.out.println("  nivelAtaque:  " + total.getNivelAtaque());
        System.out.println("  nivelDefensa: " + total.getNivelDefensa());
        System.out.println("  velocidad:    " + total.getVelocidad());
    }
}