package pf.cnam.bataillenavale;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Représente un joueur
 */
public abstract class Player {

    protected String playerName;
    protected Board board = new Board();
    protected BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private int statNbTotalShot;
    private int statNbSuccessfullShot;
    private int statNbBoatShot;
    private Cell lastCellShot = new Cell(-1, -1);

    protected abstract void placeBoats();

    protected abstract void shoot(Player enemy);

    protected abstract void setPlayerName();

    /**
     * Augmente le nombre de tirs tirés par le joueur
     * 
     * @return void
     */
    protected void incrementStatNbTotalShot() {
        statNbTotalShot++;
    }

    /**
     * Augmente le nombre de tirs réussis par le joueur
     * 
     * @return void
     */
    protected void incrementStatNbSuccessfullShot() {
        statNbSuccessfullShot++;
    }

    /**
     * Augmente le nombre de navires coulés par le joueur
     * 
     * @return void
     */
    protected void incrementStatNbBoatShot() {
        statNbBoatShot++;
    }

    /**
     * Obtient le plateau du joueur
     * 
     * @return Board
     */
    protected Board getBoard() {
        return this.board;
    }

    /**
     * Obtient le nom du joueur
     * 
     * @return String
     */
    protected String getPlayerName() {
        return this.playerName;
    }

    /**
     * Afficher les statistiques des joueurs
     * 
     * @return void
     */
    protected void printStats() {
        System.out.println();
        System.out.println("║");
        System.out.println("║ Statistiques de jeu de " + playerName + " :");
        System.out.println("║");
        System.out.println("║ Nombre de tirs réalisés : " + statNbTotalShot);
        System.out.println("║ Nombre de tirs réussis : " + statNbSuccessfullShot);
        System.out.println("║ Précision : " + (double) Math.round(((double) (statNbSuccessfullShot) / (double) (statNbTotalShot)) * 100.0) + "%");
        System.out.println("║ Nombre de bateaux adverses détruis : " + statNbBoatShot + "/" + Config.getNbBoats());
        System.out.println("║");
    }

    /**
     * Obtient le dernier tir effectué par le joueur
     * 
     * @return Cell
     */
    protected Cell getLastCellShot() {
        return lastCellShot;
    }

    /**
     * Arrête le dernier tir effectué par le joueur
     * 
     * @param int x
     * @param int y
     * 
     * @return void
     */
    protected void setLastCellShot(int x, int y) {
        lastCellShot = new Cell(x, y);
    }
}