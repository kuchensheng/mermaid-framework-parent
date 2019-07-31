# ${jobName}.job

type=${type}

<#list commands as c>
    <#if c_index == 0>
command=${c}
    <#else>
command.${c_index}=${c}
    </#if>

</#list>
<#if dependencies ??>
dependencies=${dependencies}
</#if>

