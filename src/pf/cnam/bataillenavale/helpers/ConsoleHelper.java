package pf.cnam.bataillenavale.helpers;

/**
 * Ensemble de méthodes et de propriétés utiles pour la console et l’affichage
 */
public class ConsoleHelper {
    
    /**
     * Efface l’écran de la console
     *
     * @return void
     */
    public static void eraseConsole() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("Linux")){
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("cmd", "/c", "clear").inheritIO().start().waitFor();
            }
        } catch (final Exception e){
            System.out.println("Erreur : " + e);
        }
    }

    /**
     * Pauses dans l’exécution du programme
     * 
     * @param long millies
     *
     * @return void
     */
    public static void sleep(long millies) {
        try {
            Thread.sleep(millies);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}