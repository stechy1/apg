package cz.pstechmu.apg.tetrahedron;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.DoubleStream;

public class App {

    private static final double EDGE_SIZE = Math.sqrt(8) / Math.sqrt(3);
    private static final int DEPTH = 4;

    public static void main(String[] args) {
        final Tetrahedron tetrahedron = new Tetrahedron(EDGE_SIZE);
        System.out.println(tetrahedron.toString());
        System.out.println("-------------------------");

        Queue<Triangle> triangleStack = new ArrayDeque<>(tetrahedron.getTriangles());
        Set<Triangle> triangles = new LinkedHashSet<>();

        int counter = 0;
        while(!triangleStack.isEmpty() && counter < DEPTH) {
            Triangle triangle = triangleStack.poll();
            triangles.add(triangle);
            triangleStack.addAll(triangle.getInnerTriangles(1));
            counter++;
        }

        System.out.println("Počet trojuhelniku: " + triangleStack.size());

        System.out.println(triangles.toString());

        final double[] vertices = triangles.stream().flatMapToDouble(triangle -> DoubleStream.of(triangle.toVertices())).toArray();

        System.out.println(Arrays.toString(vertices));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./data.txt")))) {
            for (double vertex : vertices) {
                writer.write(Double.toString(vertex) + " ");
            }
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
