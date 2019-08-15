package com.assimp4j;

import com.assimp4j.data.AiScene;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Dmitry
 */
public class AssImp {

    public static int PROP_CalcTangentSpace = 0x1;
    public static int PROP_JoinIdenticalVertices = 0x2;
    public static int PROP_MakeLeftHanded = 0x4;
    public static int PROP_Triangulate = 0x8;
    public static int PROP_GenNormals = 0x20;
    public static int PROP_GenSmoothNormals = 0x40;
    public static int PROP_SplitLargeMeshes = 0x80;
    /**
     * Removes the node graph and pre-transforms all vertices with the local
     * transformation matrices of their nodes.
     */
    public static int PROP_PreTransformVertices = 0x100;
    /**
     * Limits the number of bones simultaneously affecting a single vertex to a
     * maximum value.
     */
    public static int PROP_LimitBoneWeights = 0x200;
    /**
     * Validates the imported scene data structure. This makes sure that all
     * indices are valid, all animations and bones are linked correctly, all
     * material references are correct .. etc.
     */
    public static int PROP_ValidateDataStructure = 0x400;
    /**
     * Reorders triangles for better vertex cache locality.
     */
    public static int PROP_ImproveCacheLocality = 0x800;
    /**
     * Searches for redundant/unreferenced materials and removes them.
     */
    public static int PROP_RemoveRedundantMaterials = 0x1000;
    /**
     * This step tries to determine which meshes have normal vectors that are
     * facing inwards and inverts them.
     */
    public static int PROP_FixInfacingNormals = 0x2000;
    /**
     * This step splits meshes with more than one primitive type in homogeneous
     * sub-meshes.
     */
    public static int PROP_SortByPType = 0x8000;
    /**
     * This step searches all meshes for degenerate primitives and converts them
     * to proper lines or points.
     */
    public static int PROP_FindDegenerates = 0x10000;
    /**
     * This step searches all meshes for invalid data, such as zeroed normal
     * vectors or invalid UV coords and removes/fixes them. This is intended to
     * get rid of some common exporter errors.
     */
    public static int PROP_FindInvalidData = 0x20000;
    /**
     * This step converts non-UV mappings (such as spherical or cylindrical
     * mapping) to proper texture coordinate channels.
     */
    public static int PROP_GenUVCoords = 0x40000;
    /**
     * This step applies per-texture UV transformations and bakes them into
     * stand-alone vtexture coordinate channels.
     */
    public static int PROP_TransformUVCoords = 0x80000;
    /**
     * This step searches for duplicate meshes and replaces them with references
     * to the first mesh.<br>
     * <b>This step takes a while, so don't use it if speed is a concern</b>.
     * Its main purpose is to workaround the fact that many export file formats
     * don't support instanced meshes, so exporters need to duplicate meshes.
     * This step removes the duplicates again. Please note that Assimp does not
     * currently support per-node material assignment to meshes, which means
     * that identical meshes with different materials are currently *not*
     * joined, although this is planned for future versions.
     */
    public static int PROP_FindInstances = 0x100000;
    /**
     * A postprocessing step to reduce the number of meshes.
     *
     * This will, in fact, reduce the number of draw calls.
     *
     * This is a very effective optimization and is recommended to be used
     * together with #aiProcess_OptimizeGraph, if possible. The flag is fully
     * compatible with both #aiProcess_SplitLargeMeshes and
     * #aiProcess_SortByPType.
     */
    public static int PROP_OptimizeMeshes = 0x200_000;
    /**
     * A postprocessing step to optimize the scene hierarchy.
     *
     * Nodes without animations, bones, lights or cameras assigned are collapsed
     * and joined.
     *
     * Node names can be lost during this step. If you use special 'tag nodes'
     * to pass additional information through your content pipeline, use the
     * <tt>#AI_CONFIG_PP_OG_EXCLUDE_LIST</tt> importer property to specify a
     * list of node names you want to be kept. Nodes matching one of the names
     * in this list won't be touched or modified.
     *
     * Use this flag with caution. Most simple files will be collapsed to a
     * single node, so complex hierarchies are usually completely lost. This is
     * not useful for editor environments, but probably a very effective
     * optimization if you just want to get the model data, convert it to your
     * own format, and render it as fast as possible.
     *
     * This flag is designed to be used with #aiProcess_OptimizeMeshes for best
     * results.
     *
     * @note 'Crappy' scenes with thousands of extremely small meshes packed in
     * deeply nested nodes exist for almost all file formats.
     * #aiProcess_OptimizeMeshes in combination with #aiProcess_OptimizeGraph
     * usually fixes them all and makes them renderable.
     */
    public static int PROP_OptimizeGraph = 0x400_000;
    /**
     * This step removes bones losslessly or according to some threshold.
     *
     * In some cases (i.e. formats that require it) exporters are forced to
     * assign dummy bone weights to otherwise static meshes assigned to animated
     * meshes. Full, weight-based skinning is expensive while animating nodes is
     * extremely cheap, so this step is offered to clean up the data in that
     * regard.
     *
     * Use <tt>#AI_CONFIG_PP_DB_THRESHOLD</tt> to control this. Use
     * <tt>#AI_CONFIG_PP_DB_ALL_OR_NONE</tt> if you want bones removed if and
     * only if all bones within the scene qualify for removal.
     */
    public static int PROP_Debone = 0x4000_000;

    private AssImp() {
    }
    private static AssImp instance;

    public synchronized static AssImp get() {
        if (instance == null) {
            System.load("c:\\Program Files\\Assimp\\bin\\x64\\assimp-vc140-mt.dll");
            System.load("F:\\java\\Ktr4j\\AssImp4J\\src\\nativelibrary\\AssImpJni\\x64\\Debug\\AssImpJni.dll");
            init("assimp-vc140-mt");
            instance = new AssImp();
        } else {
            throw new IllegalStateException("Library " + AssImp.class.getClass().getSimpleName() + " is already initialized");
        }
        return instance;
    }

    public static String getSignature(Method m) {
        String sig;
        try {
            Field gSig = Method.class.getDeclaredField("signature");
            gSig.setAccessible(true);
            sig = (String) gSig.get(m);
            if (sig != null) {
                return sig;
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder("(");
        for (Class<?> c : m.getParameterTypes()) {
            sb.append((sig = Array.newInstance(c, 0).toString())
                    .substring(1, sig.indexOf('@')));
        }
        String result = sb.append(')')
                .append(
                        m.getReturnType() == void.class ? "V"
                                : (sig = Array.newInstance(m.getReturnType(), 0).toString()).substring(1, sig.indexOf('@'))
                )
                .toString();
        result = result.replace(".", "/");
        return result;
    }

    //runs from native code
    protected static String findMethod(Class clazz, String methodName) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return getSignature(method);
            }
        }

        throw new IllegalArgumentException("Cannot find method [" + methodName + "] in class [" + clazz.getSimpleName() + "]");
    }

    private static native void init(String assImpModuleName);

    private static native AiScene internalLoadMesh(String path, int flags);

    private static native AiScene internalLoadMeshFromBuffer(byte[] buffer, String extension, int flags);

    /**
     * Load model from file
     */
    public AiScene loadMesh(File file, int flags) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Cannot find file at [" + file.getAbsolutePath() + "]");
        }
        return internalLoadMesh(file.getAbsolutePath(), flags);
    }

    /**
     * Load model from memory buffer and extension hint(that can be null, to guess file format)
     */
    public AiScene loadMesh(byte[] memoryBuffer, String extensionHint, int flags) throws IOException {
        return internalLoadMeshFromBuffer(memoryBuffer, extensionHint, flags);
    }

    /**
     * Load model from InputStream and extension hint(that can be null, to guess file format)
     */
    public AiScene loadMesh(InputStream stream, int size, String extensionHint, int flags) throws IOException {
        byte[] buffer = new byte[size];
        for (int i = 0; i < size; i++) {
            int readByte = stream.read();
            if (readByte == -1) {
                throw new IllegalStateException("Expected to read [" + size + "] bytes from stream, but read only [" + i + "] before stream ends");
            }

            buffer[i] = (byte) readByte;
        }

        return internalLoadMeshFromBuffer(buffer, extensionHint, flags);
    }
}
