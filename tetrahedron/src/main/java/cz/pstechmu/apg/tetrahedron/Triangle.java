package cz.pstechmu.apg.tetrahedron;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.joml.Vector3d;

public class Triangle {

    private static int counter = 0;

    private final int id;
    private final Vector3d vertexA;
    private final Vector3d vertexB;
    private final Vector3d vertexC;

    public Triangle(Vector3d vertexA, Vector3d vertexB, Vector3d vertexC) {
        id = counter++;
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.vertexC = vertexC;
    }

    public Vector3d getVertexA() {
        return vertexA;
    }

    public Vector3d getVertexB() {
        return vertexB;
    }

    public Vector3d getVertexC() {
        return vertexC;
    }

    public List<Triangle> getInnerTriangles(double radius) {
//        final Vector3d deltaAB = new Vector3d(vertexA).add(vertexB).div(2);
//        final Vector3d deltaAC = new Vector3d(vertexA).add(vertexC).div(2);
//        final Vector3d deltaBC = new Vector3d(vertexB).add(vertexC).div(2);

        double angleAB = vertexA.angle(vertexB);
        double angleAC = vertexA.angle(vertexC);
        double angleBC = vertexB.angle(vertexC);

        final Vector3d newAB = new Vector3d(vertexA).add(vertexB).mul(1/Math.sin(angleAB)*Math.sin(angleAB/2));
        final Vector3d newAC = new Vector3d(vertexA).add(vertexC).mul(1/Math.sin(angleAC)*Math.sin(angleAC/2));
        final Vector3d newBC = new Vector3d(vertexB).add(vertexC).mul(1/Math.sin(angleBC)*Math.sin(angleBC/2));

//        final Vector3d newAB = new Vector3d(deltaAB).mul(radius).div(deltaAB.length());
//        final Vector3d newAC = new Vector3d(deltaAC).mul(radius).div(deltaAC.length());
//        final Vector3d newBC = new Vector3d(deltaBC).mul(radius).div(deltaBC.length());

        final Triangle inner = new Triangle(newAB, newAC, newBC);
        final Triangle A_deltaAB_deltaAC = new Triangle(vertexA, newAB, newAC);
        final Triangle B_deltaAB_deltaBC = new Triangle(vertexB, newAB, newBC);
        final Triangle C_deltaAC_deltaBC = new Triangle(vertexC, newAC, newBC);

        return Arrays.asList(inner, A_deltaAB_deltaAC, B_deltaAB_deltaBC, C_deltaAC_deltaBC);
    }

    @Override
    public String toString() {
        NumberFormat format = NumberFormat.getNumberInstance();
        return "ID: " + id +
            " {A: [" + vertexA.toString(format) + "], " +
            "B: [" + vertexB.toString(format) + "], " +
            "C: [" + vertexC.toString(format) + "]}, ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Triangle triangle = (Triangle) o;
        return Objects.equals(vertexA, triangle.vertexA) &&
            Objects.equals(vertexB, triangle.vertexB) &&
            Objects.equals(vertexC, triangle.vertexC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexA, vertexB, vertexC);
    }

    public double[] toVertices() {
        return new double[] {
            vertexA.x,
            vertexA.y,
            vertexA.z,

            vertexB.x,
            vertexB.y,
            vertexB.z,

            vertexC.x,
            vertexC.y,
            vertexC.z,
        };
    }
}
