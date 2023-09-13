public class Resultado {

        public Resultado() {
        }

        public void ImprimirTblaTokens(TablaSimbolos[] oSimbolos) {
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%70s", "Tabla de Tokens");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%10s %10s %10s %10s %10s", "Token", "Lexema", "Linea", "Inicio", "Final");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");

                for (int x = 0; x < oSimbolos.length; x++) {
                        var Simbolo = oSimbolos[x];

                        System.out.format("%10s %10s %10s %10s %10s %10s", Simbolo.getToken(), Simbolo.getTipo(),
                                        Simbolo.getIdToken(),
                                        Simbolo.getRepeticiones(), Simbolo.getLinea(), Simbolo.getValor());
                        System.out.println();

                }
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");

        }

}
