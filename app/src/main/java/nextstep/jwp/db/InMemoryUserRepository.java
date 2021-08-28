package nextstep.jwp.db;


import nextstep.jwp.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private final Map<String, User> database;
    private Long index = 1L;

    public InMemoryUserRepository() {
        database = new ConcurrentHashMap<>();
        saveInitUserData();
    }

    private void saveInitUserData() {
        final User user = new User("gugu", "password", "hkkang@woowahan.com");
        save(user);
    }

    public User save(User user) {
        final User userWithId = new User(index++, user.getAccount(), user.getPassword(), user.getEmail());
        database.put(userWithId.getAccount(), userWithId);
        return userWithId;
    }

    public Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    public boolean existsByAccount(String account) {
        return database.containsKey(account);
    }

    public boolean existsByEmail(String email) {
        return database.values().stream()
                .anyMatch(userInDB -> userInDB.hasSameEmail(email));
    }
}
