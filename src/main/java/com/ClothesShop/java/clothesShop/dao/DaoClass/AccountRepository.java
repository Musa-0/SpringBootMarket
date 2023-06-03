package com.ClothesShop.java.clothesShop.dao.DaoClass;

import com.ClothesShop.java.clothesShop.dao.DaoInterface.AccountJpaInterface;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class AccountRepository {
    @Autowired
    AccountJpaInterface accountJpaInterface;

    @Autowired//создадим менеджер общения с бд, вместо сессий как было в hibernate
    private EntityManager entityManager;

    public Account save(Account account) {
        return accountJpaInterface.save(account);
    }

    public Account findByUsername(String username) {
        return accountJpaInterface.findByUsername(username);
    }

    public List<Account> findAll() {
        return accountJpaInterface.findAll();
    }

    public Account findById(Long id) {
        return accountJpaInterface.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        accountJpaInterface.deleteById(id);
    }

    public Account findByOrganization(Organization organization) {
        return accountJpaInterface.findByOrganization(organization);
    }

    public Account findByEmail(String email) {
        return accountJpaInterface.findByEmail(email);
    }
}
