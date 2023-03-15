package academy.devdojo.springboot2essencials.service;

import academy.devdojo.springboot2essencials.domain.Anime;
import academy.devdojo.springboot2essencials.exception.BadRequestException;
import academy.devdojo.springboot2essencials.mapper.AnimeMapper;
import academy.devdojo.springboot2essencials.repository.AnimeRepository;
import academy.devdojo.springboot2essencials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essencials.requests.AnimePutRequestBody;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class AnimeService {


    private final AnimeRepository animeRepository;

    /**
     *
     * @param pageable
     * @return
     */
    public Page<Anime> listAll(Pageable pageable){

        return animeRepository.findAll(pageable);
    }

    public List<Anime> listAll(){

        return animeRepository.findAllNonPageable();
    }

    /**
     *
     * @param name
     * @return
     */
    public List<Anime> findByName(String name){

        return animeRepository.findByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Anime findByIdOrThrowBadRequestException(Long id){
        return animeRepository.findById(id).orElseThrow(() -> new BadRequestException("Anime not Found"));
    }

    /**
     *
     * @param animePostRequestBody
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    /**
     *
     * @param id
     */
    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));

    }

    /**
     *
     * @param animePutRequestBody
     */
    public void replace(AnimePutRequestBody animePutRequestBody) {
       Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(anime.getId());
        animeRepository.save(anime);

    }

    public Object listAllNonPageable() {
    }
}
