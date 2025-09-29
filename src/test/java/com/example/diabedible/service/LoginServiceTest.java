package com.example.diabedible.service;

import com.example.diabedible.model.Role;
import com.example.diabedible.model.User;
import com.example.diabedible.repository.UserRepository;
import com.example.diabedible.utils.HashUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class LoginServiceTest {

    static class FakeRepo implements UserRepository {
        private final Map<String, StoredUser> map = new HashMap<>();
        @Override
        public Optional<StoredUser> findByUsername(String username) {
            return Optional.ofNullable(map.get(username));
        }
        @Override
        public void save(StoredUser user) {
            map.put(user.username(), user);
        }
        @Override
        public Collection<StoredUser> findAll() {
            return map.values();
        }
        void put(StoredUser u){ save(u);}
    }

    @Test
    void login_validUser_success() {
        FakeRepo repo = new FakeRepo();
        String token = HashUtils.createPasswordToken("secret");
        repo.put(new UserRepository.StoredUser(UUID.randomUUID().toString(), "alice", token, Role.DIABETIC, "Alice", null));
        LoginService service = new LoginService(repo);

        Optional<User> user = service.login("alice", "secret");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(Role.DIABETIC, user.get().getRole());
        Assertions.assertEquals("alice", user.get().getUsername());
        Assertions.assertEquals("Alice", user.get().getDisplayName());
    }

    @Test
    void login_invalidPassword_empty() {
        FakeRepo repo = new FakeRepo();
        String token = HashUtils.createPasswordToken("secret");
        repo.put(new UserRepository.StoredUser(UUID.randomUUID().toString(), "bob", token, Role.DOCTOR, "Dr Bob", null));
        LoginService service = new LoginService(repo);

        Optional<User> user = service.login("bob", "wrong");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void login_userNotFound_empty() {
        FakeRepo repo = new FakeRepo();
        LoginService service = new LoginService(repo);
        Optional<User> user = service.login("nobody", "pass");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void login_repositoryThrows_wrapsAsAuthenticationException() {
        UserRepository badRepo = new UserRepository() {
            @Override
            public Optional<StoredUser> findByUsername(String username) {
                throw new RuntimeException("boom");
            }
            @Override
            public void save(StoredUser user) { }
            @Override
            public Collection<StoredUser> findAll() { return List.of(); }
        };
        LoginService service = new LoginService(badRepo);
        Assertions.assertThrows(AuthenticationException.class, () -> service.login("x", "y"));
    }
}
