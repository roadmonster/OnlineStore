package com.example.onlinestore;

import com.example.onlinestore.model.ShoppingCart;
import com.example.onlinestore.model.UserProduct;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.User;

import java.util.Objects;

@SpringBootTest(classes = OnlinestoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OnlineStoreTest {

    private TestRestTemplate restTemplate;

    @BeforeClass
    public void setUp() {
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password");

        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:8080/users", user, User.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assert.assertNotNull(response.getBody().getId());
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setName("iPhone 12");
        product.setPrice(999.99);


        ResponseEntity<Product> response = restTemplate.postForEntity("http://localhost:8080/products", product, Product.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assert.assertNotNull(Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    public void testGetProductInventory() {
        ResponseEntity<Product> response = restTemplate.getForEntity("http://localhost:8080/inventory/product/1", Product.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testAddProductToCart() {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("janedoe@example.com");
        user.setPassword("password");

        ResponseEntity<User> userResponse = restTemplate.postForEntity("http://localhost:8080/users", user, User.class);
        Assert.assertEquals(userResponse.getStatusCode(), HttpStatus.CREATED);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(userResponse.getBody());

        ResponseEntity<ShoppingCart> cartResponse = restTemplate.postForEntity("http://localhost:8080/shopping-carts", cart, ShoppingCart.class);
        Assert.assertEquals(cartResponse.getStatusCode(), HttpStatus.CREATED);

        UserProduct userProduct = new UserProduct();
        userProduct.setUser(userResponse.getBody());

        ResponseEntity<Product> iphone12 = restTemplate.getForEntity("http://localhost:8080/products/1", Product.class);


        userProduct.setProduct(iphone12.getBody());
        userProduct.setQuantity(1);
        userProduct.setShoppingCart(Objects.requireNonNull(cartResponse.getBody()));

        ResponseEntity<UserProduct> userProductResponse = restTemplate.postForEntity("/users-products", userProduct, UserProduct.class);
        Assert.assertEquals(userProductResponse.getStatusCode(), HttpStatus.CREATED);
    }

}

