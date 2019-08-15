package com.kotor4j.resourcemanager.resfilter;

import java.util.regex.Pattern;

/**
 * @author Dmitry
 */
public class ResBundleFilterWildcard {

    private Pattern resBundleWildcard;
    private Pattern fileNameWildcard;
    private Pattern extensionWildcard;
    private int matchedResourcesCount = 0;

    public ResBundleFilterWildcard(Pattern resBundleWildcard, Pattern fileNameWildcard, Pattern extensionWildcard) {
        this.resBundleWildcard = resBundleWildcard;
        this.fileNameWildcard = fileNameWildcard;
        this.extensionWildcard = extensionWildcard;
    }

    public Pattern getExtensionWildcard() {
        return extensionWildcard;
    }

    public Pattern getFileNameWildcard() {
        return fileNameWildcard;
    }

    public int getMatchedResourcesCount() {
        return matchedResourcesCount;
    }

    public Pattern getResBundleWildcard() {
        return resBundleWildcard;
    }

    public void setMatchedResourcesCount(int matchedResourcesCount) {
        this.matchedResourcesCount = matchedResourcesCount;
    }
}
