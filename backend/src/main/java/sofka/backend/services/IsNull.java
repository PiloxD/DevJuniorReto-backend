package sofka.backend.services;

import java.time.Instant;

import static java.util.Objects.isNull;

public class IsNull {

    static public String compareString(String newProduct, String lastProduct) {
        if (isNull(newProduct)) {
            return lastProduct;
        }
        return newProduct;
    }

    static public Integer compareInteger(Integer newProduct, Integer lastProduct) {

        if (isNull(newProduct)) {
            return lastProduct;
        }
        return newProduct;
    }
    static public Boolean compareBoolean(Boolean newProduct, Boolean lastProduct) {

        if (isNull(newProduct)) {
            return lastProduct;
        }
        return newProduct;
    }

    static public Instant compareInstant(Instant newProduct, Instant lastProduct) {
        if ((isNull(newProduct))) {
            return Instant.now();
        }
        return newProduct;
    }

}