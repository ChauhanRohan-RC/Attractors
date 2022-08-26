package attractors;

import math.Vector;
import org.jetbrains.annotations.NotNull;

public class LorentzAttractor implements AttractorI {

    public static final String DEFAULT_TITLE = "Lorentz Attractor";

    @NotNull
    public static final Vector DEFAULT_START = new Vector(0.01f, 0, 0);

    public static final float DEFAULT_A = 10.0f;
    public static final float DEFAULT_B = 28.0f;
    public static final float DEFAULT_C = 8.0f / 3.0f;

    @NotNull
    protected final String mTitle;
    @NotNull
    protected final Vector mStart;
    protected final float a, b, c;

    @NotNull
    protected final DrawConfig mDrawConfig;

    public LorentzAttractor(@NotNull String title, @NotNull Vector start, float a, float b, float c) {
        mTitle = title;
        mStart = start;
        this.a = a;
        this.b = b;
        this.c = c;

        mDrawConfig = new HsbDrawConfig();
    }

    public LorentzAttractor(@NotNull String title) {
        this(title, DEFAULT_START, DEFAULT_A, DEFAULT_B, DEFAULT_C);
    }

    public LorentzAttractor() {
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
        final float dx = a * (v.y - v.x) * dt;
        final float dy = (v.x * (b - v.z) - v.y) * dt;
        final float dz = ((v.x * v.y) - (c * v.z)) * dt;

        return new Vector(v.x + dx, v.y + dy, v.z + dz);
    }

    @Override
    @NotNull
    public DrawConfig drawConfig() {
        return mDrawConfig;
    }

}
