package com.ClothesShop.java.clothesShop.controllers.restControllers.adminController;

import com.ClothesShop.java.clothesShop.controllers.requestChecking.AccountRequestChecker;
import com.ClothesShop.java.clothesShop.dto.accountDto.AccountFormRegister;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.utils.validators.PasswordValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordValidate passwordValidate;

    AccountRequestChecker accountRequestChecker;

    @PostConstruct
    private void init(){
        accountRequestChecker = new AccountRequestChecker(accountService,passwordValidate);
    }

    @GetMapping("/account/all_accounts")
    public List<Account> getAll(){
        return accountService.getAll();
    }

    @GetMapping("/account/{id}")
    public Account getById(@PathVariable(value = "id") Long id){
        return accountService.getById(id);
    }

    @GetMapping("/block/{id}")
    public String block(@PathVariable(value = "id") Long id){
        if(accountService.getById(id)==null) {return "Нет такого пользователя";}
        accountService.blockAccount(id);
        return "Пользователь получил бан";
    }
    @GetMapping("/unblock/{id}")
    public String unblock(@PathVariable(value = "id") Long id){
        if(accountService.getById(id)==null) {return "Нет такого пользователя";}
        accountService.unblock(id);
        return "Пользователь разблокирован";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(Account account,
                                  @PathVariable("id") Long id){
        account.setId(id);
        Account old_account = accountService.getById(id);

        ResponseEntity response = accountRequestChecker.update_account_checker(account);
        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Account new_account = accountService.update(account, old_account);
            dict.put("account", new_account);
            response = ResponseEntity.ok(dict);
        }
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody AccountFormRegister accountFormRegister){
        Account account = AccountFormRegister.toAccount(accountFormRegister);

        ResponseEntity response = accountRequestChecker.create_account_checker(account);
        if (response == null) {
            Map<Object, Object> dict = new HashMap<>();
            Account new_account = accountService.create(account);
            dict.put("account", new_account);
            response = ResponseEntity.ok(dict);
        }


        return ResponseEntity.ok(response);

    }


    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        accountService.delete(id);
        return "Аккаунт успешно удалён";

    }
}
