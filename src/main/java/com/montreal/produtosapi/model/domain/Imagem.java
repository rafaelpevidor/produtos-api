/**
 * 
 */
package com.montreal.produtosapi.model.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.montreal.produtosapi.model.base.BaseEntity;

/**
 * @author rafaelpevidor
 *
 */
@Entity
@Table(name = "tb_imagem")
public class Imagem implements BaseEntity {

    public Imagem() {}
    
    public Imagem(Produto produto, String tipo) {
        this.produto = produto;
        this.tipo = tipo;
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 7948688105625391604L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
    
    private String tipo;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto idProduto) {
        this.produto = idProduto;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
