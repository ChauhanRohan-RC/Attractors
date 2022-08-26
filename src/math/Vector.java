package math;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * A class to describe a two or three dimensional vector
 *
 * <p>
 * The result of all functions are applied to the vector itself, with the
 * exception of cross(), which returns a new Vector (or writes to a specified
 * 'target' Vector). That is, add() will add the contents of one vector to
 * this one. Using add() with additional parameters allows you to put the
 * result into a new Vector. Functions that act on multiple vectors also
 * include static versions. Because creating new objects can be computationally
 * expensive, most functions include an optional 'target' Vector, so that a
 * new Vector object is not created with each operation.
 * <p>
 */
public class Vector implements Serializable {

    public static final Vector ZERO = new Vector();

    public static final Vector PLUS_I = new Vector(1, 0, 0);
    public static final Vector MINUS_I = new Vector(-1, 0, 0);

    public static final Vector PLUS_J = new Vector(0, 1, 0);
    public static final Vector MINUS_J = new Vector(0, -1, 0);

    public static final Vector PLUS_K = new Vector(0, 0, 1);
    public static final Vector MINUS_K = new Vector(0, 0, -1);



    /**
     * Serializable version identifier
     * */
    private static final long serialVersionUID = 2376089092374327344L;


    /**
     * The x component of the vector
     *
     * This field can be used to both get and set the value
     */
    public float x;

    /**
     * The y component of the vector
     *
     * This field can be used to both get and set the value
     */
    public float y;

    /**
     * The z component of the vector
     *
     * This field can be used to both get and set the value
     */
    public float z;

    /**
     * Array so that this can be temporarily used in an array context
     */
    transient protected float[] array;


    /**
     * Constructor for an empty vector: x, y, and z are set to 0.
     */
    public Vector() {
    }


    /**
     * Constructor for a 3D vector.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Constructor for a 2D vector: z coordinate is set to 0.
     */
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Sets the x, y, and z component of the vector using two or three separate
     * variables, the data from a Vector, or the values from a float array.
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     */
    @NotNull
    public Vector set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }


    /**
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    @NotNull
    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        return this;
    }


    /**
     * @param v any variable of type Vector
     */
    @NotNull
    public Vector set(@NotNull Vector v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }


    /**
     * Set the x, y (and maybe z) coordinates using a float[] array as the source
     *
     * @param source array to copy from
     */
    @NotNull
    public Vector set(@NotNull float[] source) {
        if (source.length >= 2) {
            x = source[0];
            y = source[1];
        }
        if (source.length >= 3) {
            z = source[2];
        } else {
            z = 0;
        }
        return this;
    }


    /**
     * Make a new 2D unit vector with a random direction
     *
     * @return the random Vector
     * @see Vector#random3D()
     */
    @NotNull
    public static Vector random2D() {
        return random2D(null);
    }

    /**
     * Make a new 2D unit vector with a random direction
     *
     * @return the random Vector
     */
    @NotNull
    public static Vector random2D(@Nullable Vector target) {
        return fromAngle((float) (Math.random() * Math.PI * 2), target);
    }


    /**
     * Make a new 3D unit vector with a random direction
     *
     * @return the random Vector
     * @see Vector#random2D()
     */
    @NotNull
    public static Vector random3D() {
        return random3D(null);
    }


    /**
     * Make a new 3D unit vector with a random direction
     *
     * @return the random Vector
     */
    @NotNull
    public static Vector random3D(@Nullable Vector target) {
        float angle = (float) (Math.random() * RMath.TWO_PI);
        float vz = (float) (Math.random() * 2 - 1);

        float vx = (float) (Math.sqrt(1 - vz * vz) * Math.cos(angle));
        float vy = (float) (Math.sqrt(1 - vz * vz) * Math.sin(angle));
        if (target == null) {
            target = new Vector(vx, vy, vz);
            //target.normalize(); // Should be unnecessary
        } else {
            target.set(vx, vy, vz);
        }
        return target;
    }


    /**
     * Make a new 2D unit vector from an angle.
     *
     * @param angle the angle in radians
     * @return the new unit Vector
     */
    @NotNull
    public static Vector fromAngle(float angle) {
        return fromAngle(angle, null);
    }


    /**
     * Make a new 2D unit vector from an angle
     *
     * @param target the target vector (if null, a new vector will be created)
     * @return the Vector
     */
    @NotNull
    public static Vector fromAngle(float angle, @Nullable Vector target) {
        if (target == null) {
            target = new Vector((float) Math.cos(angle), (float) Math.sin(angle), 0);
        } else {
            target.set((float) Math.cos(angle), (float) Math.sin(angle), 0);
        }
        return target;
    }


    /**
     * @return a copy of the vector
     */
    @NotNull
    public Vector copy() {
        return new Vector(x, y, z);
    }


    /**
     * Get a copy of coordinates of this vector into an array
     *
     * @param target array to copy coordinates, or {@code null} to create new array
     */
    @NotNull
    public float[] get(@Nullable float[] target) {
        if (target == null) {
            return new float[]{x, y, z};
        }

        if (target.length >= 2) {
            target[0] = x;
            target[1] = y;
        }
        if (target.length >= 3) {
            target[2] = z;
        }
        return target;
    }


    /**
     * @return magnitude (length) of the vector
     * @see Vector#magD(Vector)
     */
    public double magD() {
        return magD(this);
    }

    /**
     * @return magnitude (length) of the vector
     * @see Vector#mag(Vector)
     */
    public float mag() {
        return mag(this);
    }


    /**
     * @return square of magnitude (length) of the vector
     * @see Vector#magSqD(Vector)
     */
    public double magSqD() {
        return magSqD(this);
    }

    /**
     * @return square of magnitude (length) of the vector
     * @see Vector#magSq(Vector)
     */
    public float magSq() {
        return magSq(this);
    }


    /**
     * Calculates the square of magnitude (length) of the vector
     * (magSq V = <em>x*x + y*y + z*z</em>)
     *
     * @param v the vector
     * @return square of magnitude (length) of the vector
     */
    public static float magSq(@NotNull Vector v) {
        return (float) magSqD(v);
    }

    /**
     * Calculates the square of magnitude (length) of the vector
     * (magSq V = <em>x*x + y*y + z*z</em>)
     *
     * @param v the vector
     * @return square of magnitude (length) of the vector
     */
    public static double magSqD(@NotNull Vector v) {
        return (v.x * v.x) + (v.y * v.y) + (v.z * v.z);
    }


    /**
     * Calculates the magnitude (length) of the vector
     * (mag V = <em>sqrt(x*x + y*y + z*z)</em>)
     *
     * @param v the vector
     * @return magnitude (length) of the vector
     * @see Vector#magSq()
     */
    public static double magD(@NotNull Vector v) {
        return Math.sqrt(magSqD(v));
    }

    /**
     * Calculates the magnitude (length) of the vector
     * (mag V = <em>sqrt(x*x + y*y + z*z)</em>)
     *
     * @param v the vector
     * @return magnitude (length) of the vector
     * @see Vector#magSq()
     */
    public static float mag(@NotNull Vector v) {
        return (float) magD(v);
    }


    /**
     * Adds specified vector to this vector
     *
     * @param v the vector to be added
     */
    @NotNull
    public Vector add(@NotNull Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }


    /**
     * Adds specified vector to this vector
     *
     * @param x x component of the vector
     * @param y y component of the vector
     */
    @NotNull
    public Vector add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }


    /**
     * Adds specified vector to this vector
     *
     * @param x x component of the vector
     * @param y y component of the vector
     * @param z z component of the vector
     */
    @NotNull
    public Vector add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }


    /**
     * Add two vectors
     *
     * @param v1 a vector
     * @param v2 another vector
     */
    @NotNull
    public static Vector add(@NotNull Vector v1, @NotNull Vector v2) {
        return add(v1, v2, null);
    }


    /**
     * Add two vectors into a target vector
     *
     * @param target the target vector (if null, a new vector will be created)
     */
    @NotNull
    public static Vector add(@NotNull Vector v1, @NotNull Vector v2, @Nullable Vector target) {
        if (target == null) {
            target = new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }

        return target;
    }


    /**
     * Subtract specified vector from this vector
     *
     * @param v any variable of type Vector
     */
    @NotNull
    public Vector sub(@NotNull Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }


    /**
     * Subtract specified vector from this vector
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    @NotNull
    public Vector sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }


    /**
     * Subtract specified vector from this vector
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     */
    @NotNull
    public Vector sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }


    /**
     * Subtract one vector from another
     *
     * @param v1 first vector
     * @param v2 second vector
     */
    @NotNull
    public static Vector sub(@NotNull Vector v1, @NotNull Vector v2) {
        return sub(v1, v2, null);
    }


    /**
     * Subtract one vector from another and store in another vector
     *
     * @param target Vector in which to store the result
     * @return result vector
     */
    @NotNull
    public static Vector sub(@NotNull Vector v1, @NotNull Vector v2, @Nullable Vector target) {
        if (target == null) {
            target = new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }
        return target;
    }


    /**
     * Multiplies this vector by a scalar
     *
     * @param n the number to multiply with the vector
     */
    @NotNull
    public Vector mult(float n) {
        x *= n;
        y *= n;
        z *= n;
        return this;
    }


    /**
     * Multiplies a vector by a scalar
     *
     * @param v the vector to multiply by the scalar
     */
    @NotNull
    public static Vector mult(@NotNull Vector v, float n) {
        return mult(v, n, null);
    }


    /**
     * Multiply a vector by a scalar, and write the result into a target Vector
     *
     * @param target Vector in which to store the result
     */
    @NotNull
    public static Vector mult(@NotNull Vector v, float n, @Nullable Vector target) {
        if (target == null) {
            target = new Vector(v.x * n, v.y * n, v.z * n);
        } else {
            target.set(v.x * n, v.y * n, v.z * n);
        }
        return target;
    }


    /**
     * Divides this vector by a scalar
     *
     * @param n the number by which to divide the vector
     */
    @NotNull
    public Vector div(float n) {
        return div(this, n, this);
    }


    /**
     * Divide a vector by a scalar and return the result in a new vector.
     *
     * @param v the vector to divide by the scalar
     * @return a new vector that is v1 / n
     */
    @NotNull
    public static Vector div(@NotNull Vector v, float n) {
        return div(v, n, null);
    }


    /**
     * Divide a vector by a scalar and store the result in another vector
     *
     * @param target Vector in which to store the result
     */
    @NotNull
    public static Vector div(@NotNull Vector v, float n, @Nullable Vector target) {
        if (target == null) {
            target = new Vector(v.x / n, v.y / n, v.z / n);
        } else {
            target.set(v.x / n, v.y / n, v.z / n);
        }
        return target;
    }


    /**
     * Calculates the Euclidean distance between this and given vector
     *
     * @param v other vector
     */
    public float dist(@NotNull Vector v) {
        return dist(this, v);
    }

    /**
     * Calculates the square of Euclidean distance between this and given vector
     *
     * @param v other vector
     */
    public float distSq(@NotNull Vector v) {
        return distSq(this, v);
    }


    /**
     * Calculates the Euclidean distance between two points (considering a
     * point as a vector object)
     *
     * @param v1 first Vector (point)
     * @param v2 second Vector (point)
     * @return the Euclidean distance between v1 and v2
     */
    public static float dist(@NotNull Vector v1, @NotNull Vector v2) {
        return (float) Math.sqrt(distSq(v1, v2));
    }

    /**
     * Calculates the square of Euclidean distance between two points (considering a
     * point as a vector object)
     *
     * @param v1 first Vector (point)
     * @param v2 second Vector (point)
     * @return square of Euclidean distance between v1 and v2
     */
    public static float distSq(@NotNull Vector v1, @NotNull Vector v2) {
        final float dx = v1.x - v2.x,
                dy = v1.y - v2.y,
                dz = v1.z - v2.z;

        return (dx * dx) + (dy * dy) + (dz * dz);
    }


    /**
     * Calculates the dot product of this and other vector
     *
     * @param v other vector
     * @return the dot product
     */
    public float dot(@NotNull Vector v) {
        return dot(this, v);
    }


    /**
     * Calculates the dot product of this and other vector
     *
     * @param x x component of the vector
     * @param y y component of the vector
     * @param z z component of the vector
     * @return the dot product
     */
    public float dot(float x, float y, float z) {
        return (this.x * x) + (this.y * y) + (this.z * z);
    }


    /**
     * Calculates the dot product of two vectors
     *
     * @param v1 first vector
     * @param v2 second vector
     */
    public static double dotD(@NotNull Vector v1, @NotNull Vector v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
    }

    /**
     * Calculates the dot product of two vectors
     *
     * @param v1 first vector
     * @param v2 second vector
     */
    public static float dot(@NotNull Vector v1, @NotNull Vector v2) {
        return (float) dotD(v1, v2);
    }


    /**
     * Computes cross product between this and given vector and returns <strong>NEW</strong> vector
     *
     * @param v other vector
     * @return new crossed vector
     */
    @NotNull
    public Vector cross(@NotNull Vector v) {
        return cross(v, null);
    }


    /**
     * stores the cross product between this and given vector in the target vector
     *
     * @param v      other vector
     * @param target Vector to store the result
     * @return target vector, or a new vector
     */
    @NotNull
    public Vector cross(@NotNull Vector v, @Nullable Vector target) {
        return cross(this, v, target);
    }


    /**
     * Computes the cross product between two vector and stores in the target vector
     *
     * @param v1     first vector
     * @param v2     second vector
     * @param target Vector to store the result, or {@code null} to return a new vector
     * @return target vector, or a new vector
     */
    @NotNull
    public static Vector cross(@NotNull Vector v1, @NotNull Vector v2, @Nullable Vector target) {
        final float crossX = (v1.y * v2.z) - (v2.y * v1.z);
        final float crossY = (v1.z * v2.x) - (v2.z * v1.x);
        final float crossZ = (v1.x * v2.y) - (v2.x * v1.y);

        if (target == null) {
            target = new Vector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }

        return target;
    }


    /**
     * Normalize the vector to length 1 (make it a unit vector).
     */
    @NotNull
    public Vector normalize() {
        return normalize(null);
    }


    /**
     * Stores the unit vector corresponding to this vector in the target
     *
     * @param target target to store result, or {@code null} to return new vector
     * @return a new vector (if target was null), or target
     */
    @NotNull
    public Vector normalize(@Nullable Vector target) {
        return normalize(this, target);
    }


    /**
     * Stores the unit vector corresponding to a vector in the target vector
     *
     * @param v      the vector
     * @param target target to store result, or {@code null} to return new vector
     * @return a unit vector corresponding to given vector
     */
    @NotNull
    public static Vector normalize(@NotNull Vector v, @Nullable Vector target) {
        if (target == null) {
            target = new Vector();
        }

        final float mSq = magSq(v);   // square root is a costly operation
        if (mSq == 0 || mSq == 1) {
            target.set(v.x, v.y, v.z);
        } else {
            final float m = (float) Math.sqrt(mSq);
            target.set(v.x / m, v.y / m, v.z / m);
        }

        return target;
    }


    /**
     * Limit's the magnitude of this vector to the max value
     *
     * @param maxMag the maximum magnitude for this vector
     */
    @NotNull
    public Vector limit(float maxMag) {
        return limit(maxMag, this);
    }

    /**
     * Limit's the magnitude of this vector to the max value
     *
     * @param maxMag the maximum magnitude for this vector
     * @param target target to store result, or {@code null} to return new vector
     * @return a vector corresponding to this vector with magnitude limited by maxMag
     */
    @NotNull
    public Vector limit(float maxMag, @Nullable Vector target) {
        return limit(this, maxMag, target);
    }


    /**
     * Limit's the magnitude of a vector to the max value
     *
     * @param maxMag the maximum magnitude for the vector
     * @param target target to store result, or {@code null} to return new vector
     * @return a vector corresponding to given vector with magnitude limited by maxMag
     */
    @NotNull
    public static Vector limit(@NotNull Vector v, float maxMag, @Nullable Vector target) {
        if (target == null) {
            target = v.copy();
        }

        final float magSq = magSq(v);
        if (magSq > maxMag * maxMag) {
            if (!(magSq == 0 || magSq == 1)) {
                final float mag = (float) Math.sqrt(magSq);
                target.set(v.x / mag, v.y / mag, v.z / mag);
            } else if (target != v) {
                target.set(v.x, v.y, v.z);
            }

            target.mult(maxMag);
        }

        return target;
    }


    /**
     * Set the magnitude of this vector to the value used for the <b>len</b> parameter.
     *
     * @param len the new length for this vector
     * @brief Set the magnitude of the vector
     */
    @NotNull
    public Vector setMag(float len) {
        return setMag(len, this);
    }


    /**
     * Sets the magnitude of this vector, storing the result in another vector
     *
     * @param target Set to null to create a new vector
     * @param len    the new length for the new vector
     * @return a new vector (if target was null), or target
     */
    @NotNull
    public Vector setMag(float len, @Nullable Vector target) {
        return setMag(this, len, target);
    }


    /**
     * Sets the magnitude of a vector, storing the result in another vector
     *
     * @param v      the vector
     * @param mag    the new length for the given vector
     * @param target Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    @NotNull
    public static Vector setMag(@NotNull Vector v, float mag, @Nullable Vector target) {
        return normalize(v, target).mult(mag);
    }


    /**
     * Calculate the angle of rotation for this vector (only 2D vectors)
     *
     * @return the angle of rotation
     */
    public float heading2D() {
        return heading2D(this);
    }


    /**
     * Calculate the angle of rotation for a vector (only 2D vectors)
     *
     * @param v the vector
     * @return the angle of rotation
     */
    public static float heading2D(@NotNull Vector v) {
        return (float) Math.atan2(v.y, v.x);
    }


    /**
     * Rotates this vector by an angle. <strong>ONLY 2D vectors, magnitude remains the same</strong>
     *
     * @param theta the angle of rotation
     */
    @NotNull
    public Vector rotate2D(float theta) {
        return rotate2D(theta, this);
    }

    /**
     * Rotates this vector by an angle. <strong>ONLY 2D vectors, magnitude remains the same</strong>
     *
     * @param theta  the angle of rotation
     * @param target Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    @NotNull
    public Vector rotate2D(float theta, @Nullable Vector target) {
        return rotate2D(this, theta, target);
    }

    /**
     * Rotates a vector by an angle. <strong>ONLY 2D vectors, magnitude remains the same</strong>
     *
     * @param v      the vector
     * @param theta  the angle of rotation
     * @param target Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    @NotNull
    public static Vector rotate2D(@NotNull Vector v, float theta, @Nullable Vector target) {
        if (target == null) {
            target = new Vector();
        }

        return target.set((v.x * RMath.cos(theta)) - (v.y * RMath.sin(theta)), (v.x * RMath.sin(theta)) + (v.y * RMath.cos(theta)), v.z);
    }


    /**
     * Linear interpolate the vector to x,y,z values, <strong>modifying this vector</strong>
     *
     * @param x the x component to lerp to
     * @param y the y component to lerp to
     * @param z the z component to lerp to
     */
    @NotNull
    public Vector lerp(float x, float y, float z, float amt) {
        return lerp(new Vector(x, y, z), amt);
    }


    /**
     * Linear interpolate from this vector (start) to given vector (end), <strong>modifying this vector</strong>
     *
     * @param endVector the end vector
     * @param amt       linear interpolation factor, in range [0, 1]
     */
    @NotNull
    public Vector lerp(@NotNull Vector endVector, float amt) {
        return lerp(endVector, amt, this);
    }

    /**
     * Linear interpolate from this vector (start) to given vector (end)
     *
     * @param endVector the end vector
     * @param amt       linear interpolation factor, in range [0, 1]
     * @param target    Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    @NotNull
    public Vector lerp(@NotNull Vector endVector, float amt, @Nullable Vector target) {
        return lerp(this, endVector, amt, target);
    }


    /**
     * Linear interpolate between two vectors
     *
     * @param startVector start vector
     * @param endVector   end vector
     * @param amt         linear interpolation factor, in range [0, 1]
     * @param target      Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    @NotNull
    public static Vector lerp(@NotNull Vector startVector, @NotNull Vector endVector, float amt, @Nullable Vector target) {
        if (target == null) {
            target = new Vector();
        }

        return target.set(RMath.lerp(startVector.x, endVector.x, amt), RMath.lerp(startVector.y, endVector.y, amt), RMath.lerp(startVector.z, endVector.z, amt));
    }


    /**
     * @return whether this vector is of 0 length
     */
    public boolean isZero() {
        return isZero(this);
    }

    /**
     * @return whether a vector is of 0 length
     */
    public static boolean isZero(@NotNull Vector v) {
        return v.x == 0 && v.y == 0 && v.z == 0;
    }


    /**
     * Calculates the angle (in radians) between two vectors.
     *
     * @param v1 first vector
     * @param v2 second vector
     * @return the angle between two vectors
     */
    public static double angleBetweenD(@NotNull Vector v1, @NotNull Vector v2) {
        if (isZero(v1) || isZero(v2))     // We get NaN if we pass in a zero vector which can cause problems, Zero seems like a reasonable angle between a (0,0,0) vector and something else
            return 0.0f;

        final double dot = dotD(v1, v2);
        final double v1mag = magD(v1);
        final double v2mag = magD(v2);
        final double amt = dot / (v1mag * v2mag);   // should be in range [-1, 1]

        if (amt >= 1) {
            return 0;
        }

        if (amt <= -1) {
            return RMath.PI;
        }

        return Math.acos(amt);
    }

    /**
     * Calculates the angle (in radians) between two vectors.
     *
     * @param v1 first vector
     * @param v2 second vector
     * @return the angle between two vectors
     */
    public static float angleBetween(@NotNull Vector v1, @NotNull Vector v2) {
        return (float) angleBetweenD(v1, v2);
    }


    @Override
    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }


    /**
     * Return a representation of this vector as a temp float array
     * <p>
     * In most cases, the contents should be copied by using the
     * {@link #get(float[])} method to copy into your own array
     *
     * @return a representation of this vector as a float array
     */
    @NotNull
    public float[] array() {
        if (array == null) {
            array = new float[3];
        }

        array[0] = x;
        array[1] = y;
        array[2] = z;
        return array;
    }


    public static boolean equals(@NotNull Vector v1, @NotNull Vector v2) {
        return v1.x == v2.x && v1.y == v2.y && v1.z == v2.z;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Vector && equals(this, (Vector) o));
    }

    public static int hash(@NotNull Vector v) {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(v.x);
        result = 31 * result + Float.floatToIntBits(v.y);
        result = 31 * result + Float.floatToIntBits(v.z);
        return result;
    }

    @Override
    public int hashCode() {
        return hash(this);
    }
}
