package vic.test.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features = "src/test/resources/features/apiTest.feature", glue = "vic.test.step", monochrome = true)
public class ApiRunner extends AbstractTestNGCucumberTests {

}