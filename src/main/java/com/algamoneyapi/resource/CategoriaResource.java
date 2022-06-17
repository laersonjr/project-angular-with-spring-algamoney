package com.algamoneyapi.resource;

import com.algamoneyapi.model.Categoria;
import com.algamoneyapi.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listar(){
        return categoriaRepository.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo){
        Optional<Categoria> buscarCategoria = categoriaRepository.findById(codigo);
        return buscarCategoria.isPresent() ? ResponseEntity.ok().body(buscarCategoria.get()) : ResponseEntity.notFound().build();
        /* Um outra forma para retorno customizado, seria utilizando .map, exemplo abaixo:
        * return this.categoriaRepository.findById(codigo)
            .map(categoria -> ResponseEntity.ok(categoria))
  .         orElse(ResponseEntity.notFound().build());
        * */
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response){
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        // A partir da requisição atual (fromCurrentRequestUri()), vai pegar o código/id (path())
        // Por fim, vai adicionar o código na URI.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(categoriaSalva.getCodigo()).toUri();

        return ResponseEntity.created(uri).body(categoriaSalva);
    }

}
