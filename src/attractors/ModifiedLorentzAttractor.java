package attractors;

import math.Vector;
import org.jetbrains.annotations.NotNull;

public class ModifiedLorentzAttractor extends LorentzAttractor {

    public static final String DEFAULT_TITLE = "Modified Lorentz Attractor";

    @NotNull
    public static final Vector DEFAULT_START = new Vector(-8f, 4, 10);

    public static final float DEFAULT_A = 10.0f;
    public static final float DEFAULT_B = 8.0f / 3.0f;
    public static final float DEFAULT_C = 137.0f / 5.0f;

    public ModifiedLorentzAttractor(@NotNull String title, @NotNull Vector start, float a, float b, float c) {
        super(title, start, a, b, c);
    }

    public ModifiedLorentzAttractor(@NotNull String title) {
        this(title, DEFAULT_START, DEFAULT_A, DEFAULT_B, DEFAULT_C);
    }

    public ModifiedLorentzAttractor() {
        this(DEFAULT_TITLE);
    }


    @Override
    @NotNull
    public Vector calculateNextPoint(@NotNull Vector v, float dt) {
        final float x2_min_y2 = v.x * v.x - v.y * v.y;
        final float x2_plus_y2 = v.x * v.x + v.y * v.y;

        final float mag2d = (float) Math.sqrt(x2_plus_y2);

        final float dx = ((-(1 + a) * v.x  + a - c + v.z * v.y) / 3) + ((((1 - a) * x2_min_y2) + ((2 * (a + c - v.z)) * v.x * v.y)) / (3 * mag2d));
        final float dy = (((c - a - v.z) * v.x - (a + 1) * v.y) / 3) + (((2 * (a - 1) * v.x * v.y) + ( a + c - v.z) * x2_min_y2) / (3 * mag2d));
        final float dz = ((3 * v.x * v.x - v.y * v.y) * (v.y / 2)) - (b * v.z);

        return new Vector(v.x + dx * dt, v.y + dy * dt, v.z + dz * dt);



    }

}

