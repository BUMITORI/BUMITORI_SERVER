package org.example.bumitori_server.repository;


import org.example.bumitori_server.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    UserAccount findByUsername(String username);

}
