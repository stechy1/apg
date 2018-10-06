package cz.pstechmu.apg.base;

public interface ApplicationListener {

    void create();
    void update(float delta);
    void render();
    void dispose();

    void mouseMoved(double xpos, double ypos, double dx, double dy);
    void keyPressed(int keycode);
    void keyReleased(int keycode);

}
