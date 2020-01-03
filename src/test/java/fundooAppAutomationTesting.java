import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

public class fundooAppAutomationTesting {
    private String tokenValue;

    @Before
    public void setUp() throws Exception {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .body("{\"email\" : \"shikamaru@gmail.com\", \"password\" : \"123456\"}")
                .post("https://fundoopush-backend-dev.bridgelabz.com/login");
        String resAsString = response.asString();
        JsonPath jsonPath = new JsonPath(resAsString);
        tokenValue = jsonPath.getString("token");
    }


    @Test
    public void givenUser_OnPostRegistration_ShouldReturnRegisteredValue() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "shikamaru@gmail.com");
        jsonObject.put("password", "123456");
        request.body(jsonObject.toJSONString());
        Response response = request.post("https://fundoopush-backend-dev.bridgelabz.com/registration");
        int code = response.getStatusCode();
        Assert.assertEquals(409, code);
    }

    @Test
    public void givenUser_OnPostRegistration_ShouldReturnErrorForEmptyPassword() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "shikamaru@gmail.com");
        jsonObject.put("password", "");
        request.body(jsonObject.toJSONString());
        Response response = request.post("https://fundoopush-backend-dev.bridgelabz.com/registration");
        int code = response.getStatusCode();
        Assert.assertEquals(400, code);
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
    public void givenUser_OnLogin_ShouldLoginUserAndReturnValidUserCode() {
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
                .header("token", tokenValue)
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/logout");
        int code = response.getStatusCode();
        Assert.assertEquals(200, code);
    }

    @Test
    public void givenAllDetailsForForm_ShouldSubmitFormWithCorrectParameters() throws ParseException {
        File testUploadFile = new File("/home/admin1/Pictures/Screenshot from 2019-12-26 18-52-05.png");
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .multiPart("image", testUploadFile)
                .formParam("title", "sample ")
                .formParam("description", "gawaw arf")
                .formParam("redirect_link", "www.google.com")
                .formParam("is_published", false)
                .formParam("archive", false)
                .formParam("youtube_flag", false)
                .formParam("youtube_url", false)
                .formParam("video-link", false)
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

    @Test
    public void givenAllDetailsForForm_ShouldGiveErrorForForm() throws ParseException {
        File testUploadFile = new File("/home/admin1/Pictures/Screenshot from 2019-12-26 18-52-05.png");
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .multiPart("image", testUploadFile)
                .formParam("title", "sample ")
                .formParam("description", "gawaw arf")
                .formParam("redirect_link", "www.google.com")
                .formParam("is_published", false)
                .formParam("archive", false)
                .formParam("youtube_flag", false)
                .formParam("youtube_url", false)
                .formParam("video-link", false)
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/redirects");
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        String message = (String) Object.get("message");
        Assert.assertEquals("Redirect added Successfully", message);
    }

    @Test
    public void givenAllDetailsForForm_ShouldSubmitFormWithoutCorrectParameters() throws ParseException {
        File testUploadFile = new File("/home/admin1/Pictures/Screenshot from 2019-12-26 18-52-05.png");
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .multiPart("image", testUploadFile)
                .formParam("title", "sample ")
                .formParam("description", "gawaw arf")
                .formParam("redirect_link", "www.google.com")
                .formParam("is_published", false)
                .formParam("archive", false)
                .formParam("youtube_flag", false)
                .formParam("youtube_url", false)
                .formParam("video-link", false)
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/redirects");
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        String message = (String) Object.get("message");
        Assert.assertEquals("Redirect added Successfully", message);
    }
    @Test
    public void givenCorrectId_ShouldUpdateRedirectWithCorrectParameters() throws ParseException {
        File testUploadFile = new File("/home/admin1/Pictures/Screenshot from 2019-12-26 18-52-05.png");
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .formParam("_id", "5e0ad3a94d2267003253100e")
                .multiPart("image", testUploadFile)
                .formParam("title", "sample1")
                .formParam("description", "gawaw arfsa")
                .formParam("redirect_link", "www.youtube.com")
                .formParam("is_published", false)
                .formParam("archive", false)
                .formParam("youtube_flag", false)
                .formParam("youtube_url", false)
                .formParam("video-link", false)
                .when()
                .put("https://fundoopush-backend-dev.bridgelabz.com/redirects");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("Redirect updated Successfully", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenToken_ShouldRetrieveAllRedirectstWithCorrectParameters() throws ParseException {
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .when()
                .get("https://fundoopush-backend-dev.bridgelabz.com/redirects");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("All Redirects retrieved Successfully", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenToken_ShouldDeleteAllRedirects() throws ParseException {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .when()
                .body("{\"_id\":\"5e0ad3a94d2267003253100e\"}")
                .post("https://fundoopush-backend-dev.bridgelabz.com/redirects/delete");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("Redirect deleted Successfully", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenNoParameters_ShouldRetrieveAllBridgelabzRedirects() throws ParseException {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("https://fundoopush-backend-dev.bridgelabz.com/bl-redirects");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("All Redirects retrieved Successfully", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenToken_ShouldDoHashtagEdits() throws ParseException {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .when()
                .body("{\"redirect_id\":\"5e0ad3a94d2267003253100e\",\"hashtag\":\"#bridgelabz #solutions #mumbai #bangalore #fundoopush\"}")
                .post("https://fundoopush-backend-dev.bridgelabz.com/hashtag/edit");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("Hashtag edit done Successfully", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenTokenAndHashtag_ShouldsendHashtag() throws ParseException {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("token", tokenValue)
                .pathParam("hashtagname", " #bangalore")
                .when()
                .get("https://fundoopush-backend-dev.bridgelabz.com/redirects/hashtag/{hashtagname}");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("Hashtag sent Successfully", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenTokenAndURL_ShouldScrapWebData() throws ParseException {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .body("{\"url\":\"https://www.deccanchronicle.com/technology/in-other-news/270319/companies-that-are-changing-the-way-education-is-being-delivered-to-st.html\"}")
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/web-scraping");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("Successfully scrapped data", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenTokenAndHashtag_ShouldSearchHashtag() throws ParseException {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token", tokenValue)
                .body("{\"hashtag\":\"#bridgelabz\"}")
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/search/hashtag");
        int statusCode = response.getStatusCode();
        ResponseBody body = response.getBody();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        boolean status = (boolean) Object.get("status");
        String message = (String) Object.get("message");
        Assert.assertTrue(status);
        Assert.assertEquals("Successfully searched data", message);
        Assert.assertEquals(200, statusCode);
    }

    @Test
    public void givenTokenAndUserDetails_ShouldAddJob() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("redirect_id", "5e0d8a173b17ce008e85dc27");
        jsonObject.put("years_of_experience", 2);
        jsonObject.put("salary", 3.6);
        jsonObject.put("location", "Pune");
        jsonObject.put("company_profile", "Automation");
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token",tokenValue)
                .body(jsonObject.toJSONString())
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/jobs");
        int status = response.getStatusCode();
        String string = response.asString();
        System.out.println(string);
        MatcherAssert.assertThat(status, Matchers.equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void givenTokenAndJobId_ShouldAddHashtagForJob() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("job_id", "5e0ec0ad3a63df0032a2f496");
        jsonObject.put("hashtag", "#mumbai");
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token",tokenValue)
                .body(jsonObject.toJSONString())
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/jobs/hashtag/add");
        int status = response.getStatusCode();
        String string = response.asString();
        System.out.println(string);
        MatcherAssert.assertThat(status, Matchers.equalTo(HttpStatus.SC_OK));
        ResponseBody body = response.getBody();
    }

    @Test
    public void givenTokenHashtagIdJobId_ShouldRemovehashtagForJob() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("job_id", "5e0d8fe33b17ce008e85dc4a");
        jsonObject.put("hashtag_id", "5d39926ab19c56004f263df6");
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("token",tokenValue)
                .body(jsonObject.toJSONString())
                .when()
                .post("https://fundoopush-backend-dev.bridgelabz.com/jobs/hashtag/remove\n");
        int status = response.getStatusCode();
        String string = response.asString();
        System.out.println(string);
        MatcherAssert.assertThat(status, Matchers.equalTo(HttpStatus.SC_OK));
        ResponseBody body = response.getBody();
    }
}
