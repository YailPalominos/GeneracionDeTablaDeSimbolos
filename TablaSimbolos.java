public class TablaSimbolos {

    private String token;
    private String tipo;
    private int idToken;
    private int repeticiones;
    private String linea;
    private float valor;

    public TablaSimbolos() {

    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setIdToken(int idToken) {
        this.idToken = idToken;
    }

    public int getIdToken() {
        return idToken;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

}
