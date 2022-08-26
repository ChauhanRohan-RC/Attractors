package attractors;

import math.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import processing.core.PApplet;

import java.awt.*;

public interface DrawConfig {

    float getStepPerMs();

    int getDrawingMaxPoints();         // < 0 for unlimited

    float getDrawingScale(@NotNull PApplet p);

    float getDrawingStrokeWeight(@NotNull PApplet p);


    @NotNull
    Color bg();

    Color fg();

    @Nullable
    Color drawingFill();

    @NotNull
    Color colorForPoint(@NotNull Vector v, int index, int count);

}
