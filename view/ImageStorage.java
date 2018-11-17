package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Przechowalnia dla grafik.
 */
class ImageStorage {
    /**
     * Tablica zawierająca ścieżki do wszystkich grafik potrzebnych do rysowania w grze.
     */
    private final String[] PATH = {
            "src/Images/bombImg.png",
            "src/Images/bombImg_angry.png",
            "src/Images/playerImg.png",
            "src/Images/shotImg.png",
            "src/Images/alien1Img.png",
            "src/Images/alien2Img.png",
            "src/Images/alien2Img_angry.png",
            "src/Images/bossImg.png",
            "src/Images/bombImg_boss.png",
            "src/Images/bombImg_boss2.png",
            "src/Images/bombImg_boss3.png",
            "src/Images/bossImg_hit.png",
            "src/Images/bombImg_side.png",
            "src/Images/playerImg_life.png",

            "src/Images/explosion_animation/explImg1.png",
            "src/Images/explosion_animation/explImg2.png",
            "src/Images/explosion_animation/explImg3.png",
            "src/Images/explosion_animation/explImg4.png",
            "src/Images/explosion_animation/explImg5.png",
            "src/Images/explosion_animation/explImg6.png",
            "src/Images/explosion_animation/explImg7.png",
            "src/Images/explosion_animation/explImg8.png",
    };
    /**
     * Tablica o wielkości zależnej od ilości ścieżek do plików
     */
    private File file[] = new File[PATH.length];
    /**
     * Tablica obrazków o wielkości zależnej od ilości ścieżek do plików
     */
    private BufferedImage[] img = new BufferedImage[PATH.length];

    /**
     * Wczytywanie plików sprawdzane przez obsługę wyjątków.
     */
    ImageStorage() {
        try {
            config();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje się pliki w pętli.
     * @throws Exception kiedy plik o danej ścieżce istnieje oraz czy można go wczytać
     */
    private void config() throws Exception {
            int i = 0;
            for(String path: PATH) {
                file[i] = new File(path);
                if (!file[i].exists() || !file[i].canRead())
                    throw new Exception("File " + path + " does not exist or can't read");

                i++;
            }

            for(i = 0; i < PATH.length; i++)
                img[i] = ImageIO.read(file[i]);
    }

    /**
     * @param wchich numer obrazka, który chcemy pobrać
     * @return obrazek o podanym numerze
     */
    BufferedImage getImg(int wchich) { return img[wchich]; }
}
