package pf.cnam.bataillenavale;
/**
 * Représente un bateau
 */
public class Boat {
    private int id;
    private String name;
    private Cell[] cells;
    private String direction;

    public Boat(Cell[] cells, int id, String name) {
        this.name = name;
        this.cells = cells;
        this.id = id;
    }

    /**
     * Calcule si le bateau a été coulé ou non
     * 
     * @return boolean
     */
    public boolean isSunk() {
        for (int i = 0; i < this.cells.length; i++) {
            if (!this.cells[i].isShot()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Renvoie une cellule spécifique
     * 
     * @return Cell
     */
    public Cell getCells(int cellPosition) {
        return this.cells[cellPosition];
    }

    /**
     * Renvoie une cellule spécifique
     * 
     * @return Cell
     */
    public Cell getCells(int x, int y) {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].getX() == x && cells[i].getY() == y) {
                return cells[i];
            }
        }
        return cells[0];
    }

    /**
     * Renvoie le tableau de cellules appartenant au bateau
     * 
     * @return Cell[]
     */
    public Cell[] getCells() {
        return this.cells;
    }

    /**
     * Renvoie l’identifiant du bateau
     * 
     * @return int
     */
    public int getId() {
        return this.id;
    }

    /**
     * Renvoie la taille du bateau
     * 
     * @return int
     */
    public int getSize() {
        return cells.length;
    }

    /**
     * Renvoie le nom du bateau
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoie la direction du bateau
     * 
     * @return String
     */
    public String getDirection() {
        return this.direction;
    }
}