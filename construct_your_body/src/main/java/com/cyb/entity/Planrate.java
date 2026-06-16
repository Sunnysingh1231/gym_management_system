package com.cyb.entity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Planrate {

	private int platinum = 1;
	 
	private int gold = 1;
	
	private int silver = 1;

}
