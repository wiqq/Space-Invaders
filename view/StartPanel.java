package view;

import javax.swing.*;
import java.awt.*;

/**
 * Konkretyzacja klasy <code>JPanel</code>, która odpowiada panelowi startowemu gry.<br>
 * W pewnych momentach służy jako panel do pokazywania tekstu na czarnym tle.
 */
class StartPanel extends JPanel implements ViewCommons{
    /**
     * Tekst panelu
     */
    private String mesg;
    /**
     * Kolor tego tekstu
     */
    private Color color;

    /**
     * Ustawia się czarne tło, położenie i rozmiar, widoczność, pokazywany tekst i jego
     * początkowy kolor - biały.
     */
    StartPanel() {
        setBackground(Color.BLACK);
        setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        setLayout(null);
        setVisible(true);

        mesg = "New game";
        color = Color.WHITE;
    }

    /**
     * Główną funkcją jest pobranie wielkości i umieszczenie tekstu na środku ekranu.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(color);
        Font font = new Font("Monospaced", Font.BOLD, FONT_SIZE* 2);
        FontMetrics param = this.getFontMetrics(font);
        g.setFont(font);
        g.drawString(mesg, (BOARD_WIDTH - param.stringWidth(mesg))/2, BOARD_HEIGHT/2 - 100);
    }

    /**
     * Ustawia się tekst panelu i jego kolor
     * @param mesg tekst panelu
     * @param color jego kolor
     */
    void setMesg(String mesg, Color color) {
        this.mesg = mesg;
        this.color = color;
    }
}
