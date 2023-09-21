import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analisis {

    /**
     * @author Braulio Yail Palominos Patiño
     */

    private String codigoFuente = ""; // Codigo fuente resibido de la clase de ejecutador
    private List<Simbolo> TablaSimbolos;// Lista de TablaSimbolos donde se guardara toda la información
    private int posLectura = 0; // posicion de lectura con respecto al codigo fuente
    private int linea = 1; // Linea en la que va la posicion de lectura con respecto al codigo fuente
    private int Id = 0; // Id que se asingnara a las variables que se vayan inicializando

    public Analisis(String codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    // Genera las clases con los simbolos
    public void Generar() {

        // Inicializamos la tabla de simbolos
        TablaSimbolos = new ArrayList<Simbolo>();
        // Codigo fuente a chart para leer parte por parte
        var letras = this.codigoFuente.toCharArray();
        String palabra = "";
        String palabras = "";

        // Expreciones regulares:
        Pattern cambioLinea = Pattern.compile("\n");
        Pattern iniciarVariable = Pattern.compile("((Real|Entero) ([a-zA-Z0-9.]*)\\;)");
        Pattern iniciarVariableConValor = Pattern.compile("((Real|Entero) ([a-zA-Z0-9.]*)\\=\\d*\\;)");
        Pattern iniciarVariables = Pattern
                .compile("((Real|Entero) (([a-zA-Z0-9.]+)\\,)(([a-zA-Z0-9.]+)\\,)*(([a-zA-Z0-9.]+)\\;))");
        Pattern iniciarVariablesConValor = Pattern.compile(
                "((Real|Entero) (([a-zA-Z0-9.]+\\=\\d*)\\,)(([a-zA-Z0-9.]+\\=\\d*)\\,)*(([a-zA-Z0-9.]+\\=\\d*)\\;))");
        Pattern leerOEscribirVariables = Pattern.compile("((Leer|Escribir)\\(([a-zA-Z0-9.]*\\))\\;)");
        Pattern realizarOperacion = Pattern.compile(
                "[a-zA-Z0-9]+\\=+(([a-zA-Z0-9.]+(\\+|\\/|\\*|\\-)+[a-zA-Z0-9.]+\\;)|([a-zA-Z0-9.]+(\\+|\\/|\\*|\\-)+\\(+[a-zA-Z0-9.]+(\\+|\\-|\\/|\\*)+[a-zA-Z0-9.]+\\)+\\;))");

        // 1)
        // Real cuenta,numero,resultado;
        // Entero valor;
        // Leer(valor);
        // cuenta=23+(numero-valor);
        // numero=cuenta/123.99;
        // resultado=numero+cuenta;
        // Escribir(resultado);
        // 2)
        // Real a,b,c,d,e,f,g;
        // Entero var1,var2,var3,var4,var5,var6;
        // Escribir(var1);
        // 3)
        // Entero personas;
        // Real dinero,recaudado;
        // recaudado=personas*real;
        // Escribir(recaudado);

        // Recorre palabra por palabra encontrada
        for (int y = 0; y < letras.length; y++) {

            palabra += letras[y];
            posLectura++;

            if (palabra.split("\\s").length > 0) {
                palabra = palabra.trim();

                char letra = letras[y];
                var x = ((letra + "").replace("", " ").trim());

                if (x.length() == 0 | x.equals(";")) {

                    if (palabras.length() == 0) {
                        palabras += palabra;
                    } else {
                        palabras += " " + palabra;
                    }
                    palabra = "";

                    // Para detectar el cambio de linea
                    Matcher matcherCambioLinea = cambioLinea.matcher(codigoFuente);
                    matcherCambioLinea.region(0, y);
                    linea = 1;
                    while (matcherCambioLinea.find()) {
                        linea++;
                        posLectura = 0;
                    }

                    Matcher matcherIniciarVariable = iniciarVariable.matcher(palabras);
                    Matcher matcherIniciarVariableConValor = iniciarVariableConValor.matcher(palabras);
                    Matcher matcherIniciarVariables = iniciarVariables.matcher(palabras);
                    Matcher matcherIniciarVariablesConValor = iniciarVariablesConValor.matcher(palabras);
                    Matcher matcherLeerEscribirVariables = leerOEscribirVariables.matcher(palabras);
                    Matcher matcherRealizarOperaciones = realizarOperacion.matcher(palabras);

                    if (matcherIniciarVariable.find()) {
                        // Es una asignación de una sola variable sin valor asignado.
                        // Ejemplo: Real variable;
                        String[] arregloPalabras = palabras.split(" ");
                        AgregarSimbolo(arregloPalabras[1].replace(";", ""), arregloPalabras[0], Id, 1, "" + linea,
                                "0");
                        palabras = "";
                        Id++;
                    }

                    if (matcherIniciarVariableConValor.find()) {
                        // Es una asignación de una sola variable con una valor asignado.
                        // Ejemplo: Real variable=12;
                        String[] arregloPalabras = palabras.split(" ");
                        String[] valorYNombre = arregloPalabras[1].split("=");
                        AgregarSimbolo(valorYNombre[0], arregloPalabras[0], Id, 1, "" + linea,
                                valorYNombre[1].replace(";", ""));
                        palabras = "";
                        Id++;
                    }

                    if (matcherIniciarVariables.find()) {
                        // Es una asignación de varias variables a la vez sin un valor inicial.
                        // Ejemplo: Real cuenta,numero,resultado;
                        String tipo = "";

                        if (palabras.contains("Real ")) {
                            palabras = palabras.replace("Real ", "");
                            tipo = "Real";
                        }
                        if (palabras.contains("Entero ")) {
                            palabras = palabras.replace("Entero ", "");
                            tipo = "Entero";
                        }
                        palabras = palabras.replace(";", "");

                        String[] arregloPalabras = palabras.split(",");
                        for (String variable : arregloPalabras) {
                            AgregarSimbolo(variable, tipo, Id, 1,
                                    "" + linea, "0");
                            Id += 1;
                        }
                        palabras = "";
                    }

                    if (matcherIniciarVariablesConValor.find()) {
                        // Es una asignación de varias variables a la vez con un valor inicial.
                        // Ejemplo: Entero x=1,y=2,z=0;
                        String tipo = "";

                        if (palabras.contains("Real ")) {
                            palabras = palabras.replace("Real ", "");
                            tipo = "Real";
                        }
                        if (palabras.contains("Entero ")) {
                            palabras = palabras.replace("Entero ", "");
                            tipo = "Entero";
                        }
                        palabras = palabras.replace(";", "");

                        String[] arregloPalabras = palabras.split(",");
                        for (String variable : arregloPalabras) {
                            String[] valorYNombre = variable.split("=");
                            AgregarSimbolo(valorYNombre[0], tipo, Id, 1,
                                    "" + linea, valorYNombre[1]);
                            Id += 1;
                        }
                        palabras = "";
                    }

                    if (matcherLeerEscribirVariables.find()) {
                        // Es para leer o escribir las variables
                        // Ejemplo: Leer(valor);
                        // Ejemplo: Escribir(valor);
                        palabras = palabras.replace("Leer", "");
                        palabras = palabras.replace("Escribir", "");
                        palabras = palabras.replace("(", "");
                        palabras = palabras.replace(")", "");
                        palabras = palabras.replace(";", "");

                        if (ComprobarToken(palabras) == false) {
                            GenerarError("No se encontro el token", palabras);
                        }
                        palabras = "";
                    }

                    if (matcherRealizarOperaciones.find()) {
                        // Es para validar operaciones
                        // cuenta=23+(numero-valor);
                        // numero=cuenta/123.99;
                        // resultado=numero+cuenta;

                        // Primero separamos la parte de la asignación
                        palabras = palabras.replace(";", "");
                        String[] asignacion = palabras.split("=");

                        // Separamos las operaciones para identificar si son consntantes o variables
                        // previamente asignadas.
                        if (ComprobarToken(asignacion[0]) == false) {
                            GenerarError("No se encontro el token al cual se le esta asignando", palabras);
                        }
                        String operacion = asignacion[1];
                        operacion = operacion.replace("(", "");
                        operacion = operacion.replace(")", "");
                        operacion = operacion.replace(";", "");

                        operacion = operacion.replace("+", " ");
                        operacion = operacion.replace("-", " ");
                        operacion = operacion.replace("/", " ");
                        operacion = operacion.replace("*", " ");

                        String[] variables = operacion.split(" ");

                        for (String variable : variables) {
                            if (VerificarConstante(variable) == false) {
                                if (ComprobarToken(variable) == false) {
                                    GenerarError("No se encontro el token", variable);
                                }
                            }
                        }

                        palabras = "";
                    }

                }
            }
        }
        // Imprimimos la tabla de simbolos.
        System.out.println();
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%70s", "Tabla de simbolos");
        System.out.println();
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%10s %10s %10s %10s %10s %10s", "Token", "Tipo", "Id", "Repeticiones", "Linea", "Valor");
        System.out.println();
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------------");
        for (

                int x = 0; x < TablaSimbolos.size(); x++) {
            Simbolo oSimbolo = TablaSimbolos.get(x);
            System.out.format("%10s %10s %10s %10s %10s %10s", oSimbolo.token, oSimbolo.tipo, oSimbolo.idToken,
                    oSimbolo.repeticiones, oSimbolo.linea, oSimbolo.valor);
            System.out.println();
        }
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------------------------");
    }

    public boolean ComprobarToken(String token) {
        for (Simbolo simbolo : TablaSimbolos) {
            if (simbolo.token.equals(token)) {
                AñadirRepeticion(token);
                return true;
            }
        }
        return false;
    }

    public void AñadirRepeticion(String token) {
        for (Simbolo simbolo : TablaSimbolos) {
            if (simbolo.token.equals(token)) {
                simbolo.repeticiones++;
                simbolo.linea = simbolo.linea + "," + linea;
            }
        }
    }

    public void GenerarError(String error, String palabra) {

        System.out.println();
        System.out.format("%10s %10s %10s %10s",
                " \033[31mError " + error + ": \033[0m" + palabra, "Linea " + linea,
                " Inicia " + (posLectura), " Termina " + (posLectura + palabra.length()));
        System.out.println();
        return;
    }

    public boolean VerificarConstante(String constante) {
        Pattern constantes = Pattern
                .compile("[\\d.]+");
        Matcher matcherConstantes = constantes.matcher(constante);
        if (matcherConstantes.find()) {
            return true;
        } else {
            return false;
        }
    }

    public Simbolo VerSimbolo(String token) {
        for (Simbolo simbolo : TablaSimbolos) {
            if (simbolo.token.equals(token)) {
                return simbolo;
            }
        }
        return null;
    }

    public void EscribirSimbolo(Simbolo oSimbolo) {
        for (int x = 0; x < TablaSimbolos.size(); x++) {
            if (TablaSimbolos.get(x).token.equals(oSimbolo.token)) {
                TablaSimbolos.set(x, oSimbolo);
            }
        }
    }

    public void AgregarSimbolo(String token, String tipo, int idToken, int repeticiones, String linea,
            String valor) {
        Simbolo oSimbolo = new Simbolo();
        oSimbolo.token = token;
        oSimbolo.tipo = tipo;
        oSimbolo.idToken = idToken;
        oSimbolo.repeticiones = repeticiones;
        oSimbolo.linea = linea;
        oSimbolo.valor = valor;
        TablaSimbolos.add(oSimbolo);
    }
}
