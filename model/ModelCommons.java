package model;

import javax.swing.*;

/**
 * Zawiera stałe używane podczas organizacji mechaniki gry.
 */
public interface ModelCommons {
    short PLAYER_SPEED = 3;
    short SHOTS_LIMIT = 5;
    short SHOT_SPEED = 4;

    short TYPE_1 = 1;
    short TYPE_2 = 2;
    short TYPE_BOSS = 3;
    short BOSS_LIVES = 30;
    short BOSS_NUM = 8;

    short ANGRY_BOMB_DIFF_X = 30;
    short BOSS_BOMB_DIFF_X = 170;

    //************************************ for game set:
    short ALIEN_START_X = -30;
    short ALIEN_START_Y = 20;

    short ALIENS_ROWS = 4;
    short ALIENS_ROWS2 = 5;

    short ALIENS_COLS = 6;
    short ALIENS_COLS2 = 6;

    short ALIEN_SPEED1 = 3;
    short BOMB_SPEED_MIN = 3;
    short BOMB_SPEED_MAX = 6;
    short BOSS_BOMB_SPEED = 9;
    short BOSS_BOMB_SIDE_SPEED = 13;
    short BOMB_CHANCE = 2;

    short NUMBER_OF_ALIENS = 24;
    short NUMBER_OF_ALIENS2 = 10;
    short POINT_PER_ALIEN = 10;
    short POINT_PER_ALIEN2 = 20;
    short POINT_FOR_BOSS = 2000;

    //******************************************** CONTROLER:
    /**
     * Stała ułatwiająca dodawanie <code>keyBindings</code>.
     */
    int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    String PRESSED = "pressed ";
    String RELEASED = "released ";
}
