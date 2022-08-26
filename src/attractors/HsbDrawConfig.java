package attractors;

import math.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import processing.core.PApplet;

import java.awt.*;

public class HsbDrawConfig implements DrawConfig {

    @Override
    public float getStepPerMs() {
        return 0.0004f;
    }

    @Override
    public int getDrawingMaxPoints() {
        return 50000;
    }

    @Override
    public float getDrawingScale(@NotNull PApplet p) {
        return 5;
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
