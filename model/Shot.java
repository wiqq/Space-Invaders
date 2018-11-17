package model;

import view.ViewCommons;

/**
 * Reprezentuje strzał gracza. Zawiera metodę, która porusza strzałem.
 */
public class Shot extends Sprite implements ModelCommons, ViewCommons {
    /**
     * Ustawia współrzędne strzału
     * @param x współrzędna pozioma
     * @param y współrzędna pionowa
     */
    Shot(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Ruch strzału w do góry. Zawiera warunek, żeby strzał dezaktywować,
     * kiedy dotrze do górnej krawędzi okienka.
     */
    void move() {
        this.setY(this.getY() - SHOT_SPEED);

        if(this.getY() <= LABEL_HEIGHT) this.setVisible(false);
    }
}
