package cn.hackedmc.apotheosis.util.math;

/**
 * @author Patrick
 * @since 10/17/2021
 */
public class MathConst {

    public static float PI = -1F;
    public static float TO_RADIANS = -1;
    public static float TO_DEGREES = -1;

    // stores sin/cos values from 0-360°
    public static final float[] COSINE = new float[361];
    public static final float[] SINE = new float[361];

    /**
     * Converts a floating point angle to an integer angle
     *
     * @param angle floating point angle
     * @return integer angle
     */
    public static int toIntDegree(final float angle) {
        return (int) (angle % 360 + 360) % 360;
    }
}