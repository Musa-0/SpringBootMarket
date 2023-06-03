package com.ClothesShop.java.clothesShop.dao.DaoClass;

import com.ClothesShop.java.clothesShop.dao.DaoInterface.OrganizationJpaInterface;
import com.ClothesShop.java.clothesShop.models.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class OrganizationRepository {
    @Autowired
    OrganizationJpaInterface organizationJpaInterface;

    @Autowired//создадим менеджер общения с бд, вместо сессий как было в hibernate
    private EntityManager entityManager;


    public Organization findById(Long id) {
        return organizationJpaInterface.findById(id).orElse(null);
    }

    public Organization save(Organization organization) {
        return organizationJpaInterface.save(organization);
    }

    public void deleteById(Long id) {
        organizationJpaInterface.deleteById(id);
    }

    public List<Organization> findAll() {
        return organizationJpaInterface.findAll();
    }
}
