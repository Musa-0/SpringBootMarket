package com.ClothesShop.java.clothesShop.models;

import com.ClothesShop.java.clothesShop.models.dopModels.Review;
import com.ClothesShop.java.clothesShop.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "account")
//@Where(clause="is_active=t")  перенастроит репозитории, теперь они не будут брать из бд не активные аккаунты
@Data
public class Account implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(name = "username", unique = true)
    String username;

    @NotBlank
    @Column(name = "email", unique = true)
    String email;

    @NotBlank
    @Column(name = "password", length = 1000)
    String password;

    @Column(name = "balance")
    @Value("0")
    Integer balance;

    @Column(name = "count_of_pay")
    @Value("0")
    Long count_of_pay;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)  //это не набор сущностей, а набор простых типов (строк и т.д.)
    @CollectionTable(name="user_role",                                  //это многие ко многим. но при этом у нас
            joinColumns = @JoinColumn(name = "user_id"))             //сопостовляются Account с String ролями это из JavaEE
    @Enumerated(EnumType.STRING)    //преобразовываем enum в string
    @Column(name = "role")
    Set<Role> roles = new HashSet<>();

    @Column(name="active")
    private boolean active;





    boolean isAdmin(){
        return roles.contains(Role.ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
