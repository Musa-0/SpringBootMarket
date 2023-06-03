package com.ClothesShop.java.clothesShop.controllers.restControllers;

import com.ClothesShop.java.clothesShop.controllers.requestChecking.AccountRequestChecker;
import com.ClothesShop.java.clothesShop.dto.accountDto.AccountFormLogin;
import com.ClothesShop.java.clothesShop.dto.accountDto.AccountFormRegister;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.utils.validators.PasswordValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {

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


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AccountFormRegister accountFormRegister, Principal principal){
        if(principal==null) {
            Account account = AccountFormRegister.toAccount(accountFormRegister);
            ResponseEntity response = accountRequestChecker.create_account_checker(account);
            if (response == null) {
                Map<Object, Object> dict = new HashMap<>();
                Account new_account = accountService.create(account);
                dict.put("account", new_account);
                response = ResponseEntity.ok(dict);
            }
            return response;
        }
        else {
            return ResponseEntity.badRequest().body("Вы уже авторизованы");
        }
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AccountFormLogin requestDto, Principal principal) {     //принимаем username и password
        if(principal==null) {
            String username = requestDto.getUsername();
            String password = requestDto.getPassword();
            Account user = accountService.getByUsername(username);
            accountService.login(username, password, authenticationManager);

            Map<Object, Object> response = new HashMap<>();
            response.put("account", user);

            return ResponseEntity.ok(response);
        }
        else {
            return ResponseEntity.badRequest().body("Вы уже авторизованы");
        }

    }
    @GetMapping("/logout")
    public ResponseEntity logout(Principal principal){
        if(principal!=null) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("Вы успешно вышли из аккаунта");
        }
        else {
            return ResponseEntity.badRequest().body("Вы не авторизованы в системе");
        }
    }
}
