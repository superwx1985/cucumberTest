package vic.test.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features = {"test/resources/features/uiTest.feature",},
        glue = "vic.test.step", monochrome = true)
public class UiRunner extends AbstractTestNGCucumberTests {

}