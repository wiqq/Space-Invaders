package controller;

import model.Model;
import model.ModelCommons;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pobieranie informacji o panelach view i model. Zawiera główną pętlę gry.
 */
public class Controller implements Runnable, ModelCommons
{
    private View view;
    private Model model;
    /**
     * Obiekt pomagający ustawić pobieranie informacji z klawiatury
     */
    private KeyboardControl keyboardControl;

    private ActionListener actionListener;
    /**
     * Informacja, czy gracz jest aktywny
     */
    private boolean inGame;

    /**
     * Przekazanie informacji o panelach model i view. Ustawienie początkowej wartości
     * <code>inGame</code> na <code>false</code>.
     */
    public Controller(Model model, View view) {

        this.view = view;
        this.model = model;

        inGame = false;
    }

    /**
     * Ustawienie obiektów w stanie gotowości.
     */
    private void setObjects() {
        model.setAliens(view.getAlienWidth(), view.getAlienHeight());
        model.setPlayer(view.getPlayerWidth()/2, view.getPlayerHeight()/2);
    }

    /**
     * Obsługa przycisku włączającego grę. Gdy się w niego kliknie, wartość
     * <code>inGame</code> zmienia się na <code>true</code>, ustawia się panel gry,
     * panel startu, obiekty ustawiają się w gotowości oraz następuje przypisanie
     * konkretnych akcji do klawiszy klawiatury.
     */
    private void buttonControl() {
        actionListener = actionEvent -> {
            inGame = true;
            view.startGamePanel(model.getAliens(), model.getPlayer(), model.getShots());
            view.showMessagePanel("Start in", Color.WHITE);
            setObjects();
            keyBindings();
        };
    }

    /**
     * Zawier główną pętlę gry, która wykonuje się, tylko wtedy, kiedy gracz jest aktywny
     * (<code>ingame == true</code>).<br>
     * Na początku gracz porusza się, a potem po kolei przechodzi przez funkcje w modelu,
     * które zmieniają stan poszczególnych obiektów. Następnie rysuje się obiekty po
     * dokonanych zmianach. Potem sprawdzany jest warunek, czy gracz nie przeszedł do
     * kolejnego poziomu. Jeśli tak się stało, to wyświetlane są odpowiednie panele
     * informacyjne. Następnie uaktualnia się <code>label</code> z informacją o uzyskanym
     * wyniku, pozostałych życiach i poziomie gry. Na koniec stprawdza się, czy gracz stracił
     * wsystkie życia. Jeśli tak się stało, to przerywa się grę (pętlę) i gracz zostaje
     * przeniesiony z powrotem do panelu startowego gry.
     */
    private void game() {
        while (inGame) {
            model.setPlayer(keyboardControl.getPlayer());
            model.movePlayer(view.getPlayerWidth(), view.getBombWidth(), view.getBombHeight(), view.getBossBombWidth(), view.getBossBombHeight());
            model.moveShots(view.getShotWidth(), view.getShotHeight(), view.getAlienWidth(), view.getAlienHeight(), view.getBossWidth(), view.getBossHeight());
            model.moveAliens(view.getAlienWidth(), view.getBossWidth(), view.getAlienHeight(), view.getPlayerHeight());

            model.setBomb(view.getBossWidth(), view.getBossHeight(), view.getPlayerWidth());
            model.moveBomb(view.getBombHeight());

            view.paintAliens(model.getAliens());
            view.paintPlayer(model.getPlayer());

            if(model.isActualised(view.getAlienWidth(), view.getAlienHeight())) {
                if(model.isGameFinished()) {
                    view.showMessagePanel("Game won!", Color.RED);
                    makeDelay(2000);
                    return;
                }
                else {
                    view.showMessagePanel("Level " + model.getLevel(), Color.BLUE);

                    if(model.getLevel() == 3) {
                        makeDelay(500);
                        view.showMessagePanel("<html> <br> <br>It's a BOSS!</html>", Color.RED);
                    }
                }

                makeDelay(2000);
                view.hideMessagePanel();
            }

            view.updateScoreLabel(model.getPlayer().getKills());
            view.updateLevelLabel(model.getLevel());

            makeDelay(20);

            if(!model.getPlayer().isVisible()) inGame = false;
        }
        restartGame();
    }

    /**
     * Ustawienie wartości początkowych po przegranej grze. Ustawia się takie parametry jak:
     * poziom gry, wynik gracza, obiekty z modelu jak po przejściu do kolejnego poziomu.<br>
     * Pokazuje się również panel z informacją o przegranej, a nastepnie następuje restart
     * głównego panelu gry.
     */
    private void restartGame() {
        model.setLevel((short)1);
        model.getPlayer().resetScore();
        model.restartObjects();

        view.showMessagePanel("You lost!", Color.RED);
        makeDelay(2000);
        view.restartGamePanel();
    }

    /**
     * Nadpisana funkcja, wywołuje się przy wywołaniu <code>start()</code> wątku. <br>
     * Zawiera w sobie pętlę nieskończoną, w której kolejno:<br>
     *     *czeka się, aż użytkownik naciśnie przysisk startu <br>
     *     *kiedy już naciśnie, wywołuje się panel przygotowujący gracza do startu gry<br>
     *     *wywołuje się główna pętla gry<br>
     *     *gdy pętla gry zostanie przerwana, restartuje się przycisk startowy<br>
     *     *kiedy gracz wygra, następuje przerwanie pętli
     */
    @Override
    public void run() {
        while(true) {
            buttonControl();
            view.getStartButton().addActionListener(actionListener);

            while(!inGame) {
                makeDelay(40);
            }

            getReadyPanel();
            game();
            view.getStartButton().removeActionListener(actionListener);

            if(model.isGameFinished()) break;
        }
    }

    /**
     * Ustawienie akcji dla konkretnych przycisków, jeśli są wciśnięte. Nadaje się je dla
     * klawiszy "lewo" i "prawo". Nastepnie dla chwilowo wciśjniętej spacji ustawia się
     * akcję strzału.
     */
    private void keyBindings() {
        keyboardControl = new KeyboardControl(view.getPanel(), 24, model.getPlayer());

        keyboardControl.addAction("LEFT", -PLAYER_SPEED);
        keyboardControl.addAction("RIGHT", PLAYER_SPEED);

        view.getPanel().getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"), "SPACE");
        view.getPanel().getActionMap().put("SPACE", makeShot);
    }

    /**
     * Kiedy naciskana jest spacja, wywołuje się funkcja, która tworzy nowy strzał gracza
     */
    private Action makeShot = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            model.addShot(view.getPlayerWidth());
        }
    };

    /**
     * Sprawia, że program zasypia na zadaną ilość czasu
     * @param delay ilość czasu, na jaką program ma zasnąć (w milisekundach)
     */
    private void makeDelay(int delay) {
        try {
            Thread.sleep(delay);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Przygotowuje gracza do rozpoczęcia gry. Wyświetla ekran odliczania.
     */
    private void getReadyPanel() {
        view.showMessagePanel("Start in", Color.WHITE);
        makeDelay(1000); view.hideMessagePanel();

        view.showMessagePanel("3", Color.YELLOW);
        makeDelay(800); view.hideMessagePanel();
        view.showMessagePanel("2", Color.ORANGE);
        makeDelay(800); view.hideMessagePanel();
        view.showMessagePanel("1", Color.RED);
        makeDelay(800); view.hideMessagePanel();
    }
}