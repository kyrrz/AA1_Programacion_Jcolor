package AA1JColor.model;

import java.util.Random;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class Tablero {
    private final int size;
    private final int dificultad;
    private static final char LIBRE = 'L';
    private static final char SALIDA = 'S';
    private static final char VIDA_EXTRA = 'V';
    private static final char BOMBA = 'X';
    private static final int vidasExtra = 2;

    private final char[][] tablero;
    private final char jugador;
    private final char enemigo;

    //Constructor
    public Tablero(int size, int dificultad, char jugador, char enemigo){
        this.size = size;
        this.dificultad = dificultad;
        this.jugador = jugador;
        this.enemigo = enemigo;
        tablero = new char[size][size];
        inicializarTablero();
        posicionarJugador();
        posicionarEnemigos();
        posicionarVidasExtra();
        posicionarBombas();
        posicionarSalida();
    }

    private void inicializarTablero(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                tablero[i][j] = LIBRE;
            }
        }
    }

    private void posicionarJugador(){
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (tablero[x][y] != LIBRE);
        tablero[x][y] = jugador;

    }

    private void posicionarEnemigos(){
        Random random = new Random();
        int colocados = 0;
        int numEnemigos = size*dificultad;
        while (colocados < numEnemigos){
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if(tablero[x][y] == LIBRE){
                tablero[x][y] = enemigo;
                colocados++;
            }
        }
    }

    private void posicionarSalida(){
        Random random = new Random();
        int x,y;
        do  {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (tablero[x][y] != LIBRE);
        tablero[x][y] = SALIDA;
    }

    public void posicionarVidasExtra(){
        Random random = new Random();
        for (int i=0; i < vidasExtra; i++){
            int x,y;
            do{
                x = random.nextInt(size);
                y = random.nextInt(size);
            } while (tablero[x][y] != LIBRE);
            tablero[x][y] = VIDA_EXTRA;
        }
    }

    public void colocarLetra(Posicion pos, char letra){
        tablero[pos.getX()][pos.getY()] = letra;
    }

    public char getPosition(Posicion pos){
        return tablero[pos.getX()][pos.getY()];
    }

    public void limpiarCelda(Posicion pos){
        tablero[pos.getX()][pos.getY()] = LIBRE;
    }

    public boolean esSalida(Posicion pos){
        return tablero[pos.getX()][pos.getY()] == SALIDA;
    }

    public boolean recogerVidaExtra(Posicion pos) {
        if (tablero[pos.getX()][pos.getY()] == VIDA_EXTRA) {
            tablero[pos.getX()][pos.getY()] = LIBRE;
            return true;
        }
        return false;
    }

    public void moverVidasExtra() {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tablero[i][j] == VIDA_EXTRA) {
                    tablero[i][j] = LIBRE;
                    int x, y;
                    do {
                        x = random.nextInt(size);
                        y = random.nextInt(size);
                    } while (tablero[x][y] != LIBRE);
                    tablero[x][y] = VIDA_EXTRA;
                }
            }
        }
    }

    public int contarEnemigos() {
        int contador = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tablero[i][j] == enemigo) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public boolean recogerBomba(Posicion pos) {
        if (tablero[pos.getX()][pos.getY()] == BOMBA) {
            tablero[pos.getX()][pos.getY()] = LIBRE;
            return true;
        }
        return false;
    }

    public void posicionarBombas() {
        Random random = new Random();
        int colocadas = 0;
        int maxBombas = size/dificultad;
        while (colocadas < maxBombas){
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if(tablero[x][y] == LIBRE){
                tablero[x][y] = BOMBA;
                colocadas++;
            }
        }
    }

    public void mostrarTableroCiego(){
        int size = getSize();

        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                switch (tablero[i][j]){
                    case 'B': System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), YELLOW_BACK())); break;
                    case 'H': System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BLUE_BACK())); break;
                    case SALIDA: System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BRIGHT_GREEN_BACK())); break;
                    case VIDA_EXTRA: System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BRIGHT_MAGENTA_BACK())); break;
                    case BOMBA:  System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BRIGHT_BLACK_BACK())); break;
                    default: System.out.print(colorize(" " + LIBRE + " ", BLACK_TEXT(), WHITE_BACK()));
                }
            }
            System.out.println();
        }
    }

    public void mostrarTableroNormal(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                switch (tablero[i][j]){
                    case 'B': System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), YELLOW_BACK())); break;
                    case 'H': System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BLUE_BACK())); break;
                    case 'K': System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), RED_BACK())); break;
                    case 'F': System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), RED_BACK())); break;
                    case SALIDA: System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BRIGHT_GREEN_BACK())); break;
                    case VIDA_EXTRA: System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BRIGHT_MAGENTA_BACK())); break;
                    case BOMBA:  System.out.print(colorize(" " + tablero[i][j] + " ", BLACK_TEXT(), BRIGHT_BLACK_BACK())); break;
                    default: System.out.print(colorize(" " + LIBRE + " ", BLACK_TEXT(), WHITE_BACK()));
                }
            }
            System.out.println();
        }
    }

    public Posicion encontrarJugador(){
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                if (tablero[i][j] == jugador){
                    return new Posicion(i, j);
                }
            }
        }
        return null;
    }

    public int getSize() {return size;}

    public char getEnemigo() {return enemigo;}
}

