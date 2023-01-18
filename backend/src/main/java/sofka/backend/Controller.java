package sofka.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;


import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class Controller {

    private  ReactiveMongoTemplate template;

    public Controller(ReactiveMongoTemplate template) {
        this.template = template;
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
            DELETE("/product/delete/{name}").and(accept(MediaType.APPLICATION_JSON)),
                request -> template.findAndRemove(
                        filterByName(request.pathVariable("name")),
                        ProductModel.class,
                        "products"
                ).then(ServerResponse.ok().build()));
    }

    private Query filterByName(String name) {
        return new Query(
                Criteria.where("name").is(name)
        );
    }
    
}
