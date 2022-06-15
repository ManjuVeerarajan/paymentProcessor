package com.mobpay.Payment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import com.mobpay.Payment.Repository.PaymentProcessorAuthRepository;
import com.mobpay.Payment.dao.PaymentProcessorAuthDao;
import com.mobpay.Payment.dao.PaymentProcessorsysconfig;

@Configuration
@EnableWebSecurity
@Order(1)
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	PaymentProcessorAuthRepository paymentProcessorAuthRepository;
	

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
       // APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
        APIKeyAuthFilter filter = new APIKeyAuthFilter(getKeyValueFromDBString(), getKeySecretValueFromDB());
        filter.setAuthenticationManager(new AuthenticationManager() {
            
            @Override
			public Authentication authenticate(Authentication authentication) throws BadCredentialsException {
				String principal = (String) authentication.getPrincipal();

				String[] secretfromDb = getSecretValueFromDB();
				List<String> secretList = Arrays.asList(secretfromDb);

				if (!secretList.contains(principal)) {
					throw new BadCredentialsException("401");
				}
				/*
				 * if (!principalRequestValue.equals(principal)) { throw new
				 * BadCredentialsException("401"); }
				 */
				authentication.setAuthenticated(true);
				return authentication;
			}
        });
        httpSecurity.antMatcher("/**").csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(filter).authorizeRequests()
                .anyRequest().authenticated();
    }
    
    @Bean
	public HashMap<String,String> getKeySecretValueFromDB() {
		HashMap<String,String> keyMap = new HashMap<String,String>();
		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
		String[] keyfromDb = new String[configValues.size()+1];
		System.out.println("Size " +configValues.size());
		for (int i=0 ;i<configValues.size(); i++) {
			keyMap.put(configValues.get(i).getApi_key().toLowerCase(),configValues.get(i).getApi_secret());
		}
		return keyMap;
	}
    

    /*
    @Bean
   	public HashMap<String,String> getSecretValueFromDB() {
   		HashMap<String,String> secretMap = new HashMap<String,String>();
   		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
   		System.out.println("configValues 1" +configValues.get(0).getApi_key());
   		System.out.println("Size " +configValues.size());
   		for (int i=0 ;i<configValues.size(); i++) {
   			secretMap.put(configValues.get(i).getId(),configValues.get(i).getApi_secret());
   		}
   		System.out.println("secretMap " +secretMap);
   		
   		return secretMap;
   	}*/
    public String[] getKeyValueFromDBString() {
  		HashMap<String,String> keyMap = new HashMap<String,String>();
  		
  		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
  		String[] keyfromDb = new String[configValues.size()+1];
  		System.out.println("Size " +configValues.size());
  		for (int i=0 ;i<configValues.size(); i++) {
  			keyfromDb[i] = configValues.get(i).getApi_key().toLowerCase();
  		}
  		System.out.println("keyfromDb " +keyfromDb);
  		
  		return keyfromDb;
  	}
    
    @Bean
  	public String[] getSecretValueFromDB() {
  		HashMap<String,String> keyMap = new HashMap<String,String>();
  		
  		List<PaymentProcessorAuthDao> configValues = paymentProcessorAuthRepository.findAll();
  		String[] secretfromDb = new String[configValues.size()+1];
  		System.out.println("Size " +configValues.size());
  		for (int i=0 ;i<configValues.size(); i++) {
  			secretfromDb[i] = configValues.get(i).getApi_secret();
  		}
  		System.out.println("secretfromDb " +secretfromDb);
  		
  		return secretfromDb;
  	}

}