
import attractors.*;
import math.RMath;
import math.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.awt.*;
import java.util.LinkedList;

public class Main extends PApplet {

    public static final boolean DEFAULT_FREE_CAM = true;
    public static final float[] INITIAL_CAM_ROTATIONS = new float[] { 0, 0, 0 };

    private static final float SPEED_FACTOR_MIN = 0.1f;
    private static final float SPEED_FACTOR_MAX = 10f;
    private static final float SPEED_FACTOR_DEFAULT = 1f;

    private static float speedFactorUnitInc(float curValue) {
        return 0.01f;
    }

    private static float speedFactorUnitDec(float curValue) {
        return 0.01f;
    }


    public static final String CONTROLS_ATTRACTORS =
            "R: Rossler Attractor\n" +
            "L: Lorentz Attractor\n" +
            "C: ChuaAttractor\n" +
            "SHIFT-L: Modified Lorentz Attractor\n" +
            "SHIFT-C: LuChen Attractor";

    public static final String CONTROLS_OTHERS =
            "V: Toggle Camera\n" +
            "+/-: Increase/Decrease Speed\n" +
            "CTRL-R: Reset";

    public static final String DES_FULL = "\n\b\t\tCONTROLS\n\n" + CONTROLS_ATTRACTORS + "\n\n" + CONTROLS_OTHERS;

    @NotNull
    private static String getSpeedFactorText(float speedFactor) {
        return "Speed: " + (RMath.isInt(speedFactor)? String.valueOf((int) speedFactor): String.format("%.1f", speedFactor)) + "x";
    }

    @NotNull
    private static String getCameraText(boolean freeCam) {
        return "Camera [V]: " + (freeCam? "Free": "OFF");
    }

    @NotNull
    private static String getStatusText(float speedFactor, boolean freeCam) {
        return getSpeedFactorText(speedFactor) + "\n" + getCameraText(freeCam);
    }


    @NotNull
    public static Dimension windowSize(int displayW, int displayH) {
        return new Dimension(Math.round(displayW / 1.4f), Math.round(displayH / 1.4f));
    }


    private float _w, _h;

    @NotNull
    private AttractorI mAttractor = new LorentzAttractor();       // todo
    @NotNull
    private final LinkedList<Vector> mPoints = new LinkedList<>();
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private float mSpeedFactor = SPEED_FACTOR_DEFAULT;

    private long mLastDrawMs = -1;

    @Nullable
    private PeasyCam mPeasyCam;
    private boolean mFreeCam = DEFAULT_FREE_CAM;

    @Nullable
    private KeyEvent mKeyEvent;

    @NotNull
    private Vector drawOrigin() {
        return new Vector((width - xMax - xMin) / 2, (height - yMax - yMin) / 2, -(zMax + zMin) / 2);
    }


    @Override
    public void settings() {
        final Dimension s = windowSize(displayWidth, displayHeight);
        size(s.width, s.height, P3D);
//        fullScreen(P3D);

        _w = width; _h = height;
    }

    @Override
    public void setup() {
        surface.setTitle("Attractors");
        surface.setResizable(true);
        frameRate(120);

        // Sync
        setFreeCamInternal(mFreeCam);
        setAttractorInternal(mAttractor);
    }



    protected void onResized(int w, int h) {
//        if (bgImage != null) {
//            bgImage.resize(w, h);
//        }

        considerReCreateCam();
    }

    public void preDraw() {
        if (_w != width || _h != height) {
            _w = width; _h = height;
            onResized(width, height);
        }

        /* Handle Keys */
        if (keyPressed && mKeyEvent != null) {
            switch (mKeyEvent.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_DEAD_CEDILLA, java.awt.event.KeyEvent.VK_PLUS -> changeSpeedFactorByUnit(true);
                case java.awt.event.KeyEvent.VK_DEAD_OGONEK, java.awt.event.KeyEvent.VK_MINUS -> changeSpeedFactorByUnit(false);
            }
        }
    }

    @Override
    public void draw() {
        preDraw();

        final AttractorI attr = mAttractor;
        final DrawConfig drawConfig = attr.drawConfig();
        background(drawConfig.bg().getRGB());

        final long now = System.currentTimeMillis();
        final Vector lastP = mPoints.peekLast();
        final Vector newP;

        if (!(lastP == null || mLastDrawMs == -1)) {
            final float dt = drawConfig.getStepPerMs() * (now - mLastDrawMs) * mSpeedFactor;
            newP = attr.calculateNextPoint(lastP, dt);
        } else {
            newP = attr.getStart();
        }

        mPoints.add(newP);
        xMin = Math.min(xMin, newP.x); xMax = Math.max(xMax, newP.x);
        yMin = Math.min(yMin, newP.y); yMax = Math.max(yMax, newP.y);
        zMin = Math.min(zMin, newP.z); zMax = Math.max(zMax, newP.z);

        if (mPoints.size() > Math.max(drawConfig.getDrawingMaxPoints(), 1)) {
            mPoints.removeFirst();          // pop
        }


        // Draw
        pushMatrix();

        final Vector o = drawOrigin();
        translate(o.x, o.y, o.z);

        scale(drawConfig.getDrawingScale(this));
        strokeWeight(drawConfig.getDrawingStrokeWeight(this));

        final Color fill = drawConfig.drawingFill();
        if (fill != null) {
            fill(fill.getRGB());
        } else {
            noFill();
        }

        beginShape();
        int i = 0;
        final int count = mPoints.size();
        for (Vector p: mPoints) {
            stroke(drawConfig.colorForPoint(p, i, count).getRGB());
            vertex(p.x, p.y, p.z);
            i++;
        }

        endShape();
        popMatrix();

        /* .................................HUD........................... */
        if (mPeasyCam != null) {
            mPeasyCam.beginHUD();
        }



        // Title
        textAlign(LEFT, BOTTOM);
        textSize(24);
        fill(drawConfig.fg().getRGB());
        text(attr.getTitle(),20, height - 20);

        // Status
        final String status = getStatusText(mSpeedFactor, mFreeCam);
        textAlign(RIGHT, BOTTOM);
        textSize(17);
        fill(drawConfig.fg().getRGB());
        text(status,width - 20, height - 20);

        // Controls
//        textAlign(LEFT, TOP);
//        textSize(14);
//        fill(drawConfig.fg().getRGB());
//        text(CONTROLS_ATTRACTORS,20, 20);

        if (mPeasyCam != null) {
            mPeasyCam.endHUD();
        }

        mLastDrawMs = now;
        postDraw();
    }


    private void postDraw() {

    }


    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);
        mKeyEvent = event;

        final int keyCode = event.getKeyCode();

        switch (keyCode) {
            case java.awt.event.KeyEvent.VK_V -> toggleFreeCam();

            case java.awt.event.KeyEvent.VK_R -> {
                if (event.isControlDown()) {
                    reset();
                } else {
                    setAttractor(new RosslerAttractor());
                }
            }

            case java.awt.event.KeyEvent.VK_L -> setAttractor(event.isShiftDown()? new ModifiedLorentzAttractor(): new LorentzAttractor());
            case java.awt.event.KeyEvent.VK_C -> setAttractor(event.isShiftDown()? new LuChenAttractor(): new ChuaAttractor());
        }
    }

    private void reset() {
        mPoints.clear();
        mLastDrawMs = -1;
    }

    protected void onSpeedFactorChanged(float speedFactor) {

    }

    public float getSpeedFactor() {
        return mSpeedFactor;
    }



    public void setSpeedFactor(float speedFactor) {
        speedFactor = RMath.constraint(SPEED_FACTOR_MIN, SPEED_FACTOR_MAX, speedFactor);
        if (mSpeedFactor == speedFactor)
            return;

        mSpeedFactor = speedFactor;
        onSpeedFactorChanged(speedFactor);
    }

    public void changeSpeedFactorByUnit(boolean inc) {
        setSpeedFactor(mSpeedFactor + (inc? speedFactorUnitInc(mSpeedFactor): -speedFactorUnitDec(mSpeedFactor)));
    }


    protected void onAttractorChanged(@Nullable AttractorI prev, @NotNull AttractorI _new) {
        reset();
        surface.setTitle(_new.getTitle());
    }

    @NotNull
    public AttractorI getAttractor() {
        return mAttractor;
    }

    private void setAttractorInternal(@NotNull AttractorI attractor) {
        final AttractorI prev = mAttractor;
        mAttractor = attractor;
        onAttractorChanged(prev, attractor);
    }

    public boolean setAttractor(@NotNull AttractorI attractor) {
        if (mAttractor == attractor)
            return false;

        setAttractorInternal(attractor);
        return true;
    }



    /* Camera */

    @NotNull
    private PeasyCam createCam(@Nullable float[] rotations) {
        final Vector o = drawOrigin();
        final PeasyCam cam = new PeasyCam(this, o.x, o.y, o.z, o.y / tan(radians(26)));

        if (rotations == null) {
            rotations = INITIAL_CAM_ROTATIONS;
        }

        cam.setRotations(rotations[0], rotations[1], rotations[2]);
        return cam;
    }

    @NotNull
    private PeasyCam createCam() {
        return createCam(null);
    }

    private void considerReCreateCam() {
        if (!mFreeCam)
            return;

        float[] rotations = null;
        if (mPeasyCam != null) {
            mPeasyCam.setActive(false);
            rotations = mPeasyCam.getRotations();
        }

        mPeasyCam = createCam(rotations);
    }

    private void setFreeCamInternal(boolean freeCam) {
        mFreeCam = freeCam;

        if (freeCam) {
            considerReCreateCam();
        } else if (mPeasyCam != null) {
            mPeasyCam.setActive(false);     // Do not reset or nullify
        }
    }

    public void serFreeCam(boolean freeCam) {
        if (mFreeCam == freeCam)
            return;
        setFreeCamInternal(freeCam);
    }

    public void toggleFreeCam() {
        setFreeCamInternal(!mFreeCam);
    }

    public boolean isCamFree() {
        return mFreeCam;
    }



    public static void main(String[] args) {
        print(DES_FULL);

        final Main app = new Main();
        runSketch(concat(new String[]{app.getClass().getName()}, args), app);
    }
}
