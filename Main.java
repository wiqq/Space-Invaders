import controller.Controller;
import model.Model;
import view.View;

import java.awt.EventQueue;

/**
 *<p>
 * Projekt to gra okienkowa "Space Invaders". Jest podzielony na moduły MVC.
 * Grafika okienkowa opiera się na Swing, a rysowanie obrazków na AWT.<br>
 *
 * Aby wygrać, gracz musi zestrzelić wszystkich kosmitów, przy okazji nie dając
 * się zabić. Gracz ma 3 życia i może oddać 5 strzałów na raz. Do przejścia są 3 poziomy.
 * Na ostatnim poziomie pojawia się boss, który jest trudniejszy do pokonania.
 * Gra wykonuje się, aż gracz nie wygra lub okienko gry nie zostanie zamknięte.
 *</p>
 *<p>
 * Klasa <code>Main</code> tworzy obiekty paneli
 * <code>View</code> i <code>Model</code>, a następnie
 * podaje je w argumencie konstruktora klasy <code>Controller</code>.<br>
 * Następnie tworzy się nowy wątek do obiektu <code>controller</code>
 * i w nim wywołuje się funkcję <code>run()</code>.
 * </p>
 *
 *  @author Wiktoria Kaczorkiewicz
 *  Grupa: 3AR
 *  Projekt: Space Invaders
 */
public class Main {


    public static void main(String[] args) {

        EventQueue.invokeLater( () -> {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(model, view);

        Thread animator = new Thread(controller);
        animator.start();

        } );
    }
}
