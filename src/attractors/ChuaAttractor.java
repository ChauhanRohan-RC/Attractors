package attractors;

import math.RMath;
import math.Vector;
import org.jetbrains.annotations.NotNull;

public class ChuaAttractor implements AttractorI {

    public static final String DEFAULT_TITLE = "Chua Attractor";

    @NotNull
    public static final Vector DEFAULT_START = new Vector(1, 1, 0);

    public static final float DEFAULT_A = 1.3f;
    public static final float DEFAULT_B = 0.11f;
    public static final float DEFAULT_C = 7.0f;
    public static final float DEFAULT_D = 0f;

    public static final float DEFAULT_ALPHA = 10.82f;
    public static final float DEFAULT_BETA = 14.286f;

    @NotNull
    private final String mTitle;
    @NotNull
    private final Vector mStart;
    private final float a, b, c, d, alpha, beta;

    @NotNull
    private final DrawConfig mDrawConfig;

    public ChuaAttractor(@NotNull String title, @NotNull Vector start, float a, float b, float c, float d, float alpha, float beta) {
        mTitle = title;
        mStart = start;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.alpha = alpha;
        this.beta = beta;

        mDrawConfig = new HsbDrawConfig();
    }

    public ChuaAttractor(@NotNull String title) {
        this(title, DEFAULT_START, DEFAULT_A, DEFAULT_B, DEFAULT_C, DEFAULT_D, DEFAULT_ALPHA, DEFAULT_BETA);
    }

    public ChuaAttractor() {
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

    private float h(@NotNull Vector v) {
        return -b * RMath.sin((RMath.PI * v.x / (2 * a)) + d);
    }

    @Override
    @NotNull
    public Vector calculateNextPoint(@NotNull Vector v, float dt) {
        final float h = h(v);

        final float dx = alpha * (v.y - h) * dt;
        final float dy = (v.x - v.y + v.z) * dt;
        final float dz = -beta * v.y * dt;

        return new Vector(v.x + dx, v.y + dy, v.z + dz);
    }

    @Override
    @NotNull
    public DrawConfig drawConfig() {
        return mDrawConfig;
    }

}
