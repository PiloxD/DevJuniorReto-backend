package sofka.backend.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.backend.models.ProductModel;
import sofka.backend.services.ProductService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("")
@Configuration
public class ProductsController {
    private final ProductService service;
    private  ReactiveMongoTemplate template;
    
    @Autowired
    public ProductsController(ReactiveMongoTemplate template, ProductService service) {
        this.template = template;
        this.service = service;
        
    } 
    @Bean
    public RouterFunction<ServerResponse> getProducts() {
        return route(
                GET("/product/getall"),
                request -> template.findAll(ProductModel.class, "products")
                        .collectList()
                        .flatMap(list -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(Flux.fromIterable(list), ProductModel.class)))
        );}

    @Bean
    public RouterFunction<ServerResponse> createProduct() {
        return route(
                POST("/product/create/").and(accept(MediaType.APPLICATION_JSON)),
                request -> template.save(request.bodyToMono(ProductModel.class), "products")
                        .then(ServerResponse.ok().build())
        );
    }
    @Bean
    public RouterFunction<ServerResponse> deleteProduct(){
        return route(
            DELETE("/product/delete/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> template.findAndRemove(
                    filterById(request.pathVariable("id")),
                        ProductModel.class,
                        "products"
                ).then(ServerResponse.ok().build()));
    }
    // Actualizar servicios
    @PutMapping("/product/update/{id}")
    public Mono<ResponseEntity<ProductModel>> updateProduct(@PathVariable String id, @RequestBody ProductModel productModel) {
        return service.updateProduct(id, productModel);
    }



    private Query filterById(String id) {
        return new Query(
                Criteria.where("id").is(id)
        );
    }
    
}
