package com.github.gustavoflor.fronzabarberapi.infrastructure.persistence;

import com.github.gustavoflor.fronzabarberapi.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}