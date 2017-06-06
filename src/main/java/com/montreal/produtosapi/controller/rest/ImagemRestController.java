 /* 
 */
package com.montreal.produtosapi.controller.rest;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.montreal.produtosapi.model.domain.Imagem;
import com.montreal.produtosapi.model.repository.ImagemRepository;

/**
 * @author rafaelpevidor
 *
 */
@RestController
@RequestMapping("/api/imagem")
public class ImagemRestController {

    private static final Logger LOG = Logger.getLogger(ProdutoRestController.class.getName());
    
    private final ImagemRepository imagemRepository;
    
    @Autowired
    public ImagemRestController(ImagemRepository imagemRepository) {
        this.imagemRepository = imagemRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Imagem>> findAll() {
        LOG.info("Carregar imagens... ");
        List<Imagem> imagens = imagemRepository.findAll();//.stream().map(ImagemDTO::new).collect(Collectors.toList());
        imagens.add(new Imagem());
        
        if (imagens.isEmpty())
            return new ResponseEntity<List<Imagem>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<Imagem>>(imagens, HttpStatus.OK);
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
    public ResponseEntity<Void> add(@RequestBody Imagem imagem) {
        
        imagemRepository.save(imagem);
        
        return new ResponseEntity<Void>(HttpStatus.CREATED);
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
