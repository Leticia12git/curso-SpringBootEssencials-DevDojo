package academy.devdojo.springboot2essencials.util;

import academy.devdojo.springboot2essencials.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody createAnimPostRequestBody() {
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }
}