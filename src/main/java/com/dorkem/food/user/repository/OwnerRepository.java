package com.dorkem.food.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dorkem.food.user.entity.Owner;
import com.dorkem.food.user.entity.User;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

	Optional<Owner> findByUser(User user);
}
