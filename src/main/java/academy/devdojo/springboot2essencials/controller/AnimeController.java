package academy.devdojo.springboot2essencials.controller;

import academy.devdojo.springboot2essencials.domain.Anime;
import academy.devdojo.springboot2essencials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essencials.requests.AnimePutRequestBody;
import academy.devdojo.springboot2essencials.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.CacheRequest;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    /**
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable){
        return ResponseEntity.ok((animeService.listAll(pageable)));
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Anime>> listAll(){
        return ResponseEntity.ok((animeService.listAllNonPageable());
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "by-id/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails){
        log.info(userDetails)
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    /**
     *
     * @param name
     * @return
     */
    @GetMapping(path = "/{name}")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
        return ResponseEntity.ok(animeService.findByName(name));
    }

    /**
     *
     * @param animePostRequestBody
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> save(@Valid @RequestBody AnimePostRequestBody animePostRequestBody){
       return new  ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     *
     * @param animePutRequestBody
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
