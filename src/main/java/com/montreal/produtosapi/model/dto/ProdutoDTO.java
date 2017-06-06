/**
 * 
 */
package com.montreal.produtosapi.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.montreal.produtosapi.model.base.BaseDTO;
import com.montreal.produtosapi.model.domain.Produto;

/**
 * @author rafaelpevidor
 *
 */
public class ProdutoDTO extends BaseDTO<Produto> {

    /**
     * 
     */
    private static final long serialVersionUID = 9086684951960029622L;

    public ProdutoDTO(Produto entity) {
        super(entity);
        this.id = entity.getId();
        this.nome = entity.getNome();
        
        if (null != entity.getProdutoPai())
            this.produtoPai = entity.getProdutoPai().getId();
       
        if (
                Hibernate.isInitialized(entity.getProdutosFilhos()) && 
                null != entity.getProdutosFilhos() && 
                !entity.getProdutosFilhos().isEmpty()
        ) {
            this.produtosFilhos = new ArrayList<>();
            entity.getProdutosFilhos().forEach(produto -> {this.produtosFilhos.add(new ProdutoDTO(produto));});
        }
        
        if (
                Hibernate.isInitialized(entity.getImagens()) &&
                null != entity.getImagens() &&
                !entity.getImagens().isEmpty()
        ) {
           this.imagens = new ArrayList<>();
           entity.getImagens().forEach(imagem -> {this.imagens.add(new ImagemDTO(imagem));});
        }
    }

    private Long id;
    
    private String nome;
    
    private Long produtoPai;
    
    private List<ImagemDTO> imagens = new ArrayList<>();
    
    private List<ProdutoDTO> produtosFilhos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getProdutoPai() {
        return produtoPai;
    }

    public void setProdutoPai(Long produtoPai) {
        this.produtoPai = produtoPai;
    }

    public List<ImagemDTO> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemDTO> imagens) {
        this.imagens = imagens;
    }

    public List<ProdutoDTO> getProdutosFilhos() {
        return produtosFilhos;
    }

    public void setProdutosFilhos(List<ProdutoDTO> produtosFilhos) {
        this.produtosFilhos = produtosFilhos;
    }
    
}
