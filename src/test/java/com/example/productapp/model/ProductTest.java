package com.example.productapp.model;

import aj.org.objectweb.asm.ConstantDynamic;
import com.example.productapp.repo.ProductRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductTest {

    @Autowired
    private ProductRepository repo;

    @Test
    @Rollback(value = false)
    @Order(1)
    public void testCreateProduct(){
        Product product =new Product("iPhone 10", 789);
        Product savedProduct = repo.save(product);
        assertNotNull(savedProduct);
    }

    @Test
    @Order(2)
    public void testFindProductByNameExist(){
        String name= "iPhone 10";
        Product product = repo.findByName(name);
        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    @Order(3)
    public void testFindProductByNameNotExist(){
        String name= "iPhone 11";
        Product product = repo.findByName(name);
        assertNull(product);
    }


    @Test
    @Rollback(value = false)
    @Order(5)
    public void testUpdateProduct(){
        String productName="Kindle Reader";
        Product product = new Product(productName, 199);
        product.setId(1);

        repo.save(product);
        Product updatedProduct = repo.findByName(productName);
        assertThat(updatedProduct.getName()).isEqualTo(productName);
    }

    @Test
    @Order(4)
    public void testListProducts(){
        List<Product> products = (List<Product>) repo.findAll();

        for(Product product: products){
            System.out.println(product);
        }
        assertThat(products.size()).isGreaterThan(0);
    }

    @Test
    @Rollback(value = false)
    @Order(6)
    public void testDeleteProduct(){
        Integer id = 1;

        boolean isExistBeforeDelete = repo.findById(id).isPresent();

        repo.deleteById(id);

        boolean notExistAfterDelete = repo.findById(id).isPresent();

        assertTrue(isExistBeforeDelete);
        assertFalse(notExistAfterDelete);
    }

}