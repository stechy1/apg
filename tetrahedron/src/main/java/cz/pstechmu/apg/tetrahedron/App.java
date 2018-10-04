package cz.pstechmu.apg.tetrahedron;

import org.joml.Vector3d;

public class App {

    private static final int EDGE_SIZE = 1;

    public static void main(String[] args) {
        int AsB_m, AsC_m, AsD_m, BsC_m, BsD_m, CsD_m;

        AsB_m = AsC_m = AsD_m = BsC_m = BsD_m = CsD_m = EDGE_SIZE;
        int AsB_m2 = AsB_m * AsB_m; int AsC_m2 = AsC_m * AsC_m;
        int AsD_m2 = AsD_m * AsD_m; int BsC_m2 = BsC_m * BsC_m;
        int BsD_m2 = BsD_m * BsD_m; int CsD_m2 = CsD_m * CsD_m;

        double qx = AsB_m;
        double rx = (AsB_m2 + AsC_m2 - BsC_m2) / (2.0 * AsB_m);
        double ry = Math.sqrt (AsC_m2 - rx * rx);
        double sx = (AsB_m2 + AsD_m2 - BsD_m2) / (2.0 * AsB_m);
        double sy = (BsD_m2 - (sx - qx) * (sx - qx) - CsD_m2 + (sx - rx) * (sx - rx) + ry * ry) / (2 * ry);
        double sz = Math.sqrt (AsD_m2 - sx * sx - sy * sy);

        Vector3d A = new Vector3d(0.0, 0.0, 0.0);
        Vector3d B = new Vector3d (qx,  0.0, 0.0);
        Vector3d C = new Vector3d (rx,  ry,  0.0);
        Vector3d D = new Vector3d (sx,  sy,  sz );

        System.out.println("A: " + A.toString());
        System.out.println("B: " + B.toString());
        System.out.println("C: " + C.toString());
        System.out.println("D: " + D.toString());

//        SharedLibraryLoader.load();
    }

}
