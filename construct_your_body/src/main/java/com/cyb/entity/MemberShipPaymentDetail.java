package com.cyb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberShipPaymentDetail {
	
	private int id;
	
	private String email;
	
	private String memberShipType;
	
	private String memberShipPrice;
	
	private String memberShipStartDate;// = currentDate.format(formatter);

	private String memberShipEndDate;//= ScurrentDate.format(formatter);
	
	private String paymenDateTime;
	
	private long attendence;
	
	private boolean isAccountExpire;
	
}
