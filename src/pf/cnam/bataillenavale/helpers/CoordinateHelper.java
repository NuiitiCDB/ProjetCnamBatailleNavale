package pf.cnam.bataillenavale.helpers;

/**
 * Ensemble des méthodes et propriétés utilisées pour les coordonnées du jeu
 */
public class CoordinateHelper {

    /**
     * Transforme une coordonnée « lettre » par son « nombre » équivalent
     *
     * @param String coordinate : the coordinate to transform
     * 
     * @return String
     */
    public static String letterCoordinateToNumber(String coordinate) {
        return String.valueOf(new String("ABCDEFGHIJ").indexOf(coordinate));
    }

    /**
     * Transforme une coordonnée « nombre » par son équivalent « lettre »
     *
     * @param int coordinate : the coordinate to transform
     * 
     * @return String
     */
    public static String numberCoordinateToLetter(int coordinate) {
        String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
        return letters[coordinate];
    }

    /**
     * Vérifie si les coordonnées ne sortent pas du tableau
     *
     * @param int x
     * @param int y
     * 
     * @return boolean
     */
    public static boolean isValid(int x, int y) {
        return (x >= 0 && x <= 9 && y >= 0 && y <= 9);
    }
}