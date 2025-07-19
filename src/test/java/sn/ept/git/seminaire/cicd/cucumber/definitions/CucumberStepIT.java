package sn.ept.git.seminaire.cicd.cucumber.definitions;
    
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import sn.ept.git.seminaire.cicd.data.TestData;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.repositories.TodoRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CucumberStepIT {

    private final static String BASE_URI = "http://localhost";
    public static final String API_PATH = "/cicd/api/todos";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE_DEBUT = "date_debut";
    public static final String KEY_DATE_FIN = "date_fin";
    public static final String KEY_SYSTEM_ID = "system_id";
    public static final String KEY_SYSTEM_NAME = "system_name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE = "message";

    @LocalServerPort
    private int port;

    @Autowired
    private TodoRepository todoRepository;

    private String title;
    private String description;
    private Response response;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;


    @BeforeAll
    public static void beforeAll() {
        objectMapper.findAndRegisterModules();
    }

    @Before
    public void init() {
        todoRepository.deleteAll();
        dateDebut = TestData.Default.dateDebut;
        dateFin = TestData.Default.dateFin;
    }

    protected RequestSpecification request() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        return given()
                .contentType(ContentType.JSON)
                .log()
                .all();
    }


    @Given("table todo contains data:")
    public void tableTodoContainsData(DataTable dataTable) {
        List<Todo> todosList = dataTable
                .asMaps(String.class, String.class)
                .stream()
                .map(line -> Todo
                        .builder()
                        .id(line.get(KEY_ID))
                        .title(line.get(KEY_TITLE))
                        .description(line.get(KEY_DESCRIPTION))
                        .completed(line.get(KEY_COMPLETED).equals("true"))
                        .version(0)
                        .dateDebut(convertToLocalDateTime(line.get(KEY_DATE_DEBUT)))
                        .dateFin(convertToLocalDateTime(line.get(KEY_DATE_FIN)))
                        .createdDate(TestData.Default.createdDate)
                        .lastModifiedDate(TestData.Default.lastModifiedDate)
                        .build()
                ).collect(Collectors.toUnmodifiableList());
        todoRepository.saveAllAndFlush(todosList);
    }


    @When("call find by id with id={string}")
    public void callFindByIdWithId(String id) {
        response = request()
                .when()
                .get(API_PATH + "/" + id);
    }


    @Then("the http status is {int}")
    public void theHttpStatusIs(int status) {
        response.then()
                .assertThat()
                .statusCode(status);
    }

    @Given("the returned todo has following properties:")
    public void returnedTodoHasProperties(DataTable dataTable) {
        Optional<Map<String, String>> optional = dataTable
                .asMaps(String.class, String.class)
                .stream()
                .findFirst();
        Assertions.assertThat(optional).isPresent();
        Map<String, String> line = optional.get();
        response.then()
                .assertThat()
                .body(KEY_TITLE, CoreMatchers.equalTo(line.get(KEY_TITLE)))
                .body(KEY_DESCRIPTION, CoreMatchers.equalTo(line.get(KEY_DESCRIPTION)))
                .body(KEY_COMPLETED, CoreMatchers.equalTo(Boolean.valueOf(line.get(KEY_COMPLETED))))
                .body(KEY_DATE_DEBUT, CoreMatchers.equalTo(line.get(KEY_DATE_DEBUT)))
                .body(KEY_DATE_FIN, CoreMatchers.equalTo(line.get(KEY_DATE_FIN)));
    }

    @When("call find all  with page={int}, size={int} and sort={string}")
    public void callFindAllWithPageSizeAndSorting(int page, int size, String sort) {
        response = request().contentType(ContentType.JSON)
                .log()
                .all()
                .when().get(API_PATH + "?page=%s&size=%s&%s".formatted(page, size, sort));
    }

    @When("call delete with id={string}")
    public void callDeleteWithId(String id) {
        response = request()
                .when()
                .delete(API_PATH + "/" + id);
    }

    @When("call complete with id={string}")
    public void callCompleteWithId(String id) {
        response = request()
                .when()
                .get(API_PATH + "/" + id + "/complete");
    }

    @When("call add todo")
    public void callAddTodo() {
        TodoDTO requestBody = TodoDTO
                .builder()
                .title(this.title)
                .description(this.description)
                .dateDebut(this.dateDebut)
                .dateFin(this.dateFin)
                .build();
        response = request()
                .body(requestBody)
                .when().post(API_PATH);
    }

    @When("call update todo with id={string}")
    public void callUpdateTodoWithIdAndTitleAndDescription(String id) {
        TodoDTO requestBody = TodoDTO
                .builder()
                .title(this.title)
                .description(this.description)
                .dateDebut(this.dateDebut)
                .dateFin(this.dateFin)
                .build();
        response = request()
                .body(requestBody)
                .when()
                .put(API_PATH + "/" + id);
    }


    @And("the returned page has following content:")
    public void theReturnedListHasFollowingData(DataTable dataTable) {
        List<Map<String, String>> maps = dataTable.asMaps(String.class, String.class);
        Assertions.assertThat(maps.size())
                .isEqualTo(response.jsonPath().getList("content").size());
        maps.forEach(line -> response.then().assertThat()
                .body(getKeyFromPageContent(KEY_TITLE), Matchers.hasItem(line.get(KEY_TITLE)))
                .body(getKeyFromPageContent(KEY_DESCRIPTION), Matchers.hasItem(line.get(KEY_DESCRIPTION)))
                .body(getKeyFromPageContent(KEY_COMPLETED), Matchers.hasItem(Boolean.valueOf(line.get(KEY_COMPLETED))))
                .body(getKeyFromPageContent(KEY_DATE_DEBUT), Matchers.hasItem(line.get(KEY_DATE_DEBUT)))
                .body(getKeyFromPageContent(KEY_DATE_FIN), Matchers.hasItem(line.get(KEY_DATE_FIN))));
    }

    @And("the returned page has no content")
    public void theReturnedPageHasNoContent() {
        Assertions.assertThat(response.jsonPath().getList("content")).isEmpty();
    }

    @And("the returned error body looks like:")
    public void theErrorBodyIsLike(DataTable dataTable) {
        Optional<Map<String, String>> optional = dataTable
                .asMaps(String.class, String.class)
                .stream()
                .findFirst();
        Assertions.assertThat(optional).isPresent();
        Map<String, String> line = optional.get();
        response.then()
                .assertThat()
                .body(KEY_SYSTEM_ID, CoreMatchers.equalTo(line.get(KEY_SYSTEM_ID)))
                .body(KEY_SYSTEM_NAME, CoreMatchers.equalTo(line.get(KEY_SYSTEM_NAME)))
                .body(KEY_TYPE, CoreMatchers.equalTo(line.get(KEY_TYPE)))
                .body(KEY_STATUS, CoreMatchers.equalTo(Integer.parseInt(line.get(KEY_STATUS))))
                .body(KEY_MESSAGE, CoreMatchers.equalTo(line.get(KEY_MESSAGE)));
    }

    @And("the following todo to add:")
    public void theFollowingTodoToAdd(DataTable dataTable) {
        this.theFollowingTodoToUpdate(dataTable);
    }

    @And("the following todo to update:")
    public void theFollowingTodoToUpdate(DataTable dataTable) {
        Optional<Map<String, String>> optional = dataTable
                .asMaps(String.class, String.class)
                .stream()
                .findFirst();
        Assertions.assertThat(optional).isPresent();
        Map<String, String> line = optional.get();
        this.title = formatNullable(line.get(KEY_TITLE));
        this.description = formatNullable(line.get(KEY_DESCRIPTION));
        this.dateDebut = convertToLocalDateTime(formatNullable(line.get(KEY_DATE_DEBUT)));
        this.dateFin = convertToLocalDateTime(formatNullable(line.get(KEY_DATE_FIN)));
    }

    private String getKeyFromPageContent(String key) {
        return "content*.%s".formatted(Optional.ofNullable(key).orElse("?"));
    }

    private LocalDateTime convertToLocalDateTime(String date) {
        return StringUtils.isBlank(date) ? null : LocalDateTime.parse(date);
    }

    private String formatNullable(String value) {
        return StringUtils.isNotBlank(value) && value.trim().toLowerCase().equals("null") ? null : value;
    }


}