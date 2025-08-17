package br.inatel.pos.dm111.vfr.persistence.user;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserRepository {

    Optional<User> getById(String userId) throws ExecutionException, InterruptedException;

    Optional<User> getByEmail(String email) throws ExecutionException, InterruptedException;
}
