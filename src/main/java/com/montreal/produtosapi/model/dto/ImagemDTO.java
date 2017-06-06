/**
 * 
 */
package com.montreal.produtosapi.model.dto;

import com.montreal.produtosapi.model.base.BaseDTO;
import com.montreal.produtosapi.model.domain.Imagem;

/**
 * @author rafaelpevidor
 *
 */
public class ImagemDTO extends BaseDTO<Imagem> {

    /**
     * 
     */
    private static final long serialVersionUID = 7753994491928393876L;
    
    public ImagemDTO(Imagem entity) {
        super(entity);
        this.id = entity.getId();
        if (null != entity.getProduto())
            this.produto = new ProdutoDTO(entity.getProduto());
        this.tipo = entity.getTipo();
    }

    private Long id;
    
    private ProdutoDTO produto;
    
    private String tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
