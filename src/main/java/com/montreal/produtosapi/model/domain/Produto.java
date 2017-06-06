/**
 * 
 */
package com.montreal.produtosapi.model.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.montreal.produtosapi.model.base.BaseEntity;

/**
 * @author rafaelpevidor
 *
 */
@Entity
@Table(name = "tb_produto")
public class Produto implements BaseEntity {

    public Produto() {}
    
    public Produto(String nome) {
        this.nome = nome;
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = -8497256318868348158L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nome;
    
    @ManyToOne
    @JoinColumn(name = "produto_pai_id")
    private Produto produtoPai;
    
    @OneToMany(mappedBy = "produtoPai")
    private List<Produto> produtosFilhos = new ArrayList<>();
    
    @OneToMany(mappedBy = "produto")
    private List<Imagem> imagens = new ArrayList<>();
    
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
    public Produto getProdutoPai() {
        return produtoPai;
    }
    public void setProdutoPai(Produto produtoPai) {
        this.produtoPai = produtoPai;
    }
    public List<Produto> getProdutosFilhos() {
        return produtosFilhos;
    }
    public void setProdutosFilhos(List<Produto> produtosFilhos) {
        this.produtosFilhos = produtosFilhos;
    }
    public List<Imagem> getImagens() {
        return imagens;
    }
    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }
    
}
