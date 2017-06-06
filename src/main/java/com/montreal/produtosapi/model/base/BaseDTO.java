/**
 * 
 */
package com.montreal.produtosapi.model.base;

import java.io.Serializable;

/**
 * @author rafaelpevidor
 *
 */
@SuppressWarnings("serial")
public abstract class BaseDTO<T extends BaseEntity> implements Serializable {

    public BaseDTO(T entity) {
        super();
    }

    
}
