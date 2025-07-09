package sn.ept.git.seminaire.cicd.cucumber;

import org.junit.platform.suite.api.*;


@Suite(failIfNoTests = false)
@IncludeEngines(value = {"cucumber"})
@SelectClasspathResource("features")
@ConfigurationParameters({
        @ConfigurationParameter(key= CucumberRunner.GLUE_PROPERTY, value = CucumberRunner.GLUE_PROPERTY_VALUE),
        @ConfigurationParameter(key= CucumberRunner.NAMING_STRATEGY, value = CucumberRunner.NAMING_STRATEGY_VALUE),
        @ConfigurationParameter(key= CucumberRunner.PLUGIN_PROPERTY_NAME, value = CucumberRunner.PLUGIN_PROPERTY_NAME_VALUE),
        @ConfigurationParameter(key= CucumberRunner.PUBLISH_QUIET, value = CucumberRunner.PUBLISH_QUIET_VALUE)
})public class CucumberRunner {
        public static final String GLUE_PROPERTY="cucumber.glue";
        public static final String GLUE_PROPERTY_VALUE="sn.ept.git.seminaire.cicd.cucumber";

        public static final String NAMING_STRATEGY="cucumber.junit-platform.naming.strategy";
        public static final String NAMING_STRATEGY_VALUE="long";

        public static final String PUBLISH_QUIET="cucumber.publish.quiet";
        public static final String PUBLISH_QUIET_VALUE="false";

        public static final String PLUGIN_PROPERTY_NAME="cucumber.plugin";
        public static final String PLUGIN_PROPERTY_NAME_VALUE="pretty, html:target/cucumber-report/cucumber-html-report.html, json:target/cucumber-report/cucumber-json-report.json";
}