package br.inatel.pos.dm111.vfa.persistence.user;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserRepository {

    Optional<User> getByEmail(String email) throws ExecutionException, InterruptedException;
}
