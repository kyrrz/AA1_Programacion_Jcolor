package AA1JColor;

import AA1JColor.model.Jugador;
import AA1JColor.model.Movimiento;
import AA1JColor.model.Posicion;
import AA1JColor.model.Tablero;

import java.util.*;

public class Juego {

    private static final char BART = 'B';
    private static final char HOMER = 'H';
    private static final char KRUSTY = 'K';
    private static final char FLANDERS = 'F';


    private boolean colisiones = false;
    private Tablero tablero1;
    private Tablero tablero2;
    private Jugador jugador1;
    private Jugador jugador2;


    public Juego(){

     /*   jugador1 = new model.Jugador(BART,3, tablero1);
        jugador2 = new model.Jugador(HOMER, 3, tablero2);*/

    }

    private void menu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleciona el tamaño del tablero (minimo 6): ");
        int size;
        do {
            size = scanner.nextInt();
        } while (size < 6);

        System.out.println("Selecciona un nivel de dificultad (1-Facil, 2-Medio, 3-Dificil): ");
        int dificultad = scanner.nextInt();

        System.out.println("Permitir colisiones del tablero? (true/false): ");
        colisiones = scanner.nextBoolean();

        tablero1 = new Tablero(size, dificultad, BART, KRUSTY);
        tablero2 = new Tablero(size, dificultad, HOMER, FLANDERS);

        jugador1 = new Jugador(BART,3, tablero1);
        jugador2 = new Jugador(HOMER, 3, tablero2);
    }

    public void jugar(){
        Scanner scanner = new Scanner(System.in);

        boolean acabado = false;

        while (!acabado){
            //Turno de BART

            System.out.println("Turno de BART");
            if(jugador1.isWallhack()){
                tablero1.mostrarTableroNormal();
            } else {
                tablero1.mostrarTableroCiego();
            }
            procesarTurno(jugador1, scanner);
            tablero1.moverVidasExtra();
            if (verificarFinJuego(jugador1)) {
                if (jugador1.haGanado()) {
                    System.out.println("GANADOR: BART!!");
                } else {
                    System.out.println("GANADOR: HOMER!!");
                }
                acabado = true;
            }

            //Turno de HOMER
            System.out.println("Turno de HOMER");
            if(jugador2.isWallhack()){
                tablero2.mostrarTableroNormal();
            } else {
                tablero2.mostrarTableroCiego();
            }
            procesarTurno(jugador2,scanner);
            tablero2.moverVidasExtra();
            if (verificarFinJuego(jugador2)) {
                if (jugador2.haGanado()) {
                    System.out.println("GANADOR: HOMER!!");
                }else {
                    System.out.println("GANADOR: BART!!");
                }
                acabado = true;

            }
        }
        scanner.close();
    }

    private void procesarTurno(Jugador jugador, Scanner scanner){

        boolean turnoEntero = false;
        while (!turnoEntero){
            jugador.desactivarWallhack();
            System.out.println("Tiene bomba?: "+ jugador.getBomba());
            System.out.println("Escribe el movimiento! (1A) , 'B' para usar una bomba o 'T' para el menu de los trucos");
            String entrada = scanner.nextLine().toUpperCase();


            if(entrada.equals("T")){
                menuTrucos(jugador, scanner);
            }
            else if (entrada.equals("B")) {
                if(jugador.getBomba()){
                    usarBomba(jugador);
                    turnoEntero = true;
                } else {System.out.println("No tienes bombas para usar!");}
            } else {
                try{
                    Movimiento movimiento = new Movimiento(entrada.toUpperCase());
                    if (esMovimientoValido(movimiento)){
                        ejecutarMovimiento(jugador, movimiento);
                        jugador.getTablero().mostrarTableroCiego();
                        turnoEntero = true;
                    } else {System.out.println("Mal movimiento");}
                }catch (Error e){
                    System.out.println("model.Movimiento inválido: " + e.getMessage());
                }
            }
        }
    }

    private boolean esMovimientoValido(Movimiento movimiento){
        return movimiento.getPasos() >= 1 && movimiento.getPasos() <= 3;
    }

    private void ejecutarMovimiento(Jugador jugador, Movimiento movimiento){

        Tablero tablero = jugador.getTablero();
        Posicion posActual = jugador.getPosicion();
        Posicion nuevaPos = posActual;

        for (int i=0; i<movimiento.getPasos(); i++){

            nuevaPos = calcularNuevaPosicion(posActual, movimiento.getDireccion());
            posActual = nuevaPos;
        }

        if (tablero.esSalida(nuevaPos)){
            jugador.setGanador(true);
            tablero.limpiarCelda(jugador.getPosicion());
            jugador.setPosicion(posActual);
            tablero.colocarLetra(posActual, jugador.getLetra());
            return;
        }

        if (tablero.recogerVidaExtra(nuevaPos)) {
            jugador.ganarVida();
            System.out.println("¡Has recogido una vida extra! Vidas restantes: " + jugador.getVidas());
        }

        if (tablero.recogerBomba(nuevaPos)){
            jugador.recogerBomba();
            System.out.println("Has recogido una bomba!");
        }

        if (tablero.getPosition(nuevaPos) == tablero.getEnemigo()) {
            jugador.perderVida();
            System.out.println("Enemigo encontrado! Pierdes una vida" + jugador.getVidas());
            tablero.limpiarCelda(nuevaPos);

            if (!jugador.estaVivo()) {
                return;
            }
        }
        tablero.limpiarCelda(jugador.getPosicion());
        jugador.setPosicion(posActual);
        tablero.colocarLetra(posActual, jugador.getLetra());
    }

    private Posicion calcularNuevaPosicion(Posicion actual, Movimiento.Direccion direccion){
        int x = actual.getX();
        int y = actual.getY();
        int size = tablero1.getSize();
        if (!colisiones){
            switch (direccion) {
                case ARRIBA: x = (x - 1 + size) % size; break;
                case IZQUIERDA: y = (y - 1 + size) % size; break;
                case ABAJO: x = (x + 1) % size; break;
                case DERECHA: y = (y + 1) % size; break;
            }
        } else {
            switch (direccion){
                case ARRIBA: if (x>0) x--; break;
                case ABAJO: if (x<size-1) x++; break;
                case IZQUIERDA: if (y>0) y--; break;
                case DERECHA: if (y<size-1) y++; break;
            }
        }
        return new Posicion(x, y);
    }

    private boolean verificarFinJuego(Jugador jugador) {
        return !jugador.estaVivo() || jugador.haGanado();
    }

    private void usarBomba(Jugador jugador) {
        Tablero tablero = jugador.getTablero();
        Posicion posicionJugador = jugador.getPosicion();
        int x = posicionJugador.getX();
        int y = posicionJugador.getY();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                int nuevoX = x + i;
                int nuevoY = y + j;

                // Verificar límites del tablero
                if (nuevoX >= 0 && nuevoX < tablero.getSize() && nuevoY >= 0 && nuevoY < tablero.getSize()) {
                    Posicion posActual = new Posicion(nuevoX, nuevoY);
                    if (tablero.getPosition(posActual) == tablero.getEnemigo()) {
                        tablero.limpiarCelda(posActual); // Elimina al enemigo
                        System.out.println("¡Enemigo eliminado en (" + nuevoX + ", " + nuevoY + ")!");
                    }
                }
            }
        }
    }

    private void menuTrucos(Jugador jugador, Scanner scanner) {
        System.out.println("Menú de trucos:");
        System.out.println("1. Ver dónde están los enemigos (wallhack)");
        System.out.println("2. Contar cuántos enemigos quedan vivos (-1 vida)");
        System.out.println("Elige una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        switch (opcion) {
            case 1:
                jugador.activarWallhack();
                jugador.getTablero().mostrarTableroNormal();
                System.out.println("Wallhack activado temporalmente.");
                break;

            case 2:
                if (jugador.getVidas() > 1) {
                    jugador.perderVida();
                    contarEnemigos(jugador);
                    System.out.println("Te quedan " +jugador.getVidas()+ "vidas");
                } else {
                    System.out.println("No tienes suficientes vidas para usar este truco.");
                }
                break;

            default:
                System.out.println("Opción inválida. Intenta nuevamente.");
        }
    }

    private void contarEnemigos(Jugador jugador){
        if (jugador.getVidas() > 1) {
            jugador.perderVida();
            System.out.println("Enemigos restantes: " + jugador.getTablero().contarEnemigos());
        } else {
            System.out.println("No tienes suficientes vidas para usar este truco.");
        }
    }


    public static void main(String[] args){
        Juego juego = new Juego();
        juego.menu();
        juego.jugar();
    }
}