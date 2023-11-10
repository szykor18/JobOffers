package pl.joboffers.domain.loginandregister;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository{
    private Map<String, User> userDatabase = new ConcurrentHashMap<>();
    @Override
    public User save(User user) {
        UUID id = UUID.randomUUID();
        User savedUser = User.builder()
                .id(id.toString())
                .username(user.username())
                .password(user.password())
                .build();
        userDatabase.put(savedUser.username(), savedUser);
        return savedUser;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userDatabase.get(username));
    }
}
