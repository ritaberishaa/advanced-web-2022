package com.cacttus.rita.advanced.web.repository;

import com.cacttus.rita.advanced.web.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, Long> {
    User getUserByUsername(String username);
    User getUserByEmail(String email);

}
