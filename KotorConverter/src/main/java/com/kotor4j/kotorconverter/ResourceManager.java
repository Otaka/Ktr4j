package com.kotor4j.kotorconverter;

import com.kotor4j.kotorconverter.original.gff.filetypes.ItpGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UttGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UtdGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.DlgGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.PthGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.SsfGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.BtiGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.PwkGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.JrlGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UteGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.AreGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UtsGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.GitGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UtpGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.WokGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.FacGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.AbstractGffResource;
import com.kotor4j.kotorconverter.original.gff.filetypes.DwkGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UtiGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UtmGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.BtcGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UtwGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.BicGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.UtcGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.GuiGff;
import com.kotor4j.kotorconverter.original.gff.filetypes.IfoGff;
import com.kotor4j.configs.Configuration;
import com.kotor4j.io.FileUtils;
import com.kotor4j.io.NwnByteArrayInputStream;
import com.kotor4j.kotorconverter.original._2da.Array2da;
import com.kotor4j.kotorconverter.original._2da.FileReader2da;
import com.kotor4j.kotorconverter.original.bif.Biff;
import com.kotor4j.kotorconverter.original.bif.BiffEntry;
import com.kotor4j.kotorconverter.original.bif.FileReaderBiff;
import com.kotor4j.kotorconverter.original.chitinkey.BifShort;
import com.kotor4j.kotorconverter.original.chitinkey.ChitinKey;
import com.kotor4j.kotorconverter.original.chitinkey.ChitinKeyFileReader;
import com.kotor4j.kotorconverter.original.chitinkey.ContentType;
import com.kotor4j.kotorconverter.original.chitinkey.KeyResource;
import com.kotor4j.kotorconverter.original.chitinkey.ResourceType;
import com.kotor4j.kotorconverter.original.dialog.FileReaderTlk;
import com.kotor4j.kotorconverter.original.dialog.Tlk;
import com.kotor4j.kotorconverter.original.erf.Erf;
import com.kotor4j.kotorconverter.original.erf.FileReaderErf;
import com.kotor4j.kotorconverter.original.gff.FileReaderGff;
import com.kotor4j.kotorconverter.original.gff.Gff;
import com.kotor4j.kotorconverter.original.lyt.FileReaderLyt;
import com.kotor4j.kotorconverter.original.rim.FileReaderRim;
import com.kotor4j.kotorconverter.original.rim.Rim;
import com.kotor4j.kotorconverter.original.ssf.FileReaderSsf;
import com.kotor4j.kotorconverter.original.tga.TgaConverter;
import com.kotor4j.kotorconverter.original.tpc.FileReaderTpc;
import com.kotor4j.kotorconverter.original.tpc.TpcConverter;
import com.kotor4j.kotorconverter.original.tpc.TpcTexture;
import com.kotor4j.kotorconverter.original.vis.FileReaderVis;
import com.kotor4j.kotorconverter.original.vis.VisFile;
import com.kotor4j.kotorconverter.original.walkmesh.FileReaderWalkmesh;
import com.kotor4j.kotorconverter.original.walkmesh.Walkmesh;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitry
 */
public class ResourceManager {

    private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class.getName());
    private Configuration configuration;
    protected File gameFolder;
    protected File convertedMdlsFolder;
    protected Map<String, ResourceType2ResourceListPair> fileToResourceMap = new HashMap<>();
    protected Map<String, ResourceType> extensionToResourceTypeMap = new HashMap<>();
    private ChitinKey chitinKey;
    private Map<File, FileInputStream> openedFilesCache = new HashMap<>();
    protected static final String GAMERES_PREFIX = "gameres://";
    private Tlk cachedTlkFile;
    //  private NcsAssembler ncsAssembler = new NcsAssembler();
    private String currentModule;
    private File modulesFolder;
    private File rimsFolder;
    private Map<String, Map<ResourceType, ResourceRef>> keyToResourceRefMap = new HashMap<>();
    private String currentTexturePack = "swpc_tex_tpa";

    public ResourceManager(String gameFolder, String charset) {
        this.configuration = new Configuration();
        this.configuration.getProperties().put("game.folder", gameFolder);
        this.configuration.getProperties().put("tlkFileCharset", charset);
        this.gameFolder = new File(gameFolder);

        configuration.loadSwKotorIni(new File(gameFolder, "swkotor.ini"));
        for (ResourceType rt : ResourceType.values()) {
            extensionToResourceTypeMap.put(rt.getExtension().toLowerCase(), rt);
        }
    }

    public File getGameFolder() {
        return gameFolder;
    }

    public List<String> serchResourcesByPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        for (String key : keyToResourceRefMap.keySet()) {
            if (key.startsWith(prefix)) {
                result.add(key);
            }
        }
        return result;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static Context loadContext(boolean readAllModules, String gameFolder, String charset) throws IOException {
        ResourceManager resourceManager = new ResourceManager(gameFolder, charset);

        logger.info("Reading resources");

        return new Context(resourceManager, resourceManager.getConfiguration());
    }

    public void closeOpenedFilesCache() {
        for (File file : openedFilesCache.keySet()) {
            FileInputStream stream = openedFilesCache.get(file);
            try {
                stream.close();
            } catch (IOException ex) {
                logger.warn("Error while closing file [" + file.getAbsolutePath() + "]");
            }
        }

        openedFilesCache.clear();
    }

    public void scanWholeResourcesList(boolean readAllModules) throws IOException {
        chitinKey = readChitinKeyFile(gameFolder);
        modulesFolder = configuration.getFile("swkotor.alias.modules");
        if (!modulesFolder.exists()) {
            throw new IllegalArgumentException("Cannot find modules folder [" + modulesFolder.getAbsolutePath() + "]");
        }

        rimsFolder = new File(gameFolder, "rims");
        if (!rimsFolder.exists()) {
            throw new IllegalArgumentException("Cannot find rims folder [" + rimsFolder.getAbsolutePath() + "]");
        }

        readBiffs(gameFolder, chitinKey);
        addTlkFile(gameFolder);
        readErf();
        readRims();
        if (readAllModules) {
            readModules();
        }
        readOverrideFolder();
    }

    public void setCurrentModule(String moduleName) {
        currentModule = moduleName;
        
        if (!fileToResourceMap.containsKey(moduleName)) {
            logger.debug("Read header from module " + currentModule + ".rim");
            File moduleFile = new File(modulesFolder, currentModule + ".rim");
            if (!moduleFile.exists()) {
                throw new IllegalArgumentException("Cannot find module file [" + moduleFile.getAbsolutePath() + "]");
            }

            try (FileInputStream inputStream = new FileInputStream(moduleFile)) {
                Rim rimArchive = new FileReaderRim().loadFile(inputStream, moduleFile);
                processParsedRimArchive(rimArchive, "module");
            } catch (IOException ex) {
                throw new RuntimeException("Error while parsing module file [" + moduleFile.getAbsolutePath() + "]", ex);
            }
        }

        if (!fileToResourceMap.containsKey(moduleName + "_s")) {
            logger.debug("Read header from module " + currentModule + "_s.rim");
            File moduleFile = new File(modulesFolder, currentModule + "_s.rim");
            if (!moduleFile.exists()) {
                throw new IllegalArgumentException("Cannot find module file [" + moduleFile.getAbsolutePath() + "]");
            }

            try (FileInputStream inputStream = new FileInputStream(moduleFile)) {
                Rim rimArchive = new FileReaderRim().loadFile(inputStream, moduleFile);
                processParsedRimArchive(rimArchive, "module");
            } catch (IOException ex) {
                throw new RuntimeException("Error while parsing module file [" + moduleFile.getAbsolutePath() + "]", ex);
            }
        }
        reconstructResourceMap();

    }

    public void reconstructResourceMap() {
        keyToResourceRefMap = new HashMap<>();
        fillResRefMapFromArchive("global");
        fillResRefMapFromArchive("globaldx");
        fillResRefMapFromArchive("miniglobal");
        fillResRefMapFromArchive("miniglobaldx");
        fillResRefMapFromArchive("chargen");
        fillResRefMapFromArchive("chargendx");
        fillResRefMapFromArchive("mainmenu");
        fillResRefMapFromArchive("mainmenudx");
        fillResRefMapFromArchive(currentTexturePack);
        fillResRefMapFromArchive("swpc_tex_gui");
        fillResRefMapFromArchive("_newbif");
        fillResRefMapFromArchive("2da");
        fillResRefMapFromArchive("gui");
        fillResRefMapFromArchive("items");
        fillResRefMapFromArchive("layouts");
        fillResRefMapFromArchive("legacy");
        fillResRefMapFromArchive("lightmaps");
        fillResRefMapFromArchive("lightmaps2");
        fillResRefMapFromArchive("lightmaps3");
        fillResRefMapFromArchive("lightmaps4");
        fillResRefMapFromArchive("lightmaps5");
        fillResRefMapFromArchive("lightmaps6");
        fillResRefMapFromArchive("lightmaps7");
        fillResRefMapFromArchive("lightmaps8");
        fillResRefMapFromArchive("lightmaps9");
        fillResRefMapFromArchive("lightmaps10");
        fillResRefMapFromArchive("lightmaps11");
        fillResRefMapFromArchive("lightmaps12");
        fillResRefMapFromArchive("lightmaps13");
        fillResRefMapFromArchive("models");
        fillResRefMapFromArchive("party");
        fillResRefMapFromArchive("player");
        fillResRefMapFromArchive("scripts");
        fillResRefMapFromArchive("sounds");
        fillResRefMapFromArchive("templates");
        fillResRefMapFromArchive("textures");

        if (currentModule != null) {
            fillResRefMapFromArchive(currentModule);
            fillResRefMapFromArchive(currentModule + "_s");
        }
        fillResRefMapFromArchive("override");
        fillResRefMapFromArchive("talkfile");
    }

    private void fillResRefMapFromArchive(String archiveName) {
        if (!fileToResourceMap.containsKey(archiveName)) {
            throw new IllegalArgumentException("Cannot find archive [" + archiveName + "]");
        }
        ResourceType2ResourceListPair rtp = fileToResourceMap.get(archiveName);

        for (ResourceType rt : rtp.getResourceTypes()) {
            List<ResourceRef> resourceForType = rtp.getResourcesForType(rt);
            for (int i = 0; i < resourceForType.size(); i++) {
                ResourceRef ref = resourceForType.get(i);
                Map<ResourceType, ResourceRef> typeMap = keyToResourceRefMap.get(ref.getName());
                if (typeMap == null) {
                    typeMap = new HashMap<>();
                    keyToResourceRefMap.put(ref.getName().toLowerCase(), typeMap);
                }
                typeMap.put(rt, ref);
            }
        }
    }

    private void addTlkFile(File gameFolder) {
        String dialogTlk = "dialog.tlk";
        File dialogTlkFile = new File(gameFolder, dialogTlk);
        if (!dialogTlkFile.exists()) {
            throw new IllegalArgumentException("Game folder [" + gameFolder.getAbsolutePath() + "] does not have talk file [" + dialogTlk + "]");
        }
        ResourceType2ResourceListPair resourceListMap = new ResourceType2ResourceListPair("dialog", "talkfile");
        resourceListMap.add(ResourceType.TLK, new ResourceRef("dialog", ResourceType.TLK, dialogTlkFile, 0, (int) dialogTlkFile.length(), true));
        fileToResourceMap.put("talkfile", resourceListMap);
    }

    private void readOverrideFolder() {
        File overrideFolder = configuration.getFile("swkotor.alias.override");
        if (!overrideFolder.exists()) {
            logger.warn("Cannot find Override folder at " + overrideFolder.getAbsolutePath());
            return;
        }
        ResourceType2ResourceListPair resourceListMap = new ResourceType2ResourceListPair("override", "override");
        for (File file : overrideFolder.listFiles()) {
            String fileExtension = FilenameUtils.getExtension(file.getName());
            ResourceType rt = extensionToResourceTypeMap.get(fileExtension.toLowerCase());
            if (rt == null) {
                logger.warn("Override folder contains file with unknown extension [" + file.getName() + "]");
                continue;
            }

            String name = FilenameUtils.getBaseName(file.getName());
            resourceListMap.add(rt, new ResourceRef(name, rt, file, 0, (int) file.length(), true));
        }

        fileToResourceMap.put("override", resourceListMap);
    }

    /**
     * File that contains list of id<->fileName from biff files(biff file does
     * not have names, only ids)
     */
    private ChitinKey readChitinKeyFile(File gameFolder) throws FileNotFoundException, IOException {
        File chitinKeyFile = new File(gameFolder, "chitin.key");
        if (!chitinKeyFile.exists()) {
            throw new IllegalArgumentException("Cannot find [chitin.key] file in game folder [" + gameFolder.getAbsolutePath() + "]");
        }
        logger.debug("Read chitin file " + chitinKeyFile.getAbsolutePath());
        ChitinKeyFileReader chitinKeyFileReader = new ChitinKeyFileReader();
        NwnByteArrayInputStream stream = new NwnByteArrayInputStream(chitinKeyFile);
        ChitinKey keys = chitinKeyFileReader.loadFile(stream, chitinKeyFile.getName());
        logger.debug("Parsed chitin file. Found " + keys.getKeyResources().length);
        return keys;
    }

    /**
     * Archive that contains files, but does not have names, only ids described
     * in chitin key file
     */
    private void readBiffs(File gameFolder, ChitinKey chitinKey) {
        for (BifShort biff : chitinKey.getBifs()) {
            logger.debug("Read header from bif " + biff.getBifFile());
            FileReaderBiff biffReader = new FileReaderBiff();
            File biffFile = new File(gameFolder, biff.getBifFile());
            try (FileInputStream stream = new FileInputStream(biffFile)) {
                Biff parsedBiffFile = biffReader.readBiffHeader(stream, biffFile, chitinKey);
                String biffFileName = FilenameUtils.getBaseName(biff.getBifFile());
                ResourceType2ResourceListPair resourceType2ResourceListPair = new ResourceType2ResourceListPair(biffFileName, "biff");

                for (BiffEntry biffEntry : parsedBiffFile.getEntries()) {
                    String resourceName = biffEntry.getResourceId().getResRef();
                    resourceType2ResourceListPair.add(biffEntry.getResourceId().getResourceType(),
                            new ResourceRef(resourceName, biffEntry.getResourceId().getResourceType(), biffFile, biffEntry.getOffset(), biffEntry.getSize(), false)
                    );
                }
                fileToResourceMap.put(biffFileName, resourceType2ResourceListPair);
            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException("Cannot find biff file [" + biff.getBifFile() + "] that is specified in chitin key file", ex);
            } catch (IOException ex) {
                throw new RuntimeException("Error while reading biff file [" + biff.getBifFile() + "]", ex);
            }
        }
    }

    /**
     * erf - archive with textures
     */
    private void readErf() {
        File texturePackFolder = configuration.getFile("swkotor.alias.texturepacks");
        if (!texturePackFolder.exists()) {
            throw new IllegalArgumentException("Cannot find texture folder [" + texturePackFolder.getAbsolutePath() + "]");
        }

        File[] erfFiles = new File[]{new File(texturePackFolder, "swpc_tex_gui.erf"), new File(texturePackFolder, "swpc_tex_tpa.erf")};
        for (File erfFile : erfFiles) {
            logger.debug("Read header from erf " + erfFile.getName());
            try (FileInputStream inputStream = new FileInputStream(erfFile)) {
                Erf erfArchive = new FileReaderErf().loadFile(inputStream, erfFile);
                processParsedErfArchive(erfArchive);
            } catch (IOException ex) {
                throw new RuntimeException("Error while parsing erf file [" + erfFile.getAbsolutePath() + "]", ex);
            }
        }
    }

    private void processParsedErfArchive(Erf erfRimArchive) {
        ResourceType2ResourceListPair resourceListMap = new ResourceType2ResourceListPair(FilenameUtils.getBaseName(erfRimArchive.getFile().getName()).toLowerCase(), "erf");
        fileToResourceMap.put(FilenameUtils.getBaseName(erfRimArchive.getFile().getName()), resourceListMap);
        for (BiffEntry entry : erfRimArchive.getResourceEntries()) {
            KeyResource key = entry.getResourceId();
            resourceListMap.add(key.getResourceType(), new ResourceRef(key.getResRef(), key.getResourceType(), erfRimArchive.getFile(), entry.getOffset(), entry.getSize(), false));
        }
    }

    private void readModules() {
        for (File moduleFile : modulesFolder.listFiles((FilenameFilter) new WildcardFileFilter("*.rim"))) {
            logger.debug("Read header from module " + moduleFile.getName());
            try (FileInputStream inputStream = new FileInputStream(moduleFile)) {
                Rim rimArchive = new FileReaderRim().loadFile(inputStream, moduleFile);
                processParsedRimArchive(rimArchive, "module");
            } catch (IOException ex) {
                throw new RuntimeException("Error while parsing module file [" + moduleFile.getAbsolutePath() + "]", ex);
            }
        }

    }

    private void readRims() {
        for (File supportRimFile : rimsFolder.listFiles((FilenameFilter) new WildcardFileFilter("*.rim"))) {
            logger.debug("Read header from rim " + supportRimFile.getName());
            try (FileInputStream inputStream = new FileInputStream(supportRimFile)) {
                Rim rimArchive = new FileReaderRim().loadFile(inputStream, supportRimFile);
                processParsedRimArchive(rimArchive, "rim");
            } catch (IOException ex) {
                throw new RuntimeException("Error while parsing rim file [" + supportRimFile.getAbsolutePath() + "]", ex);
            }
        }
    }

    private void processParsedRimArchive(Rim rimArchive, String fileType) {
        ResourceType2ResourceListPair resourceListMap = new ResourceType2ResourceListPair(FilenameUtils.getBaseName(rimArchive.getFile().getName()).toLowerCase(), fileType);
        fileToResourceMap.put(FilenameUtils.getBaseName(rimArchive.getFile().getName()), resourceListMap);
        for (BiffEntry entry : rimArchive.getResourceEntries()) {
            KeyResource key = entry.getResourceId();
            resourceListMap.add(key.getResourceType(), new ResourceRef(key.getResRef(), key.getResourceType(), rimArchive.getFile(), entry.getOffset(), entry.getSize(), false));
        }
    }

    /*
    public ResourcesCollection getResourcesByPath(String path) {
        path = path.replace('\\', '/');
        if (path.startsWith(GAMERES_PREFIX)) {
            return getResourcesFromGameArchive(path);
        } else {
            return fileSystemResources(path);
        }
    }

    private ResourcesCollection getResourcesFromGameArchive(String path) {
        ResourceFilter resourceFilter = new ResourceFilter(this);
        return resourceFilter.filterResources(path);
    }
     */
    private ResourcesCollection fileSystemResources(String path) {
        List<ResourceRef> result = new ArrayList<>();
        if (!path.contains("*")) {
            File f = new File(path);
            if (!f.exists()) {
                throw new IllegalArgumentException("Cannot find file [" + path + "]");
            }

            ResourceRef r = fileToResource(f);
            result.add(r);
        } else {
            File[] files = FileUtils.searchFilesByGlobPath(path, false);
            for (File f : files) {
                ResourceRef r = fileToResource(f);
                result.add(r);
            }
        }

        ResourcesCollection resourceCollection = new ResourcesCollection(result);
        return resourceCollection;
    }

    private ResourceRef fileToResource(File file) {
        String extension = FilenameUtils.getExtension(file.getName()).toLowerCase();
        ResourceType resourceType = ResourceType.getResourceTypeByExtension(extension);
        if (resourceType == null) {
            throw new IllegalArgumentException("Unknown file extension [" + extension + "]");
        }

        return new ResourceRef(FilenameUtils.getBaseName(file.getName()), resourceType, file, 0, 0, true);
    }

    public List<String> collectBundleNames() {
        List<String> bundles = new ArrayList<>();
        bundles.addAll(fileToResourceMap.keySet());
        return bundles;
    }

    public NwnByteArrayInputStream getRawStream(ResourceRef resourceRef) {
        if (resourceRef.isWholeFile()) {
            NwnByteArrayInputStream stream = new NwnByteArrayInputStream(resourceRef.getContainerFile());
            return stream;
        } else {
            FileInputStream stream = openedFilesCache.get(resourceRef.getContainerFile());
            if (stream == null) {
                try {
                    stream = new FileInputStream(resourceRef.getContainerFile());
                } catch (FileNotFoundException ex) {
                    throw new IllegalStateException("Cannot find file [" + resourceRef.getContainerFile().getAbsolutePath() + "]", ex);
                }
                openedFilesCache.put(resourceRef.getContainerFile(), stream);
            }
            try {
                stream.getChannel().position(resourceRef.getPosition());
            } catch (IOException ex) {
                throw new RuntimeException("Error while trying to read [" + resourceRef.getName() + "] from file [" + resourceRef.getContainerFile().getAbsolutePath() + "] from offset [" + resourceRef.getPosition() + "]", ex);
            }

            byte[] buffer = new byte[resourceRef.getLength()];
            try {
                IOUtils.read(stream, buffer);
            } catch (IOException ex) {
                throw new RuntimeException("Error while trying to read [" + resourceRef.getName() + "] from file [" + resourceRef.getContainerFile().getAbsolutePath() + "] from offset [" + resourceRef.getPosition() + "]", ex);
            }

            return new NwnByteArrayInputStream(buffer);
        }
    }

    public Tlk getTlk() {
        if (cachedTlkFile == null) {
            ResourceType2ResourceListPair resourceType2ResourceListPair = fileToResourceMap.get("talkfile");
            if (resourceType2ResourceListPair == null || resourceType2ResourceListPair.getResourceTypes().contains(ResourceType.TLK) == false) {
                throw new IllegalStateException("Something wrong. Cannot find dialog.tlk file");
            }
            try {
                getConvertedResource(resourceType2ResourceListPair.getResourcesForType(ResourceType.TLK).get(0));
            } catch (IOException ex) {
                throw new RuntimeException("Error while read dialog.tlk file", ex);
            }
        }
        return cachedTlkFile;
    }

    public Object getConvertedResource(ResourceRef resourceRef) throws IOException {
        return getConvertedResource(resourceRef, false);
    }

    public Map<ResourceType, ResourceRef> getResourceRefByName(String name) {
        return keyToResourceRefMap.get(name);
    }

    public Object getConvertedResource(ResourceRef resourceRef, boolean mdlAsText) throws IOException {
        NwnByteArrayInputStream rawStream = null;
        ContentType contentType = resourceRef.getResourceType().getContentType();
        if (contentType != ContentType.MDL_MODEL) {
            rawStream = getRawStream(resourceRef);
        }

        switch (contentType) {
            case LYT:
                FileReaderLyt lytReader=new FileReaderLyt();
                return lytReader.loadFile(rawStream, resourceRef.getName());
            case TPC_TEXTURE:
                FileReaderTpc fileReaderTpc = new FileReaderTpc();
                TpcTexture tpcTexture = fileReaderTpc.loadFile(rawStream, resourceRef.getName());
                TpcConverter converter = new TpcConverter();
                return converter.convertToBufferedImage(tpcTexture);
            case TLK:
                if (cachedTlkFile == null) {
                    FileReaderTlk fileReaderTlk = new FileReaderTlk(configuration);
                    cachedTlkFile = fileReaderTlk.loadFile(rawStream, resourceRef.getName());
                }

                return cachedTlkFile;
            case GFF:
                FileReaderGff fileReaderGff = new FileReaderGff();
                try {
                    Gff gff = fileReaderGff.loadFile(rawStream, resourceRef.getName());
                    AbstractGffResource gffFile = createSpecificGffObject(resourceRef);
                    gffFile.setGff(gff);
                    return gffFile;
                } catch (IOException ex) {
                    throw new RuntimeException("Error while trying to read resource [" + resourceRef.getName() + "] as GFF from game archive [" + resourceRef.getContainerFile().getAbsolutePath() + "]", ex);
                }
            case WALKMESH:
                FileReaderWalkmesh fileReaderWalkmesh = new FileReaderWalkmesh();
                try {
                    Walkmesh walkmesh = fileReaderWalkmesh.loadFile(rawStream, resourceRef.getName());
                    return walkmesh;
                } catch (IOException ex) {
                    throw new RuntimeException("Error while trying to read resource [" + resourceRef.getName() + "] as Walkmesh from game archive [" + resourceRef.getContainerFile().getAbsolutePath() + "]", ex);
                }
            case _2DA:
                FileReader2da fileReader2da = new FileReader2da();
                Array2da array2da = fileReader2da.loadFile(rawStream, resourceRef.getName());
                return array2da;
            case TGA:
                BufferedImage image = TgaConverter.convertTga(rawStream);
                return image;
            case VIS:
                FileReaderVis fileReaderVis = new FileReaderVis();
                VisFile visFile = fileReaderVis.loadFile(rawStream, resourceRef.getName());
                return visFile;
            case TEXT:
                return new String(rawStream.getBuffer(), StandardCharsets.UTF_8);
//            case BYTECODE:
//                List<DisassembledLine> lines = ncsAssembler.disassembler(rawStream.getBuffer());
//                StringBuilderWithSeparator sb = new StringBuilderWithSeparator("\n");
//                for (DisassembledLine line : lines) {
//                    String addressString = ByteArrayUtils.hex(line.getOffset(), 4);
//                    String lineString = "/*" + addressString + " " + StringUtils.rightPad(line.getByteDumpString(), 30, ' ') + "*/  " + line.getLine();
//                    sb.append(lineString).newEntry();
//                }
//
//                return sb.toString();
            case BINARY:
                return rawStream;
            case SSF:
                FileReaderSsf fileReaderSsf = new FileReaderSsf();
                return fileReaderSsf.loadFile(rawStream, resourceRef.getName(), cachedTlkFile);
//            case MDL_MODEL:
//                File convertedMdlFile = new File(convertedMdlsFolder, resourceRef.getName() + "-mdledit.mdl.ascii");
//                if (!convertedMdlFile.exists()) {
//                    throw new IllegalStateException("Cannot find converted ascii.mdl for model [" + resourceRef.getName() + "]");
//                }
//
//                try (InputStream stream = new FileInputStream(convertedMdlFile)) {
//                    MdlAscii mdlAscii = new MdlAscii(IOUtils.toString(stream, StandardCharsets.UTF_8));
//                    return mdlAscii;
//                }
            default:
                throw new IllegalArgumentException("getResource for resource type [" + resourceRef.getResourceType() + "] is not implemented yet");
        }
    }

    private AbstractGffResource createSpecificGffObject(ResourceRef resRef) {
        switch (resRef.getResourceType()) {
            case ARE:
                return new AreGff();
            case BIC:
                return new BicGff();
            case BTC:
                return new BtcGff();
            case BTI:
                return new BtiGff();
            case DLG:
                return new DlgGff();
            case DWK:
                return new DwkGff();
            case FAC:
                return new FacGff();
            case GIT:
                return new GitGff();
            case GUI:
                return new GuiGff();
            case IFO:
                return new IfoGff();
            case ITP:
                return new ItpGff();
            case JRL:
                return new JrlGff();
            case PWK:
                return new PwkGff();
            case SSF:
                return new SsfGff();
            case UTC:
                return new UtcGff();
            case UTD:
                return new UtdGff();
            case UTE:
                return new UteGff();
            case UTI:
                return new UtiGff();
            case UTM:
                return new UtmGff();
            case UTP:
                return new UtpGff();
            case UTS:
                return new UtsGff();
            case UTT:
                return new UttGff();
            case UTW:
                return new UtwGff();
            case WOK:
                return new WokGff();
            case PTH:
                return new PthGff();
            default:
                throw new IllegalStateException("Unknown GFF resource object [" + resRef.getResourceType() + "]");
        }
    }

    public String getCurrentModule() {
        return currentModule;
    }

    /**
     * Expects file path and returns null in case if file is not found<br/>
     * Can contain placeholder {game.folder} to actual game folder path without
     * /
     */
    public File getGameFile(String path) {
        if (path.contains("{game.folder}")) {
            path = path.replace("{game.folder}", gameFolder.getAbsolutePath());
        }
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        return f;
    }

    /**
     * Expects file path and returns null in case if file is not found<br/>
     * Can contain placeholder {game.folder} to actual game folder path that
     * ends with /
     */
    public File getGameFileThrowErrorIfNotExists(String path) {
        File f = getGameFile(path);
        if (f == null) {
            throw new IllegalArgumentException("Cannot find file [" + path + "]");
        }
        return f;
    }

    public Map<String, ResourceType2ResourceListPair> getFileToResourceMap() {
        return fileToResourceMap;
    }
    
    
}
