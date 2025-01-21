package AA1JColor.model;

public class Movimiento {
    private final int pasos;
    private final Direccion direccion;

    public enum Direccion{
        ARRIBA('W'), IZQUIERDA('A'), ABAJO('S'), DERECHA('D');

        private final char letra;
        Direccion(char letra){this.letra = letra;}
        public char getLetra() {return letra;}

        public static Direccion desdeLetra(char l){
            for (Direccion d : values()){
                if (d.letra == l) return d;
            }
            throw new IllegalArgumentException("Direccion Inválida: " + l);
        }
    }
    public Movimiento(String entrada){
        if(entrada.length() != 2){
            throw new IllegalArgumentException("Formato de movimiento inválido");
        }
        this.pasos = Character.getNumericValue(entrada.charAt(0));
        this.direccion = Direccion.desdeLetra(entrada.charAt(1));
    }
    public int getPasos(){return pasos;}

    public Direccion getDireccion() {
        return direccion;
    }
}
