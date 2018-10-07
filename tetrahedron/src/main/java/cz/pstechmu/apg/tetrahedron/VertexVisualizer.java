package cz.pstechmu.apg.tetrahedron;

import cz.pstechmu.apg.base.Application;
import cz.pstechmu.apg.base.ApplicationConfiguration;
import cz.pstechmu.apg.base.ApplicationListener;
import cz.pstechmu.apg.base.Input;
import cz.pstechmu.apg.engine.Camera;
import cz.pstechmu.apg.engine.PerspectiveCamera;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("all")
public class VertexVisualizer implements ApplicationListener {

    private Camera camera;
    private float speed;
    private float sensitivity;
//    private Shader simpleShader;
//    private VAO vao;

    private VertexVisualizer() {
        ApplicationConfiguration config = new ApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.title = "Vertex visualizer";
        config.vSyncEnabled = true;
        config.show = true;

        Application app = new Application(this, config);
        try {
            app.run();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private float[] parseVertices(File file) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final String line = reader.readLine();
            final String[] rawData = line.split(" ");
            final float[] data = new float[rawData.length];
            for (int i = 0; i < rawData.length; i++) {
                data[i] = Float.parseFloat(rawData[i]);
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem");
        }
    }

    public static void main(String[] args) {
        new VertexVisualizer();
    }

    @Override
    public void create() {
//        final float[] vertices = parseVertices(new File("./data.txt"));
//        vao = new VAO(vertices, 3);
//        try {
//            simpleShader = new SimpleShader();
//        } catch (IOException e) {
//            System.out.println("Shader not found.");
//            System.exit(1);
//        }

        camera = new PerspectiveCamera(65.0f, 1280.0f/720.0f, 0.01f, 1000f);
    }

    @Override
    public void update(float delta) {
        final float speedThisFrame = speed * delta;

        // Move forward
        if (Input.isKeyPressed(GLFW.GLFW_KEY_W))
            camera.move(Camera.Direction.FORWARD, speedThisFrame);
        // Move backward
        if (Input.isKeyPressed(GLFW.GLFW_KEY_S))
            camera.move(Camera.Direction.BACKWARD, speedThisFrame);
        // Move right
        if (Input.isKeyPressed(GLFW.GLFW_KEY_D))
            camera.move(Camera.Direction.RIGHT, speedThisFrame);
        // Move left
        if (Input.isKeyPressed(GLFW.GLFW_KEY_A))
            camera.move(Camera.Direction.LEFT, speedThisFrame);
        // Move up
        if (Input.isKeyPressed(GLFW.GLFW_KEY_SPACE))
            camera.move(Camera.AXIS_Y, speedThisFrame);
        // Move down
        if (Input.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            camera.move(Camera.AXIS_Y, -speedThisFrame);

        camera.update();
    }

    @Override
    public void render() {

//        simpleShader.bind();
//        vao.render();
//        simpleShader.unbind();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void mouseMoved(double xpos, double ypos, double dx, double dy) {

    }

    @Override
    public void keyPressed(int keycode) {

    }

    @Override
    public void keyReleased(int keycode) {

    }
}
