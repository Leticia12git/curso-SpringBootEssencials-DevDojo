package academy.devdojo.springboot2essencials.integration;

import academy.devdojo.springboot2essencials.domain.Anime;
import academy.devdojo.springboot2essencials.repository.AnimeRepository;
import academy.devdojo.springboot2essencials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essencials.service.AnimeService;
import academy.devdojo.springboot2essencials.util.AnimeCreator;
import academy.devdojo.springboot2essencials.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2essencials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;

    @InjectMocks
    private AnimeRepository animeRepository;
    @Mock
    private AnimeService animeServiceMock;

    @Test
    @DisplayName("List returns list of anime inside page object when sucessful\"")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSucessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectdName = savedAnime.getName();
        PagebleResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectdName);
    }

    @Test
    @DisplayName("ListAll returns list of anime when sucessful")
    void listAll_ReturnsListOfAnimes_WhenSucessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectdName = savedAnime.getName();
        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectdName);
    }

    @Test
    @DisplayName("FindById returns anime when sucessful")
    void findById_ReturnsAnimes_WhenSucessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved())
        Long expectdId = savedAnime.getId();
        Anime anime = testRestTemplate.getForObject("/animes/id", Anime.class, expectdId);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectdId);
    }

    @Test
    @DisplayName("findByName returns list of anime when sucessful")
    void findByName_ReturnsListOfAnimes_WhenSucessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved())
        String expectdName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectdName);
        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes.get(0).getName()).isNotNull().isEqualTo(expectdName);
    }


    @Test
    @DisplayName("findByName returns an empty  list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnimes_WhenAnimeIsNotFound(){
        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save returns anime when sucessful")
    void save_ReturnsAnimes_WhenSucessful(){
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimPostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates anime when sucessful")
    void replace_UpdatesAnimes_WhenSucessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        savedAnime.setName();
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimPostRequestBody();
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes",
                HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName(" delete removes anime when sucessful")
    void delete_ReturnsListOfAnimes_WhenSucessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimPostRequestBody();
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE, null, Void.class, savedAnime.getId());
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
