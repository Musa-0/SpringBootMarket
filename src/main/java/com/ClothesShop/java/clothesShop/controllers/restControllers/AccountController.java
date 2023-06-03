package com.ClothesShop.java.clothesShop.controllers.restControllers;

import com.ClothesShop.java.clothesShop.controllers.requestChecking.AccountRequestChecker;
import com.ClothesShop.java.clothesShop.dto.accountDto.AccountFormChangePassword;
import com.ClothesShop.java.clothesShop.dto.accountDto.AccountFormUpdate;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.utils.validators.PasswordValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    PasswordValidate passwordValidate;

    AccountRequestChecker accountRequestChecker;

    @PostConstruct
    private void init(){
        accountRequestChecker = new AccountRequestChecker(accountService,passwordValidate);
    }

    @GetMapping("/account/")
    public List<Account> getAll(){
        return accountService.getAll();
    }

    @GetMapping("/account/{id}")
    public Account getById(@PathVariable(value = "id") Long id){
        return accountService.getById(id);
    }


    @PutMapping("/update")
    public ResponseEntity update(AccountFormUpdate accountFormUpdate, Principal principal){
        Account account = AccountFormUpdate.toAccount(accountFormUpdate);
        Account last_account = accountService.getByUsername(principal.getName());

        boolean logout = true;

        if(last_account.getUsername().equals(account.getUsername()) || account.getUsername()==null){
            account.setUsername(null);
            logout = false;
        }
        if(last_account.getEmail().equals(account.getEmail())){
            account.setEmail(null);
        }
        ResponseEntity response = accountRequestChecker.update_account_checker(account);
        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Account new_account = accountService.update(account, last_account);
            dict.put("account", new_account);
            if(logout) {
                SecurityContextHolder.clearContext();
            }
            response = ResponseEntity.ok(dict);
        }
        return response;
    }

    @DeleteMapping("/delete")
    public String delete(Principal principal){
        Account account = accountService.getByUsername(principal.getName());
        accountService.delete(account.getId());
        return "Аккаунт успешно удалён";
    }

    @PutMapping("/change_password")
    public ResponseEntity change_password(AccountFormChangePassword accountFormChangePassword, Principal principal){
        Account account = accountService.getByUsername(principal.getName());
        ResponseEntity response = accountRequestChecker.change_password_chacker(accountFormChangePassword,account);
        if(response==null){
            accountService.changePassword(account,accountFormChangePassword.getNew_password());
            response = ResponseEntity.ok("пароль успешно изменён");
        }
        return response;
    }



}
