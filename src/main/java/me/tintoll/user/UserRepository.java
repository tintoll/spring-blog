package me.tintoll.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by tintoll on 2017. 5. 20..
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByGithub(String github);
}
