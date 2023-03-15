package academy.devdojo.springboot2essencials.service;

import academy.devdojo.springboot2essencials.domain.Anime;
import academy.devdojo.springboot2essencials.exception.BadRequestException;
import academy.devdojo.springboot2essencials.repository.AnimeRepository;
import academy.devdojo.springboot2essencials.util.AnimeCreator;
import academy.devdojo.springboot2essencials.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2essencials.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any());



    }
    @Test
    @DisplayName("ListAll returns list of anime inside page object when sucessful\"")
    void listAll_ReturnsListOfAnimesInsidePageObject_WhenSucessful(){
        String expectdName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectdName);
    }

    @Test
    @DisplayName("ListAll returns list of anime when sucessful")
    void listAllNonPageable_ReturnsListOfAnimes_WhenSucessful(){
        String expectdName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.listAllNonPageable();
        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectdName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when sucessful")
    void findByIdOrThrowBadRequestException_ReturnsAnimes_WhenSucessful(){
        Long expectdId = AnimeCreator.createValidAnime().getId();
        Anime anime = animeService.findByIdOrThrowBadRequestException(1);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectdId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException returns anime when anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("findByName returns list of anime when sucessful")
    void findByName_ReturnsListOfAnimes_WhenSucessful(){
        String expectdName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.findByName("anime");
        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectdName);
    }


    @Test
    @DisplayName("findByName returns an empty  list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnimes_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        List<Anime> animes = animeService.findByName("anime");
        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    @DisplayName("Save returns anime when sucessful")
    void save_ReturnsAnimes_WhenSucessful(){
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimPostRequestBody());
        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("replace updates anime when sucessful")
    void replace_UpdatesAnimes_WhenSucessful(){
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName(" delete removes anime when sucessful")
    void delete_ReturnsListOfAnimes_WhenSucessful() {
        Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();
    }

}