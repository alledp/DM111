package br.inatel.pos.dm111.vfa.persistence.user;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Profile("test")
@Component
public class MemoryUserRepositoryImpl implements UserRepository {

    private Map<String, User> db = new HashMap<>();

    @Override
    public Optional<User> getByEmail(String email) throws ExecutionException, InterruptedException {
        return db.values().stream()
                .filter(user -> user.email().equals(email))
                .findAny();
    }
}
