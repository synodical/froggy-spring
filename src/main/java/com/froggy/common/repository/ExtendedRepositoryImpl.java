package com.froggy.common.repository;

import com.froggy.common.exception.business.BusinessErrorCode;
import com.froggy.common.exception.business.BusinessException;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

public class ExtendedRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ExtendedRepository<T, ID> {
    private final EntityManager entityManager;
    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";

    public ExtendedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public T findByIdOrElseThrow(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Class<T> domainType = getDomainClass();
        T result = entityManager.find(domainType, id);

        if (result == null) {
            //TODO: 다른 exception을 정의해야 함
            throw new BusinessException(BusinessErrorCode.valueOf("NOT_FOUND_" + getClassName()), List.of(id));
        }
        return result;
    }

    private String getClassName() {
        Class<T> domainType = getDomainClass();
        return StringUtils.capitalize(domainType.getSimpleName())
                .replaceAll("(.)(\\p{javaUpperCase})", "$1_$2")
                .toUpperCase();
    }

}
