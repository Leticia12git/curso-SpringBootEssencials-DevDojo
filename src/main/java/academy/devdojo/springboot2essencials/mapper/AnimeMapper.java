package academy.devdojo.springboot2essencials.mapper;

import academy.devdojo.springboot2essencials.domain.Anime;
import academy.devdojo.springboot2essencials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essencials.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "string")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
    public abstract  Anime toAnime (AnimePostRequestBody animePostRequestBody);
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
