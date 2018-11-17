package model;

import view.ViewCommons;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

/**
 * Klasa zawierająca ogólną mechanikę gry. Korzysta z: {@link Alien}, {@link Player}, {@link Shot}.
 * Klasy te reprezentują poszczególne elementy gry, każda z nich dziedziczy po {@link Sprite}, która
 * zawiera charakterystyczne elementy (jak na przykład położenie obiektu).
 */
public class Model implements ModelCommons, ViewCommons {
    /**
     * zawiera informację o poziomie
     */
    private short level;
    /**
     * informuje nas czy gra się skończyła. Dostaje wartość <code>true</code>,
     *  kiedy nastąpił koniec (śmierć grasza lub wygrana)
     */
    private boolean gameFinished;

    private ArrayList<Alien> aliens = new ArrayList<>();
    private Player player = new Player();
    private ArrayList<Shot> shots = new ArrayList<>();

    /**
     * Konstruktor ustawia początkowe wartości: <code>level</code> na 1, a <code>gameFinished</code> na <code>false</code>
     */
    public Model() {
        level = 1;
        gameFinished = false;
    }

    //******************************************************** aliens functions

    /**
     * Ustawia współrzędne x, y kosmitów. Rozstawieni są na planie prostokąta
     *
     * @param x szerokość (w pikselach) kosmity
     * @param y wysokość kosmity
     */
    public void setAliens(int x, int y) {
        for(int i = 1; i <= ALIENS_COLS; i++)
            for(int j = 1; j <= ALIENS_ROWS; j++) {
                Alien alien = new Alien(ALIEN_START_X + (x + 20) * i, ALIEN_START_Y + (y + 20) * j, level);
                aliens.add(alien);
            }
    }

    /**
     * Funkcja poruszając kosmitami w poziomie i w pionie. Zawiera warunek, że
     * kiedy kosmici dotrą do dolnej linii gracz przegrywa.
     */
    public void moveAliens(int alienWidth,int bossWidth, int alienHeight, int playerHeight) {
        for(Alien alien: aliens) {
            if ((alien.getY() + alienHeight >= LINE_HEIGHT - playerHeight/2)
                    && alien.isVisible())

                player.setVisible(false);

            if(alien.getType() == TYPE_BOSS)    //boss moving
                alien.moveX(bossWidth);
            else
                alien.moveX(alienWidth);        //rest of aliens
        }

        if(Alien.getIsDirectionChanged()) {
            for(Alien alien: aliens)
                alien.setY(alien.getY() + ALIEN_SPEED1*5);

            if (Alien.getDirection() == 1) Alien.setDirection(-1);
            else if (Alien.getDirection() == -1) Alien.setDirection(1);

            Alien.setIsDirectionChanged(false);
        }
    }

    //********************************************************** bomb functions:

    /**
     * Dla każdego kosmity w <code>ArrayList</code> losuje się liczba i na jej podstawie
     * ustala się, czy kosmita generuje bombę. Muszą być przy tym spełnione warunki, jak na przykład
     * kosmita musi być widoczny, a jego bomba nie. <br>
     * Dla każdego rodzaju kosmity generowane są inne rodzaje bomb.
     */
    public void setBomb(int bossWidht, int bossHeight, int playerWidth) {
        Random generator = new Random();
        int rand;

        for(Alien alien: aliens) {
            rand  = generator.nextInt(100);

            if(rand <= BOMB_CHANCE && alien.isVisible() && !alien.getBomb().isVisible()) {
                alien.getBomb().setVisible(true);
                alien.getBomb().setX(alien.getX());
                alien.getBomb().setY(alien.getY());

                if(alien.getType() == TYPE_2 && alien.getLives() == 1) alien.getBomb().setAngryBombX();
                else if(alien.getType() == TYPE_BOSS) {
                    alien.getBomb().setSideX(alien.getX() + BOSS_BOMB_DIFF_X);
                    alien.getBomb().setY(alien.getY() + bossHeight);
                }
            }
            if(rand <= BOMB_CHANCE && alien.getType() == TYPE_BOSS && !alien.getBossBomb().isVisible()
                && alien.isVisible()) {
                alien.getBossBomb().setVisible(true);
                alien.getBossBomb().setBossBombCoordinates(trackPlayer(bossWidht, bossHeight, playerWidth));
                alien.getBossBomb().setBossBombX(alien.getX() + (float) bossWidht / 2);
                alien.getBossBomb().setBossBombY(alien.getY() + bossHeight);
            }
        }
    }

    /**
     * Poruszanie się bomb do dołu i warunki, że jeśli przekroczą dolną linię, to znikają.
     * @param bombHeight    do określania, czy bomba przekroczyła dolną linię
     */
    public void moveBomb(int bombHeight) {
        for(Alien alien: aliens) {
            if(alien.getBomb().isVisible())
                alien.getBomb().move();

            if(alien.getBomb().getY() + bombHeight >= LINE_HEIGHT)
                alien.getBomb().setVisible(false);

            if(alien.getType() == TYPE_BOSS && alien.getBossBomb().isVisible()) {
                alien.getBossBomb().move();
                if(alien.getBossBomb().getBossBombY() + bombHeight >= LINE_HEIGHT)
                    alien.getBossBomb().setVisible(false);

            }
        }
    }

    //********************************************************** player functions;
    public void setPlayer(int pWidth, int pHeight) {
        player = new Player(pWidth, pHeight);
    }

    /**
     * Wywołuje się ruch gracza, po czym przechodzi się przez wszystkie bomby kosmitów i sprawdza się,
     * czy któraś nie trafiła w gracza. Parametry wysokości i szerokości poszczególnych obiektów są
     * potrzebne do tego określania.<br>
     * Bomby dla każdego typu kosmitów sprawdza się osobno.
     * @param pWidth szerokość gracza
     * @param bWidth szerokość bomby
     * @param bHeight wysokość bomby
     * @param bossBombWidth szerokość fioletowej bomby bossa
     * @param bossBombHeight wysokość fioletowej bomby bossa
     */
    public void movePlayer(int pWidth, int bWidth, int bHeight, int bossBombWidth, int bossBombHeight) {
        player.move(pWidth);

        for(Alien alien: aliens) {
            if(alien.getBomb().getY() + bHeight >= player.getY() && alien.getBomb().isVisible()) {

                if((alien.getBomb().getX() + bWidth <= player.getX() + pWidth &&
                        alien.getBomb().getX() >= player.getX())
                                            ||
                        (alien.getType() == TYPE_BOSS &&
                                alien.getBomb().getSideX() >= player.getX() &&
                                alien.getBomb().getSideX() + bWidth <= player.getX() + pWidth)) {

                    alien.getBomb().setVisible(false);
                    player.decreaseLife();
                    player.gotHit();
                }
            }

            if(alien.getType() == TYPE_2 && alien.getLives() == 1) {
                if(alien.getBomb().getAngryX() >= player.getX() &&
                        alien.getBomb().getAngryX() + bWidth <= player.getX() + pWidth &&
                        alien.getBomb().getY() + bHeight >= player.getY() &&
                        alien.getBomb().isVisible()) {

                    alien.getBomb().setVisible(false);
                    player.decreaseLife();
                    player.gotHit();
                }
            } else if(alien.getType() == TYPE_BOSS) {
                if(alien.getBossBomb().isVisible() &&
                    alien.getBossBomb().getBossBombY() + bossBombHeight >= player.getY() &&
                    alien.getBossBomb().getBossBombX() + bossBombWidth >= player.getX() &&
                    alien.getBossBomb().getBossBombX() <= player.getX() + pWidth) {

                    alien.getBossBomb().setVisible(false);
                    player.decreaseLife();
                    player.gotHit();
                }
            }
        }
    }

    //*********************************************************** shots functions:

    /**
     * Na początku, kiedy <code>ArrayList</code> strzałów gracza jest pusta, sprawdza się,
     * czy gracz strzalił maksymalną ilość razy, jesli nie, wystrzela.<br>
     * Jeśli liczba strzałów osiągnęła maksimum, ale jakiś strzał przestał być aktywny,
     * to może być użyty ponownie.
     * @param playerWidth parametr potrzebny do ustawienia strzału pośrodku gracza
     */
    public void addShot(int playerWidth) {
        if(shots.size() < SHOTS_LIMIT) {
            Shot shot = new Shot(player.getX() + playerWidth / 2, player.getY());
            shots.add(shot);
        }
        else {
            for(Shot shot1: shots) {
                if(!shot1.isVisible()) {
                    shot1.setVisible(true);
                    shot1.setX(player.getX() + playerWidth/2);
                    shot1.setY(player.getY());

                    break;
                }
            }
        }
    }

    /**
     * Porusza się każdym aktywnym strzałem, po czym sprawdza się, czy któryś nie trafił kosmity.
     * Sprawdza się osobno małych kosmitów i dużych, ponieważ mają inne parametry wysokości i szerokości.
     * Jeśli strzał trifi, to przestaje być aktywny, a kosmita w zależności od rodzaju, albo traci życie,
     * albo przestaje żyć.
     */
    public void moveShots(int sWidth, int sHeight, int aWidth, int aHeight, int bossWidht, int bossHeight) {
        for(Shot shot: shots) {
            if(shot.isVisible()) {
                shot.move();

                for(Alien alien: aliens) {
                    if(alien.isVisible() && shot.getX() >= alien.getX()){   //for all aliens
                        if((alien.getType() != TYPE_BOSS &&
                                shot.getX() + sWidth <= alien.getX() + aWidth &&    //this
                                shot.getY() <= alien.getY() + aHeight &&            //is for
                                shot.getY() + sHeight >= alien.getY())              //normal aliens

                                            ||

                                (alien.getType() == TYPE_BOSS &&
                                        shot.getX() + sWidth <= alien.getX() + bossWidht &&  //this
                                        shot.getY() <= alien.getY() + bossHeight &&          //is for
                                        shot.getY() + sHeight >= alien.getY())) {            //the boss


                            shot.setVisible(false);

                            if(alien.getType() == TYPE_1) {
                                player.addKill(POINT_PER_ALIEN);
                                alien.setVisible(false);
                                alien.gotHit();

                            } else if(alien.getType() == TYPE_2) {
                                alien.gotHit();

                                if(alien.getLives() == 2) {
                                    alien.decreaseLife();
                                } else {
                                    player.addKill(POINT_PER_ALIEN2);
                                    alien.setVisible(false);
                                }
                            } else if(alien.getType() == TYPE_BOSS) {
                                alien.decreaseLife();
                                alien.gotHit();
                                if(alien.getLives() == 0) {
                                    player.addKill(POINT_FOR_BOSS);
                                    alien.setVisible(false);
                                }
                            }
                            break;

                        }
                    }
                }
            }
        }
    }

    //********************************************************** for level 2:

    /**
     * Ustaienie początkowych parametrów kosmitów w 2 poziomie. Dodatkowo ustawia się typ 2 niektórych kosmitów.
     * @param x szerokość kosmity
     * @param y wysokość kosmity
     */
    private void setAliens2(int x, int y) {
        Alien alien;

        for(int i = 1; i <= ALIENS_COLS2; i++)
            for(int j = 1; j <= ALIENS_ROWS2; j++) {
                if(((i == 3 || i == 4) && (j == 1 || j == 5)) ||
                        ((i == 2 || i == 5) && (j == 2 || j == 4)) ||
                        ((i == 1 || i == 6) && j == 3))
                    alien = new Alien(ALIEN_START_X + (x + 20) * i, ALIEN_START_Y + (y + 20) * j, TYPE_2);

                else alien = new Alien(ALIEN_START_X + (x + 20) * i, ALIEN_START_Y + (y + 20) * j, TYPE_1);

                aliens.add(alien);
            }
    }

    //*********************************************************** for level 3:

    /**
     * Ustawienie początkowych parametów kosmitów w 3 poziomie. Ustawia się tylko tym 2 kosmitów
     * oraz dodatkowo bossa, który jest ostatni na liście, ale znajduje się na środku.
     * @param x szerokość kosmity (typ 2)
     * @param y wysokość kosmity (typ 2)
     */
    private void setAliens3(int x, int y) {
        Alien alien;

        for(int j = 1; j <= 4; j++) {
            alien = new Alien(ALIEN_START_X + (x + 20), ALIEN_START_Y + (y + 20)*j, TYPE_2);
            aliens.add(alien);

            alien = new Alien(ALIEN_START_X + (x + 520), ALIEN_START_Y + (y + 20)*j, TYPE_2);
            aliens.add(alien);
        }

        alien = new Alien(ALIEN_START_X + 180, ALIEN_START_Y, TYPE_BOSS);
        aliens.add(alien);
    }

    /**
     * Oblicza, o ile fioletowa bomba gracza ma się przesunąć. Znajduje aktualne położenie gracza i
     * wylicza, pod jakim kątem powinien strzelić boss. Bierze się pod uwagę, żeby szybkość bomby
     * była zawsze taka sama.
     * @return punkt zawierajacy informację, o ile pikseli w bok i w dół musi się przesunąć bomba
     * za każdym razem
     */
    private Point2D.Double trackPlayer(int bossWidht, int bossHeight, int playerWidth) {
        double x1, y1, dx, dy;
        x1 = (player.getX() + (double)playerWidth/2) - (aliens.get(BOSS_NUM).getX() + (double)bossWidht/2) ;

        y1 = player.getY() - (aliens.get(BOSS_NUM).getY() + bossHeight);

        if(aliens.get(BOSS_NUM).getX() + bossWidht/2 <= player.getX() + playerWidth/2)
            dx = BOSS_BOMB_SPEED/sqrt(1 + Math.pow(y1/x1, 2));
        else
            dx = -BOSS_BOMB_SPEED/sqrt(1 + Math.pow(y1/x1, 2));

        if(abs(dx) < 0.1)
            dy = BOSS_BOMB_SPEED;
        else
            dy = (y1/abs(x1))*abs(dx);

        return new Point2D.Double(dx, dy);
    }

    //********************************************************** actualisation:

    /**
     * Na początku przechodzi się przez wszystkich kosmitów i aktualizuje się parametry dotyczące animacji
     * wybuchu. To samo dla gracza.<br>
     * Następnie sprawdane jest, czy wszyscy kosmici zostali zniszczeni. Jeśli tak, to gracz przechodzi
     * do kolejnego poziomu oraz kosmici resetują się i ustawiają parametry początkowe funkcjami:
     * <code>setAliens2</code> oraz <code>setAliens3</code>. Jeśli gracz przejdzie 3 poziom, wygrywa grę.
     * @return <code>true</code> jeśli właśnie zmienił się poziom, <code>false</code> w przeciwnym razie
     */
    public boolean isActualised(int aWidth, int aHeight) {
        for(Alien alien: aliens) alien.notHit();
        player.notHit();

        if(level == 1 && player.getKills() == NUMBER_OF_ALIENS*POINT_PER_ALIEN) {
            level++;
            restartObjects();
            setAliens2(aWidth, aHeight);
            return true;
        }
        else if(level == 2 && player.getKills() == (NUMBER_OF_ALIENS + 30-NUMBER_OF_ALIENS2)*POINT_PER_ALIEN
                + NUMBER_OF_ALIENS2*POINT_PER_ALIEN2) {
            level++;
            restartObjects();
            setAliens3(aWidth, aHeight);
            return true;
        }
        else if(level == 3) {
            for(Alien alien: aliens) {
                if(alien.isVisible()) return false;
            }

            gameFinished = true;
            return true;
        }
        return false;
    }

    //********************************************************** operations on objects;
    public ArrayList<Alien> getAliens() { return aliens; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public ArrayList<Shot> getShots() { return shots; }

    public short getLevel() { return level; }
    public void setLevel(short lev) { level = lev; }
    public boolean isGameFinished() { return gameFinished; }

    //********************************************************** restarting:

    /**
     * Usuwa się wszystkie elementy z <code>ArrayList</code> kosmitów oraz strzałów gracza.
     * Na wszelki wypadek, jeśli gracz wcześniej przegrał ustawia się gracza jako widocznego.
     */
    public void restartObjects() {
        aliens.clear();
        shots.clear();
        player.setVisible(true);
    }
}
