package cz.pstechmu.apg.tetrahedron;

import cz.pstechmu.apg.base.Application;
import cz.pstechmu.apg.base.ApplicationConfiguration;
import cz.pstechmu.apg.base.ApplicationListener;
import cz.pstechmu.apg.engine.VAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("all")
public class VertexVisualizer implements ApplicationListener {

    private VAO vao;

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
        final float[] vertices = parseVertices(new File("./data.txt"));
        vao = new VAO(vertices, 3);
    }

    @Override
    public void update(float delta) {
        
    }

    @Override
    public void render() {
        vao.render();
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
