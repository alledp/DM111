package br.inatel.pos.dm111.vfr.persistence.promotion;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface PromotionRepository {

    List<Promotion> getAll() throws ExecutionException, InterruptedException;

    Optional<Promotion> getById(String id) throws ExecutionException, InterruptedException;

    Optional<Promotion> getByName(String name);

    Optional<Promotion> getByRestaurant(String restaurant);

    Promotion save (Promotion promotion);

    void delete(String id) throws ExecutionException, InterruptedException;
}
