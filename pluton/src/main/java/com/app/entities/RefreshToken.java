package com.app.entities;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String token;
	private Instant expiryDate;
	@OneToOne
	@JoinColumn(name="user_id",referencedColumnName="customerId")
	private Customer userInfo;
}
