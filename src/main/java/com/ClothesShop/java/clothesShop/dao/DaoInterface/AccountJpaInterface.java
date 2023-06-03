package com.ClothesShop.java.clothesShop.dao.DaoInterface;

import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AccountJpaInterface extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    Account findByOrganization(Organization organization);

    Account findByEmail(String email);
}
