package com.ClothesShop.java.clothesShop.controllers.restControllers.adminController;

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
@RequestMapping("/admin/organizations")
public class AdminOrganizationController {

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
    @GetMapping("/organization/all_organizations")
    public List<Organization> getAll(){
        return organizationService.getAll();
    }

    @GetMapping("/block/{id}")
    public String block(@PathVariable("id") Long id){
        if(organizationService.getById(id)==null) {return "Нет такой организации";}
        organizationService.block(id);
        return "Организация заблокирована";
    }

    @GetMapping("/unblock/{id}")
    public String unblock(@PathVariable("id") Long id){
        if(organizationService.getById(id)==null) {return "Нет такой организации";}
        organizationService.unblock(id);
        return "Организация разблокирована";
    }

    @PostMapping("/create/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(OrganizationForm organizationForm, @PathVariable("id") Long id, @RequestParam("file") MultipartFile logo){
        Organization organization = OrganizationForm.toOrganization(organizationForm);
        Account account = accountService.getById(id);

        ResponseEntity response = organizationRequestChecker.create_organization_checker(account);

        if(response==null) {
            Map<Object, Object> dict = new HashMap<>();
            Organization organization_new = organizationService.create(organization, logo, account);
            dict.put("organization", organization_new);
            response = ResponseEntity.ok(dict);
        }
        return response;

    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, OrganizationForm organizationForm, Principal principal){
        Organization organization = OrganizationForm.toOrganization(organizationForm);


        if(organizationService.getById(id)!=null) {
            Map<Object, Object> dict = new HashMap<>();
            organization.setId(id);
            Organization organization_new = organizationService.update(organization);
            dict.put("organization", organization_new);
            return ResponseEntity.ok(dict);
        }
        else {
            return ResponseEntity.ok("нет такой организации");
        }

    }
    @PutMapping("/update_image/{id}")
    public ResponseEntity update_image(@PathVariable("id") Long id, @RequestParam("files") MultipartFile logo, Principal principal){

        if(organizationService.getById(id)!=null) {
            Map<Object, Object> dict = new HashMap<>();
            Organization organization_last = organizationService.getById(id);
            Organization organization = organizationService.update_image(organization_last,logo);
            dict.put("organization", organization);
            return ResponseEntity.ok(dict);
        }
        else {
            return ResponseEntity.ok("нет такой организации");
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){


        if(organizationService.getById(id)!=null) {
            Map<Object, Object> dict = new HashMap<>();
            organizationService.delete(id);
            dict.put("Статус", "Организация успешно удалена");
            return ResponseEntity.ok(dict);
        }
        return ResponseEntity.ok("нет такой организации");


    }
}
