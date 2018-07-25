package com.kotor4j.resourcemanager.walkmesh;

import com.kotor4j.utils.StringBuilderWithPadding;

/**
 * @author Dmitry
 */
public class Walkmesh {

    private float[] position;
    private int[] faces;
    private float[] vertices;
    private WalkmeshAABBBox[] aabbBoxes;
    private WalkType[] walktypes;

    public void setWalktypes(WalkType[] walktypes) {
        this.walktypes = walktypes;
    }

    public WalkType[] getWalktypes() {
        return walktypes;
    }

    public WalkmeshAABBBox[] getAabbBoxes() {
        return aabbBoxes;
    }

    public int[] getFaces() {
        return faces;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setAabbBoxes(WalkmeshAABBBox[] aabbBoxes) {
        this.aabbBoxes = aabbBoxes;
    }

    public void setFaces(int[] faces) {
        this.faces = faces;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public String toJson() {
        StringBuilderWithPadding sb = new StringBuilderWithPadding();
        sb.println("{");
        sb.incLevel();

        sb.println("\"position\":[");
        sb.incLevel();
        sb.println("" + position[0] + ", " + position[1] + ", " + position[2]);
        sb.decLevel();
        sb.println("],");

        sb.println("\"vertices\":[");
        sb.incLevel();
        boolean first = true;
        for (int i = 0; i < vertices.length; i += 3) {
            if (first == false) {
                sb.println(",");
            }
            first = false;

            sb.print("" + vertices[i + 0] + ", " + vertices[i + 1] + ", " + vertices[i + 2]);
        }

        sb.decLevel();
        sb.println("\n],");

        sb.println("\"faces\":[");
        sb.incLevel();
        {
            int i = 0;
            first = true;
            for (; i < faces.length;) {
                
                for (int j = 0; j < 3&& i < faces.length; j++, i += 3) {
                    if (first == false) {
                        sb.println(",");
                    }

                    first = false;
                    sb.print("" + faces[i + 0] + ", " + faces[i + 1] + ", " + faces[i + 2]);
                }
            }
        }

        sb.decLevel();
        sb.println("\n],");
        sb.println("\"walktypes\":[");
        sb.incLevel();
        for (int i = 0; i < walktypes.length; i++) {
            if (i > 0) {
                sb.println(",");
            }

            sb.print(walktypes[i].name());
        }

        sb.decLevel();
        sb.println("\n],");

        sb.println("\"aabb\":[");
        sb.incLevel();

        for (int aabbIndex = 0; aabbIndex < aabbBoxes.length; aabbIndex++) {
            if (aabbIndex > 0) {
                sb.println(",");
            }

            WalkmeshAABBBox box = aabbBoxes[aabbIndex];
            sb.println("{");
            sb.incLevel();

            sb.println("\"minVertex\":[" + box.getMinVertex()[0] + "," + box.getMinVertex()[1] + "," + box.getMinVertex()[2] + "],");
            sb.println("\"maxVertex\":[" + box.getMaxVertex()[0] + "," + box.getMaxVertex()[1] + "," + box.getMaxVertex()[2] + "],");
            sb.println("\"leftFacePartNumber\":" + box.getLeftFacePartNumber() + ",");
            sb.println("\"leftNodeArrayIndex\":" + box.getLeftNodeArrayIndex() + ",");
            sb.println("\"rightNodeArrayIndex\":" + box.getRightNodeArrayIndex() + ",");
            sb.println("\"mostSignifPlane\":" + box.getMostSignifPlane());

            sb.decLevel();
            sb.print("}");
        }

        sb.decLevel();
        sb.println("\n]");

        sb.decLevel();
        sb.print("}");
        return sb.toString();
    }
}
