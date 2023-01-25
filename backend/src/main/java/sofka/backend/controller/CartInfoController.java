package sofka.backend.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mongodb.internal.connection.Server;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.backend.models.CartInfoModel;
import sofka.backend.models.ProductModel;
import sofka.backend.models.ProductsSelectedModel;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:4200")
@Configuration
public class CartInfoController {
    private ReactiveMongoTemplate template;

    public CartInfoController(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Bean
    public RouterFunction<ServerResponse> createCart() {
        return route(
                POST("/cart/create/").and(accept(MediaType.APPLICATION_JSON)),
                request -> {
                    validation(request).doOnNext((mono) -> ((Mono<List<ProductModel>>) mono).subscribe());
                    return saveCart(request).then(ServerResponse.ok().build());

                });
    }

    public Mono<ServerResponse> saveCart(ServerRequest request){
        return template.save(request.bodyToMono(CartInfoModel.class), "Carts")
        .then(ServerResponse.ok().build());
    }

    @Bean
    public RouterFunction<ServerResponse> getHistory() {
        return route(
                GET("/cart/getall"),
                request -> template.findAll(CartInfoModel.class, "Carts")
                        .collectList()
                        .flatMap(list -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(Flux.fromIterable(list), CartInfoModel.class))));
    }

    public Mono<List<ProductModel>> validation(ServerRequest request) {
        return getProductsInCart(request).flatMap(products -> {
            return stockList(products).flatMap(p -> {
                stockValidation(p, products);
                minValidation(p);
                minAndMaxValidation(p, products);
                return Mono.just(p);
            });
        });
    }

    public Mono<List<ProductsSelectedModel>> getProductsInCart(ServerRequest request) {
        return request.bodyToMono(CartInfoModel.class).flatMap(sales -> Mono.just(sales.getClientSelection()));
    }
    public Mono<List<ProductModel>> stockList(List<ProductsSelectedModel> products) {
        var idList = products.stream().map(p -> p.getIdProduct()).collect(Collectors.toList());
        return template.find(findProducts(idList), ProductModel.class, "products")
                .collectList();
    }
    private Query findProducts(List<String> id) {
        return new Query(Criteria.where("id").in(id));
    }
    private void minAndMaxValidation(List<ProductModel> products, List<ProductsSelectedModel> productsInCart) {
        products.forEach(product -> {
            var productoActual = productsInCart.stream()
                    .filter(sale -> sale.getIdProduct() == product.getId())
                    .findFirst().orElseThrow();
            if (product.getMin() < productoActual.getQuantity() ||
                    product.getMax() > productoActual.getQuantity()) {
                throw new RuntimeException("Incorrect min");
            }
            if (productoActual.getQuantity() > product.getInInventary()) {
                throw new RuntimeException("Incorrect max");
            }
        });
    }

    private void minValidation(List<ProductModel> products) {
        products.forEach(product -> {
            if (product.getInInventary() < product.getMin()) {
                throw new RuntimeException(product.getName() + " without stock");
            }
        });
    }
    public Mono<Void> stockValidation(List<ProductModel> productsList, List<ProductsSelectedModel> productsInCart) {
        if (productsList.size() != productsInCart.size()) {
            throw new RuntimeException(productsInCart.size() - productsList.size() + "Not available");
        }
        return Mono.empty();
    }
}
