package com.ClothesShop.java.clothesShop.dao.DaoInterface;

import com.ClothesShop.java.clothesShop.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationJpaInterface extends JpaRepository<Organization, Long> {
}
