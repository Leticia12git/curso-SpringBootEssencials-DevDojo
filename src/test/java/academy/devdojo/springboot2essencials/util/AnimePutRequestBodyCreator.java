package academy.devdojo.springboot2essencials.util;

import academy.devdojo.springboot2essencials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essencials.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody createAnimePutRequestBody() {
        return AnimePutRequestBody.builder()
                .id(AnimeCreator.createValidAnime().getId())
                .name(AnimeCreator.createValidAnime().getName())
                .build();
    }
}