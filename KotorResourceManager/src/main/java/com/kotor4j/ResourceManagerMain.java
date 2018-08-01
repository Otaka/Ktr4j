package com.kotor4j;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kotor4j.configs.Configuration;
import com.kotor4j.io.NwnByteArrayInputStream;
import com.kotor4j.resourcemanager.HelpAboutTypes;
import com.kotor4j.resourcemanager.ResourceRef;
import com.kotor4j.resourcemanager.ResourceManager;
import com.kotor4j.resourcemanager.ResourcesCollection;
import com.kotor4j.resourcemanager._2da.Array2da;
import com.kotor4j.resourcemanager.chitinkey.ResourceType;
import com.kotor4j.resourcemanager.dialog.Tlk;
import com.kotor4j.resourcemanager.gff.GffToJsonConverter;
import com.kotor4j.resourcemanager.gff.filetypes.AbstractGffResource;
import com.kotor4j.resourcemanager.mdl.MdlAscii;
import com.kotor4j.resourcemanager.ssf.SsfFile;
import com.kotor4j.resourcemanager.vis.VisFile;
import com.kotor4j.resourcemanager.walkmesh.Walkmesh;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitry
 */
public class ResourceManagerMain {

    @Parameter(names = {"--print_file_types"}, description = "If provided 'all' - print known file types and small help about them, or your can provide specific file type", required = false)
    private String printHelpAboutTypes = null;

    @Parameter(names = {"--printbundles"}, description = "Print all found resource bundles", required = false)
    private boolean printBundles = false;

    @Parameter(names = {"--path", "-p"}, description = "Path to resource. Can have wildcards.", required = false)
    private String path;

    @Parameter(names = {"--convert", "-c"}, description = "Convert while extracting. Default - false", required = false)
    private boolean convert = false;

    @Parameter(names = {"--dest"}, description = "Extraction destination folder", required = false)
    private String destinationFolderPath;

    @Parameter(names = {"--flat"}, description = "Extract all files to single directory if true, or to directory structure if false. Default - false", required = false)
    private boolean flat = false;

    @Parameter(names = "--help", help = true)
    private boolean help = false;
    private static final Logger logger = LoggerFactory.getLogger(ResourceManagerMain.class.getName());

    public static void main(String[] args) throws IOException {
        new ResourceManagerMain().start(args);
    }

    private void start(String[] args) throws IOException {
        parseCommandLine(args);
        if (printHelpAboutTypes != null) {
            printHelpAboutTypes(printHelpAboutTypes);
        }

        Configuration configuration = new Configuration();
        ResourceManager resourceManager = new ResourceManager(configuration);
        logger.info("Reading resources");
        resourceManager.scanWholeResourcesList();
        logger.info("Finished reading resources");
        if (printBundles) {
            printBundles(resourceManager);
        }

        if (destinationFolderPath != null && path == null) {
            throw new IllegalArgumentException("Please provide --path where you will specify what resources you want to extract");
        }

        ResourcesCollection resources;
        if (path != null) {
            resources = resourceManager.getResourcesByPath(path);
            logger.info("Found " + resources.collectedResources().size() + " resources from provided path " + path);
            logger.info("Started extraction");
            processExtraction(resources, resourceManager);
        }

        logger.info("Finish");
    }

    private void processExtraction(ResourcesCollection resourcesCollection, ResourceManager resourceManager) throws IOException {
        List<ResourceRef> resources = resourcesCollection.collectedResources();
        //sort by container file and position inside this file, to move only forward
        Collections.sort(resources, new Comparator<ResourceRef>() {
            @Override
            public int compare(ResourceRef o1, ResourceRef o2) {
                int sortResult = o1.getContainerFile().compareTo(o2.getContainerFile());
                if (sortResult == 0) {
                    return o1.getPosition() - o2.getPosition();
                }

                return sortResult;
            }
        });

        Collections.sort(resources, new Comparator<ResourceRef>() {
            @Override
            public int compare(ResourceRef o1, ResourceRef o2) {
                if (o1.getResourceType() == ResourceType.TLK) {
                    return -1;
                }
                if (o1.getResourceType() == o2.getResourceType()) {
                    return 0;
                }

                return 1;
            }
        });

        File destBaseFolder = new File(destinationFolderPath);
        File lastContainer = null;
        String fileSuffix;
        if (convert) {
            fileSuffix = ".converted";
        } else {
            fileSuffix = "";
        }

        for (ResourceRef r : resources) {
            /*if (//r.getResourceType() == ResourceType.MDL
                //    || r.getResourceType() == ResourceType.MDX
                    ) {
                logger.info("skip " + r.getResourceType() + " resource " + r.getName());
                continue;
            }*/
            if (!r.getContainerFile().equals(lastContainer)) {
                logger.info("Start to process container file " + r.getContainerFile());

            }
            lastContainer = r.getContainerFile();
            logger.info("Extract resource " + r.getName() + "." + r.getResourceType());
            File destinationFolder = destBaseFolder;
            if (flat == false) {
                String folderName;
                if (r.getContainerFile().getParentFile().getName().equalsIgnoreCase("override")) {
                    folderName = "override";
                } else {
                    folderName = FilenameUtils.getBaseName(r.getContainerFile().getName());
                }
                destinationFolder = new File(new File(destinationFolder, r.getResourceType().getExtension()), folderName);
            }
            String tSuffix = fileSuffix;
            if (convert && (r.getResourceType() == ResourceType.TGA || r.getResourceType() == ResourceType.TPC)) {
                tSuffix = ".png";
            }

            File destinationFile = new File(destinationFolder, r.getName() + "." + r.getResourceType().getExtension() + tSuffix);
            if (convert == false) {
                NwnByteArrayInputStream stream = resourceManager.getRawStream(r);
                FileUtils.writeByteArrayToFile(destinationFile, stream.getBuffer());
            } else if (convert == true) {
                Object resource = resourceManager.getConvertedResource(r, true);
                if (resource == null) {
                    throw new RuntimeException("Cannot convert file [" + r.toString() + "]");
                }

                if (resource instanceof AbstractGffResource) {
                    String convertedJson = new GffToJsonConverter().gffToJson((AbstractGffResource) resource);
                    writeFile(destinationFile, convertedJson);
                } else if (resource instanceof Walkmesh) {
                    String convertedJson = ((Walkmesh) resource).toJson();
                    writeFile(destinationFile, convertedJson);
                } else if (resource instanceof Array2da) {
                    String convertedJson = ((Array2da) resource).toJson();
                    writeFile(destinationFile, convertedJson);
                } else if (resource instanceof Tlk) {
                    String convertedJson = ((Tlk) resource).toJson();
                    writeFile(destinationFile, convertedJson);
                } else if (resource instanceof BufferedImage) {
                    BufferedImage image = (BufferedImage) resource;
                    writeFile(destinationFile, image);
                } else if (resource instanceof VisFile) {
                    String visData = ((VisFile) resource).toRawVisFile();
                    writeFile(destinationFile, visData);
                } else if (resource instanceof String) {
                    writeFile(destinationFile, (String) resource);
                } else if (resource instanceof NwnByteArrayInputStream) {
                    writeFile(destinationFile, (NwnByteArrayInputStream) resource);
                } else if (resource instanceof SsfFile) {
                    writeFile(destinationFile, ((SsfFile) resource).toJson());
                } else if (resource instanceof MdlAscii) {
                    writeFile(destinationFile, ((MdlAscii) resource).getAsciiText());
                } else {
                    throw new IllegalStateException("Converting object [" + resource.getClass().getSimpleName() + "] is not implemented yet");
                }
            }
        }
    }

    private boolean debugSkipWriting = false;

    private void writeFile(File file, String value) throws IOException {
        if (!debugSkipWriting) {
            FileUtils.write(file, value, StandardCharsets.UTF_8);
        }
    }

    private void writeFile(File file, NwnByteArrayInputStream value) throws IOException {
        if (!debugSkipWriting) {
            FileUtils.writeByteArrayToFile(file, value.getBuffer());
        }
    }

    private void writeFile(File file, BufferedImage value) throws IOException {
        if (!debugSkipWriting) {
            File parentFolder = file.getParentFile();
            if (!parentFolder.exists()) {
                parentFolder.mkdirs();
            }
            ImageIO.write(value, "png", file);
        }
    }

    private void printBundles(ResourceManager resourceManager) {
        List<String> bundles = resourceManager.collectBundleNames();
        Collections.sort(bundles, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        System.out.println("Resource bundles");
        for (String bundle : bundles) {
            System.out.println(bundle);
        }
    }

    private void parseCommandLine(String[] args) {
        JCommander jc = JCommander.newBuilder()
                .addObject(this)
                .build();
        jc.parse(args);
        if (help) {
            jc.usage();
            String jar = getCurrentJarName();
            System.out.println("Examples:");
            System.out.println(" -Print all known resource types");
            System.out.println("   java -jar " + jar + " --print_file_types");
            System.out.println("");
            System.out.println(" -Print information about particular resource type");
            System.out.println("  java -jar " + jar + " --print_file_types mdl");
            System.out.println("");
            System.out.println(" -Print all found bundles in scanned resources");
            System.out.println("  java -jar " + jar + " --printbundles");
            System.out.println("");
            System.out.println(" -Extract and convert resources. Extract only from biff bundle file with .mdl and nss extension, and ignore scanning swpc_tex_tpb and swpc_tex_tpc bundles (just for example)");
            System.out.println(" (Application will try to convert some resources to more user friendly formats(tpc -> png, ncs -> disassembled_ncs...))");
            System.out.println("  java -jar " + jar + " --path \"gameres://+[biff/*.mdl, biff/*.nss] -[swpc_tex_tpb/*,swpc_tex_tpc/*]\" --convert --dest \"D:/KOTOR_extracted\"");
            System.exit(0);
        }
    }

    private String getCurrentJarName() {
        return new java.io.File(ResourceManagerMain.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
    }

    private void printHelpAboutTypes(String fileType) {
        HelpAboutTypes helpAboutTypes = new HelpAboutTypes();
        String value = helpAboutTypes.getHelpAboutTypes(fileType);
        System.out.println(value);
        System.exit(0);
    }
}
