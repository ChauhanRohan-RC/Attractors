package attractors;

import math.Vector;
import org.jetbrains.annotations.NotNull;

public class RosslerAttractor implements AttractorI {

    public static final String DEFAULT_TITLE = "Rossler Attractor";

    @NotNull
    public static final Vector DEFAULT_START = new Vector(1f, 2f, 3f);

    public static final float DEFAULT_A = 0.2f;
    public static final float DEFAULT_B = 0.2f;
    public static final float DEFAULT_C = 5.7f;

    @NotNull
    private final String mTitle;
    @NotNull
    private final Vector mStart;
    private final float a, b, c;

    @NotNull
    private final DrawConfig mDrawConfig;

    public RosslerAttractor(@NotNull String title, @NotNull Vector start, float a, float b, float c) {
        mTitle = title;
        mStart = start;
        this.a = a;
        this.b = b;
        this.c = c;

        mDrawConfig = new HsbDrawConfig();
    }

    public RosslerAttractor(@NotNull String title) {
        this(title, DEFAULT_START, DEFAULT_A, DEFAULT_B, DEFAULT_C);
    }

    public RosslerAttractor() {
        this(DEFAULT_TITLE);
    }

    @Override
    @NotNull
    public String getTitle() {
        return mTitle;
    }

    @Override
    @NotNull
    public Vector getStart() {
        return mStart;
    }

    @Override
    @NotNull
    public Vector calculateNextPoint(@NotNull Vector v, float dt) {
        final float dx = -(v.y + v.z) * dt;
        final float dy = (v.x + (a * v.y)) * dt;
        final float dz = (b + (v.z * (v.x - c))) * dt;

        return new Vector(v.x + dx, v.y + dy, v.z + dz);
    }

    @Override
    @NotNull
    public DrawConfig drawConfig() {
        return mDrawConfig;
    }


}



