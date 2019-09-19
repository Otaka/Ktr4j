package com.jmonkey.simplegeomloader;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Bone;
import com.jme3.animation.BoneTrack;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.animation.Track;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import com.jmonkey.simplegeomloader.animation.NodeTrack;
import com.jmonkey.simplegeomloader.data.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dmitry
 */
public class SimpleGeometryModelConverter {

    private final AssetManager assetManager;
    private final static String ROOT_NODE = "ROOT_NODE_SGL_OBJECT";
    private final List<String> collectedBoneNames = new ArrayList<>();
    private boolean hasSkin = false;
    private Map<String, SglNode> nodeName2NodeObject;
    private final Map<String, Node> nodes = new HashMap<>();
    private Node[] childSpatialFlatArray;

    public SimpleGeometryModelConverter(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Node covert(SglModel sglModel) {
        Map<String, List<SglNode>> parentToNodesList = fillParentToNodesListMap(sglModel);
        nodeName2NodeObject = fillNodeName2NodeObjectMap(sglModel);
        List<SglNode> roots = parentToNodesList.get(ROOT_NODE.toLowerCase());
        if (roots == null || roots.isEmpty()) {
            throw new IllegalArgumentException("Model [" + sglModel.getFilename() + "] does not have any root object. But should have only one root");
        }

        if (roots.size() > 1) {
            throw new IllegalArgumentException("Model [" + sglModel.getFilename() + "] has " + roots.size() + " roots, but should have only one root");
        }

        hasSkin = sglModel.hasSkin();
        if (hasSkin) {
            collectBoneNamesFromWeightInformation(sglModel, collectedBoneNames);
        }

        Node rootNode = loadGeometry(sglModel, parentToNodesList);
        childSpatialFlatArray = createChildSpatialFlatArray(rootNode);
        if (!sglModel.getAnimations().isEmpty()) {
            loadAnimation(rootNode, sglModel);
        }

        return rootNode;
    }

    private Node loadGeometry(SglModel model, Map<String, List<SglNode>> parentToNodesList) {
        Node rootNode = null;
        //first we create a nodes and put them into the map
        for (SglNode child : model.getNodes()) {
            Node node;
            if (child.hasGeometry()) {
                node = createMeshNode((SglGeometry) child);
            } else {
                node = createDummyNode(child);
            }
            nodes.put(child.getName().toLowerCase(), node);
        }
        //in this loop we attach nodes to own parents(we do this in separated loop, because nodes may come in wrong order - child first, parent second)
        //may be some presort will help
        for(SglNode child:model.getNodes()){
            Node node=nodes.get(child.getName().toLowerCase());
            String parentString = child.getParent();
            if (parentString != null) {
                parentString = parentString.toLowerCase();
            }
            Node parentNode = nodes.get(parentString);
            if (parentNode == null) {
                rootNode = node;
            }
            if (parentNode != null) {
                parentNode.attachChild(node);
            }
        }

        return rootNode;
    }

    private boolean isZeroPositionArray(float[] array) {
        for (int i = 0; i < array.length; i++) {
            if (Math.abs(array[i]) >= 0.000001f) {
                return false;
            }
        }
        return true;
    }

    private void bakeTransformationIntoVertexBuffer(SglGeometry node) {
        FloatBuffer originalBuffer = node.getVertices();
        originalBuffer.rewind();
        FloatBuffer newVertexBuffer = BufferUtils.createFloatBuffer(originalBuffer.limit());
        Matrix4f bakePositionMatrix = new Matrix4f();
        bakePositionMatrix.loadIdentity();
        bakePositionMatrix.setTranslation(node.getPosition());
        bakePositionMatrix.setRotationQuaternion(node.getOrientation());

        node.setPosition(new float[]{0, 0, 0});
        node.getOrientation().loadIdentity();
        Vector3f vertexPosition = new Vector3f();
        for (int i = 0; i < originalBuffer.limit(); i += 3) {
            vertexPosition.x = originalBuffer.get();
            vertexPosition.y = originalBuffer.get();
            vertexPosition.z = originalBuffer.get();
            bakePositionMatrix.mult(vertexPosition, vertexPosition);
            newVertexBuffer.put(vertexPosition.x);
            newVertexBuffer.put(vertexPosition.y);
            newVertexBuffer.put(vertexPosition.z);
        }
        node.setVertices(newVertexBuffer);
    }

    private Node createMeshNode(SglGeometry geometryNode) {
        Mesh mesh = new Mesh();
        if (geometryNode.isSkin() && (!isZeroPositionArray(geometryNode.getPosition()) || !geometryNode.getOrientation().isIdentity())) {
            bakeTransformationIntoVertexBuffer(geometryNode);
            mesh.setBuffer(VertexBuffer.Type.Position, 3, geometryNode.getVertices());
        } else {
            mesh.setBuffer(VertexBuffer.Type.Position, 3, geometryNode.getVertices());
        }
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, geometryNode.getNormals());
        if (geometryNode.getTextureCoords() != null) {
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, geometryNode.getTextureCoords());
        }

        mesh.setBuffer(VertexBuffer.Type.Index, 3, geometryNode.getIndices());

        mesh.setStatic();
        mesh.updateBound();
        mesh.updateCounts();

        Geometry geometry = new Geometry("GeometryNode" + Math.random(), mesh);
        String texturePath = geometryNode.getTexture();
        if (texturePath != null && !texturePath.equalsIgnoreCase("NULL")) {
            Texture texture = null;
            try {
                texture = assetManager.loadTexture(texturePath + ".png");
            } catch (Exception ex) {
                try {
                    texture = assetManager.loadTexture(texturePath + ".tga");
                } catch (Exception ex2) {
                    System.out.println("Cannot find texture " + texturePath + ".png or " + texturePath + ".tga");
                    Material m1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    m1.setColor("Color", ColorRGBA.randomColor());
                    geometry.setMaterial(m1);
                }
            }

            if (texture != null) {
                texture.setWrap(Texture.WrapMode.Repeat);
                Material geometryMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

                //geometryMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                if (geometryNode.isWireframe()) {
                    geometryMaterial.getAdditionalRenderState().setWireframe(true);
                }
                geometryMaterial.setTexture("ColorMap", texture);
                geometry.setMaterial(geometryMaterial);
            }
        } else {
            Material m1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            m1.setColor("Color", ColorRGBA.randomColor());
            geometry.setMaterial(m1);
        }

        Node node = new Node();
        node.setName(geometryNode.getName());
        if (geometryNode.getPosition() != null) {
            float[] position = geometryNode.getPosition();
            node.setLocalTranslation(position[0], position[1], position[2]);
        }

        if (geometryNode.getOrientation() != null) {
            node.setLocalRotation(geometryNode.getOrientation());
        }

        if (geometryNode.isVisible() && !geometryNode.getName().endsWith("_g")) {
            node.attachChild(geometry);
            if (hasSkin) {
                node.setUserData("bone", false);
            }
        } else {
            if (hasSkin) {
                node.setUserData("bone", true);
            }
        }

        return node;
    }

    private Node createDummyNode(SglNode dummyNode) {
        Node node = new Node();
        node.setName(dummyNode.getName());
        if (hasSkin) {
            node.setUserData("bone", true);
        }

        if (dummyNode.getPosition() != null) {
            float[] position = dummyNode.getPosition();
            node.setLocalTranslation(position[0], position[1], position[2]);
        }

        if (dummyNode.getPosition() != null) {
            node.setLocalRotation(dummyNode.getOrientation());
        }
        return node;
    }

    private void loadAnimation(Node root, SglModel model) {
        List<Animation> animations = new ArrayList<>();
        for (SglAnimation animation : model.getAnimations()) {
            Animation jmonkeyAnimation = processAnimation(root, model, animation);
            animations.add(jmonkeyAnimation);
        }

        if (hasSkin) {
            Bone[] bones = bonesNameToBoneArrays();
            postProcessSkinAnimation(bones, animations, root, model);
            Skeleton skeleton = new Skeleton(bones);
            SkeletonControl skeletonControl = new SkeletonControl(skeleton);
            skeletonControl.setHardwareSkinningPreferred(true);
            AnimControl animControl = new AnimControl(skeleton);
            for (Animation animation : animations) {
                animControl.addAnim(animation);
            }

            if (root != null) {
                root.addControl(animControl);
                root.addControl(skeletonControl);
            }
            processObjectsAttachedToBones(skeletonControl, root, bones);
            skeleton.setBindingPose();
        } else {
            root.setUserData("childrenArray", childSpatialFlatArray);
            AnimControl animControl = new AnimControl();
            for (Animation animation : animations) {
                animControl.addAnim(animation);
            }

            if (root != null) {
                root.addControl(animControl);
            }
        }
    }

    private void postProcessSkinAnimation(Bone[] bones, List<Animation> animations, Node rootNode, SglModel model) {
        fillWeightBuffers(rootNode, model);

        List<Animation> newAnimationList = new ArrayList<>();
        for (Animation animation : animations) {

            Animation na = new Animation(animation.getName(), animation.getLength());
            for (Track track : animation.getTracks()) {
                NodeTrack nodeTrack = (NodeTrack) track;
                String nodeName = nodeTrack.getNodeName();

                int boneIndex = searchBoneIndexByName(bones, nodeName);
                if (boneIndex == -1) {
                    continue;
                }
                Quaternion[] rotations = nodeTrack.getOriginalQuaternions();
                Vector3f[] translations = nodeTrack.getOriginalTranslations();
                adaptAnimationRotationToBindPoseAnimation(bones[boneIndex], rotations);
                adaptAnimationTranslationToBindPoseAnimation(bones[boneIndex], translations);

                BoneTrack boneTrack = new BoneTrack(boneIndex, nodeTrack.getKeyFrameTimes(), translations, rotations);
                na.addTrack(boneTrack);
            }

            newAnimationList.add(na);
        }

        animations.clear();
        for (Animation a : newAnimationList) {
            animations.add(a);
        }
    }

    private void fillWeightBuffers(Node rootNode, SglModel model) {
        Map<String, Integer> boneNameToIndexMap = createBoneNameToIndexMap();
        for (SglNode node : model.getNodes()) {
            if (node.hasGeometry()) {
                SglGeometry geometry = (SglGeometry) node;
                if (geometry.isSkin()) {
                    List<SglSkinWeightElement[]> weightElementsArray = geometry.getVertexWeights();
                    Mesh mesh = getMeshFromNodeName(rootNode, geometry.getName());
                    mesh.setMaxNumWeights(4);
                    VertexBuffer weightsHW = new VertexBuffer(VertexBuffer.Type.HWBoneWeight);
                    VertexBuffer indicesHW = new VertexBuffer(VertexBuffer.Type.HWBoneIndex);
                    indicesHW.setUsage(VertexBuffer.Usage.CpuOnly);
                    weightsHW.setUsage(VertexBuffer.Usage.CpuOnly);
                    mesh.setBuffer(weightsHW);
                    mesh.setBuffer(indicesHW);

                    // Setup bone weight buffer
                    FloatBuffer weights = FloatBuffer.allocate(mesh.getVertexCount() * 4);
                    VertexBuffer weightsBuf = new VertexBuffer(VertexBuffer.Type.BoneWeight);
                    weightsBuf.setupData(VertexBuffer.Usage.CpuOnly, 4, VertexBuffer.Format.Float, weights);
                    mesh.setBuffer(weightsBuf);

                    // Setup bone index buffer
                    ByteBuffer indices = ByteBuffer.allocate(mesh.getVertexCount() * 4);
                    VertexBuffer indicesBuf = new VertexBuffer(VertexBuffer.Type.BoneIndex);
                    indicesBuf.setupData(VertexBuffer.Usage.CpuOnly, 4, VertexBuffer.Format.UnsignedByte, indices);
                    mesh.setBuffer(indicesBuf);

                    int index = -1;
                    for (SglSkinWeightElement[] weightElements : weightElementsArray) {
                        index++;
                        int weightIndex = 0;
                        for (SglSkinWeightElement element : weightElements) {
                            String boneName = element.getBoneName();
                            int boneId = boneNameToIndexMap.get(boneName.toLowerCase());
                            if (boneId == -1) {
                                System.out.println("");
                            }
                            float weight = element.getWeight();

                            indices.array()[(index * 4) + weightIndex] = (byte) boneId;
                            weights.array()[(index * 4) + weightIndex] = weight;
                            weightIndex++;
                        }

                        for (; weightIndex < 4; weightIndex++) {
                            indices.array()[(index * 4) + weightIndex] = 0;
                            weights.array()[(index * 4) + weightIndex] = 0;
                        }
                    }

                    mesh.prepareForAnim(true);
                    mesh.generateBindPose(true);
                }
            }
        }
    }

    private Mesh getMeshFromNodeName(Node node, String childName) {
        Node child = (Node) node.getChild(childName);
        for (Spatial sp : child.getChildren()) {
            if (sp instanceof Geometry) {
                return ((Geometry) sp).getMesh();
            }
        }

        throw new IllegalArgumentException("Cannot find geometry in child [" + childName + "]");
    }

    private int searchBoneIndexByName(Bone[] bones, String name) {
        for (int i = 0; i < bones.length; i++) {
            if (bones[i].getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    private Map<String, Integer> createBoneNameToIndexMap() {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < collectedBoneNames.size(); i++) {
            result.put(collectedBoneNames.get(i).toLowerCase(), i);
        }

        return result;
    }

    private void adaptAnimationRotationToBindPoseAnimation(Bone bone, Quaternion[] quaternions) {
        Quaternion invertedBind = bone.getBindRotation().clone().inverseLocal();
        for (int i = 0; i < quaternions.length; i++) {
            quaternions[i] = invertedBind.mult(quaternions[i]);
        }
    }

    private Vector3f[] adaptAnimationTranslationToBindPoseAnimation(Bone bone, Vector3f[] translations) {
        Vector3f invertedPosition = bone.getBindPosition().mult(-1);
        for (int i = 0; i < translations.length; i++) {
            translations[i] = translations[i].add(invertedPosition);
        }

        return translations;
    }

    private void processObjectsAttachedToBones(SkeletonControl skeletonControl, Node node, Bone[] bones) {
        if (node.getUserData("bone") == Boolean.FALSE) {
            Node parentNode = node.getParent();
            if (parentNode != null && parentNode.getUserData("bone") == Boolean.TRUE) {
                String boneName = parentNode.getName();
                Bone bone = searchBoneByName(bones, boneName);
                if (bone == null) {
                    throw new IllegalArgumentException("Cannot find bone with name [" + boneName + "]");
                }
                System.out.println("Create attachment node for bone [" + boneName + "]");
                Node attachmentNode = skeletonControl.getAttachmentsNode(boneName);
                parentNode.detachChild(node);
                attachmentNode.attachChild(node);
            }
        }
        for (Spatial child : node.getChildren()) {
            if (child instanceof Node) {
                processObjectsAttachedToBones(skeletonControl, (Node) child, bones);
            }
        }
    }

    private Bone searchBoneByName(Bone[] bones, String name) {
        for (int i = 0; i < bones.length; i++) {
            if (bones[i].getName().equalsIgnoreCase(name)) {
                return bones[i];
            }
        }
        //throw new IllegalArgumentException("Cannot find bone with name [" + name + "]");
        return null;
    }

    private Bone[] bonesNameToBoneArrays() {
        Map<String, Bone> bonesMap = new HashMap<>();
        Bone[] bones = new Bone[collectedBoneNames.size()];
        for (int i = 0; i < collectedBoneNames.size(); i++) {
            String nodeName = collectedBoneNames.get(i);
            Bone bone = new Bone(nodeName);
            //bone.setUserControl(true);

            bones[i] = bone;
            bonesMap.put(nodeName.toLowerCase(), bone);
        }

        for (int i = 0; i < collectedBoneNames.size(); i++) {
            String nodeName = collectedBoneNames.get(i);
            String nodeNameLower = nodeName.toLowerCase();
            Node boneNode = nodes.get(nodeNameLower);
            Bone bone = bonesMap.get(nodeNameLower);
            bone.setBindTransforms(boneNode.getLocalTranslation(), boneNode.getLocalRotation(), boneNode.getLocalScale());
            for (Spatial child : boneNode.getChildren()) {
                Bone childBone = bonesMap.get(child.getName().toLowerCase());
                if (childBone != null) {
                    bone.addChild(childBone);
                }
            }
        }

        return bones;
    }

    private Animation processAnimation(Node rootNode, SglModel model, SglAnimation animation) {
        List<NodeTrack> nodeTracks = new ArrayList<>();
        for (SglAnimationTrack track : animation.getAnimationTracks()) {
            
            int nodeIndex = getNodeIndexByName(track.getNodeName());
            Node spatial = nodes.get(track.getNodeName().toLowerCase());
            List<AnimationData> animationDataList = new ArrayList<>();
            for (SglAnimationPosition position : track.getPositionAnimation()) {
                AnimationData animationData = findAnimData(animationDataList, position.getTime());
                float[] pos = position.getPositions();
                animationData.position = new Vector3f(pos[0], pos[1], pos[2]);
            }
            for (SglAnimationRotation rotation : track.getRotationAnimation()) {
                AnimationData animationData = findAnimData(animationDataList, rotation.getTime());
                animationData.rotation = rotation.getRotation();
            }
            Collections.sort(animationDataList, (AnimationData t1, AnimationData t2) -> {
                if (t1.time < t2.time) {
                    return -1;
                } else if (t1.time > t2.time) {
                    return 1;
                } else {
                    return 0;
                }
            });

            if (animationDataList.isEmpty()) {
                continue;
            }

            float[] times = new float[animationDataList.size()];
            Vector3f[] positions = new Vector3f[animationDataList.size()];
            Quaternion[] rotations = new Quaternion[animationDataList.size()];

            for (int i = 0; i < animationDataList.size(); i++) {
                AnimationData animationData = animationDataList.get(i);
                times[i] = animationData.time;
                positions[i] = animationData.position;
                rotations[i] = animationData.rotation;
            }

            interpolateRotations(times, rotations, spatial);
            interpolatePositions(times, positions, spatial);
            Vector3f[] scales = new Vector3f[animationDataList.size()];
            Arrays.fill(scales, Vector3f.UNIT_XYZ);
            NodeTrack nodeTrack = new NodeTrack(nodeIndex, track.getNodeName(), times, positions, rotations, scales);
            nodeTracks.add(nodeTrack);
        }
        Animation jmonkeyAnimation = new Animation(animation.getName(), animation.getAnimationLength());
        jmonkeyAnimation.setTracks(nodeTracks.toArray(new Track[nodeTracks.size()]));
        return jmonkeyAnimation;
    }

    private void interpolatePositions(float[] times, Vector3f[] positions, Spatial spatial) {
        if (!hasNulls(positions)) {
            return;
        }

        boolean foundNotNull = false;
        for (int i = 0; i < times.length; i++) {
            if (positions[i] != null) {
                foundNotNull = true;
                break;
            } else {
                positions[i] = spatial.getLocalTranslation();
            }
        }

        if (foundNotNull == false) {
            return;
        }

        int lastNotNullIndex = getLastNotNullIndex(positions);
        Vector3f lastPosition = positions[lastNotNullIndex];
        for (int i = lastNotNullIndex; i < positions.length; i++) {
            if (positions[i] == null) {
                positions[i] = lastPosition;
            }
        }

        while (fillPositionsGap(times, positions)) {

        }
    }

    private boolean fillPositionsGap(float[] times, Vector3f[] positions) {
        int startIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] == null) {
                startIndex = i - 1;
                break;
            }
        }

        if (startIndex == -1) {
            return false;//no gap
        }

        for (int i = startIndex + 1; i < positions.length; i++) {
            if (positions[i] != null) {
                endIndex = i;
                break;
            }
        }

        float startTime = times[startIndex];
        float endTime = times[endIndex];
        float timeInterval = endTime - startTime;
        Vector3f startPosition = positions[startIndex];
        Vector3f endPosition = positions[endIndex];
        for (int i = startIndex; i < endIndex; i++) {
            float currentTime = times[i];
            float fract = (currentTime - startTime) / timeInterval;
            Vector3f position = new Vector3f();
            position.interpolateLocal(startPosition, endPosition, fract);
            positions[i] = position;
        }

        return true;
    }

    private void interpolateRotations(float[] times, Quaternion[] rotations, Spatial spatial) {
        if (!hasNulls(rotations)) {
            return;
        }

        boolean foundNotNull = false;
        for (int i = 0; i < times.length; i++) {
            if (rotations[i] != null) {
                foundNotNull = true;
                break;
            } else {
                rotations[i] = spatial.getLocalRotation();
            }
        }

        if (foundNotNull == false) {
            return;
        }

        int lastNotNullIndex = getLastNotNullIndex(rotations);
        Quaternion lastRotation = rotations[lastNotNullIndex];
        for (int i = lastNotNullIndex; i < rotations.length; i++) {
            if (rotations[i] == null) {
                rotations[i] = lastRotation;
            }
        }

        while (fillRotationGap(times, rotations)) {

        }

    }

    private int getLastNotNullIndex(Object[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] != null) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasNulls(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                return true;
            }
        }

        return false;
    }

    private boolean fillRotationGap(float[] times, Quaternion[] rotations) {
        int startIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < rotations.length; i++) {
            if (rotations[i] == null) {
                startIndex = i - 1;
                break;
            }
        }

        if (startIndex == -1) {
            return false;//no gap
        }

        for (int i = startIndex + 1; i < rotations.length; i++) {
            if (rotations[i] != null) {
                endIndex = i;
                break;
            }
        }

        float startTime = times[startIndex];
        float endTime = times[endIndex];
        float timeInterval = endTime - startTime;
        Quaternion startQuaternion = rotations[startIndex];
        Quaternion endQuaternion = rotations[endIndex];
        for (int i = startIndex; i < endIndex; i++) {
            float currentTime = times[i];
            float fract = (currentTime - startTime) / timeInterval;
            Quaternion q = new Quaternion();
            q.slerp(startQuaternion, endQuaternion, fract);
            rotations[i] = q;
        }

        return true;
    }

    private AnimationData findAnimData(List<AnimationData> animationDataList, float time) {
        for (int i = 0; i < animationDataList.size(); i++) {
            AnimationData ad = animationDataList.get(i);
            if (Math.abs(ad.time - time) <= 0.0000001f) {
                return ad;
            }
        }

        AnimationData animationData = new AnimationData();
        animationData.time = time;
        animationDataList.add(animationData);
        return animationData;
    }

    private int getNodeIndexByName(String nodeName) {
        for (int i = 0; i < childSpatialFlatArray.length; i++) {
            Node child = childSpatialFlatArray[i];
            if (child.getName().equalsIgnoreCase(nodeName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot find node with name [" + nodeName + "]");
    }

    private static Node[] createChildSpatialFlatArray(Node rootNode) {
        List<Node> children = new ArrayList<>();
        fillChildren(rootNode, children);
        return children.toArray(new Node[children.size()]);
    }

    private static void fillChildren(Node node, List<Node> children) {
        children.add(node);
        for (Spatial spatial : node.getChildren()) {
            if (spatial instanceof Node) {
                fillChildren((Node) spatial, children);
            }
        }
    }

    private void collectBoneNamesFromWeightInformation(SglModel model, List<String> collectedBoneNames) {
        for (SglNode node : model.getNodes()) {
            collectedBoneNames.add(node.getName());
        }
        /*Set<String> isProcessedBoneSet = new HashSet<>();
        for (SglNode node : model.getNodes()) {
            if (node.hasGeometry()) {
                SglGeometry geometry = (SglGeometry) node;
                if (geometry.isSkin()) {
                    collectBoneNamesFromSqlGeometry(geometry, collectedBoneNames, isProcessedBoneSet);
                }
            }
        }

        //now let's go up in the bones hierarchy and register any found element as bone, until root
        String[] collectedBonesArray = collectedBoneNames.toArray(new String[collectedBoneNames.size()]);
        for (String boneName : collectedBonesArray) {
            goUpInBonesHierarchy(boneName, collectedBoneNames, isProcessedBoneSet);
        }*/
    }

   /* private void goUpInBonesHierarchy(String name, List<String> collectedBoneNames, Set<String> isProcessedBoneSet) {
        if (name == null) {
            return;
        }

        String nameLowered = name.toLowerCase();
        SglNode node = nodeName2NodeObject.get(nameLowered);
        if (!isProcessedBoneSet.contains(nameLowered)) {
            if (name.equals("c_gammorean")) {
                System.out.println("");
            }
            collectedBoneNames.add(name);
            isProcessedBoneSet.add(nameLowered);
        }
        String parent = node.getParent();
        goUpInBonesHierarchy(parent, collectedBoneNames, isProcessedBoneSet);
    }

    private void collectBoneNamesFromSqlGeometry(SglGeometry geometry, List<String> collectedBoneNames, Set<String> isProcessedBoneSet) {
        for (SglSkinWeightElement[] elements : geometry.getVertexWeights()) {
            for (SglSkinWeightElement element : elements) {
                String boneName = element.getBoneName();
                String boneNameLower = element.getBoneName().toLowerCase();
                if (isProcessedBoneSet.contains(boneNameLower)) {
                    continue;
                }
                if (boneName.equals("c_gammorean")) {
                    System.out.println("");
                }
                collectedBoneNames.add(boneName);
                isProcessedBoneSet.add(boneNameLower);
            }
        }
    }*/

    private Map<String, SglNode> fillNodeName2NodeObjectMap(SglModel model) {
        Map<String, SglNode> map = new HashMap<>();
        for (SglNode node : model.getNodes()) {
            map.put(node.getName().toLowerCase(), node);
        }

        return map;
    }

    private Map<String, List<SglNode>> fillParentToNodesListMap(SglModel model) {
        Map<String, List<SglNode>> map = new HashMap<>();
        for (SglNode node : model.getNodes()) {
            String parent = node.getParent();
            if (parent == null) {
                parent = ROOT_NODE;
            }

            List<SglNode> tNodes = map.computeIfAbsent(parent.toLowerCase(), (String) -> {
                return new ArrayList<>();
            });

            tNodes.add(node);
        }
        return map;
    }

    private static class AnimationData {

        private float time;
        private Vector3f position;
        private Quaternion rotation;

        @Override
        public String toString() {
            return time + "sec. Position:" + position + ". Rotation:" + rotation;
        }

    }
}
