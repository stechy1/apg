package cz.pstechmu.apg.tetrahedron;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import org.joml.Vector3d;

public class Triangle {

    private final Vector3d vertexA;
    private final Vector3d vertexB;
    private final Vector3d vertexC;

    public Triangle(Vector3d vertexA, Vector3d vertexB, Vector3d vertexC) {
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
        final Vector3d deltaAB = vertexA.add(vertexB).div(2);
        final Vector3d deltaAC = vertexA.add(vertexC).div(2);
        final Vector3d deltaBC = vertexB.add(vertexC).div(2);

        final Vector3d newAB = deltaAB.mul(radius).div(deltaAB.length());
        final Vector3d newAC = deltaAC.mul(radius).div(deltaAC.length());
        final Vector3d newBC = deltaBC.mul(radius).div(deltaBC.length());

        final Triangle inner = new Triangle(newAB, newAC, newBC);
        final Triangle A_deltaAB_deltaAC = new Triangle(vertexA, newAB, newAC);
        final Triangle B_deltaAB_deltaBC = new Triangle(vertexB, newAB, newBC);
        final Triangle C_deltaAC_deltaBC = new Triangle(vertexC, newAC, newBC);

        return Arrays.asList(inner, A_deltaAB_deltaAC, B_deltaAB_deltaBC, C_deltaAC_deltaBC);
    }

    @Override
    public String toString() {
        NumberFormat format = NumberFormat.getNumberInstance();
        return
            "\t{A: [" + vertexA.toString(format) + "], \n" +
            "\tB: [" + vertexB.toString(format) + "], \n" +
            "\tC: [" + vertexC.toString(format) + "]}, \n";
    }
}
