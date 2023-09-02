import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analisis {

    /**
     * @author Braulio Yail Palominos Patiño
     * @co Author Jose Adrian Terrones Perez
     */

    /*
     * Ojo
     * Tomar en cuenta que las llaves alteran la posicion de los grupos del marcher
     * En caso de agregar mas grupos
     */

    /*
     * Estas son las expreciones regulares que utiliza generalmente el analizador
     * lexico
     *
     * Palabras reservadas
     * (class|for|if|float|int|boolean|static|new|static|void|int|string|import|
     * public|else|programa|binario)|"
     * + "Identificadores ([a-zA-Z]+)|"
     * + "Relacionales ([<|>]+)|"
     * + "Aritmeticas ([+]|[-])|"
     * + "Asignacion([=]+)|"
     * + "Parentesis ([(|)]+)|"
     * + "LLaves ([{|}]+)|"
     * + "Punto y coma(;)|"
     * + "Binario ([0-1]b)|"
     * + "Octal ([0-8]o)|"
     * + "Hexadecimal (^[0-9A-F]+$)|"
     * + "Espacios (^(\s)*)";
     */

    String sCodigoFuente = ""; // Codigo fuente resibido
    String sCodigoFuenteErrores = ""; // Codigo fuente resibido
    // de analisis
    String[] aFiguras = new String[0]; // Cadena de las figuras a analizar
    Resultado oResultado = new Resultado(); // Clase para imprimir los resultados
    // Validacion oDesglosar = new Validacion(); // Clase para verificar la cadena
    // de figura
    int nPosLectura = 0; // posicion de lectura con respecto al codigo fuente
    int nLinea = 0; // Linea en la que va la posicion de lectura con respecto al codigo fuente

    TablaSimbolos[] TaToken = new TablaSimbolos[0];

    public Analisis(String sCodigoFuente) {
        this.sCodigoFuente = sCodigoFuente;
        this.sCodigoFuenteErrores = sCodigoFuente;
    }

    // Genera las clases con los simbolos
    public void Generar() {

        // Codigo fuente a chart para leer parte por parte
        var cLetras = this.sCodigoFuenteErrores.toCharArray();
        String sPalabra = "";
        Pattern pPatronError = Pattern.compile(
                "(((\\(\\d{1,2},\\d{1,2}\\)){2,4})+-+\\([1-3]\\)+-+\\((25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2}),(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2}),(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2})\\))");
        this.nPosLectura = 0;
        // Recorre palabra por palabra encontrada
        for (int y = 0; y < cLetras.length; y++) {

            nLinea = ObtenerLinea(sCodigoFuenteErrores, y);
            sPalabra += cLetras[y];
            this.nPosLectura++;

            if (sPalabra.split("\\s").length > 0) {
                this.nPosLectura -= 1;
                sPalabra = sPalabra.trim();

                char cLeta = cLetras[y];
                var x = ((cLeta + "").replace("", " ").trim());

                if (sPalabra.equals("Rectangulo")) {
                    sPalabra = sPalabra.replaceAll("Rectangulo", "");
                    nPosLectura += "Rectangulo".length();
                }

                if (sPalabra.equals("Triangulo")) {
                    sPalabra = sPalabra.replaceAll("Triangulo", "");
                    nPosLectura += "Triangulo".length();
                }

                if (sPalabra.equals("Cuadrado")) {
                    sPalabra = sPalabra.replaceAll("Cuadrado", "");
                    nPosLectura += "Cuadrado".length();
                }
                if (sPalabra.equals("Circulo")) {
                    sPalabra = sPalabra.replaceAll("Circulo", "");
                    nPosLectura += "Circulo".length();
                }
                if (sPalabra.equals("Linea")) {
                    sPalabra = sPalabra.replaceAll("Linea", "");
                    nPosLectura += "Linea".length();
                }

                if (x.length() == 0) {
                    Matcher mMatcherError = pPatronError.matcher(sPalabra);

                    if (mMatcherError.find()) {
                        sPalabra = "";
                    } else {
                        this.nPosLectura += sPalabra.length();
                        int nPosInicioLexema = this.nPosLectura - sPalabra.length();
                        System.out.println();
                        System.out.format("%10s %10s %10s %10s",
                                " \033[31mERROR léxico:  \033[0m" + sPalabra, " Linea " + nLinea,
                                " Inicia " + nPosInicioLexema, " Termina " + nPosLectura);
                        System.out.println();
                        sPalabra = "";
                    }
                }
            }
        }

        this.oResultado.ImprimirTblaTokens(TaToken);

    }

    public void AgregarTablaToken(String sToken, String sLexema, int nLinea, int nPosInicioLexema,
            int nPosFinalLexema) {

        TablaSimbolos[] aTblaTokensNueva = this.TaToken;
        aTblaTokensNueva = new TablaSimbolos[this.TaToken.length + 1];
        System.arraycopy(this.TaToken, 0, aTblaTokensNueva, 0, this.TaToken.length);

        // TablaSimbolos oTblaSimbolo = new TablaSimbolos();
        // oTblaSimbolo.simbolo = sToken;
        // oTblaSimbolo.lexema = sLexema;
        // oTblaSimbolo.linea = nLinea;
        // oTblaSimbolo.posInicioSimbolo = nPosInicioLexema;
        // oTblaSimbolo.posFinalSimbolo = nPosFinalLexema;
        // oTblaSimbolo.posFinalSimbolo = nPosFinalLexema;

        // if (oTblaSimbolo.simbolo.contains("Figura:")) {
        // AgregarFigura(oTblaSimbolo.lexema);
        // }

        // aTblaTokensNueva[aTblaTokensNueva.length - 1] = oTblaSimbolo;
        this.TaToken = aTblaTokensNueva;

    }

    int ObtenerLinea(String sCodigoFuente, int nInicio) {
        int nLinea = 0;
        Pattern pPatron = Pattern.compile("\n");
        Matcher mMatcher = pPatron.matcher(sCodigoFuente);
        mMatcher.region(0, nInicio);

        while (mMatcher.find()) {
            nLinea++;
        }

        if (this.nLinea != nLinea) {
            nPosLectura = 0;
        }

        return (nLinea);
    }

}