 /* 
 */
package com.montreal.produtosapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.montreal.produtosapi.model.domain.Imagem;
import com.montreal.produtosapi.model.repository.ImagemRepository;

/**
 * @author rafaelpevidor
 *
 */
@RestController
@RequestMapping("/api/imagem")
public class ImagemController {

private final ImagemRepository imagemRepository;
    
    @Autowired
    public ImagemController(ImagemRepository imagemRepository) {
        this.imagemRepository = imagemRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Imagem>> findAll() {
        
        List<Imagem> imagems = imagemRepository.findAll();
        
        if (imagems.isEmpty())
            return new ResponseEntity<List<Imagem>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<Imagem>>(imagems, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Imagem> findById(@PathVariable("id") Long id) {
        
        Imagem imagem = imagemRepository.findOne(id);
        
        if (null == imagem)
            return new ResponseEntity<Imagem>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<Imagem>(imagem, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> add(@RequestBody Imagem imagem, UriComponentsBuilder ucBuilder) {
        
        imagemRepository.save(imagem);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/imagem/{id}").buildAndExpand(imagem.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Imagem> update(@PathVariable("id") Long id, @RequestBody Imagem imagem) {
        
        Imagem imagemDB = imagemRepository.findOne(id);
        
        if (imagemDB == null)
            return new ResponseEntity<Imagem>(HttpStatus.NOT_FOUND);
        
        imagemRepository.save(imagem);
        
        return new ResponseEntity<Imagem>(imagem, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Imagem> delete(@PathVariable("id") Long id) {
        
        Imagem imagemDB = imagemRepository.findOne(id);
        
        if (imagemDB == null)
            return new ResponseEntity<Imagem>(HttpStatus.NOT_FOUND);
        
        imagemRepository.delete(imagemDB);
        
        return new ResponseEntity<Imagem>(imagemDB, HttpStatus.NO_CONTENT);
    }
}
