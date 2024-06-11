package pf.cnam.bataillenavale;
import java.util.Random;
import java.util.regex.Pattern;

import pf.cnam.bataillenavale.helpers.ConsoleHelper;
import pf.cnam.bataillenavale.helpers.CoordinateHelper;

/**
 * Représente un joueur informatique
 */
public class PlayerComputer extends Player {

    private int difficulty;
    private Cell[] knownBoatCells = new Cell[0];
    private Cell[] potentialCells = new Cell[0];
    private int currentBoat = 0;

    public PlayerComputer() {
        setDifficulty();
        setPlayerName();
    }

    /**
     * Tire sur une cellule ennemie déterminée par différents algorithmes pour différents niveaux de difficulté
     * 
     * @param Player enemy
     * 
     * @return void
     */
    protected void shoot(Player enemy) {
        for (int i = 0; i < 2; i++) {
            ConsoleHelper.eraseConsole();
            System.out.print(playerName + " est en train de jouer");
            ConsoleHelper.sleep(375);
            System.out.print(".");
            ConsoleHelper.sleep(375);
            System.out.print(".");
            ConsoleHelper.sleep(375);
            System.out.print(".");
        }
        System.out.println();

        boolean error = true;
        Cell targetCell = new Cell(-1, -1);

        while (error) {

            switch (difficulty) {
                case 1:
                    targetCell = enemy.getBoard().getCells()[(int) (Math.random() * 10)][(int) (Math.random() * 10)];
                    break;
                case 2:

                    if (currentBoat != 0) {
                        targetCell = potentialCells[(int) (Math.random() * potentialCells.length)];
                        if (knownBoatCells.length > 1) {
                            updateNonPotentialCells(knownBoatCells, enemy);
                        }
                    } else {
                        targetCell = enemy.getBoard()
                                .getCells()[(int) (Math.random() * 10)][(int) (Math.random() * 10)];
                    }
                    break;
                case 3:
                    int[] coordinates = nickBerryAlgortithm(enemy.getBoard());
                    targetCell = enemy.getBoard().getCells()[coordinates[0]][coordinates[1]];
                    break;
            }

            if (!targetCell.isShot() && targetCell.isPotential()) {
                if (targetCell.getId() > 0) {
                    currentBoat = targetCell.getId();
                    enemy.getBoard().getBoats(targetCell.getId()).getCells(targetCell.getX(), targetCell.getY())
                            .shoot();
                    knownBoatCells = cellArrayAdd(knownBoatCells, targetCell);
                    potentialCells = updatePotentialCells(knownBoatCells, enemy);
                    incrementStatNbSuccessfullShot();
                    if (enemy.getBoard().getBoats(targetCell.getId()).isSunk()) {
                        incrementStatNbBoatShot();
                        potentialCells = updatePotentialCells(knownBoatCells, enemy);
                        freezeCells(potentialCells, enemy);
                        currentBoat = 0;
                        knownBoatCells = new Cell[0];
                        potentialCells = new Cell[0];
                    }
                }
                setLastCellShot(targetCell.getX(), targetCell.getY());
                targetCell.shoot();
                error = false;
                incrementStatNbTotalShot();
            }
        }
    }

    /**
     * Ajoute automatiquement des bateaux sur la grille
     * 
     * @return void
     */
    protected void placeBoats() {
        ConsoleHelper.eraseConsole();
        System.out.println(playerName + " est en train de placer ses bateaux...");
        for (int i = 0; i < Config.getNbBoats(); i++) {
            Random rand = new Random();
            String direction = "";
            int x = -1;
            int y = -1;
            int boatSize = Integer.valueOf(Config.getBoatsConfig(i)[2]);
            boolean error = true;
            do {
                int randomDirection = rand.nextInt(2);
                if (randomDirection == 1) {
                    direction = "H";
                    int xLimit = 10 - boatSize;
                    x = rand.nextInt(xLimit + 1);
                    y = rand.nextInt(11);
                } else {
                    direction = "V";
                    int yLimit = -boatSize + 10;
                    x = rand.nextInt(yLimit + 1);
                    y = rand.nextInt(11);
                }
                Cell[] boatCoordinates = board.generateBoatCoordinates(x, y, direction, boatSize,
                        Integer.valueOf(Config.getBoatsConfig(i)[0]));
                if (board.isInBoard(boatCoordinates) && !board.existsOverlap(boatCoordinates)
                        && !board.existsNeighbors(boatCoordinates)) {
                    board.addBoat(new Boat(boatCoordinates, Integer.valueOf(Config.getBoatsConfig(i)[0]),
                            Config.getBoatsConfig(i)[1]));
                    error = false;
                }
            } while (error);
        }
        ConsoleHelper.sleep(2500);
        System.out.println(playerName + " a placé tous ses bateaux.");
        ConsoleHelper.sleep(2000);
    }

    /**
     * Définit le nom de l’ordinateur
     * 
     * @return void
     */
    protected void setPlayerName() {
        switch (difficulty) {
            case 1:
                playerName = "Emma";
                break;
            case 2:
                playerName = "Jean-Jacques";
                break;
            case 3:
                playerName = "Nick Berry";
                break;
        }
    }

    /**
     * Définit le niveau de l’ordinateur
     * 
     * @return void
     */
    private void setDifficulty() {
        String input = "";
        
        System.out.println("╔═══════════╗  ╔══════════════════╗  ╔══════════════╗");
        System.out.println("║ 1. Facile ║  ║ 2. Intermédiaire ║  ║ 3. Difficile ║");
        System.out.println("╚═══════════╝  ╚══════════════════╝  ╚══════════════╝");
        System.out.println();

        do {
            System.out.print("Quel ordinateur souhaitez-vous affronter ? ");
            try {
                input = in.readLine();
            } catch (java.io.IOException e) {
                System.out.println("Une erreur est survenue : " + e);
            }
        } while (!Pattern.matches("[123]", input));
        this.difficulty = Integer.valueOf(input);
    }

    /**
     * Ajoute une cellule à un tableau de cellules
     * 
     * @param Cell[] array
     * @param Cell cell
     * 
     * @return Cell[]
     */
    private Cell[] cellArrayAdd(Cell[] array, Cell cell) {
        Cell[] result = new Cell[array.length + 1];
        for (int i = 0; i < result.length - 1; i++) {
            result[i] = array[i];
        }
        result[result.length - 1] = cell;
        return result;
    }

    /**
     * Utile pour le niveau de difficulté 3 : met à jour toutes les cellules nécessaires à un niveau de non
     * cellule potentielle
     * 
     * @param Cell[] array
     * @param Player player
     * 
     * @return void
     */
    private void freezeCells(Cell[] array, Player player) {
        for (Cell c : array) {
            player.getBoard().getCell(c.getX(), c.getY()).updatePotential(false);
        }
    }

    /**
     * Détermine les cellules qui peuvent être un bateau en fonction des cellules connues du bateau
     * 
     * @param Cell[] knownBoatCells
     * @param Player player
     * 
     * @return Cell[]
     */
    private Cell[] updatePotentialCells(Cell[] knownBoatCells, Player player) {

        Cell[] result = new Cell[0];

        int[][] testingCoordinates = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };

        System.out.println();

        for (Cell c : knownBoatCells) {
            for (int[] coordinates : testingCoordinates) {
                if (CoordinateHelper.isValid(c.getX() + coordinates[0], c.getY() + coordinates[1])) {

                    result = cellArrayAdd(result,
                            player.getBoard().getCell(c.getX() + coordinates[0], c.getY() + coordinates[1]));

                }
            }
        }
        return result;
    }

    /**
     * Définit les cellules voisines sur non potentiel
     * 
     * @param Cell[] array
     * @param Player player
     * 
     * @return void
     */
    private void updateNonPotentialCells(Cell[] array, Player player) {
        boolean vertical = array[0].getX() == array[1].getX();

        int[] tempCoordintates = { -1, 1 };

        if (vertical) {
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < 2; j++) {
                    int x = array[i].getX() + tempCoordintates[j];
                    if (CoordinateHelper.isValid(x, array[i].getY())) {
                        player.getBoard().getCell(x, array[i].getY()).updatePotential(false);
                    }
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < 2; j++) {
                    int y = array[i].getY() + tempCoordintates[j];
                    if (CoordinateHelper.isValid(array[i].getX(), y)) {
                        player.getBoard().getCell(array[i].getX(), y).updatePotential(false);
                    }
                }
            }
        }

    }

    /**
     * Renvoie un vecteur avec les positions déterminées par un algorithme inspiré de la méthode
     * Algorithme de Nick Berry. Cet algorithme utilise une approche propabiliste,
     * calculer toutes les positions possibles pour chaque bateau restant puis
     * renvoyer les coordonnées de la cellule qui a la plus grande probabilité de contenir un
     * bateau. Cela peut parfois entraîner des mouvements qui semblent irréalistes ou non
     * semblable à un humain mais, statistiquement parlant, ce sera toujours la meilleure décision.
     * 
     * En moyenne, cet algorithme a besoin de 40 mouvements pour trouver tous les navires sur le plateau.
     * 
     * Une explication plus complète peut être trouvée ici :
     * https://www.datagenetics.com/blog/december32011/
     * 
     * En fait, l’algorithme que vous voyez ci-dessous est encore plus puissant que celui vu
     * sur internet car celui-ci prend en considération le fait que les bateaux
     * ne peut pas se toucher, par exemple, s’il voit deux cellules se toucher
     * horizontalement, il sait qu’il n’a pas besoin de vérifier au-dessus et en dessous de ceux-ci
     * deux cellules car un bateau ne peut pas être là. Donc, en théorie, cet algorithme peut
     * Résolvez en moyenne un puzzle en moins de 40 coups.
     * 
     * @param Board board
     * 
     * @return int[]
     */
    private int[] nickBerryAlgortithm(Board board) {

        int[][] simplifiedBoard = new int[10][10];

        // simplifier le tableau au strict minimum, nous n’avons pas besoin de l’identifiant de la cellule et
        // d’autres choses

        for (int x = 0; x < simplifiedBoard.length; x++) {
            for (int y = 0; y < simplifiedBoard[0].length; y++) {
                if (!board.getCell(x, y).isShot()) {
                    simplifiedBoard[x][y] = 0; // Si aucune information n’est connue sur la cellule, définissez sur 1
                } else if (board.getCell(x, y).getId() == 0) {
                    simplifiedBoard[x][y] = 1; // S’il y a un échec, définissez sur 1
                } else {
                    simplifiedBoard[x][y] = 2; // Si la cellule est un succès, définissez sur 2
                }
            }
        }

        int numberOfBoatsLeft = 0;

        // Si un bateau est coulé, définissez toutes les cellules environnantes sur 1 : Un bateau ne peut pas être là.
        // Calculez également le nombre de bateaux restants

        int[][] testingCoordinates = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int i = 0; i < board.getBoats().length; i++) {
            if (!board.getBoats()[i].isSunk()) {
                numberOfBoatsLeft++;
            } else {
                for (Cell c : board.getBoats()[i].getCells()) {
                    for (int[] coordinates : testingCoordinates) {
                        if (CoordinateHelper.isValid(c.getX() + coordinates[0], c.getY() + coordinates[1])) {
                            simplifiedBoard[c.getX() + coordinates[0]][c.getY() + coordinates[1]] = 1;
                        }
                    }
                }
            }
        }

        // Si nous avons deux cellules voisines, un autre bateau ne peut pas se toucher, alors
        // mettez ces cellules à 1 : Un bateau ne peut pas être là

        Cell[] everyShotCells = new Cell[0];

        for (int x = 0; x < simplifiedBoard.length; x++) {
            for (int y = 0; y < simplifiedBoard[0].length; y++) {

                if (board.getCell(x, y).isShot() && board.getCell(x, y).getId() != 0) {
                    everyShotCells = cellArrayAdd(everyShotCells, board.getCell(x, y));
                }

            }
        }

        for (int i = 0; i < everyShotCells.length; i++) {
            for (int j = 0; j < everyShotCells.length; j++) {
                if (neighbors(everyShotCells[i], everyShotCells[j]) && j != i) {

                    boolean vertical = everyShotCells[i].getX() == everyShotCells[j].getX();

                    if (vertical) {
                        if (CoordinateHelper.isValid(everyShotCells[i].getX() - 1, everyShotCells[i].getY())) {
                            simplifiedBoard[everyShotCells[i].getX() - 1][everyShotCells[i].getY()] = 1;
                            simplifiedBoard[everyShotCells[j].getX() - 1][everyShotCells[j].getY()] = 1;
                        }
                        if (CoordinateHelper.isValid(everyShotCells[i].getX() + 1, everyShotCells[i].getY())) {
                            simplifiedBoard[everyShotCells[i].getX() + 1][everyShotCells[i].getY()] = 1;
                            simplifiedBoard[everyShotCells[j].getX() + 1][everyShotCells[j].getY()] = 1;
                        }
                    } else {
                        if (CoordinateHelper.isValid(everyShotCells[i].getX(), everyShotCells[i].getY() - 1)) {
                            simplifiedBoard[everyShotCells[i].getX()][everyShotCells[i].getY() - 1] = 1;
                            simplifiedBoard[everyShotCells[j].getX()][everyShotCells[j].getY() - 1] = 1;
                        }
                        if (CoordinateHelper.isValid(everyShotCells[i].getX(), everyShotCells[i].getY() + 1)) {
                            simplifiedBoard[everyShotCells[i].getX()][everyShotCells[i].getY() + 1] = 1;
                            simplifiedBoard[everyShotCells[j].getX()][everyShotCells[j].getY() + 1] = 1;
                        }
                    }

                }
            }
        }
        // Enregistrez la longueur de chaque bateau restant

        int[] lengthOfBoatsLeft = new int[numberOfBoatsLeft];

        int ind = 0;

        for (int i = 0; i < board.getBoats().length; i++) {
            if (!board.getBoats()[i].isSunk()) {
                lengthOfBoatsLeft[ind] = board.getBoats()[i].getSize();
                ind++;
            }
        }

        // Créez un tableau qui enregistre le nombre de fois que vous pouvez placer un bateau dans un
        // une cellule donnée

        int[][] probabilityBoard = new int[10][10];

        for (int x = 0; x < probabilityBoard.length; x++) {
            for (int y = 0; y < probabilityBoard[0].length; y++) {
                probabilityBoard[x][y] = 0;
            }
        }

        // remplir ledit tableau

        for (int b = 0; b < numberOfBoatsLeft; b++) { // pour chaque bateau restant
            for (int d = 0; d <= 1; d++) { // pour chaque direction : horizontale ou verticale
                for (int x = 0; x < probabilityBoard.length; x++) { // pour chaque position x
                    for (int y = 0; y < probabilityBoard[0].length; y++) { // pour chaque position y
                        boolean possible = true;
                        boolean containsNonSunkBoat = false;
                        for (int i = 0; i < lengthOfBoatsLeft[b]; i++) { // déterminez si vous pouvez placer le bateau ici
                            if (d == 0) {
                                if (probabilityBoard.length - x >= lengthOfBoatsLeft[b]) {
                                    if (simplifiedBoard[x + i][y] == 1) {
                                        possible = false;
                                    } else if (simplifiedBoard[x + i][y] == 2) { // déterminer qu’il y a une cellule d’accès dans
                                                                                 // ces coordonnées
                                        containsNonSunkBoat = true;
                                    }
                                } else {
                                    possible = false;
                                }
                            } else { // Même chose pour l’autre direction
                                if (probabilityBoard.length - y >= lengthOfBoatsLeft[b]) {
                                    if (simplifiedBoard[x][y + i] == 1) {
                                        possible = false;
                                    } else if (simplifiedBoard[x][y + i] == 2) {
                                        containsNonSunkBoat = true;
                                    }
                                } else {
                                    possible = false;
                                }
                            }
                        }
                        if (possible) { // si vous pouvez placer un bateau ici
                            for (int i = 0; i < lengthOfBoatsLeft[b]; i++) {
                                if (d == 0) {
                                    if (containsNonSunkBoat) {
                                        probabilityBoard[x + i][y] += 15; // Si une cellule de réception est dans le mix, ajoutez plus
                                                                          // de probabilité
                                    } else {
                                        probabilityBoard[x + i][y]++; // sinon, ajoutez 1 à la probabilité
                                    }
                                } else { // Même chose pour l’autre direction
                                    if (containsNonSunkBoat) {
                                        probabilityBoard[x][y + i] += 15;
                                    } else {
                                        probabilityBoard[x][y + i]++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Si une cellule est touchée, vous ne devez pas la définir comme votre cellule de taget : pour ce faire, définissez la
        // probabilité de cette cellule à 0

        for (int x = 0; x < probabilityBoard.length; x++) {
            for (int y = 0; y < probabilityBoard[0].length; y++) {
                if (simplifiedBoard[x][y] == 2) {
                    probabilityBoard[x][y] = 0;
                }
            }
        }

        // enregistrer les coordonnées de la cellule qui a la probabilité la plus élevée

        int finalX = -1;
        int finalY = -1;
        int maxProbability = 0;

        for (int x = 0; x < probabilityBoard.length; x++) {
            for (int y = 0; y < probabilityBoard[0].length; y++) {
                if (probabilityBoard[x][y] >= maxProbability) {
                    finalX = x;
                    finalY = y;
                    maxProbability = probabilityBoard[x][y];
                }
            }
        }

        int[] result = { finalX, finalY };
        return result;
    }

    /**
     * Renvoie si deux cellules sont voisines
     * 
     * @param Cell cell1
     * @param Cell cell2
     * 
     * @return boolean
     */
    private boolean neighbors(Cell cell1, Cell cell2) {
        int[][] neighborCells1 = { { 0 + cell1.getX(), 1 + cell1.getY() }, { 0 + cell1.getX(), -1 + cell1.getY() },
                { -1 + cell1.getX(), 0 + cell1.getY() }, { 1 + cell1.getX(), 0 + cell1.getY() } };

        for (int[] coor1 : neighborCells1) {
            if (coor1[0] == cell2.getX() && coor1[1] == cell2.getY()) {
                return true;
            }
        }
        return false;
    }
}