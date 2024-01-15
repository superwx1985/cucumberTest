package vic.test.step;

import io.cucumber.java.en.*;

public class ApiSteps {
    public int a;
    public int b;


    @Given("I provide a = {int}")
    public void i_provide_a(int a) {
        this.a = a;
    }

    @Given("I provide b = {int}")
    public void i_provide_b(int b) {
        this.b = b;
    }

    @When("I call the add function")
    public void i_call_the_add_function() {

    }

    @Then("I should get the result of a + b = {int}")
    public void i_should_get_the_result_of_a_b(int r) {
        if (r==a+b) {
            System.out.println("Passed");
        } else {
            System.out.println("Failed");
            throw new RuntimeException();
        }
    }

}
