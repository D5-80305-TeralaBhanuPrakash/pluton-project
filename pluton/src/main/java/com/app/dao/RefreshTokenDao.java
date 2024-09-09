package com.app.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.RefreshToken;

public interface RefreshTokenDao extends JpaRepository<RefreshToken,Integer>{

	Optional<RefreshToken> findByToken(String token);

}
