package com.upb;

import java.net.URL;

public class KnowledgeSource {
    private String name;
    private String description;
    private URL url;

    public KnowledgeSource(String name, String description, URL url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return name + " [" + url + "]: " + description;
    }
}
