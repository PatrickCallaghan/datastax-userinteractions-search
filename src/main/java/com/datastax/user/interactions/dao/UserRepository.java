package com.datastax.user.interactions.dao;

import org.springframework.data.repository.CrudRepository;

import com.datastax.user.interactions.model.UserInteraction;

public interface UserRepository extends CrudRepository<UserInteraction, Long> {

	UserInteraction save(UserInteraction userInteraction);
}