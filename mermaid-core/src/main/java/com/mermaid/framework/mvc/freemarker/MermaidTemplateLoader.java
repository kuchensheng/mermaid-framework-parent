package com.mermaid.framework.mvc.freemarker;

import freemarker.cache.TemplateLoader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/26 21:35
 * version 1.0
 */
public class MermaidTemplateLoader implements TemplateLoader {
    private static final Logger logger = LoggerFactory.getLogger(MermaidTemplateLoader.class);

    private static final String HTML_ESCAPE_PREFIX = "<#escape x as x?html>";

    private static final String HTML_ESCAPE_SUFFIX = "</#escape>";

    private PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver;

    private String templatePath;

    private boolean autoEscapeEnabled;

    private boolean classpathFirstPolicyEnabled;

    public MermaidTemplateLoader(String templatePath, boolean autoEscapeEnabled) {
        this(templatePath, autoEscapeEnabled, true);
    }

    public MermaidTemplateLoader(String templatePath, boolean autoEscapeEnabled, boolean classpathFirstPolicyEnabled) {
        this.autoEscapeEnabled = autoEscapeEnabled;
        this.pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        this.setTemplatePath(templatePath);
        this.classpathFirstPolicyEnabled = classpathFirstPolicyEnabled;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        if (this.classpathFirstPolicyEnabled && this.templatePath.startsWith("classpath*:")) {
            Resource resource = this.pathMatchingResourcePatternResolver.getResource(this.templatePath.replace("classpath*:", "classpath:") + name);
            if (resource != null && resource.exists()) {
                return resource;
            }
        }

        Resource[] resources = this.pathMatchingResourcePatternResolver.getResources(this.templatePath + name);
        if (resources.length == 1) {
            return resources[0];
        } else if (resources.length > 1) {
            logger.warn("FreeMarker template file name conflict");
        }
        return null;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Resource resource = (Resource) templateSource;
        try {
            Reader reader = new InputStreamReader(resource.getInputStream(), encoding);
            return this.getAutoEscapeReader(reader);
        } catch (IOException ex) {
            logger.debug("Could not find FreeMarker template: " + resource);
            throw ex;
        }
    }

    @Override
    public void closeTemplateSource(Object o) throws IOException {

    }

    @Override
    public long getLastModified(Object templateSource) {
        Resource resource = (Resource) templateSource;
        try {
            return resource.lastModified();
        } catch (IOException ex) {
            logger.debug("Could not obtain last-modified timestamp for FreeMarker template in " +
                    resource + ": " + ex);
            return -1;
        }
    }

    protected Reader getAutoEscapeReader(Reader reader) throws IOException {
        if (!this.autoEscapeEnabled) {
            return reader;
        }

        String templateText = IOUtils.toString(reader);
        reader.close();
        return new StringReader(HTML_ESCAPE_PREFIX + templateText + HTML_ESCAPE_SUFFIX);
    }

    public PathMatchingResourcePatternResolver getPathMatchingResourcePatternResolver() {
        return pathMatchingResourcePatternResolver;
    }

    public void setPathMatchingResourcePatternResolver(PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver) {
        this.pathMatchingResourcePatternResolver = pathMatchingResourcePatternResolver;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        if(!templatePath.endsWith("/")) {
            templatePath += "/";
        }
        this.templatePath = templatePath;
    }

    public boolean isAutoEscapeEnabled() {
        return autoEscapeEnabled;
    }

    public void setAutoEscapeEnabled(boolean autoEscapeEnabled) {
        this.autoEscapeEnabled = autoEscapeEnabled;
    }

    public boolean isClasspathFirstPolicyEnabled() {
        return classpathFirstPolicyEnabled;
    }

    public void setClasspathFirstPolicyEnabled(boolean classpathFirstPolicyEnabled) {
        this.classpathFirstPolicyEnabled = classpathFirstPolicyEnabled;
    }
}
