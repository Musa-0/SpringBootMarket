package com.ClothesShop.java.clothesShop.dto.organizationDto;

import com.ClothesShop.java.clothesShop.models.Organization;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

import javax.persistence.Column;
@Data
public class OrganizationForm {
    String title;
    String description;
    public static Organization toOrganization(OrganizationForm organizationForm){
        Organization organization = new Organization();
        organization.setTitle(organizationForm.getTitle());
        organization.setDescription(organizationForm.getDescription());
        return organization;
    }
}
