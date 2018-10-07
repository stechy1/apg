package cz.pstechmu.apg.engine;


import java.io.IOException;

public class World {

//    private HashMap<String, Region> regions;
    private final Shader shader;
    private int viewDistance;

    public World() throws IOException {
        this.shader = new SimpleShader();

        viewDistance = 4;
    }

    public void update(final PerspectiveCamera camera, final float delta) {

    }

    public void render(final PerspectiveCamera camera) {
        shader.bind();
//        shader.update(camera, directionalLight);


    }



}
