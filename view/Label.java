package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Różnego rodzaju labele umieszczane na panelach.
 */
class Label implements ViewCommons{
    /**
     * Obwódka niektórych z labeli
     */
    private Border border;

    /**
     * Konkretyzacja obwódki,, ustawia się jego kolor - ciemnoszary.
     */
    Label() { border = BorderFactory.createLineBorder(Color.DARK_GRAY); }

    /**
     * Ustawienia dotyczące labelu na którym pokazywany jest wynik.
     * @return label wyniku
     */
    JLabel getScoreLabel() {
        JLabel scoreLabel = new JLabel("Score: " + 0);
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE));
        scoreLabel.setForeground(Color.WHITE);

        scoreLabel.setBounds(0, 0, BOARD_WIDTH/2, LABEL_HEIGHT);
        scoreLabel.setBorder(border);

        return scoreLabel;
    }

    /**
     * Ustawienia dotyczące labelu na którym pokazywany jest poziom
     * @return label poziomu gry
     */
    JLabel getLevelLabel() {
        JLabel levelLabel = new JLabel("Level: " + 1 + " ", SwingConstants.RIGHT);
        levelLabel.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE));
        levelLabel.setForeground(Color.WHITE);

        levelLabel.setBounds(BOARD_WIDTH/2, 0, BOARD_WIDTH/2, LABEL_HEIGHT);
        levelLabel.setBorder(border);

        return levelLabel;
    }

    /**
     * Ustawienia dotyczące labelu na którym pokazana jest liczba pozostałych
     * graczowi żyć
     * @return label żyć
     */
    JLabel getLifeLabel() {
        JLabel lifeLabel = new JLabel(" Lives: ", SwingConstants.LEFT);
        Font font = new Font("Monospaced", Font.BOLD, FONT_SIZE);

        lifeLabel.setFont(font);
        lifeLabel.setForeground(Color.WHITE);
        lifeLabel.setBounds(BOARD_WIDTH/2, 0, BOARD_WIDTH/2, LABEL_HEIGHT);

        return lifeLabel;
    }

    /**
     * Ustawienia dotyczące labelu na którym pokazuje się informacje o sterowaniu
     * @return label informacyjny
     */
    JLabel getInfoLabel() {
        JLabel infoLabel = new JLabel("<html>INFO:<br>Arrow keys - control the starship<br>" +
                "SPACE - make a shot</html>" , SwingConstants.CENTER);
        Font font = new Font("Monospaced", Font.BOLD, FONT_SIZE);

        infoLabel.setFont(font);
        infoLabel.setForeground(Color.BLUE);
        infoLabel.setBounds(0, BOARD_HEIGHT/5 - 20, BOARD_WIDTH, BOARD_HEIGHT);

        return infoLabel;
    }
}
