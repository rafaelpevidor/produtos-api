/**
 * 
 */
package com.montreal.produtosapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.montreal.produtosapi.model.domain.Imagem;
import com.montreal.produtosapi.model.domain.Produto;
import com.montreal.produtosapi.model.enums.ProdutoDependenciaEnum;
import com.montreal.produtosapi.model.repository.ImagemRepository;
import com.montreal.produtosapi.model.repository.ProdutoRepository;

/**
 * @author rafaelpevidor
 *
 */
@RestController
@RequestMapping("/api/produto")
public class ProdutoController {
    
    private final ProdutoRepository produtoRepository;
    private final ImagemRepository imagemRepository;
    
    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository, ImagemRepository imagemRepository) {
        this.produtoRepository = produtoRepository;
        this.imagemRepository = imagemRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Produto>> findAll(@RequestParam(value="fetch") String colecao) {
        
        ProdutoDependenciaEnum dependencia = getFetchCollection(colecao);
        
        List<Produto> produtos = null;
        
        if (null != dependencia) {
            if (ProdutoDependenciaEnum.FILHOS.equals(dependencia))
                produtos = produtoRepository.findAllWithFilhos();
            
            if (ProdutoDependenciaEnum.IMAGENS.equals(dependencia))
                produtos = produtoRepository.findAllWithImagens();
            
        } else {
            produtos = produtoRepository.findAll();
        }
        
        if (produtos.isEmpty())
            return new ResponseEntity<List<Produto>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
    }
    
    private ProdutoDependenciaEnum getFetchCollection(String colecao) {
        ProdutoDependenciaEnum dependencia = null;
        if ((null != colecao)&&(!StringUtils.isEmpty(colecao))) {
            try {
                dependencia = ProdutoDependenciaEnum.valueOf(colecao.toLowerCase());
            } catch (IllegalArgumentException e) {
                dependencia = null;
            }
        }
        return dependencia;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> findById(@PathVariable("id") Long id, @RequestParam(value="fetch") String colecao) {
        
        ProdutoDependenciaEnum dependencia = getFetchCollection(colecao);
        Produto produto = null;
        
        if (null != dependencia) {
            if (ProdutoDependenciaEnum.FILHOS.equals(dependencia))
                produto = produtoRepository.findOneWithFilhoss(id);
            
            if (ProdutoDependenciaEnum.IMAGENS.equals(dependencia))
                produto = produtoRepository.findOneWithImagens(id);
            
        } else {
            produto = produtoRepository.findOne(id);
        }
        
        if (null == produto)
            return new ResponseEntity<Produto>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> add(@RequestBody Produto produto, UriComponentsBuilder ucBuilder) {
        
        Produto produtoDB = produtoRepository.findByNome(produto.getNome());
        
        if (produtoDB != null)
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        
        produtoRepository.save(produto);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/produto/{id}").buildAndExpand(produto.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Produto> update(@PathVariable("id") Long id, @RequestBody Produto produto) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<Produto>(HttpStatus.NOT_FOUND);
        
        produtoDB.setProdutoPai(produto.getProdutoPai());
        produtoDB.setImagens(produto.getImagens());
        produtoDB.setNome(produto.getNome());
        produtoDB.setProdutosFilhos(produto.getProdutosFilhos());
        
        produtoRepository.save(produto);
        
        return new ResponseEntity<Produto>(produto, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Produto> delete(@PathVariable("id") Long id) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<Produto>(HttpStatus.NOT_FOUND);
        
        produtoRepository.delete(produtoDB);
        
        return new ResponseEntity<Produto>(produtoDB, HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/{id}/imagem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Imagem>> findByIdWithImagens(@PathVariable("id") Long id) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<List<Imagem>>(HttpStatus.NOT_FOUND);
        
        List<Imagem> imagens = imagemRepository.findByProduto(produtoDB);
        
        if (imagens.isEmpty())
            return new ResponseEntity<List<Imagem>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<Imagem>>(imagens, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/referencia", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Produto>> findByIdWithFilhos(@PathVariable("id") Long id) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<List<Produto>>(HttpStatus.NOT_FOUND);
        
        List<Produto> filhos = produtoRepository.findByProdutoPai(produtoDB);
        
        if (filhos.isEmpty())
            return new ResponseEntity<List<Produto>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<Produto>>(filhos, HttpStatus.OK);
    }
    
}
