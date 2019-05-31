package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    //User findByIdMessagesId(Long uid, Long mid);
    //Iterable<Message> findByUsernameAndMessages(String username);
}
