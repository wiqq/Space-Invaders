package view;

import model.Alien;
import model.ModelCommons;
import model.Player;
import model.Shot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Konkretyzacja panelu, która jest głównym panelem gry
 */
class Panel extends JPanel implements ViewCommons, ModelCommons {
    /**
     * Obiekt klasy, która przechowuje grafiki
     */
    private ImageStorage imgStorage = new ImageStorage();

    private ArrayList<Alien> aliens = new ArrayList<>();
    private Player player;
    private ArrayList<Shot> shots = new ArrayList<>();

    /**
     * Ustawia się czarne tło, podwójną buferację oraz widoczność.
     */
    Panel() {
        setBackground(Color.BLACK);
        setLayout(null);
        setDoubleBuffered(true);
        setVisible(true);
    }

    void importAliens(ArrayList<Alien> aliens) { this.aliens = aliens; }
    void importPlayer(Player player) { this.player = player; }
    void importShots(ArrayList<Shot> shots) { this.shots = shots; }

    /**
     * Rysowanie kosmitów. W zależności od typu kosmity rysuje się inny obrazek. Przy trafieniu
     * pokazuje się animacja wybuchu.
     */
    private void drawAliens(Graphics g) {
        for(Alien alien: aliens) {

            if(alien.isVisible()) {
                if(alien.getType() == TYPE_1)
                    g.drawImage(imgStorage.getImg(ALIEN_IMG), alien.getX(), alien.getY(), this);

                else if(alien.getType() == TYPE_2) {
                    if(alien.getLives() == 2)
                        g.drawImage(imgStorage.getImg(ALIEN_IMG_2), alien.getX(), alien.getY(), this);
                    else if(alien.getLives() == 1)
                        g.drawImage(imgStorage.getImg(ALIEN_IMG_2_ANGRY), alien.getX(), alien.getY(), this);
                } else if(alien.getType() == TYPE_BOSS) {
                    if(alien.isHit() == 0) g.drawImage(imgStorage.getImg(BOSS_IMG), alien.getX(), alien.getY(), this);
                    else if(alien.isHit() > 0)
                        g.drawImage(imgStorage.getImg(BOSS_IMG_HIT), alien.getX(), alien.getY(), this);
                }
            }
            if(alien.isHit() > 0 && alien.getType() != TYPE_BOSS)
                g.drawImage(imgStorage.getImg(EXPL_IMG - alien.isHit()), alien.getX(), alien.getY(), this);
        }
    }

    /**
     * Rysowanie bomb. W zależności od rodzaju kosmity, który wypuścił daną bombę rysowany jest
     * inny obrazek. Fioletowy pocisk bossa jest animowany i wymaga wywołania dodatkowej funkcji.
     */
    private void drawBombs(Graphics g) {
        for(Alien alien: aliens) {
            if(alien.getBomb().isVisible())
                if(alien.getType() == TYPE_2 && alien.getLives() == 1) {
                    g.drawImage(imgStorage.getImg(BOMB_IMG_ANGRY), alien.getBomb().getX(),
                            alien.getBomb().getY(), this);
                    g.drawImage(imgStorage.getImg(BOMB_IMG_ANGRY), alien.getBomb().getAngryX(),
                            alien.getBomb().getY(), this);
                } else
                    g.drawImage(imgStorage.getImg(BOMB_IMG), alien.getBomb().getX(),
                            alien.getBomb().getY(), this);

            if(alien.getType() == TYPE_BOSS) {
                alien.getBossBomb().moveBombSequence();

                /* normal bombs for the edges */
                if(alien.getBomb().isVisible()) {
                    g.drawImage(imgStorage.getImg(BOMB_IMG_SIDE), alien.getBomb().getX(), alien.getBomb().getY(), this);
                    g.drawImage(imgStorage.getImg(BOMB_IMG_SIDE), alien.getBomb().getSideX(), alien.getBomb().getY(), this);
                }
                /* animated violet bombs */
                if(alien.getBossBomb().isVisible()) {
                    if(alien.getBossBomb().getBossBombSequence() == 1)
                        g.drawImage(imgStorage.getImg(BOSS_IMG_BOMB), (int) alien.getBossBomb().getBossBombX(), (int) alien.getBossBomb().getBossBombY(), this);
                    else if(alien.getBossBomb().getBossBombSequence() == 2) {
                        g.drawImage(imgStorage.getImg(BOSS_IMG_BOMB2), (int) alien.getBossBomb().getBossBombX(), (int) alien.getBossBomb().getBossBombY(), this);
                    } else if(alien.getBossBomb().getBossBombSequence() == 3)
                        g.drawImage(imgStorage.getImg(BOSS_IMG_BOMB3), (int) alien.getBossBomb().getBossBombX(), (int) alien.getBossBomb().getBossBombY(), this);
                }
            }

        }
    }

    /**
     * Rysowanie gracza. Jeśli trafi go bomba kosmity, rysowany jest animowany wybuch.
     */
    private void drawPlayer(Graphics g) {
        if(player.isVisible()) {
            g.drawImage(imgStorage.getImg(PLAYER_IMG), player.getX(), player.getY(), this);

            for(int i = 1; i <= player.getLives(); i++)
                g.drawImage(imgStorage.getImg(PLAYER_IMG_LIFE),
                        BOARD_WIDTH/2 + imgStorage.getImg(PLAYER_IMG_LIFE).getWidth()*i + 65,
                        14, this);
        }
        if(player.isHit() > 0)
            g.drawImage(imgStorage.getImg(EXPL_IMG - player.isHit()),player.getX()-10,player.getY()-10, this);

    }

    /**
     * Rysowanie strzałów gracza. Rysowane są te aktywne z listy.
     */
    private void drawShots(Graphics g) {
        for(Shot shot: shots) {
            if(shot.isVisible())
                g.drawImage(imgStorage.getImg(SHOT_IMG),
                        shot.getX(), shot.getY(), this);
        }
    }

    /**
     * Nadpisana funkcja, w której po kolei wywołuje się funkcje rusowania obiektów gry. Dodatkowo
     * rysowana jest linia na dole, po której porusza się gracz i której nie mogą przekroczyć
     * kosmici.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit.getDefaultToolkit().sync();

        g.drawLine(0, LINE_HEIGHT, BOARD_WIDTH, LINE_HEIGHT);
        drawBombs(g);
        drawAliens(g);
        drawPlayer(g);
        drawShots(g);
    }

    ImageStorage getImgStorage() { return imgStorage; }
}
