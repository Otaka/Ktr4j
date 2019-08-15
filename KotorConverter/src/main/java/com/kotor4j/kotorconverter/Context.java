package com.kotor4j.kotorconverter;

import com.kotor4j.configs.Configuration;

/**
 * @author Dmitry
 */
public class Context {

    private ResourceManager resourceManager;
    private Configuration configuration;
    
    public Context(ResourceManager resourceManager, Configuration configuration) {
        this.resourceManager = resourceManager;
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

}
