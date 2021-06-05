package com.gmdb.movieservice.dao;

import com.gmdb.movieservice.bean.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByName(String userName);
}
