package com.cyb.service;

import java.util.List;
import java.util.Set;

import com.cyb.entity.MemberShipPaymentDetail;
import com.cyb.entity.User;

public interface UserService {
	
	String generateUserId();

	void addUser(User user);
	
	User findUserByUserName(String username);
		
	Set<User> findAllUser();
	
	
	
	List<MemberShipPaymentDetail> getSetMemberShipDetails();
	
	void addMemberShip(MemberShipPaymentDetail memberShipDetail);
	
	MemberShipPaymentDetail findMemberShipByUserName(String username);
	
	List<MemberShipPaymentDetail> findAllMemberShipDetail();
	
	float bmiCalculator(float wt, int ht);
	
	List<String> lastPayment(String userID);
	
	List<MemberShipPaymentDetail> userAllPaymentDetail(String userId);
}
