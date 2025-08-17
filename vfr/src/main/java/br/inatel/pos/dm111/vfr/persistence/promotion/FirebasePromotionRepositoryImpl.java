package br.inatel.pos.dm111.vfr.persistence.promotion;

import com.google.cloud.firestore.Firestore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Profile("local")
@Component
public class FirebasePromotionRepositoryImpl implements PromotionRepository {

    private static final String COLLECTION_NAME = "promotions";

    private final Firestore firestore;

    public FirebasePromotionRepositoryImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public List<Promotion> getAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(doc -> doc.toObject(Promotion.class))
                .toList();
    }

    @Override
    public Optional<Promotion> getById(String id) throws ExecutionException, InterruptedException {
        var promotion = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(Promotion.class);

        return Optional.ofNullable(promotion);
    }

//    @Override
//    public Optional<Promotion> getByPromotionId(String promotionId) throws ExecutionException, InterruptedException {
//        return firestore.collection(COLLECTION_NAME)
//                .get()
//                .get()
//                .getDocuments()
//                .stream()
//                .map(doc -> doc.toObject(Promotion.class))
//                .filter(promotion -> promotion.id().equalsIgnoreCase(promotionId))
//                .findFirst();
//    }

    @Override
    public Promotion save(Promotion promotion) {
        firestore.collection(COLLECTION_NAME)
                .document(promotion.id())
                .set(promotion);
        return promotion;
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .get();
    }

    @Override
    public Optional<Promotion> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Promotion> getByRestaurant(String restaurant) {
        return Optional.empty();
    }
}
