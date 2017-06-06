/**
 * 
 */
package com.montreal.produtosapi.controller.rest;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.montreal.produtosapi.model.domain.Produto;
import com.montreal.produtosapi.model.dto.ImagemDTO;
import com.montreal.produtosapi.model.dto.ProdutoDTO;
import com.montreal.produtosapi.model.enums.ProdutoDependenciaEnum;
import com.montreal.produtosapi.model.repository.ImagemRepository;
import com.montreal.produtosapi.model.repository.ProdutoRepository;

/**
 * @author rafaelpevidor
 *
 */
@RestController
@RequestMapping("/api/produto")
public class ProdutoRestController {
    
    private static final Logger LOG = Logger.getLogger(ProdutoRestController.class.getName());
    
    private final ProdutoRepository produtoRepository;
    private final ImagemRepository imagemRepository;
    
    @Autowired
    public ProdutoRestController(ProdutoRepository produtoRepository, ImagemRepository imagemRepository) {
        this.produtoRepository = produtoRepository;
        this.imagemRepository = imagemRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ProdutoDTO>> findAll(@RequestParam(name = "fetch", required = false) String colecao) {
        LOG.info("fetch > "+colecao);
        ProdutoDependenciaEnum dependencia = getFetchCollection(colecao);
        List<ProdutoDTO> produtos = null;
        
        if (null != dependencia) {
            LOG.info("Carregar produtos e dependentes... ");
            if (ProdutoDependenciaEnum.FILHOS.equals(dependencia)){
                produtos = produtoRepository.findAllWithFilhos()
                        .stream().map(ProdutoDTO::new).collect(Collectors.toList());
            }
            
            if (ProdutoDependenciaEnum.IMAGENS.equals(dependencia)) {
                produtos = produtoRepository.findAllWithImagens()
                        .stream().map(ProdutoDTO::new).collect(Collectors.toList());
            }
            
        } else {
            LOG.info("Carregar produtos... ");
            produtos = produtoRepository.findAll()
                    .stream().map(ProdutoDTO::new).collect(Collectors.toList());
        }
        
        if (produtos.isEmpty())
            return new ResponseEntity<List<ProdutoDTO>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<ProdutoDTO>>(produtos, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> findById(@PathVariable("id") Long id, @RequestParam(name = "fetch", required = false) String colecao) {
        
        ProdutoDependenciaEnum dependencia = getFetchCollection(colecao);
        Produto produto = null;
        System.out.println("dependencia >> "+dependencia);
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
    public ResponseEntity<Void> add(@RequestBody Produto produto) {
        
        Produto produtoDB = produtoRepository.findByNome(produto.getNome());
        
        if (produtoDB != null)
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        
        produtoRepository.save(produto);
        
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProdutoDTO> update(@PathVariable("id") Long id, @RequestBody Produto produto) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<ProdutoDTO>(HttpStatus.NOT_FOUND);
        
        produtoDB.setProdutoPai(produto.getProdutoPai());
        produtoDB.setImagens(produto.getImagens());
        produtoDB.setNome(produto.getNome());
        produtoDB.setProdutosFilhos(produto.getProdutosFilhos());
        
        produtoRepository.save(produtoDB);
        
        return new ResponseEntity<ProdutoDTO>(new ProdutoDTO(produtoDB), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ProdutoDTO> delete(@PathVariable("id") Long id) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<ProdutoDTO>(HttpStatus.NOT_FOUND);
        
        produtoRepository.delete(produtoDB);
        
        return new ResponseEntity<ProdutoDTO>(new ProdutoDTO(produtoDB), HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/{id}/imagem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ImagemDTO>> findByIdWithImagens(@PathVariable("id") Long id) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<List<ImagemDTO>>(HttpStatus.NOT_FOUND);
        
        List<ImagemDTO> imagens = imagemRepository.findByProduto(produtoDB)
                .stream().map(ImagemDTO::new).collect(Collectors.toList());
        
        if (imagens.isEmpty())
            return new ResponseEntity<List<ImagemDTO>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<ImagemDTO>>(imagens, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}/referencia", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProdutoDTO>> findByIdWithFilhos(@PathVariable("id") Long id) {
        
        Produto produtoDB = produtoRepository.findOne(id);
        
        if (produtoDB == null)
            return new ResponseEntity<List<ProdutoDTO>>(HttpStatus.NOT_FOUND);
        
        List<ProdutoDTO> filhos = produtoRepository.findByProdutoPai(produtoDB)
                .stream().map(ProdutoDTO::new).collect(Collectors.toList());
        
        if (filhos.isEmpty())
            return new ResponseEntity<List<ProdutoDTO>>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<List<ProdutoDTO>>(filhos, HttpStatus.OK);
    }

    private ProdutoDependenciaEnum getFetchCollection(String colecao) {
        LOG.info("Resolvendo dependentes para carregar...");
        ProdutoDependenciaEnum dependencia = null;
        if ((null != colecao)&&(!StringUtils.isEmpty(colecao))) {
            try {
                dependencia = ProdutoDependenciaEnum.valueOf(colecao.toUpperCase());
            } catch (IllegalArgumentException e) {
                dependencia = null;
            }
        }
        
        LOG.info("Dependentes..." + dependencia);
        return dependencia; 
    }
}
