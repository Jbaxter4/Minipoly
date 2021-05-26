package minipoly;

import java.util.Scanner;

public class CLI_Version {

    public static void main(String[] args) {
        boolean gameOver = false;
        Scanner kb = new Scanner(System.in);
        //create players
        System.out.println("Enter Player 1's name: ");
        String name_1 = kb.nextLine();
        if(name_1.isEmpty()) {
            name_1 = "A";
        }
        System.out.println("Enter Player 2's name: ");
        String name_2 = kb.nextLine();
        if(name_2.isEmpty()) {
            name_2 = "B";
        }
        Player P1 = new Player(name_1, 1);
        Player P2 = new Player(name_2, 2);
        //create board, give the board access to the players, and create the properties
        Model Bd = new Model();
        Bd.setPlayers(P1, P2);
        Bd.createProperties();
        //Randomly decide which player will go first
        Bd.decideOrder();
        if (P1.isMyTurn()) {
            System.out.println(P1.getName() + " has been randomly chosen to go "
                    + "first.");
        } else {
            System.out.println(P2.getName() + " has been randomly chosen to go "
                    + "first.");
        }
        //Option to activate cheat mode
        System.out.println("Enter Y to activate cheat mode: ");
        String checkCheat = kb.nextLine();
        if (checkCheat.equalsIgnoreCase("Y")) {
            Bd.setCheatMode(true);
            System.out.println("Cheat Mode activated.");
        }
        //place palyers on board and display the board
        Bd.movePlayers();
        Bd.display_CLI();
        //game loop
        do {
            //play each turn
            Bd.playTurn_CLI(kb);
            //check to see if someone has won
            if (P1.getBalance() < 0) {
                System.out.println(P2.getName() + " wins!");
                gameOver = true;
            } else if (P2.getBalance() < 0) {
                System.out.println(P1.getName() + " wins!");
                gameOver = true;
            }
        } while (gameOver == false);
    }
}
