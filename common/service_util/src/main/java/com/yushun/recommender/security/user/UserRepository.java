package com.yushun.recommender.security.user;

import com.yushun.recommender.model.common.User;
import com.yushun.recommender.security.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * <p>
 * Get and Init User Role
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-12
 */

@Repository
public class UserRepository {
    private static final Map<String, User> allUsers = new HashMap<>();

    @Autowired
    private SecurityUserService userService;

    @PostConstruct
    protected void init() {
//        allUsers.put("admin", new User("admin", passwordEncoder.encode("123456"), Collections.singletonList("ROLE_ADMIN")));

        for(User user:userService.list()) {
            if(user.getRoles().isEmpty()) user.setRoles(Collections.singletonList("ROLE_USER"));

            if(user.getPassword() == null) {
                allUsers.put(user.getEmail(), new User(user.getUsername(),
                        user.getEmail(),
                        Collections.singletonList(user.getRoles().get(0))));
            }else {
                allUsers.put(user.getEmail(), new User(user.getUsername(),
                        user.getPassword(),
                        Collections.singletonList(user.getRoles().get(0))));
            }
        }
    }

    public void addSystemUser(User user) {
        allUsers.put(user.getEmail(), new User(user.getUsername(),
                user.getPassword(),
                Collections.singletonList("ROLE_USER")));
    }

    public void addGoogleUser(User user) {
        allUsers.put(user.getEmail(), new User(user.getUsername(),
                user.getEmail(),
                Collections.singletonList("ROLE_USER")));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(allUsers.get(username));
    }
}