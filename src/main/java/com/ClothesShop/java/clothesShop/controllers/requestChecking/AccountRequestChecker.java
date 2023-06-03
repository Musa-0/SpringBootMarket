package com.ClothesShop.java.clothesShop.controllers.requestChecking;

import com.ClothesShop.java.clothesShop.dto.accountDto.AccountFormChangePassword;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.utils.validators.PasswordValidate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountRequestChecker {

    PasswordValidate passwordValidate;

    AccountService accountService;

    public AccountRequestChecker(AccountService accountService, PasswordValidate passwordValidate){
        this.passwordValidate = passwordValidate;
        this.accountService = accountService;
    }

    public ResponseEntity create_account_checker(Account account){
        Map<Object, Object> response = new HashMap<>();
        if(account.getEmail()==null) {
            response.put("Ошибка", String.format("Вы не указали email"));
            return ResponseEntity.ok(response);
        }
        if(account.getPassword()==null) {
            response.put("Ошибка", String.format("Вы не указали пароль"));
            return ResponseEntity.ok(response);
        }
        if(account.getUsername()==null) {
            response.put("Ошибка", String.format("Вы не указали Имя пользователя"));
            return ResponseEntity.ok(response);
        }

        if(accountService.getByUsername(account.getUsername())!=null){
            response.put("Ошибка", "Пользователь с таким именем уже есть");
            return ResponseEntity.ok(response);
        }
        if(accountService.getByEmail(account.getEmail())!=null){
            response.put("Ошибка", "Пользователь с таким email уже есть");
            return ResponseEntity.ok(response);
        }

        return check_password(account.getPassword());   //вернет ошибки если пароль легкий. иначе вернет null


    }

    public ResponseEntity check_password(String password){
        List<String> password_valid = passwordValidate.is_valid(password);
        Map<Object, Object> response = new HashMap<>();
        if(password_valid!=null){
            response.put("Слишком простой пароль", password_valid);
            return ResponseEntity.ok(response);
        }
        return null;
    }

    public ResponseEntity update_account_checker(Account account) {
        Map<Object, Object> response = new HashMap<>();

        if(accountService.getByUsername(account.getUsername())!=null && account.getUsername()!=null){
            response.put("Ошибка", "Пользователь с таким именем уже есть");
            return ResponseEntity.ok(response);
        }
        if(accountService.getByEmail(account.getEmail())!=null && account.getEmail()!=null){
            response.put("Ошибка", "Пользователь с таким email уже есть");
            return ResponseEntity.ok(response);
        }

        return null;
    }

    public ResponseEntity change_password_chacker(AccountFormChangePassword accountFormChangePassword, Account account){
        String last_password = accountFormChangePassword.getLast_password();
        String new_password = accountFormChangePassword.getNew_password();
        String new_password_repeat = accountFormChangePassword.getNew_password_repeat();

        if(last_password==null){return ResponseEntity.ok("введите старый пароль");}
        if(new_password==null){return ResponseEntity.ok("введите новый пароль");}
        if(new_password_repeat==null){return ResponseEntity.ok("введите повторный новый");}

        BCryptPasswordEncoder passwordEncoder = accountService.getPasswordEncoder().getBCryptPasswordEncoder();

        if(!passwordEncoder.matches(last_password, account.getPassword())){//сравнивает новый пароль. с хешом старого пароля
            return ResponseEntity.ok("Не верный старый пароль");
        }
        if(!new_password.equals(new_password_repeat)){
            return ResponseEntity.ok("пароли не совпадают");
        }
        return check_password(new_password);

    }
}
