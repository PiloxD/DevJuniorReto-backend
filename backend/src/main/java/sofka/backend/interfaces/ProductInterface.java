package sofka.backend.interfaces;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import sofka.backend.models.ProductModel;

@Repository
public interface ProductInterface extends ReactiveMongoRepository<ProductModel, String> {
}
