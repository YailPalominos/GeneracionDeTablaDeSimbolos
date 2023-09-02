import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Analisis {

    String sCodigoFuente = "";
    
    TablaSimbolos[] TaSimbolos = new TablaSimbolos[0];
    

    Resultado oResultado = new Resultado();
    int nPosLectura = 0;
    int nLinea = 0;
    Set<String> simbolosAgregados = new HashSet<>();
    Set<String> PRAgregados = new HashSet<>();

    public Analisis(String sCodigoFuente) {
        this.sCodigoFuente = sCodigoFuente;
        
    }

    public void Generar() {

        System.out.println("hola");
        // Establecemos una cadena de coincidencias Esto es una expresion regular
        String coincidencias = "(Linea|Circulo|Triangulo|Cuadrado|Rectangulo)\\b|"
        + "([0-9]+)|"
        + "(\\s+)|"
        + "^(\\s*)";

        // Define un patron de busquedas dentro de nuestra cadena de coincidencias
        Pattern pPatron = Pattern.compile(coincidencias);
        // ralizara la búsqueda de nuestra coincidencias
        Matcher mMatcher = pPatron.matcher(sCodigoFuente);

        // Buscamos las coincidencias con el ciclo While
        int idToken = 0;
        
        while (mMatcher.find()) {

            String tokenPalabrasrReservadas = mMatcher.group(1);
            String tokenDigito = mMatcher.group(2);
            String tokenEspacios = mMatcher.group(3);
            
            int nPosInicioLexema = 0;
            int repeticiones = 1;
            nLinea = ObtenerLinea(sCodigoFuente, mMatcher.start());

            //Palabras Reservadas
            if (tokenPalabrasrReservadas != null) {

                nPosLectura += tokenPalabrasrReservadas.length();
                nPosInicioLexema = nPosLectura - tokenPalabrasrReservadas.length();
                idToken++;
                AgregarTablaSimbolos(tokenPalabrasrReservadas, "String", idToken, repeticiones, String.valueOf(nLinea), 0f);
                
            if (!PRAgregados.contains(tokenPalabrasrReservadas))
                PRAgregados.add(tokenPalabrasrReservadas);
            }


            //Digito
            if(tokenDigito != null){
                nPosLectura += tokenDigito.length();
                nPosInicioLexema = nPosLectura - tokenDigito.length();
                idToken++;
                float valor = Float.parseFloat(tokenDigito);
                String tipo = (Math.floor(valor) == valor) ? "Int" : "Float";
                AgregarTablaSimbolos(tokenDigito, tipo, idToken, repeticiones, String.valueOf(nLinea), Float.parseFloat(tokenDigito));
            }

           //Espacios
            if (tokenEspacios != null) {
                nPosLectura += tokenEspacios.length();
                nPosInicioLexema = nPosLectura - tokenEspacios.length();
            }
            

        }

        
        oResultado.ImprimirTblaSimb(TaSimbolos);

    }

    
    /**
     * Metodo para agregar un simbolo a la Tabla de simbolos
     * @param sSimbolo
     * @param sLexema
     * @param sLinea
     * @param sPosInicioLexema
     * @param sPosFinalLexema
     */
    public void AgregarTablaSimbolos(String sToken, String sTipo, int sIdToken, int sRepeticiones, String sLinea, float sValor) {
    // Verificamos si el token ya se encuentra en la tabla de símbolos
    for (int i = 0; i < this.TaSimbolos.length; i++) {
        if (this.TaSimbolos[i].getToken().equals(sToken)) {
            this.TaSimbolos[i].setRepeticiones(this.TaSimbolos[i].getRepeticiones() + 1);
            // Si el token ya existe, concatenamos las líneas
            this.TaSimbolos[i].setLinea(this.TaSimbolos[i].getLinea() + ", " + sLinea);
            return; // Salimos del método, no agregamos un nuevo registro
        }
    }

    // Si el token no existe en la tabla, entonces lo agregamos como un nuevo registro
    TablaSimbolos[] aSimboloNuevo = new TablaSimbolos[this.TaSimbolos.length + 1];
    System.arraycopy(this.TaSimbolos, 0, aSimboloNuevo, 0, this.TaSimbolos.length);

    TablaSimbolos oSimbolo = new TablaSimbolos();
    oSimbolo.setToken(sToken);
    oSimbolo.setTipo(sTipo);
    oSimbolo.setIdToken(sIdToken);
    oSimbolo.setRepeticiones(sRepeticiones);
    oSimbolo.setLinea(sLinea);
    oSimbolo.setValor(sValor);

    aSimboloNuevo[aSimboloNuevo.length - 1] = oSimbolo;

    this.TaSimbolos = aSimboloNuevo;
}//Fin del metodo AgregarTablaSimbolos

    int ObtenerLinea(String sCodigoFuente, int nInicio) {
        int nLinea = 1;
        Pattern pPatron = Pattern.compile("\n");
        Matcher mMatcher = pPatron.matcher(sCodigoFuente);
        mMatcher.region(0, nInicio);

        while (mMatcher.find()) {
            nLinea++;
        }
        return (nLinea);
    }
}