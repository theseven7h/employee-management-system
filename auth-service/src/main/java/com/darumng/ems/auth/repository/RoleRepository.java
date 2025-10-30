package com.darumng.ems.auth.repository;

import com.darumng.ems.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}