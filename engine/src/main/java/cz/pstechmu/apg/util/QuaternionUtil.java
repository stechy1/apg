package cz.pstechmu.apg.util;


import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class QuaternionUtil
{
    /**
     * Creates a rotation matrix from a quaternion and stores
     * the result in the dest matrix.
     *
     * @param q    The quaternion to create the matrix from.
     * @param dest The destination matrix to store the result.
     * @return The rotation matrix obtained from the quaternion
     */
    public static Matrix4f toRotationMatrix(Quaternionf q, Matrix4f dest)
    {
        if (dest == null)
            dest = new Matrix4f();
        else
            dest.identity();

        // Normalize the quaternion
        q.normalize();

        // The length of the quaternion
        float s = 2f / q.lengthSquared();

        // Convert the quaternion to matrix
        dest.m00(1 - s * (q.y * q.y + q.z * q.z));
        dest.m10(s * (q.x * q.y + q.w * q.z));
        dest.m20(s * (q.x * q.z - q.w * q.y));

        dest.m01(s * (q.x * q.y - q.w * q.z));
        dest.m11(1 - s * (q.x * q.x + q.z * q.z));
        dest.m21(s * (q.y * q.z + q.w * q.x));

        dest.m02(s * (q.x * q.z + q.w * q.y));
        dest.m12(s * (q.y * q.z - q.w * q.x));
        dest.m22(1 - s * (q.x * q.x + q.y * q.y));

        return dest;
    }

    /**
     * Returns the conjugate of a Quaternionf and stores the
     * result in another 'dest' quaternion.
     *
     * @param src  The source Quaternionf
     * @param dest The destination Quaternionf
     * @return The Conjugate of the given quaternion
     */
    public static Quaternionf conjugate(Quaternionf src, Quaternionf dest)
    {
        if (dest == null)
            dest = new Quaternionf();

        // Only negate the axis
        dest.x = -src.x;
        dest.y = -src.y;
        dest.z = -src.z;
        dest.w = +src.w;

        return dest;
    }

    /**
     * Rotates a vector by a Quaternionf and stores the result
     * in another destination vector.
     *
     * @param v    The vector to rotate
     * @param q    The quaternion with rotation
     * @param dest The destination vector
     * @return The rotated vector
     */
    public static Vector3f rotate(Vector3f v, Quaternionf q, Vector3f dest)
    {
        if (dest == null)
            dest = new Vector3f();

        // Calculate the conjugate of quaternion
        Quaternionf q1 = conjugate(q, null);
        Quaternionf qv = new Quaternionf(v.x, v.y, v.z, 1);

        // Rotate the quaternion
        q.mul(qv, qv);
        qv.mul(q1, qv);
//        Quaternionf.mul(q, qv, qv);
//        Quaternionf.mul(qv, q1, qv);

        // Extract vector from rotated quaternion
        dest.x = qv.x;
        dest.y = qv.y;
        dest.z = qv.z;

        return dest;
    }

    /**
     * Creates a Quaternionf from an Axis and an angle.
     *
     * @param axis  The axis to rotate upon
     * @param angle The angle of rotation (in degrees)
     * @param dest  The destination quaternion to store
     * @return The new rotation quaternion
     */
    public static Quaternionf createFromAxisAngle(Vector3f axis, float angle, Quaternionf dest)
    {
        if (dest == null)
            dest = new Quaternionf();

        // Normalize the axis
        axis.normalize();

        // Calculate the halfAngle
        float halfAngle = (float) Math.toRadians(angle/2);

        // Create the Quaternionf from axis-angle
        dest.x = axis.x * (float)Math.sin(halfAngle);
        dest.y = axis.y * (float)Math.sin(halfAngle);
        dest.z = axis.z * (float)Math.sin(halfAngle);

        dest.w = (float)Math.cos(halfAngle);

        // Normalize the Quaternionf
        dest.normalize();

        return dest;
    }
}