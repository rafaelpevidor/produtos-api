/**
 * 
 */
package com.montreal.produtosapi.model.enums;

/**
 * @author rafaelpevidor
 *
 */
public enum ProdutoDependenciaEnum {

    FILHOS,
    IMAGENS
    ;
    
    public String getNome() {
        return name().toLowerCase();
    }
    
    @Override
    public String toString() {
        return getNome();
    }
}
