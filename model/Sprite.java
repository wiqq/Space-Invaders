package model;

/**
 * Posiada podstawowe parametry, z których korzysta prawie każda klasa w modelu.
 * Klasę tą rozszerzają: {@link Alien}, {@link model.Alien.Bomb}, {@link Player},
 * {@link Shot}.
 */
class Sprite {
    /**
     * Wpółrzędne obiektu
     */
    private int x, y;
    /**
     * Parametr określający sekwencję animacji obrazka
     */
    private int hits;
    /**
     * Określa, czy dany obiekt jest aktywny (widoczny), czy nie. Jeśli nie jest, nie jest
     * brany pod uwagę podczas rozgrywki oraz nie wyświetla się jego obrazka.
     */
    private boolean visible;

    /**
     * Ustawienie obiektu jako widoczny oraz animacji obrazka na początek kolejki
     */
    Sprite() {
        visible = true;
        hits = 0;
    }

    public int getX() { return x; }
    void setX(int x) { this.x = x; }

    public int getY() { return  y; }
    void setY(int y) { this.y = y; }

    public int isHit() { return hits; }
    void gotHit() { hits = 8; }
    void notHit() { if(hits > 0) hits--; }


    public boolean isVisible() { return visible; }
    void setVisible(boolean vis) { this.visible = vis; }
}
