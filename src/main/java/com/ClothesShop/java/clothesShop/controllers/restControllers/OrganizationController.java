package com.ClothesShop.java.clothesShop.controllers.restControllers;

import com.ClothesShop.java.clothesShop.controllers.requestChecking.OrganizationRequestChecker;
import com.ClothesShop.java.clothesShop.dto.organizationDto.OrganizationForm;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Organization;
import com.ClothesShop.java.clothesShop.services.AccountService;
import com.ClothesShop.java.clothesShop.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;
    @Autowired
    AccountService accountService;

    OrganizationRequestChecker organizationRequestChecker;

    @PostConstruct
    private void init(){
        organizationRequestChecker = new OrganizationRequestChecker(organizationService);
    }

    @GetMapping("/organization/{id}")
    public ResponseEntity getById(@PathVariable(value = "id") Long id){
        Organization organization = organizationService.getById(id);
        Map<Object,Object> response = new HashMap();
        if(organization==null){
            response.put("Ошибка", "Нет такой организации");
            return ResponseEntity.ok(response);
        }
        response.put("Товар", organization);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/organization/")
    public List<Organization> getAll(){
        return organizationService.getAll();
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(OrganizationForm organizationForm, @RequestParam("file") MultipartFile logo, Principal principal){
        Organization organization = OrganizationForm.toOrganization(organizationForm);
        Account account = accountService.getByUsername(principal.getName());

        ResponseEntity response = organizationRequestChecker.create_organization_checker(account);

        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Organization organization_new = organizationService.create(organization, logo, account);
            dict.put("organization", organization_new);
            response = ResponseEntity.ok(dict);
        }
        return response;

    }

    @PutMapping("/update")
    public ResponseEntity update(OrganizationForm organizationForm, Principal principal){
        Organization organization = OrganizationForm.toOrganization(organizationForm);
        Account account = accountService.getByUsername(principal.getName());

        ResponseEntity response = organizationRequestChecker.update_delete_organization_checker(account);

        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            organization.setId(account.getOrganization().getId());
            Organization organization_new = organizationService.update(organization);
            dict.put("organization", organization_new);
            response = ResponseEntity.ok(dict);
        }
        return response;


    }
    @PutMapping("/update_image")
    public ResponseEntity update_image(@RequestParam("files") MultipartFile logo, Principal principal){
        Account account = accountService.getByUsername(principal.getName());

        ResponseEntity response = organizationRequestChecker.update_delete_organization_checker(account);

        if (response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Organization organization_last = organizationService.getById(account.getOrganization().getId());
            Organization organization = organizationService.update_image(organization_last,logo);
            dict.put("organization", organization);
            response = ResponseEntity.ok(dict);
        }
        return response;


    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(Principal principal){
        Account account = accountService.getByUsername(principal.getName());

        ResponseEntity response = organizationRequestChecker.update_delete_organization_checker(account);

        if (response==null) {
            Map<Object, Object> dict = new HashMap<>();
            organizationService.delete(account.getOrganization().getId());
            dict.put("Статус", "Организация успешно удалена");
            response = ResponseEntity.ok(dict);
        }
        return response;


    }
}
