package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Iterable<Message>findAllByUid(Long id);
    Message findByIdAndUid(Long id, Long uid);
}
