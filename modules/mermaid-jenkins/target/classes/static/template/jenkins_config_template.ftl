<?xml version='1.0' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@2.19">
    <description>${description!}</description>
    <keepDependencies>false</keepDependencies>
        <#if cron ??>
        <actions>
            <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin="pipeline-model-definition@1.2.8"/>
            <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction plugin="pipeline-model-definition@1.2.8">
                <jobProperties/>
                <triggers>
                    <string>hudson.triggers.TimerTrigger</string>
                </triggers>
                <parameters/>
            </org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction>
        </actions>
    <properties>
            <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
                <triggers>
                    <hudson.triggers.TimerTrigger>
                        <spec>${cron!}</spec>
                    </hudson.triggers.TimerTrigger>
                </triggers>
            </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
    </properties>
        </#if>
    <#if !cron?exists && parameterMap??>
        <properties>
            <hudson.model.ParametersDefinitionProperty>
                <parameterDefinitions>
                 <#list parameterMap?keys as key>
                     <hudson.model.StringParameterDefinition>
                         <name>${key}</name>
                         <description>${parameterMap[key]}</description>
                         <defaultValue></defaultValue>
                     </hudson.model.StringParameterDefinition>
                 </#list>
                </parameterDefinitions>
            </hudson.model.ParametersDefinitionProperty>
        </properties>
    </#if>
    <#if cron?? && parameterMap??>
        <actions>
            <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin="pipeline-model-definition@1.2.8"/>
            <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction plugin="pipeline-model-definition@1.2.8">
                <jobProperties/>
                <triggers>
                    <string>hudson.triggers.TimerTrigger</string>
                </triggers>
                <parameters/>
            </org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction>
        </actions>
    <properties>
        <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
            <triggers>
                <hudson.triggers.TimerTrigger>
                    <spec>${cron!}</spec>
                </hudson.triggers.TimerTrigger>
            </triggers>
        </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>

        <hudson.model.ParametersDefinitionProperty>
            <parameterDefinitions>
                 <#list parameterMap?keys as key>
                     <hudson.model.StringParameterDefinition>
                         <name>${key}</name>
                         <description>${parameterMap[key]}</description>
                         <defaultValue></defaultValue>
                     </hudson.model.StringParameterDefinition>
                 </#list>
            </parameterDefinitions>
        </hudson.model.ParametersDefinitionProperty>
    </properties>
    </#if>
    <#if !cron?exists && !parameterMap?exists>
        <actions/>
        <properties/>
    </#if>
    <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps@2.48">
        <script>${script!}</script>
        <sandbox>true</sandbox>
    </definition>
    <triggers/>
    <disabled>false</disabled>
</flow-definition>