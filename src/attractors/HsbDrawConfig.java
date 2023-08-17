package attractors;

import math.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import processing.core.PApplet;

import java.awt.*;

public class HsbDrawConfig implements DrawConfig {

    private static final Color COLOR_ACCENT = new Color(255, 219, 77, 255);
    private static final Color COLOR_ACCENT2 = new Color(77, 157, 255, 255);

    @Override
    public float getStepPerMs() {
        return 0.0004f;
    }

    @Override
    public int getDrawingMaxPoints() {
        return 70000;
    }

    @Override
    public float getDrawingScale(@NotNull PApplet p) {
        return 8;
    }

    @Override
    public float getDrawingStrokeWeight(@NotNull PApplet p) {
        return 0.5f;
    }

    @Override
    @NotNull
    public Color bg() {
        return Color.BLACK;
    }

    @Override
    public Color fg() {
        return Color.WHITE;
    }

    @Override
    public Color accent() {
        return COLOR_ACCENT;
    }

    @Override
    public Color accent2() {
        return COLOR_ACCENT2;
    }

    @Override
    @Nullable
    public Color drawingFill() {
        return null;
    }

    @Override
    @NotNull
    public Color colorForPoint(@NotNull Vector v, int index, int count) {
        return Color.getHSBColor((float) index / count, 1, 1);
    }

}
