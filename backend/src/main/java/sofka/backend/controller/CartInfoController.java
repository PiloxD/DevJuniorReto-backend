package sofka.backend.controller;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import sofka.backend.models.CartInfoModel;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

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
                request -> template.save(request.bodyToMono(CartInfoModel.class), "Carts")
                        .then(ServerResponse.ok().build())
        );
    }
    @Bean
    public RouterFunction<ServerResponse> getHistory() {
        return route(
                GET("/cart/getall"),
                request -> template.findAll(CartInfoModel.class, "Carts")
                        .collectList()
                        .flatMap(list -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromPublisher(Flux.fromIterable(list), CartInfoModel.class)))
        );}

}
