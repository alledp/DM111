package br.inatel.pos.dm111.vfa.persistence.user;

import com.google.cloud.firestore.Firestore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Profile("local")
@Component
public class FirebaseUserRepositoryImpl implements UserRepository {

    private static final String COLLECTION_NAME = "users";

    private final Firestore firestore;

    public FirebaseUserRepositoryImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Optional<User> getByEmail(String email) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> doc.toObject(User.class))
                .filter(user -> user.email().equalsIgnoreCase(email))
                .findFirst();
    }
}
