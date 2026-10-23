import java.util.Scanner;

public class Consola {
    private static final Scanner SC = new Scanner(System.in);

    public static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return SC.nextLine().trim();
    }

    public static String leerNoVacio(String mensaje) {
        String texto = leerLinea(mensaje);
        while (texto.isEmpty()) {
            System.out.println("No puede estar vacío.");
            texto = leerLinea(mensaje);
        }
        return texto;
    }

    public static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            String texto = leerLinea(mensaje);
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // se vuelve a pedir
            }
            System.out.println("Ingrese un número entre " + minimo + " y " + maximo + ".");
        }
    }
}