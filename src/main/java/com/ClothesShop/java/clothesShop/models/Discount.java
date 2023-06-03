package com.ClothesShop.java.clothesShop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Table
@Data
public class Discount {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private Date created;       // дата и время создания объекта

    @Column(name = "expire")
    private Date expire;        //срок действия скидки

    @Max(99)                        //максимальный обём скидки 99 процентов
    @Min(1)
    private Integer amount;         //размер скидки в процентах

    @OneToOne(mappedBy = "discount")
    @JsonIgnore
    Product product;
}
