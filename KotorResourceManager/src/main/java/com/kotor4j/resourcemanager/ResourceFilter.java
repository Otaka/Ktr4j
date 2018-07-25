package com.kotor4j.resourcemanager;

import com.kotor4j.io.FileUtils;
import com.kotor4j.resourcemanager.chitinkey.ResourceType;
import com.kotor4j.resourcemanager.resfilter.ResBundleFilterWildcard;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitry
 */
public class ResourceFilter {

    private ResourceManager resourceManager;
    private List<ResBundleFilterWildcard> pathsToInclude = new ArrayList<>();
    private List<ResBundleFilterWildcard> pathsToExclude = new ArrayList<>();
    private List<ResourceRef> tempResources = new ArrayList<>();

    public ResourceFilter(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * gameres://+[override/*.nss, biff/*.mdl]
     */
    public ResourcesCollection filterResources(String fullSearchPath) {
        fullSearchPath = fullSearchPath.substring(ResourceManager.GAMERES_PREFIX.length());
        String path = fullSearchPath;
        parsePath(path);
        List<ResourceRef> result = new ArrayList<>();

        for (String fileName : resourceManager.fileToResourceMap.keySet()) {
            ResourceType2ResourceListPair rrp = resourceManager.fileToResourceMap.get(fileName);
            processResourceType2ResourceListPair(rrp, result);
        }

        ResourcesCollection resourcesCollection = new ResourcesCollection(result);
        return resourcesCollection;
    }

    private void processResourceType2ResourceListPair(ResourceType2ResourceListPair rrp, List<ResourceRef> result) {
        tempResources.clear();
        for (ResBundleFilterWildcard includeFilter : pathsToInclude) {
            //match file name
            if (includeFilter.getResBundleWildcard().matcher(rrp.getFileType()).matches()) {
                for (ResourceType resType : rrp.getResourceTypes()) {
                    //match extension
                    if (includeFilter.getExtensionWildcard().matcher(resType.getExtension()).matches()) {
                        for (ResourceRef resource : rrp.getResourcesForType(resType)) {
                            if (includeFilter.getFileNameWildcard().matcher(resource.getName()).matches()) {
                                tempResources.add(resource);
                            }
                        }
                    }
                }
            }
        }

        if (!pathsToExclude.isEmpty()) {
            List<ResourceRef> notExcludedResources = new ArrayList<>();
            for (ResBundleFilterWildcard excludeFilter : pathsToExclude) {
                if (!excludeFilter.getResBundleWildcard().matcher(rrp.getFile()).matches()) {
                    for (ResourceRef resource : tempResources) {
                        if (!excludeFilter.getExtensionWildcard().matcher(resource.getResourceType().getExtension()).matches()) {
                            if (!excludeFilter.getFileNameWildcard().matcher(resource.getName()).matches()) {
                                notExcludedResources.add(resource);
                            }
                        }
                    }
                }
            }

            tempResources = notExcludedResources;
        }

        result.addAll(tempResources);
    }

    private void parsePath(String path) {
        Pattern pattern = Pattern.compile("(?:(?<includeExclude>\\+|\\-)?\\[(?<resBundles>.+?)\\]\\s*)|(?<notcaptured>.+)");
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            String notCaptured = matcher.group("notcaptured");
            if (notCaptured != null) {
                throw new IllegalArgumentException("Error while parsing resource path [" + path + "]. It contains wrong files descrition [" + notCaptured + "]");
            }

            String includeExclude = matcher.group("includeExclude");
            String modules = matcher.group("resBundles");
            String[] splittedResourceBundle = modules.split("\\s*,\\s*");
            for (String resBundlePath : splittedResourceBundle) {
                checkResBundlePath(resBundlePath);
                if (includeExclude == null || includeExclude.trim().equals("+")) {
                    pathsToInclude.add(createResBundleFilterFromPath(resBundlePath));
                } else if (includeExclude.trim().equals("-")) {
                    pathsToExclude.add(createResBundleFilterFromPath(resBundlePath));
                } else {
                    throw new IllegalArgumentException("Expected only + or - but found [" + includeExclude + "]");
                }
            }
        }
    }

    private ResBundleFilterWildcard createResBundleFilterFromPath(String resBundlePath) {
        String[] resBundlePathParts = resBundlePath.split("/");
        String bundleWildcard = resBundlePathParts[0];
        String fileNameWildcard = resBundlePathParts[1];
        String extension = "*";
        if (fileNameWildcard.contains(".")) {
            String[] fileNameParts = fileNameWildcard.split("\\.");
            fileNameWildcard = fileNameParts[0];
            extension = fileNameParts[1];
        }

        return new ResBundleFilterWildcard(
                Pattern.compile(FileUtils.wildcardToRegex(bundleWildcard)),
                Pattern.compile(FileUtils.wildcardToRegex(fileNameWildcard)),
                Pattern.compile(FileUtils.wildcardToRegex(extension))
        );
    }

    private void checkResBundlePath(String resourceBundle) {
        Pattern pattern = Pattern.compile("[0-9_a-zA-Z\\*]+/[_0-9a-zA-Z\\*\\.]+");
        Matcher matcher = pattern.matcher(resourceBundle);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Error in resourceBundle path [" + resourceBundle + "]");
        }
    }
}
