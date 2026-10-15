package com.telusko.ecom_proj.controller;

import com.telusko.ecom_proj.model.Product;
import com.telusko.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin // to merge port 8080 java and 5173 react
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;
    /*
    so basically it says the client about the http status of the server,
    so in a website if we wish to stop the service of a particular feature,
    we can use response entity with 404 not found and stop it
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> list1 = service.getAll();
        return new ResponseEntity<>(list1, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getOneProduct(@PathVariable("id" )int id){
        Product p =service.getOne(id);
        if(p!=null){
            return new ResponseEntity<>(p,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart("product") Product product,
                                        @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            Product p = service.addproduct(product, imageFile);
            return new ResponseEntity<>(p, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable("productId") int prodId){

        Product p = service.getOne(prodId);
        byte[] imageFile = p.getImageDate();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(p.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable("id") int id,
                                                @RequestPart("product") Product product,
                                                @RequestPart("imageFile") MultipartFile imageFile){
        Product p = null;
        try {
            p = service.updateproduct(id, product, imageFile);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        if(p!=null){
            return new ResponseEntity<>("Updated!",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Failed!",HttpStatus.BAD_REQUEST);
        }

    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int id){
        Product p = service.getOne(id);
        if(p!=null){
            service.deleteproduct(id);
            return new ResponseEntity<>("Deleted!",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("NOT found !",HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        List<Product> list = service.searchproduct(keyword);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }


}
