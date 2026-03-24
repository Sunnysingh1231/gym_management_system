package com.cyb.service.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cyb.entity.MemberShipPaymentDetail;
import com.cyb.entity.User;
import com.cyb.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	//list all element in ordered manner
	//allow null 
	//allow duplicate value
//	List<User> listUsers = new ArrayList<>();
	
	List<MemberShipPaymentDetail> setMemberShipDetails = new ArrayList<>();
	
	public List<MemberShipPaymentDetail> getSetMemberShipDetails() {
		return setMemberShipDetails;
	}
	
	
	//unordered
	//no null value allow / no duplicate value allowed

	
	Set<User> setUsers = new HashSet<>();
	
	private static final SecureRandom random = new SecureRandom();
	
	
	
//	Set<MemberShipPaymentDetail> setMemberShipDetails = new HashSet<>();
	
	@Override
	public String generateUserId() {
		
		StringBuilder idString = new StringBuilder("000000");

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            idString.setCharAt(i, (char) ('0' + digit));
        }
		return idString.toString();
	}

	//register new use in set
	@Override
	public void addUser(User user) {
		
		setUsers.add(user);
		
	}

	//get all users stored in set(setUser)
	@Override
	public Set<User> findAllUser() {
		
		return setUsers;
		
	}

	@Override
	public User findUserByUserName(String username) {
		
		return setUsers.stream().filter(user->username.equals(user.getEmail())).findFirst().orElse(null);
	}
	
	
	

	@Override
	public void addMemberShip(MemberShipPaymentDetail memberShipDetail) {
		
		setMemberShipDetails.add(memberShipDetail);
		
	}

	@Override
	public MemberShipPaymentDetail findMemberShipByUserName(String userName) {
		
		return setMemberShipDetails.stream().filter(ms->userName.equals(ms.getEmail())).findFirst().orElse(null);
		
	}

	@Override
	public List<MemberShipPaymentDetail> findAllMemberShipDetail() {
		
		return setMemberShipDetails;
	}

	@Override
	public float bmiCalculator(float wt, int ht) {
		
		if(wt<1) {
			wt = 1;
		}
		if(ht<1) {
			ht = 1;
		}
		Float bmi = (wt*10000)/(ht*ht);
		return (float) (Math.round(bmi*10.0)/10.0);
	}

	@Override
	public List<String> lastPayment(String userID) {
		
//		String lastPayment = null;
		
		List<String> lastPayment = new ArrayList<>(3);
		
		for(MemberShipPaymentDetail memberShipPaymentDetail : setMemberShipDetails) {
			if(memberShipPaymentDetail.getEmail().equals(userID)) {
				lastPayment.clear();
				lastPayment.add(memberShipPaymentDetail.getMemberShipType());
				lastPayment.add(memberShipPaymentDetail.getMemberShipPrice());
				lastPayment.add(memberShipPaymentDetail.getMemberShipStartDate());
				lastPayment.add(memberShipPaymentDetail.getMemberShipEndDate());
			}			
		}
		return lastPayment;
	}

	@Override
	public List<MemberShipPaymentDetail> userAllPaymentDetail(String userId) {

		List<MemberShipPaymentDetail> memberShipPaymentDetails = new ArrayList<>();

		for(MemberShipPaymentDetail m1 : setMemberShipDetails) {
			if(m1.getEmail().equals(userId)) {
				memberShipPaymentDetails.add(m1);
			}
		}
		
		return memberShipPaymentDetails;
	}
	
}
