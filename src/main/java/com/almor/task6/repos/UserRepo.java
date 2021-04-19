package com.almor.task6.repos;

import com.almor.task6.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User,Long> {
    User findByName(String name);
}
