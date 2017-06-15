package pl.java.scalatech.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.java.scalatech.entity.User;

@Transactional
public interface UserRepository extends JpaRepository<User,Long>{
  Optional<User> findByLogin(String login);
}