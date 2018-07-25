package com.kotor4j.resourcemanager.walkmesh;

import com.kotor4j.io.NwnByteArrayInputStream;
import com.kotor4j.resourcemanager.BaseReader;
import java.io.IOException;

/**
 * @author Dmitry
 */
public class FileReaderWalkmesh extends BaseReader {

    public Walkmesh loadFile(NwnByteArrayInputStream binaryReader, String fileName) throws IOException {
        float[] unknown = new float[13];
        float[] position = new float[3];
        binaryReader.setPosition(8);
        for (int i = 0; i <= 12; i++) {
            unknown[i] = binaryReader.readFloat();
        }

        position[0] = binaryReader.readFloat();
        position[1] = binaryReader.readFloat();
        position[2] = binaryReader.readFloat();
        int verticesCount = binaryReader.readInt();
        int verticesOffset = binaryReader.readInt();
        int facesCount = binaryReader.readInt();
        int facesOffset = binaryReader.readInt();
        int walkTypeOffset = binaryReader.readInt();
        int unkXYZOffset = binaryReader.readInt();
        int unk3Offset = binaryReader.readInt();
        int AABBCount = binaryReader.readInt();
        int AABBOffset = binaryReader.readInt();
        int unkInt32L = binaryReader.readInt();
        int unk5Count = binaryReader.readInt();
        int unk5Offset = binaryReader.readInt();
        int unk6Count = binaryReader.readInt();
        int unk6Offset = binaryReader.readInt();
        int unk7Count = binaryReader.readInt();
        int unk7Offset = binaryReader.readInt();

        WalkType[] walkTypes = new WalkType[facesCount];
        float[] vertices = new float[verticesCount * 3];

        binaryReader.setPosition(verticesOffset);
        for (int i = 0; i < verticesCount; i++) {
            int offset = i * 3;
            vertices[offset + 0] = binaryReader.readFloat();
            vertices[offset + 1] = binaryReader.readFloat();
            vertices[offset + 2] = binaryReader.readFloat();
        }

        int[] faces = new int[facesCount * 3];
        binaryReader.setPosition(facesOffset);
        for (int i = 0; i < facesCount; i++) {
            int offset = i * 3;
            faces[offset + 0] = binaryReader.readInt();
            faces[offset + 1] = binaryReader.readInt();
            faces[offset + 2] = binaryReader.readInt();
        }

        binaryReader.setPosition(walkTypeOffset);
        for (int i = 0; i < facesCount; i++) {
            int walkTypeCode = binaryReader.readInt();
            WalkType wt = WalkType.getWalkType(walkTypeCode);
            if (wt == null) {
                throw new IllegalArgumentException("Unknown walktype ["+walkTypeCode+"]");
            }

            walkTypes[i] = wt;

        }

        binaryReader.setPosition(AABBOffset);
        WalkmeshAABBBox[] walkmeshAaBbBoxes = new WalkmeshAABBBox[AABBCount];
        for (int i = 0; i < AABBCount; i++) {
            float[] minVertex = new float[3];
            float[] maxVertex = new float[3];
            minVertex[0] = binaryReader.readFloat();
            minVertex[1] = binaryReader.readFloat();
            minVertex[2] = binaryReader.readFloat();
            maxVertex[0] = binaryReader.readFloat();
            maxVertex[1] = binaryReader.readFloat();
            maxVertex[2] = binaryReader.readFloat();

            int leafFacePartNumber = binaryReader.readInt();
            int always4 = binaryReader.readInt();
            int mostSignifPlane = binaryReader.readInt();
            int leftNodeArrayIndex = binaryReader.readInt();
            int rightNodeArrayIndex = binaryReader.readInt();

            walkmeshAaBbBoxes[i] = new WalkmeshAABBBox(minVertex, maxVertex, leafFacePartNumber, leftNodeArrayIndex, rightNodeArrayIndex, always4, mostSignifPlane);
        }

        Walkmesh walkmesh = new Walkmesh();
        walkmesh.setAabbBoxes(walkmeshAaBbBoxes);
        walkmesh.setFaces(faces);
        walkmesh.setPosition(position);
        walkmesh.setVertices(vertices);
        walkmesh.setWalktypes(walkTypes);
        return walkmesh;
    }
}
