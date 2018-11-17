package model;

import view.ViewCommons;

import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Zawiera parametry kosmity oraz metody, które je zmieniają.
 */
public class Alien extends Sprite implements ModelCommons, ViewCommons {
    /**
     * informuje, jaki jest typ kosmity, czy jest na przykład bossem
     */
    private int type;
    /**
     * informuje, ile żyć ma kosmita
     */
    private int lives;
    /**
     * informacja, w którą stronę mają poruszyć się kosmici. Jeśli 1 to w prawo, jeśli -1, to w lewo.
     */
    private static int direction = 1;
    /**
     * informuje, czy kierunek kosmitów przed chwilą się zmienił
     */
    private static boolean isDirectionChanged = false;

    private Bomb bomb;
    private BossBomb bossBomb;

    /**
     * Konstruktor ustawia takie parametry jak położenie czy typ kosmity. W zależności od podanego typu
     * ustala się ilość żyć kosmity. Jeśli jest to <code>TYPE_1</code>, to dostaje 1 życie, jeśli
     * <code>TYPE_2</code> - 2 życia, jeśli kosmita jest bossem, otrzymuje 30 żyć oraz dodatkową bombę
     * unikalną dla bossa. Na koniec inicjalizuje się bombę.
     * @param x położenie poziome kosmity
     * @param y położenie pionowe kosmity
     * @param type typ kosmity
     */
    Alien(int x, int y, int type) {
        setX(x);
        setY(y);

        this.type = type;

        switch(type) {
            case TYPE_1:
                lives = 1;
                break;
            case TYPE_2:
                lives = 2;
                break;
            case TYPE_BOSS:
                lives = BOSS_LIVES;
                bossBomb = new BossBomb();
                break;
        }

        bomb = new Bomb();
    }

    /**
     * Poruszanie się kosmitów w poziomie. Sprawdzane jest, czy kosmita nie dotarł do krawędzi okienka.
     * Jeśli tak, to zmienia się kierunek chmury kosmitów.<br>
     * Jeśli skrajna kolumna kozmitów została zlikwidowana, nie jest brana pod uwagę w sprawdzaniu.
     * Dodatkowo ustawia się <code>isDirectionChanged</code> na true.
     * @param alienWidth parametr potrzebny do określenia, czy kosmita dotarł do prawej krawędzi
     */
    void moveX(int alienWidth) {
        this.setX(this.getX() + ALIEN_SPEED1 * direction);

        if((this.getX() >= BOARD_WIDTH - alienWidth - ALIEN_SPEED1) && isVisible())
            isDirectionChanged = true;
        else if((this.getX() <= ALIEN_SPEED1) && isVisible())
            isDirectionChanged = true;
    }

    public int getType() { return type; }

    void decreaseLife() { lives--; }
    public int getLives() { return lives; }

    static boolean getIsDirectionChanged() {
        return isDirectionChanged;
    }
    static void setIsDirectionChanged(boolean dir) {
        isDirectionChanged = dir;
    }

    static int getDirection() {
        return direction;
    }
    static void setDirection(int dir) {
        direction = dir;
    }

    public Bomb getBomb() { return bomb; }
    public BossBomb getBossBomb() { return bossBomb; }

    //************************************************************* inner classes:
    /**
     * Standardowa bomba kosmity. Podlega pewnym zmianom przy zmianie stanu lub typu kosmity, ale
     * zasada jej działania pozostaje taka sama.
     */
    public class Bomb extends Sprite {
        /**
         * <code>velocity</code> to prędkość dla konkretnej bomby
         */
        int velocity;
        /**
         * to współrzędna pozioma dodatkowej bomby, którą wypuszcza kosmita typu 2, kiedy raz dostał
         */
        int angryX;
        /**
         * to współrzędna pozioma dodatkowej bomby bocznej bossa
         */
        int sideX;

        /**
         * Ustawia się widoczność na <code>false</code>, oraz losuje się liczbę w zakresie 3-6, która będzie
         * szybkością bomby (<code>velocity</code>).
         */
        Bomb() {
            this.setVisible(false);
            Random generator = new Random();
            velocity = generator.nextInt(BOMB_SPEED_MAX+1 - BOMB_SPEED_MIN) + BOMB_SPEED_MIN;
        }

        /**
         * Jeśli bomba jest aktywna, to porusza się nią w dół. Dla typu 1 i typu 2, kiedy nie jest
         * agresywny (ma 2 życia), ruch zależy od <code>velocity</code>. Dla agresywnego typu 2
         * prędkość jest zwiększona i taka sama jak szybkość bocznych strzałów bossa.
         */
        void move() {
            if(this.isVisible()) {
                if(type == TYPE_1 || (type == TYPE_2 && lives == 2)) this.setY(getY() + velocity);

                else if(type == TYPE_2 && lives == 1) this.setY(getY() + BOSS_BOMB_SIDE_SPEED);
                else if(type == TYPE_BOSS) this.setY(getY() + BOSS_BOMB_SIDE_SPEED);
            }
        }

        /**
         * Przesunięcie w poziomie bomby dla agresywnego typu 2
         */
        void setAngryBombX() { angryX = this.getX() + ANGRY_BOMB_DIFF_X; }
        public int getAngryX() { return angryX; }

        /**
         * Przesunięcie w poziomie drugiej z podwójnej bomby bocznej dla bossa
         * @param x
         */
        void setSideX(int x) { sideX = x; }
        public int getSideX() { return sideX; }
    }

    //********************************************************************** boss violet bomb:

    /**
     * Tylko dla bossa. Zawiera informacje o fioletowej bombie bossa, która namierza gracza i strzela
     * w jego kierunku.
     */
    public class BossBomb {
        /**
         * Informacja o aktualnym stanie animacji bomby
         */
        private int bossBombSequence;
        /**
         * Informacja, o ile pikseli w dół i w bok ma się przesunąć bomba.
         */
        private Point2D.Double bossBombCoordinates;
        /**
         * Położenie bomby
         */
        private double bossBombX, bossBombY;
        /**
         * <code>true</code> jeśli bomba jest aktywna, <code>false</code> w przeciwnym razie
         */
        private boolean visible;

        /**
         * Ustala się brak początkowej aktywności, sekwencję animacji na 1 (początek animacji),
         * oraz inicjalizuje się punkt przesunięcia bomby
         */
        BossBomb() {
            this.visible = false;

            bossBombSequence = 1;
            if(type == TYPE_BOSS) bossBombCoordinates = new Point2D.Double();
        }

        /**
         * Przesuwa bombę w zależności od <code>bossBombCoordinates</code>
         */
        void move() {
            if(this.visible && type == TYPE_BOSS) {
                this.bossBombX += bossBombCoordinates.x;
                this.bossBombY += bossBombCoordinates.y;
            }
        }

        public int getBossBombSequence() { return bossBombSequence; }

        /**
         * W każdym wywołaniu dodaje 1 do sekwencji animacji, jeśli osiągnie wartość 4,
         * to wraca znowu na 1.
         */
        public void moveBombSequence() {
            bossBombSequence++;
            if(bossBombSequence == 4) bossBombSequence = 1;
        }

        void setBossBombCoordinates(Point2D.Double p) { bossBombCoordinates.setLocation(p);}

        void setBossBombX(double x) { bossBombX = x; }
        void setBossBombY(double y) { bossBombY = y; }

        public double getBossBombX() { return bossBombX; }
        public double getBossBombY() { return bossBombY; }

        public boolean isVisible() { return visible; }
        void setVisible(boolean visible) { this.visible = visible; }
    }
}
