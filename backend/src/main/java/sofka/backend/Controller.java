package sofka.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class Controller {

    private  ReactiveMongoTemplate template;

    public Controller(ReactiveMongoTemplate template) {
        this.template = template;
    } 



    @Bean
    public RouterFunction<ServerResponse> createProduct() {
        return route(
                POST("/product/create/").and(accept(MediaType.APPLICATION_JSON)),
                request -> template.save(request.bodyToMono(ProductModel.class), "products")
                        .then(ServerResponse.ok().build())
        );
    }
    
}
