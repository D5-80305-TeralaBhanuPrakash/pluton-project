package com.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CustomerDTO;
import com.app.dto.RefreshTokenRequest;
import com.app.dto.SigninRequest;
import com.app.dto.SigninResponse;
import com.app.entities.RefreshToken;
import com.app.security.JwtUtils;
import com.app.service.CustomerService;
import com.app.service.RefreshTokenServiceImpl;

@RestController
@RequestMapping("/")
@CrossOrigin(origins="http://localhost:3000")
@Validated
public class SignInSignUpController {
	@Autowired
	private CustomerService custService;
	
	@Autowired
	private JwtUtils utils;

	@Autowired
	private AuthenticationManager mgr;
	
	@Autowired
	private RefreshTokenServiceImpl refTokenService;
	
	@PostMapping("/register")
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO custDto) {
            CustomerDTO createdCustomer = custService.addCustomer(custDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
       
    }
	
	@PostMapping("/signin")
	public ResponseEntity<?> signinUser(@RequestBody @Valid SigninRequest reqDTO) {
    	try {
            // Perform user authentication
            Authentication verifiedAuth = mgr.authenticate(
                    new UsernamePasswordAuthenticationToken(reqDTO.getEmail(), reqDTO.getPassword()));
            CustomerDTO custDto = custService.getCustomerByEmailAddress(reqDTO.getEmail());
            // If authentication is successful, return a JWT token
            if (verifiedAuth.isAuthenticated()) {
            	RefreshToken refToken = refTokenService.createRefreshToken(reqDTO.getEmail());
                return ResponseEntity.ok(new SigninResponse(utils.generateJwtToken(verifiedAuth), custDto,refToken.getToken()));
            } else {
                // If authentication fails, return a 401 Unauthorized status code
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
            }
        } catch (AuthenticationException e) {
        	
        	System.out.println("inside signin exception");
            // If an AuthenticationException occurs (e.g., invalid credentials), return a 401 Unauthorized status code
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
        }
	}
	
//	@PostMapping("/refreshToken")
//	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refTokenReq){
//		refTokenService.findByToken(refTokenReq.getToken()).map(refTokenService::verifyExpiration).map(RefreshToken::getUserInfo).map(userInfo -> {
//			String jwtToken = utils.generateJwtToken(null);
//		})
//	}
}
