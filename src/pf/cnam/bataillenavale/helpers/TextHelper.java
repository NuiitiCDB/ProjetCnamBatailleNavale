package pf.cnam.bataillenavale.helpers;

/**
 * Ensemble de méthodes et de propriétés utilisées pour afficher du texte
 */
public class TextHelper {

    // Constantes à appeler pour changer la couleur du texte ou de l’arrière-plan
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    /**
     * Affiche les espaces les uns après les autres
     *
     * @param int spaceSize : the number of spaces to generate
     * 
     * @return void
     */
    public static void generateSpaceBlanks(int spaceSize) {
        for (int i = 0; i < spaceSize; i++) {
            System.out.print(" ");
        }
    }

}