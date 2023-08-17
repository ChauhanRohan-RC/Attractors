
import attractors.*;
import math.RMath;
import math.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.KeyEvent;
import processing.opengl.PJOGL;

import java.awt.*;
import java.util.LinkedList;

public class Main extends PApplet {

    public static final boolean DEFAULT_FREE_CAM = true;
    public static final float[] INITIAL_CAM_ROTATIONS = new float[] { 0, 0, 0 };

    public static final boolean DEFAULT_CONTROLS_SHOWN = true;
    public static final boolean SHOW_CONTROLS_DES = true;

    private static final float SPEED_FACTOR_MIN = 0.1f;
    private static final float SPEED_FACTOR_MAX = 10f;
    private static final float SPEED_FACTOR_DEFAULT = 1f;

    public static final float ATTRACTOR_ZOOM_MIN = 0.05f;
    public static final float ATTRACTOR_ZOOM_MAX = 5;

    public static final boolean ATTRACTOR_INVERT_X = false;
    public static final boolean ATTRACTOR_INVERT_Y = true;
    public static final boolean ATTRACTOR_INVERT_Z = false;

    public static float attractorZoomIncStep(float current) {
        return 0.01f;
    }

    public static float attractorZoomDecStep(float current) {
        return 0.01f;
    }


    private static float speedFactorUnitInc(float curValue) {
        return 0.01f;
    }

    private static float speedFactorUnitDec(float curValue) {
        return 0.01f;
    }


    public static final String DES_CONTROLS_ATTRACTORS = "R .............. Rossler Attractor\n" +
            "L .............. Lorentz Attractor\n" +
            "C .............. ChuaAttractor\n" +
            "M .............. Modified Lorentz Attractor\n" +
            "H .............. LuChen Attractor\n\n" +
            "+/- ............ Increase/Decrease Speed\n" +
            "Shift +/- ........ Increase/Decrease fixed Zoom";

    public static final String DES_CONTROLS_OTHERS =
            "V .............. Toggle Camera\n" +
            "CTRL-R ........... Reset Attractor\n" +
            "Shift-R .......... Reset fixed Zoom\n" +
            "Shift-C .......... Toggle Controls";

    public static final String DES_FULL = "\n\b\t\tCONTROLS\n\n" + DES_CONTROLS_ATTRACTORS + "\n\n" + DES_CONTROLS_OTHERS;

    @NotNull
    private static String getSpeedFactorText(float speedFactor) {
        return "Speed: " + (RMath.isInt(speedFactor)? String.valueOf((int) speedFactor): String.format("%.1f", speedFactor)) + "x";
    }

    @NotNull
    private static String getCameraText(boolean freeCam) {
        return "Camera [V]: " + (freeCam? "Free": "Fixed");
    }

    @NotNull
    private static String getStatusText(float speedFactor, boolean freeCam) {
        return getCameraText(freeCam) + "  |  " + getSpeedFactorText(speedFactor);
    }


    @NotNull
    public static Dimension windowSize(int displayW, int displayH) {
        return new Dimension(Math.round(displayW / 1.4f), Math.round(displayH / 1.4f));
    }

    public float getTextSize(float size) {
        return R.getTextSize(this, size);
    }



    // Ui
    private float _w, _h;
    private PFont pdSans, pdSansMedium;

    @NotNull
    private AttractorI mAttractor = new LorentzAttractor();       // todo
    @NotNull
    private final LinkedList<Vector> mPoints = new LinkedList<>();
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private float mSpeedFactor = SPEED_FACTOR_DEFAULT;

    private float attractorZoom = 1;

    private long mLastDrawMs = -1;

    @Nullable
    private PeasyCam mPeasyCam;
    private boolean mFreeCam = DEFAULT_FREE_CAM;
    private boolean mShowControls = DEFAULT_CONTROLS_SHOWN;

    @Nullable
    private KeyEvent mKeyEvent;

    @NotNull
    private Vector drawOrigin() {
        return new Vector((width - xMax - xMin) / 2, (height - yMax - yMin) / 2, -(zMax + zMin) / 2);
    }


    @Override
    public void settings() {
        final Dimension size = windowSize(displayWidth, displayHeight);
        size(size.width, size.height, P3D);

        _w = width;
        _h = height;

        PJOGL.setIcon(R.APP_ICON.toString());       // app icon
    }

    @Override
    public void setup() {
        surface.setTitle(R.APP_NAME);
        surface.setResizable(true);
        frameRate(120);

        // Sync
        setFreeCamInternal(mFreeCam);
        setAttractorInternal(mAttractor);


        pdSans = createFont(R.FONT_PD_SANS_REGULAR.toString(), 20);
        pdSansMedium = createFont(R.FONT_PD_SANS_MEDIUM.toString(), 20);

        textFont(pdSans);       // Default
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
                case java.awt.event.KeyEvent.VK_DEAD_CEDILLA, java.awt.event.KeyEvent.VK_PLUS -> {
                    if (mKeyEvent.isShiftDown()) {
                        incAttractorZoom();
                    } else {
                        changeSpeedFactorByUnit(true);
                    }
                }
                case java.awt.event.KeyEvent.VK_DEAD_OGONEK, java.awt.event.KeyEvent.VK_MINUS -> {
                    if (mKeyEvent.isShiftDown()) {
                        decAttractorZoom();
                    } else {
                        changeSpeedFactorByUnit(false);
                    }
                }

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

        if (!mFreeCam) {
            camera(o.x * 0.53f /* sin(radians(32)) */, o.y * -0.95f /* sin(radians(-72)) */, o.y * 1.6f /* 1/tan(radians(32)) */, 0, 0, 0, 0, 1, 0);
        }

        final float scale = drawConfig.getDrawingScale(this) * getAttractorZoom();
        scale(scale * (ATTRACTOR_INVERT_X ? -1 : 1), scale * (ATTRACTOR_INVERT_Y ? -1 : 1), scale * (ATTRACTOR_INVERT_Z ? -1 : 1));

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


        final float h_offset = width * 0.009f;
        final float v_offset = height / 96f;

        // Title
        final float attTitleTextSize = getTextSize(R.ATTRACTOR_TITLE_TEXT_SIZE);
        textAlign(LEFT, BOTTOM);
        textFont(pdSansMedium, attTitleTextSize);
        fill(drawConfig.accent().getRGB());
        text(attr.getTitle(), h_offset, height - v_offset - attTitleTextSize);

        // Status
        final float statusTextSize = getTextSize(R.STATUS_TEXT_SIZE);
        final String status = getStatusText(mSpeedFactor, mFreeCam);
        textAlign(RIGHT, BOTTOM);
        textFont(pdSans, statusTextSize);
        fill(drawConfig.fg().getRGB());
        text(status,width - h_offset, height - v_offset - statusTextSize);

        // Controls
        if (controlsShown()) {
            pushStyle();
            final float titleTextSize = getTextSize(R.CONTROLS_DES_TITLE_TEXT_SIZE);
            final float titleY = v_offset * 2;

            fill(drawConfig.accent().getRGB());
            textFont(pdSansMedium, titleTextSize);
            textAlign(LEFT, TOP);
            text("Attractor Controls", h_offset * 3, titleY);
            textAlign(RIGHT, TOP);
            text("View Controls", width - h_offset * 3, titleY);

            fill(drawConfig.fg().getRGB());
            textFont(pdSans, getTextSize(R.CONTROLS_DES_TEXT_SIZE));

            final float desY = titleY + titleTextSize + v_offset * 1.5f;
            textAlign(LEFT, TOP);
            text(DES_CONTROLS_ATTRACTORS, h_offset, desY);

            textAlign(RIGHT, TOP);
            text(DES_CONTROLS_OTHERS, width - h_offset, desY);
            popStyle();
        }

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
                    resetAttractor();
                } else if (event.isShiftDown()) {
                    resetAttractorZoom();
                } else {
                    setAttractor(new RosslerAttractor());
                }
            }

            case java.awt.event.KeyEvent.VK_L -> setAttractor(new LorentzAttractor());
            case java.awt.event.KeyEvent.VK_C -> {
                if (event.isShiftDown()) {
                    toggleShowControls();
                } else {
                    setAttractor(new ChuaAttractor());
                }
            }

            case java.awt.event.KeyEvent.VK_M -> setAttractor(new ModifiedLorentzAttractor());
            case java.awt.event.KeyEvent.VK_H -> setAttractor(new LuChenAttractor());
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyReleased(event);
        if (mKeyEvent != null && mKeyEvent.getKeyCode() == event.getKeyCode()) {
            mKeyEvent = null;
        }
    }

    private void resetAttractor() {
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
        resetAttractor();
        surface.setTitle(R.APP_NAME + " - " + _new.getTitle());
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


    public void setShowControls(boolean showControls) {
        mShowControls = showControls;
    }

    public void toggleShowControls() {
        setShowControls(!mShowControls);
    }

    public boolean controlsShown() {
        return SHOW_CONTROLS_DES && mShowControls;
    }


    /* Camera */

    public void setAttractorZoom(float zoom) {
        if (mFreeCam)
            return;
        attractorZoom = constrain(zoom, ATTRACTOR_ZOOM_MIN, ATTRACTOR_ZOOM_MAX);
    }

    public float getAttractorZoom() {
        return mFreeCam? 1 : attractorZoom;
    }

    public void incAttractorZoom() {
        setAttractorZoom(attractorZoom + attractorZoomIncStep(attractorZoom));
    }

    public void decAttractorZoom() {
        setAttractorZoom(attractorZoom - attractorZoomDecStep(attractorZoom));
    }

    public void resetAttractorZoom() {
        setAttractorZoom(1);
    }

    @NotNull
    private PeasyCam createCam(@Nullable float[] rotations) {
        final Vector o = drawOrigin();
        final PeasyCam cam = new PeasyCam(this, o.x, o.y, o.z, o.y / tan(radians(26)));

        if (rotations == null) {
            rotations = INITIAL_CAM_ROTATIONS;
        }

        cam.reset();

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
