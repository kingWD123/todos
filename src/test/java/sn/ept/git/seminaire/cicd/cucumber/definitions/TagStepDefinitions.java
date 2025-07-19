package sn.ept.git.seminaire.cicd.cucumber.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import sn.ept.git.seminaire.cicd.entities.Tag;
import sn.ept.git.seminaire.cicd.models.TagDTO;
import sn.ept.git.seminaire.cicd.repositories.TagRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class TagStepDefinitions {
    private static final String BASE_URI = "http://localhost";
    private static final String API_PATH = "/cicd/api/tags";

    @LocalServerPort
    private int port;

    @Autowired
    private TagRepository tagRepository;

    private String tagName;
    private String newTagName;
    private Response response;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected RequestSpecification request() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        return given().contentType(ContentType.JSON).log().all();
    }

    @Before
    public void cleanTags() {
        tagRepository.deleteAll();
    }

    @Given("l'utilisateur est authentifié")
    public void utilisateurEstAuthentifie() {}

    @Given("plusieurs tags existent")
    public void plusieursTagsExistent(DataTable dataTable) {
        List<String> names;
        if (dataTable != null && !dataTable.isEmpty()) {
            names = dataTable.asList(String.class);
        } else {
            names = List.of("Important", "Travail", "Perso");
        }
        List<Tag> tags = names.stream()
            .map(name -> Tag.builder().id(UUID.randomUUID().toString()).name(name).build())
            .collect(Collectors.toList());
        tagRepository.saveAllAndFlush(tags);
    }

    @Given("un tag nommé {string} existe")
    public void unTagNommeeExiste(String name) {
        Tag tag = Tag.builder().id(UUID.randomUUID().toString()).name(name).build();
        tagRepository.saveAndFlush(tag);
    }

    @When("il crée un tag nommé {string}")
    public void ilCreeUnTagNommee(String name) {
        TagDTO dto = TagDTO.builder().name(name).build();
        response = request().body(dto).when().post(API_PATH);
        tagName = name;
    }

    @When("l'utilisateur consulte la liste des tags")
    public void utilisateurConsulteListeTags() {
        response = request().when().get(API_PATH);
    }

    @When("l'utilisateur renomme le tag {string} en {string}")
    public void utilisateurRenommeTag(String oldName, String newName) {
        Optional<Tag> optionalTag = tagRepository.findByName(oldName);
        Assertions.assertThat(optionalTag).isPresent();
        String id = optionalTag.get().getId();
        TagDTO dto = TagDTO.builder().name(newName).build();
        response = request().body(dto).when().put(API_PATH + "/" + id);
        tagName = oldName;
        newTagName = newName;
    }

    @When("l'utilisateur supprime le tag {string}")
    public void utilisateurSupprimeTag(String name) {
        Optional<Tag> optionalTag = tagRepository.findByName(name);
        Assertions.assertThat(optionalTag).isPresent();
        String id = optionalTag.get().getId();
        response = request().when().delete(API_PATH + "/" + id);
        tagName = name;
    }

    @Then("le tag {string} doit exister dans la liste des tags")
    public void tagDoitExisterDansListe(String name) {
        response = request().when().get(API_PATH);
        List<String> names = response.jsonPath().getList("content.name");
        Assertions.assertThat(names).contains(name);
    }

    @Then("il voit tous les tags existants")
    public void ilVoitTousLesTagsExistants(DataTable dataTable) {
        List<String> expected = dataTable.asList(String.class);
        List<String> names = response.jsonPath().getList("content.name");
        Assertions.assertThat(names).containsAll(expected);
    }

    @Then("le tag {string} doit exister et {string} ne doit plus exister")
    public void tagRenommeExisteEtAncienNExistePlus(String newName, String oldName) {
        response = request().when().get(API_PATH);
        List<String> names = response.jsonPath().getList("content.name");
        Assertions.assertThat(names).contains(newName).doesNotContain(oldName);
    }

    @Then("le tag {string} ne doit plus exister dans la liste des tags")
    public void tagNeDoitPlusExisterDansListe(String name) {
        response = request().when().get(API_PATH);
        List<String> names = response.jsonPath().getList("content.name");
    
        // Si la liste est null (pas de "content" dans la réponse), on la traite comme vide
        if (names == null || names.isEmpty()) {
            // Rien à vérifier, le tag ne peut pas exister
            Assertions.assertThat(names).isNullOrEmpty();
        } else {
            // Vérifie explicitement que le nom ne figure pas dans la liste
            Assertions.assertThat(names).doesNotContain(name);
        }
    }
    
}
