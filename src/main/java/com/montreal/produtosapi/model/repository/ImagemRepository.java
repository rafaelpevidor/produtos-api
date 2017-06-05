/**
 * 
 */
package com.montreal.produtosapi.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.montreal.produtosapi.model.domain.Imagem;
import com.montreal.produtosapi.model.domain.Produto;

/**
 * @author rafaelpevidor
 *
 */
@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {

    List<Imagem> findByProduto(Produto produto);

}
