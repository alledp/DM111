package br.inatel.pos.dm111.vfr.persistence.promotion;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Profile("test")
@Component
public class MemoryPromotionRepositoryImpl implements PromotionRepository{

    private Map<String, Promotion> db = new HashMap<>();

    @Override
    public List<Promotion> getAll() {
        return db.values().stream().toList();
    }

    @Override
    public Optional<Promotion> getById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Optional<Promotion> getByName(String name) {
        return db.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(promotion -> promotion.name().equals(name))
                .findAny();
    }

    @Override
    public Optional<Promotion> getByRestaurant(String restaurant) {
        return db.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(promotion -> promotion.restaurantName().equals(restaurant))
                .findAny();
    }

    @Override
    public Promotion save(Promotion promotion) {
        return db.put(promotion.id(), promotion);
    }

    @Override
    public void delete(String id) {
        db.values().removeIf(promotion -> promotion.id().equals(id));
    }
}
