package com.ClothesShop.java.clothesShop.services;

import com.ClothesShop.java.clothesShop.utils.PasswordEncoder;
import com.ClothesShop.java.clothesShop.dao.DaoClass.AccountRepository;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.enums.Role;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Data
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {       //метод, который указывает как именно возвращать пользователя по имени
        Account user = accountRepository.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("Нет такого пользователя");     //указываем ошибку если не найдем
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles()); //передадим Spring User с именем паролем и ролями, юзера из нашей таблицыы
        //таким образом мы перегнали НАШИХ юзеров, в юзеров которых понимает Spring Security. ведь ему главное username, password, roles и всё
    }

    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    public Account getByUsername(String username){
        return accountRepository.findByUsername(username);
    }
    public Account getById(Long id){
        return accountRepository.findById(id);
    }

    public Account create(Account account){
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        account.setRoles(roles);
        account.setActive(true);
        account.setCount_of_pay(0L);
        account.setBalance(0);

        account.setPassword(passwordEncoder.getBCryptPasswordEncoder().encode(account.getPassword()));
        try {
            return accountRepository.save(account);
        }
        catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    public Account update(Account account, Account last_account){
        if(account.getUsername()!=null){
            last_account.setUsername(account.getUsername());
        }
        if(account.getEmail()!=null){
            last_account.setEmail(account.getEmail());
        }
        if(account.getBalance()!=null){
            last_account.setBalance(account.getBalance());
        }

        try {
            return accountRepository.save(last_account);
        }
        catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    public void login(String username, String password, AuthenticationManager authenticationManager) {
        try {
            Account user = getByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public void delete(Long id){
        accountRepository.deleteById(id);
    }

    public Account getByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public void blockAccount(Long id) {
        Account account = accountRepository.findById(id);
        account.setActive(false);
        accountRepository.save(account);
    }

    public void unblock(Long id) {
        Account account = accountRepository.findById(id);
        account.setActive(true);
        accountRepository.save(account);
    }

    public void changePassword(Account account,String password) {
        account.setPassword(passwordEncoder.getBCryptPasswordEncoder().encode(password));
        accountRepository.save(account);
    }
}
