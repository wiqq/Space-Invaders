package controller;

import model.ModelCommons;
import model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Służy do pobierania wciśniętych klawiszy od użytkownika
 */
public class KeyboardControl implements ActionListener, ModelCommons {
    private JComponent component;
    private Timer timer;
    /**
     * Informacja o wciśniętych klawiszach
     */
    private Map<String, Integer> pressedKeys = new HashMap<>();

    private Player player;

    /**
     * Ustawienie aktywnego komponentu, przesłanie informacji o graczu oraz
     * rozpoczęcie odliczania zegara.
     * @param component komponent, z którego pobierane będą informacje
     * @param delay opóźnienie pobierania klawiszy
     * @param player obiekt gracza, od którego pobierane będą informacje
     */
    KeyboardControl(JComponent component, int delay, Player player) {
        this.component = component;
        this.player = player;

        timer = new Timer(delay, this);
        timer.setInitialDelay(0);
    }

    Player getPlayer() { return player; }

    /**
     * Dodanie akcji do konkretnego klawisza. Akcja polega na zmianie liczby, która
     * będzie potem służyła do przesunięcia gracza w poziomie. Od razu tworzy się
     * akcja dla klawisza wciśniętego i puszczonego.
     * @param keyStroke wciśnięty klawisz
     * @param deltaX wartość, o jaką ma się przesunąć gracz
     */
    void addAction(String keyStroke, int deltaX) {
        int offset = keyStroke.lastIndexOf(" ");
        String key = offset == -1 ? keyStroke : keyStroke.substring(offset + 1);
        String modifiers = keyStroke.replace(key, "");

        InputMap inputMap = component.getInputMap(IFW);
        ActionMap actionMap = component.getActionMap();

        Action pressedAction = new KeyboardControl.AnimationAction(key, deltaX);
        String pressedKey = modifiers + PRESSED + key;
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKeyStroke, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        Action releasedAction = new KeyboardControl.AnimationAction(key, 0);
        String releasedKey = modifiers + RELEASED + key;
        KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKeyStroke, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    /**
     * Sprawdza się, czy jakiś klawisz został wciśnięty. Jeśli tak, to zegar
     * rozpoczyna odliczanie. Jeśli wszystkie klawisze zostają puszczone,
     * gracz zatrzymuje się.
     * @param key wciśnięty klawisz
     * @param deltaX wartość przesunięcia gracza
     * @param player obiekt gracza
     */
    private void handleKeyEvent(String key, int deltaX, Player player) {
        if(deltaX == 0) pressedKeys.remove(key);
        else pressedKeys.put(key, deltaX);

        if(pressedKeys.size() == 1) timer.start();

        if(pressedKeys.size() == 0) {
            timer.stop();
            player.stopMove();
        }
    }

    /**
     * Wywołuje metodę poruszania obiektem dla wciśniętego klawisza
     */
    public void actionPerformed(ActionEvent e) { moveComponent(); }

    /**
     * W zależności od wciśniętych klawiszy gracz przesuwa się w lewo lub w prawo
     */
    private void moveComponent() {
        int deltaX = 0;

        for(Integer delta: pressedKeys.values()) {
            deltaX = delta;
        }

        if(deltaX == PLAYER_SPEED) player.moveRight();
        else if(deltaX == -PLAYER_SPEED) player.moveLeft();
    }

    /**
     * Konkretyzacja klasy akcji. Reprezentuje akcję poruszania się.
     */
    private class AnimationAction extends AbstractAction implements ActionListener {
        /**
         * Zmiana położenia
         */
        private int moveDelta;

        /**
         * Ustawienie zmiany położenia
         */
        AnimationAction(String key, int moveDelta) {
            super(key);
            this.moveDelta = moveDelta;
        }

        /**
         * Konkretyzacja akcji wykonującej się po wciśnięciu klawisza
         */
        public void actionPerformed(ActionEvent e) {
            handleKeyEvent((String) getValue(NAME), moveDelta, player);
        }
    }
}
