package view;

/**
 * Interfejs zawiera stałe niezbędne do ustawienia parametrów okienek,
 * takie jak wymiary czy wysokość dolnej linii.<br>
 * Znajdują się też stałe odpowiadające numerom obrazków z klasy
 * {@link ImageStorage}.
 */
public interface ViewCommons
{
    short BOARD_WIDTH = 800;
    short BOARD_HEIGHT = 700;

    short LABEL_HEIGHT = 50;
    short FONT_SIZE = 20;

    short LINE_HEIGHT = 700;

    short BOMB_IMG = 0;
    short BOMB_IMG_ANGRY = 1;
    short PLAYER_IMG = 2;
    short SHOT_IMG = 3;
    short ALIEN_IMG = 4;
    short ALIEN_IMG_2 = 5;
    short ALIEN_IMG_2_ANGRY = 6;
    short BOSS_IMG = 7;
    short BOSS_IMG_BOMB = 8;
    short BOSS_IMG_BOMB2 = 9;
    short BOSS_IMG_BOMB3 = 10;
    short BOSS_IMG_HIT = 11;
    short BOMB_IMG_SIDE = 12;
    short PLAYER_IMG_LIFE = 13;

    short EXPL_IMG = 22;
}
