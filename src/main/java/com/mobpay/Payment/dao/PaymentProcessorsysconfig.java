package com.mobpay.Payment.dao;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "paymentprocessor_sysconfig")
@Data
public class PaymentProcessorsysconfig {
	
	   @Id
	    @Column(name = "name")
	    private String name;
	    @Column(name = "value")
	    private String value;
	    @Column(name = "description")
	    private String description;
	    @Column(name = "createdAt")
	    private Date createdAt;
	    @Column(name = "updatedAt")
	    private String updatedAt;
}
