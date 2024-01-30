package vic.test.step;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.core.StringContains;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import com.typesafe.config.*;

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
        res.then().log().all();

    }

    @Then("I should get the result of a + b = {int}")
    public void i_should_get_the_result_of_a_b(int r) {
        res.then().assertThat()
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
        res.then().log().all();

    }

    @Then("I can see the max value in the number of winnerId:{int} is {int}")
    public void iCanSeeTheMaxValueInTheNumberOfWinnerIdIs(int arg0, int arg1) {
        res.then().assertThat()
                .statusCode(200)
                .header("Content-Type", new StringContains("application/json"))
                .body(String.format("lotto.winners.find{it.winnerId==%s}.numbers.max()", arg0), equalTo(arg1));
    }

    @When("I call the xpath api")
    public void iCallTheXpathApi() {
        res = given()
                .when()
                .get("http://127.0.0.1:8080/api/xpath");
        res.then().log().all();
    }

    @Then("I can see the quantity of {string} in {string} category is {int}")
    public void iCanSeeTheQuantityOfInCategoryIs(String arg0, String arg1, Integer arg2) {
        res.then().assertThat()
                .statusCode(200)
                .header("Content-Type", new StringContains("application/xml"))
                .body(String.format("shopping.category.find { it.@type == '%s'}.item.find{ it == '%s'}.@quantity", arg1, arg0), equalTo(arg2.toString()));
    }

    @And("I can see {string} is {string}")
    public void iCanSeeIs(String arg0, String arg1) {
        res.then().assertThat()
                .body(String.format("**.find { it.@when == '%s'}", arg0), equalTo(arg1));
    }

    @And("I can see {string} has {string}")
    public void iCanSeeHas(String arg0, String arg1) {
        String[] items = arg1.split("\\|");
        res.then().assertThat()
                .body(String.format("*.category.find { it.@type == '%s'}", arg0), hasItems(items));
    }

    @And("I can see the max value of winnerId")
    public void iCanSeeTheMaxValueOfWinnerId(List<Map<String, String>> list)  {
        for (Map<String, String> map: list) {
            iCanSeeTheMaxValueInTheNumberOfWinnerIdIs(Integer.parseInt(map.get("winnerId")), Integer.parseInt(map.get("max")));
        }
    }

    private void verifyExpectedFields(Config config, Response res) {
        Config verifyItems = config.getConfig("expectedFields");
        JsonPath jsonPath = res.jsonPath().using(new JsonPathConfig(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
        StringBuilder sb = new StringBuilder();
        verifyItems.root().forEach((key, value) -> {
            try {
                switch (value.valueType()) {
                    case NUMBER:
                        verifyExpectedFieldsNumber(key, value, jsonPath);
                        break;
                    case NULL:
                        assertThat(jsonPath.get(key), is(nullValue()));
                        break;
                    case BOOLEAN:
                        assertThat(jsonPath.get(key), not(nullValue()));
                        assertThat(jsonPath.getBoolean(key), is(equalTo(value.unwrapped())));
                        break;
                    case LIST:
                        assertThat(jsonPath.getList(key), is(equalTo(value.unwrapped())));
                        break;
                    case OBJECT:
                        assertThat(jsonPath.get(key), is(equalTo(value.unwrapped())));
                        break;
                    default:
                        assertThat(jsonPath.getString(key), is(equalTo(value.unwrapped())));
                }
            }
            catch (AssertionError e) {
                sb.append("\n====================\n");
                sb.append(key);
                sb.append(e.getMessage());
            } catch (IllegalArgumentException e) {
                sb.append(String.format("%s not a available path\n", key));
                sb.append(e.getMessage());
                sb.append("\n====================\n");
            }
        });
        if (!"".equals(sb.toString())) {
            throw new AssertionError(sb.toString());
        }
    }

    private void verifyExpectedFieldsNumber(final String key, final ConfigValue value, final JsonPath jsonPath) {
        String sValue;
        try {
            Method method = value.getClass().getDeclaredMethod("transformToString");
            method.setAccessible(true);
            sValue = (String) method.invoke(value);
        } catch (Exception e) {
            sValue = value.unwrapped().toString();
        }

        Object jsonValue = jsonPath.get(key);
        try {
            Object exceptValue = sValue;
            if (jsonValue instanceof BigDecimal) {
                exceptValue = new BigDecimal(sValue);
            } else if (jsonValue instanceof Integer) {
                exceptValue = Integer.parseInt(sValue);
            } else if (jsonValue instanceof Double) {
                exceptValue = Double.parseDouble(sValue);
            }
            assertThat(jsonValue , is(equalTo(exceptValue)));
        } catch (NumberFormatException e) {
            assertThat(jsonValue, is(equalTo(sValue)));
        }
    }

    @And("I can see the except result in response body")
    public void iCanSeeTheExceptResultInResponseBody() {
        String resourceBasename = "src/test/resources/config/api-verify.conf";
        Config conf = ConfigFactory.parseFile(new File(resourceBasename));
        verifyExpectedFields(conf.getConfig("payload"), res);
    }
}
