package com.app.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.CustomerDao;
import com.app.dao.RefreshTokenDao;
import com.app.entities.RefreshToken;

@Service
@Transactional
public class RefreshTokenServiceImpl {
	@Autowired
	private RefreshTokenDao refTokenDao;
	
	@Autowired
	private CustomerDao custDao;
	
	public RefreshToken createRefreshToken(String username) {
		RefreshToken refToken = RefreshToken.builder()
				.userInfo(custDao.findByEmail(username).orElseThrow())
				.token(UUID.randomUUID().toString())
				.expiryDate(Instant.now().plusMillis(600000))
				.build();
		return refTokenDao.save(refToken);
	}
	
	public Optional<RefreshToken> findByToken(String token){
		return refTokenDao.findByToken(token);
	}
	
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now())<0) {
			refTokenDao.delete(token);
			throw new RuntimeException(token.getToken()+"Refresh Token is expired.");
		}
		return token;
	}
}
