package sofka.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;
import sofka.backend.interfaces.ProductInterface;
import sofka.backend.models.ProductModel;

@Service
public class ProductService {
    private final ProductInterface productInterface;

    @Autowired
    public ProductService(ProductInterface productInterface) {
        this.productInterface = productInterface;
    }

    public Mono<ResponseEntity<ProductModel>> updateProduct(String id, ProductModel productSend) {

        return productInterface.findById(id).flatMap(productFind -> {
            productFind.setName(IsNull.compareString(productSend.getName(), productFind.getName()));
            productFind.setInInventary(IsNull.compareInteger(productSend.getInInventary(), productFind.getInInventary()));
            productFind.setEnabled(IsNull.compareBoolean(productSend.getEnabled(), productFind.getEnabled()));
            productFind.setMax(IsNull.compareInteger(productSend.getMax(), productFind.getMax()));
            productFind.setMin(IsNull.compareInteger(productSend.getMin(), productFind.getMin()));
                    return productInterface.save(productFind);
                })
                .map(update -> ResponseEntity.ok().<ProductModel>build())
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
