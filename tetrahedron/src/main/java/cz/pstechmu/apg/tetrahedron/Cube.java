package cz.pstechmu.apg.tetrahedron;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3fv;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Cube {
    private static float[][] vertices;
    private static float[][] colors;

    private static final Random random = new Random();

    public static void drawCube() {

        for (int i = 0; i < vertices.length; i+=3) {

            GL11.glBegin(GL11.GL_TRIANGLES);
//            glColor3f(0.9f - i * 0.25f, 0.3f + i * 0.15f, blue);
            glColor3fv(colors[i]);
            GL11.glVertex3fv(vertices[i]);
            GL11.glVertex3fv(vertices[i+1]);
            GL11.glVertex3fv(vertices[i+2]);
            GL11.glEnd();
        }
    }

    static void loadVertices() {
        try (final BufferedReader reader = new BufferedReader(new FileReader("./data.txt"))) {
            final String line = reader.readLine();
            final String[] rawData = line.split(" ");
            vertices = new float[rawData.length / 3][3];
            colors = new float[vertices.length][3];
            int dataCounter = 0;
            int vertexCounter = 0;
            for (int i = 0; i < rawData.length / 3; i++) {
                vertices[vertexCounter][0] = Float.parseFloat(rawData[dataCounter++]);
                vertices[vertexCounter][1] = Float.parseFloat(rawData[dataCounter++]);
                vertices[vertexCounter][2] = Float.parseFloat(rawData[dataCounter++]);
                colors[vertexCounter][0] = random.nextFloat();
                colors[vertexCounter][1] = random.nextFloat();
                colors[vertexCounter][2] = random.nextFloat();
                vertexCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        loadVertices();


        long windowID = createWindow();
        float rotate = 0.005f;
        glScalef(0.5f, 0.5f, 0.5f);

        /* Zapnout jemný stínování*/
        glShadeModel(GL_SMOOTH);// Jemné stínování
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);// Černé pozadí
        glClearDepth(1.0f);// Nastavení hloubkového bufferu
        glEnable(GL_DEPTH_TEST);// Zapne hloubkové testování
        glDepthFunc(GL_LEQUAL);// Typ hloubkového testování
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);// Nejlepší perspektivní korekce

        while (!glfwWindowShouldClose(windowID)) {

            glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            drawCube();

            /* !Rotace okolo osy X*/
            GL11.glRotatef(rotate, 1, 1, 1);

            glfwSwapBuffers(windowID);
            /* Zpracování eventů GLFW */
            glfwPollEvents();
        }
    }

    /**
     * Vytvoření okna
     *
     * @return
     */
    private static long createWindow() {
        long windowID;
        glfwInit();

        windowID = glfwCreateWindow(800, 600, "ZPG", MemoryUtil.NULL, MemoryUtil.NULL);

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(0);
        glfwShowWindow(windowID);

        GL.createCapabilities();

        return windowID;
    }
}