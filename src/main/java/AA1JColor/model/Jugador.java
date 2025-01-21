package AA1JColor.model;

public class Jugador {
    private final char letra;
    private int vidas;
    private Posicion posicion;
    private final Tablero tablero;
    private boolean haGanado;
    private boolean wallhack = false;
    private boolean tieneBomba = false;
//Constructor
    public Jugador(char letra, int vidasIniciales, Tablero tablero){
        this.letra = letra;
        this.vidas = vidasIniciales;
        this.tablero = tablero;
        this.posicion = tablero.encontrarJugador();
        this.haGanado = false;
    }
//Getters
    public char getLetra() { return letra;}
    public int getVidas() { return vidas;}
    public Posicion getPosicion() { return posicion;}
    public Tablero getTablero() { return  tablero;}
    public boolean getBomba(){return tieneBomba;}
    public boolean haGanado() {
        return haGanado;
    }

    //Setters
    public void setPosicion(Posicion posicion) { this.posicion = posicion;}
    public void perderVida() { vidas--;}
    public void ganarVida() { vidas++;}
    public void recogerBomba() {
        tieneBomba = true;
    }
    public boolean estaVivo() { return vidas>0 ;}

    public void setGanador(boolean haGanado) {
        this.haGanado = haGanado;
    }

    public void activarWallhack() {
       wallhack = true;
       System.out.println("Wallhack activado! Ahora puedes ver a tus enemigos en el tablero!");
    }

    public boolean isWallhack(){
        return wallhack;
    }

    public void desactivarWallhack(){
        wallhack = false;
    }

}


