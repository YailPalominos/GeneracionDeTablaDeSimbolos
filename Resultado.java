public class Resultado {

        public Resultado() {
        }

        public void ImprimirTblaSimb(TablaSimbolos[] oSimbolos) {
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%70s", "Tabla de Simbolos");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%15s %15s %15s %15s %15s %15s", "Token", "Tipo", "id_Token", "Repeticiones", "Linea", "Valor", "");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");

                for (int x = 0; x < oSimbolos.length; x++) {
                        TablaSimbolos Simbolo = oSimbolos[x];

                        System.out.format("%15s %15s %15s %15s %15s %15s", Simbolo.getToken(), Simbolo.getTipo(), Simbolo.getIdToken(),
                                        Simbolo.getRepeticiones(), Simbolo.getLinea(), Simbolo.getValor());
                        System.out.println();

                }
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
        }

}
