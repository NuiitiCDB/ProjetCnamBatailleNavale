import helpers.ConsoleHelper;
import helpers.CoordinateHelper;
import helpers.TextHelper;

/**
 * Représente un tableau qui contient un tableau 2D de cellules
 */
public class Board {

    private Cell[][] board;
    private Boat[] boats;
    private int nbBoats;

    public Board() {
        this.board = generateBlankBoard(10, 10);
        this.boats = new Boat[Config.getNbBoats()];
    }

    /**
     * Renvoyer une cellule spécifique du tableau
     * 
     * @param int x
     * @param int y
     *
     * @return Cell
     */
    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    /**
     * Affiche le tableau personnel
     *
     * @return void
     */
    public void showPersonnalBoard() {
        System.out.println("  ╔═══════════════════════════════════════╗");
        System.out.println("  ║              Votre grille             ║");
        System.out.println("  ╚═══════════════════════════════════════╝");
        System.out.println();
        System.out.println("    1   2   3   4   5   6   7   8   9  10 ");

        for (int j = 0; j < this.board.length; j++) {

            if (j == 0) {
                System.out.println("  ╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗");
            }

            for (int i = 0; i < this.board[j].length; i++) {
                if (i == 0) {
                    System.out.print(CoordinateHelper.numberCoordinateToLetter(j) + " ║");
                } else {
                    System.out.print("║");
                }

                if (this.board[i][j].getId() > 0) {
                    System.out.print(" " + String.valueOf(this.board[i][j].getId()) + " ");
                } else if (this.board[i][j].getId() == -1) {
                    System.out.print(" X ");
                } else {
                    System.out.print("   ");
                }

                if (i == this.board[j].length - 1) {
                    System.out.print("║");
                }
            }
            System.out.println();

            if (j == this.board.length - 1) {
                System.out.println("  ╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝");
            } else {
                System.out.println("  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣");
            }
        }
        System.out.println();
    }

    /**
     * Affiche le plateau de jeu, c’est-à-dire le plateau des deux joueurs
     * 
     * @param Player player
     * @param Player enemy
     *
     * @return void
     */
    public void showPlayBoard(Player player, Player enemy) {
        ConsoleHelper.eraseConsole();

        int titleBlanks1 = 39 - (14 + player.playerName.length());
        int titleBlanks2 = 39 - (10 + enemy.getPlayerName().length());

        Cell[][] enemyBoard = enemy.getBoard().getCells();

        System.out.println("  ═════════════════════════════════════════        ═════════════════════════════════════════");
        System.out.print("   ");
        TextHelper.generateSpaceBlanks(titleBlanks1 / 2);
        System.out.print("Votre grille, " + player.playerName);
        TextHelper.generateSpaceBlanks(titleBlanks1 / 2);
        System.out.print("          ");
        TextHelper.generateSpaceBlanks(titleBlanks2 / 2);
        System.out.print("Grille de " + enemy.getPlayerName());
        TextHelper.generateSpaceBlanks(titleBlanks2 / 2);
        System.out.println(" ");
        System.out.println("  ═════════════════════════════════════════        ═════════════════════════════════════════");
        System.out.println();
        System.out.println("    1   2   3   4   5   6   7   8   9  10            1   2   3   4   5   6   7   8   9  10");

        for (int j = 0; j < board.length; j++) {

            if (j == 0) {
                System.out.println("  ╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗        ╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗");
            }

            // On boucle sur les colonnes de la première grille (grille du joueur)
            for (int i = 0; i < board[j].length; i++) {
                if (i == 0) {
                    System.out.print(CoordinateHelper.numberCoordinateToLetter(j) + " ║");
                } else {
                    System.out.print("║");
                }

                // S’il s’agit du dernier tir effectué par l’adversaire, il est affiché en bleu
                if (i == enemy.getLastCellShot().getX() && j == enemy.getLastCellShot().getY()) {
                    System.out.print(TextHelper.ANSI_CYAN_BACKGROUND);
                }

                if (board[i][j].getId() > 0 && board[i][j].isShot()) {
                    System.out.print(TextHelper.ANSI_RED + " X " + TextHelper.ANSI_RESET);
                } else if (board[i][j].getId() == 0 && board[i][j].isShot()) {
                    System.out.print(TextHelper.ANSI_BLUE + " O " + TextHelper.ANSI_RESET);
                } else if (board[i][j].getId() > 0 && !board[i][j].isShot()) {
                    System.out.print(" " + String.valueOf(board[i][j].getId()) + " ");
                } else {
                    System.out.print("   ");
                }

                // S’il s’agit du dernier tir effectué par l’adversaire, il est affiché en bleu. Nous l’avons remis ici en noir
                if (i == enemy.getLastCellShot().getX() && j == enemy.getLastCellShot().getY()) {
                    System.out.print(TextHelper.ANSI_BLACK_BACKGROUND);
                }

                if (i == board[j].length - 1) {
                    System.out.print("║");
                }
            }
            System.out.print("      ");
            // On boucle sur les colonnes de la deuxième grille (grille de l’adversaire sur laquelle sont marqués les coups précédents des joueurs)
            for (int i = 0; i < enemyBoard[j].length; i++) {
                if (i == 0) {
                    System.out.print(CoordinateHelper.numberCoordinateToLetter(j) + " ║");
                } else {
                    System.out.print("║");
                }

                if (enemyBoard[i][j].getId() > 0 && enemyBoard[i][j].isShot()) {
                    System.out.print(TextHelper.ANSI_RED + " X " + TextHelper.ANSI_RESET);
                } else if (enemyBoard[i][j].getId() == 0 && enemyBoard[i][j].isShot()) {
                    System.out.print(TextHelper.ANSI_BLUE + " O " + TextHelper.ANSI_RESET);
                } else {
                    System.out.print("   ");
                }

                if (i == enemyBoard[j].length - 1) {
                    System.out.print("║");
                }
            }
            System.out.println();

            if (j == board.length - 1) {
                System.out.println("  ╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝        ╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝");
            } else {
                System.out.println("  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣        ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣");
            }
        }

        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗  ╔════════════════════════════════════════════╗");
        System.out.print("║ " + player.playerName + ", voici l'état de vos troupes :");
        TextHelper.generateSpaceBlanks(46 - (34 + player.playerName.length()));
        System.out.println("║  ║  Voici les navires que vous avez coulés :  ║");

        if (player.getBoard().getNbBoats() != 0) {
            for (int i = 1; i <= player.getBoard().getNbBoats(); i++) {
                int lineSize = 4 + player.getBoard().getBoats(i).getName().length() + 4 + player.getBoard().getBoats(i).getSize() * 3 + 4;
                int blanksToGenerate = 46 - lineSize;

                System.out.print("║");
                System.out.print(" - " + player.getBoard().getBoats(i).getName() + " (" + player.getBoard().getBoats(i).getId() + ") ");
                TextHelper.generateSpaceBlanks(blanksToGenerate);
                for (int j = 0; j < player.getBoard().getBoats(i).getSize(); j++) {
                    System.out.print("[");
                    if (player.getBoard().getBoats(i).getCells(j).isShot()) {
                        System.out.print(TextHelper.ANSI_RED + "X" + TextHelper.ANSI_RESET);
                    } else {
                        System.out.print(" ");
                    }
                    System.out.print("]");
                }
                System.out.print("  ║  ║");
                lineSize = 4 + player.getBoard().getBoats(i).getName().length() + 3 + player.getBoard().getBoats(i).getSize() * 3;
                blanksToGenerate = 46 - lineSize;
                if (enemy.getBoard().getBoats(i).isSunk()) {
                    System.out.print(" - " + enemy.getBoard().getBoats(i).getName() + " ");
                    TextHelper.generateSpaceBlanks(blanksToGenerate);
                    for (int j = 0; j < enemy.getBoard().getBoats(i).getSize(); j++) {
                        System.out.print("[");
                        if (enemy.getBoard().getBoats(i).getCells(j).isShot()) {
                            System.out.print(TextHelper.ANSI_RED + "X" + TextHelper.ANSI_RESET);
                        } else {
                            System.out.print(" ");
                        }
                        System.out.print("]");
                    }
                    System.out.println(" ║");
                } else {
                    System.out.println("                                            ║");
                }
            }
        } else {
            System.out.println("║        Aucun navire pour le moment         ║");
        }
        System.out.println("╚════════════════════════════════════════════╝  ╚════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("╔════════════════════════════════════════════╗  ╔════════════════════════════════════════════╗");
        System.out.println("║ " + TextHelper.ANSI_BLUE + "O" + TextHelper.ANSI_RESET + " : tir raté de votre adversaire           ║  ║ " + TextHelper.ANSI_BLUE + "O" + TextHelper.ANSI_RESET + " : tir raté que vous avez effectué        ║");
        System.out.println("║ " + TextHelper.ANSI_RED + "X" + TextHelper.ANSI_RESET + " : tir réussi de votre adversaire         ║  ║ " + TextHelper.ANSI_RED + "X" + TextHelper.ANSI_RESET + " : tir réussi que vous avez effectué      ║");
        System.out.println("║ " + TextHelper.ANSI_CYAN_BACKGROUND + " " + TextHelper.ANSI_BLACK_BACKGROUND + " : dernier tir de votre adversaire        ║  ║                                            ║");
        System.out.println("║ 1 à " + Config.getNbBoats() + " : vos navires                        ║  ║                                            ║");
        System.out.println( "╚════════════════════════════════════════════╝  ╚════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Ajoute un bateau au réseau de bateaux existant
     * 
     * @param Boat boat
     * 
     * @return void
     */
    public void addBoat(Boat boat) {
        int ind = 0;
        while (this.boats[ind] != null) {
            ind++;
        }
        this.boats[ind] = boat;
        for (int i = 0; i < boat.getCells().length; i++) {
            this.board[boat.getCells()[i].getX()][boat.getCells()[i].getY()].addBoat(boat.getId());
        }
        nbBoats++;
    }

    /**
     * Vérifie que le bateau n’a pas de voisin immédiat
     *
     * @param Cell[] cells
     * 
     * @return boolean
     */
    public boolean existsNeighbors(Cell[] cells) {

        /*
         * On prend en paramètre les cellules potentielles occupées par le bateau. 
         * Nous regardons les cellules des deux points d’extrémité que nous stockons dans 4 variables.
         */
        int minX = cells[0].getX();
        int minY = cells[0].getY();
        int maxX = cells[cells.length - 1].getX();
        int maxY = cells[cells.length - 1].getY();

        int[][] cellsThatNeedTesting = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int[] c : cellsThatNeedTesting) {
                    int X = x + c[0];
                    int Y = y + c[1];

                    if (CoordinateHelper.isValid(X, Y) && this.board[X][Y].getId() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Vérifie que les coordonnées (simulées) d’un bateau ne dépassent pas les limites de la planche
     *
     * @param Cell[] cells
     * 
     * @return boolean
     */
    public boolean isInBoard(Cell[] cells) {
        for (Cell c : cells) {
            if (!CoordinateHelper.isValid(c.getX(), c.getY())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si les coordonnées d’un bateau se chevauchent avec celles d’un autre bateau
     *
     * @param Cell[] cells
     * 
     * @return boolean
     */
    public boolean existsOverlap(Cell[] cells) {
        for (int i = 0; i < cells.length; i++) {
            if (this.board[cells[i].getX()][cells[i].getY()].getId() != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prend une direction et une position et génère la liste des coordonnées
     * des cellules concernées
     *
     * @param int    x
     * @param int    y
     * @param String direction
     * @param int    boatSize
     * @param int    boatId
     * 
     * @return Cell[]
     */
    public Cell[] generateBoatCoordinates(int x, int y, String direction, int boatSize, int boatId) {
        Cell[] response = new Cell[boatSize];
        for (int i = 0; i < boatSize; i++) {
            if (direction.equals("H")) {
                response[i] = new Cell(x + i, y);
            } else if (direction.equals("V")) {
                response[i] = new Cell(x, y + i);
            }
            response[i].addBoat(boatId);
        }
        return response;
    }

    /**
     * Obtient l’onglet des bateaux d’un joueur
     * 
     * @return Boat[]
     */
    public Boat[] getBoats() {
        return boats;
    }

    /**
     * Obtenez un bateau de joueur
     * 
     * @param int boatId
     * 
     * @return Boat
     */
    public Boat getBoats(int boatId) {
        for (int i = 0; i < boats.length; i++) {
            if (boats[i].getId() == boatId) {
                return boats[i];
            }
        }
        return boats[0];
    }

    /**
     * Obtient l’onglet des cellules du tableau
     * 
     * @return Cell[][]
     */
    public Cell[][] getCells() {
        return board;
    }

    /**
     * Remplit le tableau de cellules vides
     * 
     * @param int w
     * @param int h
     * 
     * @return Cell[][]
     */
    private Cell[][] generateBlankBoard(int w, int h) {
        Cell[][] result = new Cell[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                result[i][j] = new Cell(i, j);
            }
        }
        return result;
    }

    /**
     * Renvoyer le nombre de bateaux
     * 
     * @return int
     */
    private int getNbBoats() {
        return nbBoats;
    }
}