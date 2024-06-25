package pf.cnam.bataillenavale;
/**
 * Config : fichier de configuration du jeu
 */
public abstract class Config {

    /**
     * Les bateaux et leur configuration
     * {:id, :name, :size}
     */
    private static final String[][] boats =
    {
        {"1", "Porte-avions", "5"},
        {"2", "Croiseur", "4",},
        {"3", "Contre-torpilleur", "3"},
        {"4", "Contre-torpilleur", "3"},
        {"5", "Torpilleur", "2"}
    };

    /**
     * Renvoie la configuration d’un bateau donné
     * 
     * @param int boatId
     *
     * @return String[]
     */
    public static String[] getBoatsConfig(int boatId) {
        if (boatId < 0 || boatId >= boats.length) {
            return new String[0];
        }
        return boats[boatId];
    }

    /**
     * Renvoie la configuration de chaque bateau
     *
     * @return String[][]
     */
    public static String[][] getBoatsConfig() {
        return boats;
    }

    /**
     * Renvoie le nombre de bateaux
     *
     * @return int
     */
    public static int getNbBoats() {
        return boats.length;
    }
}