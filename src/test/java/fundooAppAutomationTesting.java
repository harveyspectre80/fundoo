import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.post;

public class fundooAppAutomationTesting {
    @Test
    public void givenUser_OnPostRegistration_ShouldReturnRegisteredValue() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "shikamaru1@gmail.com");
        jsonObject.put("password", "123456");
        request.body(jsonObject.toJSONString());
        Response response = request.post("https://fundoopush-backend-dev.bridgelabz.com/registration");
        int code = response.getStatusCode();
        Assert.assertEquals(201, code);
    }

    @Test
    public void givenUser_OnPostRegistration_ShouldReturnErrorForEmptyPassword() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "shikamaru1@gmail.com");
        jsonObject.put("password", "");
        request.body(jsonObject.toJSONString());
        Response response = request.post("https://fundoopush-backend-dev.bridgelabz.com/registration");
        int code = response.getStatusCode();
        Assert.assertEquals(201, code);
    }

    @Test
    public void givenUser_OnPostRegistration_ShouldReturnErrorForEmptyUsername() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "");
        jsonObject.put("password", "123456");
        request.body(jsonObject.toJSONString());
        Response response = request.post("https://fundoopush-backend-dev.bridgelabz.com/registration");
        int code = response.getStatusCode();
        Assert.assertEquals(400, code);
    }

    @Test
    public void givenEmployee_OnLogin_ShouldLoginUserAndReturnValidUserCode() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "shikamaru@gmail.com");
        jsonObject.put("password", "123456");
        request.body(jsonObject.toJSONString());
        Response response = request.post("https://fundoopush-backend-dev.bridgelabz.com/login");
        int code = response.getStatusCode();
        Assert.assertEquals(200, code);
    }

    @Test
    public void givenEmployee_OnLogout_ShouldReturnLogoutUserAndReturnCurnCorrectStatusCode() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7Il9pZCI6IjVlMDlkZmE4NGQyMjY3MDAzMjUzMGZkMCJ9LCJpYXQiOjE1Nzc3MDU4NjQsImV4cCI6MTU3Nzc5MjI2NH0.Sn0bRz-1rSKIo2qzwCp2_UPP1dPYXBFjM7XXrygHlh4")
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/logout");
        int code = response.getStatusCode();
        Assert.assertEquals(200,code);
    }

    @Test
    public void givenAllDetailsForForm_ShouldSubmitFormWithCorrectParameters() throws ParseException {
        File testUploadFile = new File("/home/admin1/Pictures/Screenshot from 2019-12-26 18-52-05.png");
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7Il9pZCI6IjVlMDlkZjEyNGQyMjY3MDAzMjUzMGZjYyJ9LCJpYXQiOjE1Nzc3Njc3NjMsImV4cCI6MTU3Nzg1NDE2M30.eTC8a7jKE1SKSQG_McwdOEVWlrgrNVs7dQP3vJHPMtk")
                .multiPart("image",testUploadFile)
                .formParam("title", "sample ")
                .formParam("description", "gawaw arf")
                .formParam("redirect_link", "www.google.com")
                .formParam("is_published",false)
                .formParam("archive", false)
                .formParam("youtube_flag", false)
                .formParam("youtube_url",false)
                .formParam("video-link",false)
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/redirects");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("Redirect added Successfully", message);
        Assert.assertEquals(201, statusCode);
    }
}
