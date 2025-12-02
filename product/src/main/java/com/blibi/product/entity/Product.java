package com.blibi.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Document(collection = Product.COLLECTION_NAME)
public class Product {
    static final String COLLECTION_NAME = "products";

    @Id
    private ObjectId productId;
    private String productName;
    private String description;
    private Double price;
    private String category;
    private List<String> images;
}
