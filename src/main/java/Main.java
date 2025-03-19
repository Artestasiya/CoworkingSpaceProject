import Interface.MainMenu;
import Service.CustomClassLoader;

public class Main {
    public static void main(String[] args) {
        CustomClassLoader customClassLoader = new CustomClassLoader("classes/");

        try {
            Class<?> loadedClass = customClassLoader.loadClass("main.java.Data.CoworkingSpace");
            System.out.println("Class loaded: " + loadedClass.getName());
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load class: " + e.getMessage());
            e.printStackTrace();
        }

        MainMenu mainMenu = new MainMenu();
        mainMenu.showMainMenu();
    }
}