package cz.pstechmu.apg.tetrahedron;

import java.util.Stack;

public class App {

    private static final int EDGE_SIZE = 1;
    private static final int DEPTH = 4;

    public static void main(String[] args) {
        final Tetrahedron tetrahedron = new Tetrahedron(EDGE_SIZE);
        System.out.println(tetrahedron.toString());
        System.out.println("-------------------------");

        Stack<Triangle> triangleStack = new Stack<>();
        tetrahedron.getTriangles().forEach(triangleStack::push);

        int counter = 0;
        while(!triangleStack.empty() && counter < DEPTH) {
            Triangle triangle = triangleStack.pop();
            System.out.println(triangle.toString());
            triangle.getInnerTriangles(1.65).forEach(triangleStack::push);
            counter++;
        }

        System.out.println("Počet trojuhelniku: " + triangleStack.size());
    }

}
