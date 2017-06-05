/**
 * 
 */
package com.montreal.produtosapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.montreal.produtosapi.model.domain.Imagem;
import com.montreal.produtosapi.model.domain.Produto;

/**
 * @author rafaelpevidor
 *
 */
public class ProdutoDTO extends Produto {

    @JsonIgnore
    @Override
    public List<Imagem> getImagens() {
        return super.getImagens();
    }
    
    @JsonIgnore
    @Override
    public List<Produto> getProdutosFilhos() {
        return super.getProdutosFilhos();
    }
}
