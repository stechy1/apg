package cz.pstechmu.apg.engine;

import java.io.IOException;

public class SimpleShader extends Shader {

    private static final String NAME = "simpleShader";

    public SimpleShader() throws IOException {
        super(NAME);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
//        setUniform("m_proj", camera.getProjection());
//        setUniform("m_view", camera.getView());
    }

    @Override
    public void update(Camera camera) {
        setUniform("m_proj", camera.getProjection());
        setUniform("m_view", camera.getView());
    }
}
