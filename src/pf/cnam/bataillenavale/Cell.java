package pf.cnam.bataillenavale;
/**
 * Représente une cellule de tableau
 */
public class Cell {

    private int x;
    private int y;
    private int id;             // est égal à l’ID du bateau auquel il appartient (0 par défaut)
    private boolean shot;       // indique si une cellule a été abattue par l’adversaire ou non
    private boolean potential;  // utile pour le BOT de niveau 3, indique si une cellule est un bateau potentiel ou non

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = 0;
        this.shot = false;
        this.potential = true;
    }

    /**
     * Attribue la cellule à un bateau lorsqu’il est appelé
     * 
     * @param int id
     * 
     * @return void
     */
    public void addBoat(int id) {
        this.id = id;
    }

    /**
     * Met à jour le booléen potentiel à la valeur indiquée
     * 
     * @param boolean value
     * 
     * @return void
     */
    public void updatePotential(boolean value) {
        potential = value;
    }

    /**
     * Indique si la cellule est ou non une cellule de bateau potentielle ou non
     * 
     * @return boolean
     */
    public boolean isPotential() {
        return potential;
    }

    /**
     * Renvoie la valeur x
     * 
     * @return int
     */
    public int getX() {
        return this.x;
    }

    /**
     * Renvoie la valeur y
     * 
     * @return int
     */
    public int getY() {
        return this.y;
    }

    /**
     * Met à jour le booléen de plan lorsque la cellule est tournée
     * 
     * @return void
     */
    public void shoot() {
        this.shot = true;
    }

    /**
     * Renvoie si la cellule a été abattue ou non
     * 
     * @return boolean
     */
    public boolean isShot() {
        return this.shot;
    }

    /**
     * Renvoie l’identifiant de la cellule
     * 
     * @return int
     */
    public int getId() {
        return this.id;
    }
}