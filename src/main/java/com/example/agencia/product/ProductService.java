package com.example.agencia.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    HashMap<String, Object> datos;
    private final ProductRepository productRepository;
    @Autowired

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<Product> getProducts(){
        return this.productRepository.findAll();
    }

    public ResponseEntity<Object> newProduct(Product product) {
        Optional<Product> res = productRepository.findProductByName(product.getName());
        datos = new HashMap<>();

        if (res.isPresent() && product.getId()==null) {
            datos.put("error", true);
            datos.put("message", "Ya existe un producto con su nombre");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }



        datos.put("message", "Se Guardo con éxito");
        if(product.getId()!=null){
            datos.put("message", "Se Actualizo con éxito");
        }
        datos.put("data", product);
        productRepository.save(product);
        return new ResponseEntity<>(
                datos,
                HttpStatus.CREATED
        );
    }
    public ResponseEntity<Object> deleteProduct(long id){
        datos = new HashMap<>();
        boolean existe =this.productRepository.existsById(id);

        if (!existe){
            datos.put("error", true);
            datos.put("message", "no existe un producto con su id");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }
        productRepository.deleteById(id);
        datos.put("message", "Producto eliminado con id="+id);
        return new ResponseEntity<>(
                datos,
                HttpStatus.ACCEPTED
        );

    }
}
