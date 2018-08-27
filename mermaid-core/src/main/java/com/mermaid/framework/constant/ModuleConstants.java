package com.mermaid.framework.constant;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2018/8/26 23:36
 * version 1.0
 */
public interface ModuleConstants {
    String MODULE_DEFINITION_RESOURCES = "classpath*:META-INF/mermaid-*.properties";

    String CONFIG_ITEM_CODE_PACKAGE = "mermaid.module.code.basePackages";

    String CONFIG_ITEM_DAO_PACKAGE = "mermaid.module.dao.basePackages";

    String LAUNCH_ARG_CODE_PACKAGES = "mermaid.modules.basePackages";

    String LAUNCH_ARG_DAO_PACKAGES = "mermaid.modules.daoPackages";

    String CONFIG_ITEM_CLOUD_CLIENT_PACKAGE = "mermaid.module.cloudClient.packages";

    String LAUNCH_ARG_CLOUD_CLIENT_PACKAGES = "mermaid.modules.cloudClient.packages";

    String CONFIG_ITEM_CONFIGURATION_NAMESPACES = "mermaid.module.configuration.namespaces";

    String LAUNCH_ARG_CONFIGURATION_NAMESPACES = "mermaid.module.configuration.namespaces";
}
