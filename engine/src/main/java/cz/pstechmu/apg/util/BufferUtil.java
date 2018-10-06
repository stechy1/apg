package cz.pstechmu.apg.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class BufferUtil {

    /**
     * Stores the specified data into a FloatBuffer
     * @param data The array of float data
     * @return The generated FloatBuffer
     */
    public static FloatBuffer toBuffer(float[] data) {
        // Create an empty FloatBuffer with the correct size
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

        // Store all the data in the buffer
        for(int i = 0; i < data.length; i++) {
            buffer.put(data[i]);
        }

        // Prepares the buffer for get() operations
        buffer.flip();

        return buffer;
    }

    /**
     * Stores the specified data into a IntBuffer
     * @param data The array of integer data
     * @return The generated IntBuffer
     */
    public static IntBuffer toBuffer(int[] data) {
        // Create an empty FloatBuffer with the correct size
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);

        // Store all the data in the buffer
        for(int i = 0; i < data.length; i++) {
            buffer.put(data[i]);
        }

        // Prepares the buffer for get() operations
        buffer.flip();

        return buffer;
    }

}
