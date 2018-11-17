package view;

import model.Alien;
import model.Player;
import model.Shot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.lang.Math;

/**
 * Organizuje wszystkie okienka i grafikę. Korzysta z danych o obiekrach klas z modelu,
 * w celu ustalenia współrzędnych do rysowania (w pikselach).
 */
public class View implements ViewCommons{
    /**
     * Obiekt klasy zawierającej labele o konkretnych funkcjach w grze
     */
    private Label label = new Label();
    /**
     * Główny panel gry
     */
    private Panel panel;
    /**
     * Panel startowy gry - na nim jest przycisk startowy
     */
    private StartPanel startPanel = new StartPanel();
    /**
     * Główna ramka gry zawierająca wszystkie inne komponenty
     */
    private JFrame frame;
    /**
     * Przycisk startu umieszczany na panelu startowym
     */
    private JButton startButton;
    /**
     * Labele różnych funkcji
     */
    private JLabel scoreLabel, levelLabel, lifeLabel, infoLabel;

    /**
     * Tworzony jest przycisk startu i ustawiane są jego parametry. Następnie ustawia się
     * labele, po czym na panel startowy dodawane są: przycisk startowy oraz label informacyjny.
     * Na koniec tworzona jest główna ramka gry.
     */
    public View() {
        startButton = new JButton("Start");
        startButton.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE));
        startButton.setBackground(Color.WHITE);
        startButton.setBounds(BOARD_WIDTH/2 - 50, BOARD_HEIGHT/2 - 50, 100, 50);

        setScoreLabel();
        setLevelLabel();
        setLifeLabel();
        setInfoLabel();

        startPanel.add(startButton);
        startPanel.add(infoLabel);

        frame = makeFrame();
    }

    /**
     * Ustawiane są parametry ramki takie jak: rozmiar, tytuł, operację zamknięcia, widoczność.
     * Dodaje się panel startowy do ramki
     * @return ramka gry
     */
    private JFrame makeFrame() {
        frame = new JFrame();

        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT + LABEL_HEIGHT);
        frame.setTitle("Space Invaders");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.add(startPanel);

        return frame;
    }

    /**
     * Usuwa widoczność panelu startowego, dodaje labele funkcyjne. Do rysowania potrzeba informacji
     * o obiektach, więc importuje się je. Uwidacznia się panel gry i dodaje się go do ramki.
     * @param aliens importowani kosmici
     * @param player importowany gracz
     * @param shots importowane strzały
     */
    public void startGamePanel(ArrayList<Alien> aliens, Player player, ArrayList<Shot> shots) {
        startPanel.setVisible(false);

        panel = new Panel();
        panel.add(scoreLabel);
        panel.add(levelLabel);
        panel.add(lifeLabel);

        panel.importAliens(aliens);
        panel.importPlayer(player);
        panel.importShots(shots);

        panel.setVisible(true);


        frame.add(panel);
    }

    /**
     * Pokazuje panel na całe okienko, na którym wyświetlany jest tekst
     * @param string tekst do pokazania
     * @param color jego kolor
     */
    public void showMessagePanel(String string, Color color) {
        startPanel.setMesg(string, color);
        startPanel.setVisible(true);

        startButton.setVisible(false);
        infoLabel.setVisible(false);
    }

    /**
     * Sprawia, że panel z tekstem przestaje być widoczny
     */
    public void hideMessagePanel() { startPanel.setVisible(false);}

    /**
     * Resetuje panel startowy i jego komponenty
     */
    public void restartGamePanel() {
        startPanel.setMesg("New game", Color.WHITE);
        startButton.setVisible(true);
        infoLabel.setVisible(true);
    }

    //************************************************************ LABELS:
    private void setScoreLabel() { scoreLabel = label.getScoreLabel(); }

    /**
     * Przekazuje się aktualny wynik do labelu z wynikiem
     * @param score wynik gracza
     */
    public void updateScoreLabel(int score) { scoreLabel.setText("Score: " + score); }

    private void setLevelLabel() { levelLabel = label.getLevelLabel(); }

    /**
     * Przekazuje się aktualny poziom do labelu z poziomem
     * @param level aktualny poziom gry
     */
    public void updateLevelLabel(int level) { levelLabel.setText("Level: " + level + " "); }

    private void setLifeLabel() { lifeLabel = label.getLifeLabel(); }

    private void setInfoLabel() { infoLabel = label.getInfoLabel(); }
    //************************************************************ PAINT:

    /**
     * Rysowanie kosmitów po zaktualizowaniu danych o nich
     * @param aliens kosmici, od których poierane są informacje
     */
    public void paintAliens(ArrayList<Alien> aliens) {
        panel.importAliens(aliens);
        panel.repaint();
    }

    /**
     * Rysowanie gracza po zaktualizowaniu danych o nim
     * @param player gracz, od którego pobierane są informacje
     */
    public void paintPlayer(Player player) {
        panel.importPlayer(player);
        panel.repaint();
    }

    //********************************************* getting size of images:

    /**
     * Analizuje szerokość obrazka kosmitów i wybiera większą z nich
     * @return większa szerokość obrazka kosmity
     */
    public int getAlienWidth() {
        return Math.max(panel.getImgStorage().getImg(ALIEN_IMG).getWidth(),
                        panel.getImgStorage().getImg(ALIEN_IMG_2).getWidth());
    }

    /**
     * Analizuje wysokość obrazka kosmitów i wybiera większą z nich
     * @return większa wysokość obrazka kosmity
     */
    public int getAlienHeight() {
        return Math.max(panel.getImgStorage().getImg(ALIEN_IMG).getHeight(),
                        panel.getImgStorage().getImg(ALIEN_IMG_2).getHeight());
    }

    public int getBossWidth() { return panel.getImgStorage().getImg(BOSS_IMG).getWidth(); }
    public int getBossHeight() { return panel.getImgStorage().getImg(BOSS_IMG).getHeight(); }

    public int getBossBombWidth() { return panel.getImgStorage().getImg(BOSS_IMG_BOMB).getWidth(); }
    public int getBossBombHeight() { return panel.getImgStorage().getImg(BOSS_IMG_BOMB).getHeight(); }

    public int getBombWidth() { return panel.getImgStorage().getImg(BOMB_IMG).getWidth(); }
    public int getBombHeight() { return panel.getImgStorage().getImg(BOMB_IMG).getHeight(); }

    public int getPlayerWidth() { return panel.getImgStorage().getImg(PLAYER_IMG).getWidth(); }
    public int getPlayerHeight() { return panel.getImgStorage().getImg(PLAYER_IMG).getHeight(); }

    public int getShotWidth() { return panel.getImgStorage().getImg(SHOT_IMG).getWidth(); }
    public int getShotHeight() { return panel.getImgStorage().getImg(SHOT_IMG).getHeight(); }

    //************************************************ getting elements:
    public JButton getStartButton() { return startButton; }
    public JPanel getPanel() { return panel; }
}
