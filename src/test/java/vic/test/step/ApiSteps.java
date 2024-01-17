package vic.test.step;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.core.StringContains;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ApiSteps {
    public int a;
    public int b;
    public Response res;


    @Given("I provide a = {int}")
    public void i_provide_a(int a) {
        this.a = a;
    }

    @Given("I provide b = {even}")
    public void i_provide_b(Even b) {
        this.b = b.getI();
    }

    @ParameterType("2|4|6|8|10")
    public Even even(String i) {
        return new Even(i);
    }

    @When("I call the add function")
    public void i_call_the_add_function() {
        // https://www.bilibili.com/video/BV1nM4y197S9 参考视频
        res = given()
                .queryParam("a", a)
                .queryParam("b", b)
                .when().get("http://127.0.0.1:8080/api/add");
//                .when().get("https://4cc8ea55-9e44-4439-9cd1-2ce22e2fdf44.mock.pstmn.io/api/add");

    }

    @Then("I should get the result of a + b = {int}")
    public void i_should_get_the_result_of_a_b(int r) {
        res.then().log().all()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", new StringContains("application/json"))
                .body("input", equalTo("2+6"))
                .body("debug", nullValue())
                .body("result", equalTo(a + b));
    }

    @Then("show {} end")
    public void show_any_End(String str) {
        System.out.println(str);
    }

    @When("I call the json api")
    public void iCallTheJsonApi() {
        res = given()
                .when()
                .get("http://127.0.0.1:8080/api/json");
    }

    @Then("I can see the max value in the number of winnerId:{int} is {int}")
    public void iCanSeeTheMaxValueInTheNumberOfWinnerIdIs(int arg0, int arg1) {
        res.then()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", new StringContains("application/json"))
                .body(String.format("lotto.winners.find{it.winnerId==%s}.numbers.max()", arg0), equalTo(arg1));
    }

    @When("I call the xpath api")
    public void iCallTheXpathApi() {
        res = given()
                .when()
                .get("http://127.0.0.1:8080/api/xpath");
    }

    @Then("I can see the quantity of {string} in {string} category is {int}")
    public void iCanSeeTheQuantityOfInCategoryIs(String arg0, String arg1, Integer arg2) {
        res.then()
                .assertThat()
                .statusCode(200)
                .header("Content-Type", new StringContains("application/xml"))
                .body(String.format("shopping.category.find { it.@type == '%s'}.item.find{ it == '%s'}.@quantity", arg1, arg0), equalTo(arg2.toString()));
    }

    @And("I can see {string} is {string}")
    public void iCanSeeIs(String arg0, String arg1) {
        res.then()
                .assertThat()
                .body(String.format("**.find { it.@when == '%s'}", arg0), equalTo(arg1));
    }

    @And("I can see {string} has {string}")
    public void iCanSeeHas(String arg0, String arg1) {
        String[] items = arg1.split("\\|");
        res.then()
                .assertThat()
                .body(String.format("*.category.find { it.@type == '%s'}", arg0), hasItems(items));
    }
}
