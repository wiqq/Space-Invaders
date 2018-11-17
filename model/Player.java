package model;

import view.ViewCommons;

/**
 * Zawiera parametry gracza oraz metody zmieniające je.
 */
public class Player extends Sprite implements ModelCommons, ViewCommons {
    /**
     * <code>x</code> zmieniające się położenie poziome gracza
     */
    private int x;
    /**
     * <code>dx</code> informacja, czy gracz ma się przesunąć w prawo, w lewo, czy stoi
     */
    private int dx;
    /**
     * <code>kills</code> liczba punktów gracza
     */
    private int kills;
    /**
     * <code>lives</code> liczba żyć gracza
     */
    private int lives;

    /**
     * Domyślny konstruktor
     */
    Player() {}

    /**
     * Ustala się punkty na 0 oraz liczbę żyć na 3. Położenie poziome będzie środkiem planszy, a
     * pionowe środkiem dolnej linii.
     * @param pWidth szerokość gracza
     * @param pHeight wysokość gracza
     */
    Player(int pWidth, int pHeight) {
        kills = 0;
        lives = 3;

        x = (BOARD_WIDTH - pWidth)/2;
        setX(x);
        setY(LINE_HEIGHT - pHeight);
    }

    /**
     * Ruch gracza w poziomie, do aktualnej współrzędnej dodaje się <code>dx</code>.
     * Zawiera warunki, żeby gracz nie przekroczył krawędzi planszy.
     * @param playerWidth szerokość gracza potrzebna do sprawdenia, czy gracz nie
     *                    dotarł do prawej krawędzi
     */
    void move(int playerWidth) {
        x += dx;

        if(this.getX() + dx < 0) x = 0;
        if(this.getX() + dx > BOARD_WIDTH - playerWidth)
            x = BOARD_WIDTH - playerWidth;

        this.setX(x);
    }

    /**
     * przypisuje na <code>dx</code> ujemną wartość prędkości gracza
     */
    public void moveLeft() { dx = -PLAYER_SPEED; }

    /**
     * przypisuje na <code>dx</code> dodatnią wartość prędkości gracza
     */
    public void moveRight() { dx = PLAYER_SPEED; }

    /**
     * przypisuje 0 do <code>dx</code>
     */
    public void stopMove() { dx = 0; }

    /**
     * dodaje punkty z każdym wywołaniem
     * @param points wartość punktów o jakie zwększy się <code>kills</code>
     */
    void addKill(int points) { kills += points;}
    public int getKills() { return kills; }

    /**
     * zmniejsza o 1 liczbę żyć za każdym wywołaniem. Jeśli liczba żyć jest
     * równa 0, to gracz umiera.
     */
    void decreaseLife() {
        lives--;
        if(lives == 0) setVisible(false);
    }
    public int getLives() { return lives; }

    /**
     * Ustawienie liczby punktów na 0.
     */
    public void resetScore() { kills = 0;}
}
