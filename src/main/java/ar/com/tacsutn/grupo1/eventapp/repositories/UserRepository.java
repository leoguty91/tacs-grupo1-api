package ar.com.tacsutn.grupo1.eventapp.repositories;

import ar.com.tacsutn.grupo1.eventapp.models.AuthorityName;
import ar.com.tacsutn.grupo1.eventapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    Optional<User> getById(String id);

    Optional<User> getUserByTelegramUserId(Integer telegramUserId);

    Page<User> findAll(Pageable pageable);

}
