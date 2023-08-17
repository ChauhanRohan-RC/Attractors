import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;

import java.nio.file.Path;

public class R {

    public static final boolean FROZEN = false;         // TODO: set true before packaging

    // Dir structure
    public static final Path DIR_MAIN = (FROZEN? Path.of("app") : Path.of("")).toAbsolutePath();
    public static final Path DIR_RES = DIR_MAIN.resolve("res");
    public static final Path DIR_IMAGE = DIR_RES.resolve("image");
    public static final Path DIR_FONT = DIR_RES.resolve("font");

    // Resources
    public static final String APP_NAME = "Chaotic Systems";
    public static final Path APP_ICON = DIR_IMAGE.resolve("icon.png");

    // Fonts
    public static final Path FONT_PD_SANS_REGULAR = DIR_FONT.resolve("product_sans_regular.ttf");
    public static final Path FONT_PD_SANS_MEDIUM = DIR_FONT.resolve("product_sans_medium.ttf");

    public static float getTextSize(float width, float height, float size) {
        return Math.min(width, height) * size;
    }

    public static float getTextSize(@NotNull PApplet app, float size) {
        return getTextSize(app.width, app.height, size);
    }


    public static final float ATTRACTOR_TITLE_TEXT_SIZE = 0.025f;
    public static final float STATUS_TEXT_SIZE = 0.0195f;
    public static final float CONTROLS_DES_TITLE_TEXT_SIZE = 0.020f;
    public static final float CONTROLS_DES_TEXT_SIZE = 0.0186f;

}
