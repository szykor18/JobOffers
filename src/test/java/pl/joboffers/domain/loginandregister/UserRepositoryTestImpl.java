package pl.joboffers.domain.loginandregister;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryTestImpl implements UserRepository{
    private Map<String, User> userDatabase = new ConcurrentHashMap<>();

    @Override
    public User save(User userToSave) {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id.toString())
                .username(userToSave.username())
                .password(userToSave.password())
                .build();
        userDatabase.put(user.username(), user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userDatabase.get(username));
    }
}
