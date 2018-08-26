<hudson.model.ListView>
    <name>${viewName}</name>
    <filterExecutors>false</filterExecutors>
    <filterQueue>false</filterQueue>
    <properties class="hudson.model.View$PropertyList"/>
    <jobNames>
        <comparator class="hudson.util.CaseInsensitiveComparator"/>
        <#if views?exists &&  views?size &gt; 0>
            <#list views as v>
                <string>${v}</string>
            </#list>
        </#if>

    </jobNames>
    <jobFilters/>
    <columns>
        <hudson.views.StatusColumn/>
        <hudson.views.WeatherColumn/>
        <hudson.views.JobColumn/>
        <hudson.views.LastSuccessColumn/>
        <hudson.views.LastFailureColumn/>
        <hudson.views.LastDurationColumn/>
        <hudson.views.BuildButtonColumn/>
        <hudson.plugins.favorite.column.FavoriteColumn plugin="favorite@2.3.1"/>
    </columns>
    <recurse>false</recurse>
</hudson.model.ListView>