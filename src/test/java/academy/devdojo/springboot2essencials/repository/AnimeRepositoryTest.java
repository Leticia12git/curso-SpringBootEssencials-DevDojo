package academy.devdojo.springboot2essencials.repository;

import academy.devdojo.springboot2essencials.domain.Anime;
import academy.devdojo.springboot2essencials.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Teste para Anime epository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save create anime when Sucessful")
    void save_Persistence_whenSucessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved  = this.animeRepository.save(animeToBeSaved);
        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save update anime when Sucessful")
    void save_UpdatesAnime_whenSucessful(){
        Anime animeToBeSaved = AnimeCreator.createUpdateAnime();
        Anime animeSaved  = this.animeRepository.save(animeToBeSaved);
        Anime animeUpdate = this.animeRepository.save(animeSaved);
        Assertions.assertThat(animeUpdate).isNotNull();
        Assertions.assertThat(animeUpdate.getId()).isNotNull();
        Assertions.assertThat(animeUpdate.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Delete removes  anime when Sucessful")
    void delete_RemovesAnime_whenSucessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved  = this.animeRepository.save(animeToBeSaved);
        this.animeRepository.delete(animeSaved);
        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());
        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of anime when Sucessful")
    void findByName_ReturnsListOfAnime_whenSucessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved  = this.animeRepository.save(animeToBeSaved);
        String name = animeSaved.getName();
        List<Anime> animes  = this.animeRepository.findByName(name);
        Assertions.assertThat(animes).isNotEmpty();
        Assertions.assertThat(animes).contains(animeSaved);
    }

    @Test
    @DisplayName("Find by name returns empty list when no anime is found")
    void findByName_ReturnsEmptyListOfAnime_whenAnimeIsNotFound(){
        List<Anime> animes  = this.animeRepository.findByName("asdsfds");
        Assertions.assertThat(animes).isEmpty();
    }

}