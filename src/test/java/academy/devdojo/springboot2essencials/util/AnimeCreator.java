package academy.devdojo.springboot2essencials.util;

import academy.devdojo.springboot2essencials.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("XPTO")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .id(1L)
                .name("XPTO")
                .build();
    }

    public static Anime createUpdateAnime(){
        return Anime.builder()
                .name("XPTO 2")
                .id(1L)
                .build();
    }
}
