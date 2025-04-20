import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import config.AppConfig;

public class Main {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            context.getBean(Interface.MainMenu.class).showMainMenu();
        }
    }
}