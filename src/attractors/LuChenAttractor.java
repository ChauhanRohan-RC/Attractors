package attractors;

import math.Vector;
import org.jetbrains.annotations.NotNull;

public class LuChenAttractor implements AttractorI {

    public static final String DEFAULT_TITLE = "Lu Chen Attractor";

    @NotNull
    public static final Vector DEFAULT_START = new Vector(0.1f, 0.3f, -0.6f);

    public static final float DEFAULT_A = 36f;
    public static final float DEFAULT_B = 3f;
    public static final float DEFAULT_C = 20f;
    public static final float DEFAULT_U = -15.15f;

//    public static final float DEFAULT_D1 = 1;
//    public static final float DEFAULT_D2 = -20;

//    public static final float DEFAULT_TAU = 0.2f;


    @NotNull
    private final String mTitle;
    @NotNull
    private final Vector mStart;
    private final float a, b, c, u;

    @NotNull
    private final DrawConfig mDrawConfig;

    public LuChenAttractor(@NotNull String title, @NotNull Vector start, float a, float b, float c, float u) {
        mTitle = title;
        mStart = start;
        this.a = a;
        this.b = b;
        this.c = c;
        this.u = u;

        mDrawConfig = new HsbDrawConfig();
    }

    public LuChenAttractor(@NotNull String title) {
        this(title, DEFAULT_START, DEFAULT_A, DEFAULT_B, DEFAULT_C, DEFAULT_U);
    }

    public LuChenAttractor() {
        this(DEFAULT_TITLE);
    }


    @Override
    public @NotNull String getTitle() {
        return mTitle;
    }

    @Override
    public @NotNull Vector getStart() {
        return mStart;
    }

    @Override
    public @NotNull Vector calculateNextPoint(@NotNull Vector v, float dt) {
        final float dx = (a * (v.y - v.x)) * dt;
        final float dy = ((v.x * (1 - v.z)) + (c * v.y) + u) * dt;
        final float dz = ((v.x * v.y) - (b * v.z)) * dt;

        return new Vector(v.x + dx, v.y + dy, v.z + dz);
    }

    @Override
    public @NotNull DrawConfig drawConfig() {
        return mDrawConfig;
    }
}
