package cz.pstechmu.apg.tetrahedron;

import cz.pstechmu.apg.engine.Loader;
import cz.pstechmu.apg.engine.RawModel;
import cz.pstechmu.apg.engine.Renderer;
import cz.pstechmu.apg.engine.WindowManager;
import cz.pstechmu.apg.engine.WindowManager.OnFrameListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class VertexVisualizer implements OnFrameListener {

    private final WindowManager windowManager = new WindowManager(this);
    private final Loader loader = new Loader();
    private final Renderer renderer = new Renderer();

    private RawModel rawModel;

    public static void main(String[] args) throws Exception {
        new VertexVisualizer().work(new File(args[0]));
    }

    public VertexVisualizer() {
        windowManager.init();
    }

    private double[] parseVertices(File data) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(data))) {
            final String line = reader.readLine();
            final String[] rawData = line.split(" ");
            return Arrays.stream(rawData).mapToDouble(Double::parseDouble).toArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem");
        }
    }

    private void work(File dataFile) {
        double[] vertices = parseVertices(dataFile);

        rawModel = loader.loadToVAO(vertices);

        windowManager.run();

        loader.cleanUp();
    }

    @Override
    public void onFrame() {
        renderer.prepare();
        renderer.render(rawModel);
    }
}
