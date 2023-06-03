package com.ClothesShop.java.clothesShop.services;

import com.ClothesShop.java.clothesShop.dao.DaoClass.AccountRepository;
import com.ClothesShop.java.clothesShop.dao.DaoClass.OrganizationRepository;
import com.ClothesShop.java.clothesShop.dao.DaoInterface.OrganizationImageJpaInterface;
import com.ClothesShop.java.clothesShop.models.Account;
import com.ClothesShop.java.clothesShop.models.Organization;
import com.ClothesShop.java.clothesShop.models.dopModels.BaseImage.ImageMedia;
import com.ClothesShop.java.clothesShop.models.dopModels.OrganizationImage;
import com.ClothesShop.java.clothesShop.services.FileService.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    OrganizationImageJpaInterface organizationImageJpaInterface;

    @Autowired
    private FileUploadService uploadService;

    @Autowired ProsuctService prosuctService;

    @Autowired
    AccountRepository accountRepository;

    public Organization getById(Long id){
        return organizationRepository.findById(id);
    }

    public List<Organization> getAll(){
        return organizationRepository.findAll().stream().filter(e -> e.getActive()).collect(Collectors.toList());
    }

    public Organization create(Organization organization, MultipartFile logo, Account account) {

        ImageMedia imageMedia = uploadService.uploadFile(logo);
        OrganizationImage organizationImage = new OrganizationImage();
        organizationImage.setImageMedia(imageMedia);
        organization.setLogo(organizationImage);
        organization.setActive(false);
        Organization organization_acc = organizationRepository.save(organization);
        account.setOrganization(organization_acc);

        accountRepository.save(account);
        return organization;
    }

    public Organization update(Organization organization) {

        Organization last_organization = organizationRepository.findById(organization.getId());
        if(organization.getTitle()!=null){
            last_organization.setTitle(organization.getTitle());
        }
        if(organization.getDescription()!=null){
            last_organization.setDescription(organization.getDescription());
        }

        return organizationRepository.save(last_organization);
    }

    public Organization update_image(Organization organization, MultipartFile logo) {

        delete_files(organization);
        create_files(organization,logo);


        return organizationRepository.save(organization);
    }


    public void delete(Long id){
        Organization organization = organizationRepository.findById(id);
        delete_files(organization);
        organization.getProducts().stream().forEach(element -> prosuctService.delete_files(element));
        Account account = accountRepository.findByOrganization(organization);
        account.setOrganization(null);
        organizationRepository.deleteById(id);
        accountRepository.save(account);
    }

    public Organization create_files(Organization organization, MultipartFile logo){
        ImageMedia imageMedia = uploadService.uploadFile(logo);
        OrganizationImage organizationImage = new OrganizationImage();
        organizationImage.setImageMedia(imageMedia);
        organization.setLogo(organizationImage);
        return organization;
    }

    public void delete_files(Organization organization){
            Path file = Paths.get("uploads/" + organization.getLogo().getImageMedia().getName());
            organizationImageJpaInterface.delete(organization.getLogo());
            try {
                Files.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public void block(Long id) {
        Organization organization = organizationRepository.findById(id);
        organization.setActive(false);
        organizationRepository.save(organization);
    }

    public void unblock(Long id) {
        Organization organization = organizationRepository.findById(id);
        organization.setActive(true);
        organizationRepository.save(organization);
    }
}




