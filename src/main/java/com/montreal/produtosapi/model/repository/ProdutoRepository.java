/**
 * 
 */
package com.montreal.produtosapi.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.montreal.produtosapi.model.domain.Produto;

/**
 * @author rafaelpevidor
 *
 */

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p from Produto p JOIN FETCH p.imagens img")
    List<Produto> findAllWithImagens();

    Produto findByNome(String nome);

    @Query("SELECT p from Produto p JOIN FETCH p.produtosFilhos pf")
    List<Produto> findAllWithFilhos();
    
    @Query("SELECT p from Produto p JOIN FETCH p.imagens img WHERE p.id = :id")
    Produto findOneWithImagens(@Param(value = "id") Long id);
    
    @Query("SELECT p from Produto p JOIN FETCH p.produtosFilhos pf WHERE p.id = :id")
    Produto findOneWithFilhoss(@Param(value = "id") Long id);

    List<Produto> findByProdutoPai(Produto produtoPai);

}
