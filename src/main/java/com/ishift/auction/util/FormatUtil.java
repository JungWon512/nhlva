package com.ishift.auction.util;


import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Format변환용 클래스
 * @author photoprogrammer
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FormatUtil {


	/**
	 * 날짜에 .를 추가해서 리턴
	 * @param input
	 * @return 날짜 정보
	 */
	public String dateAddDot(String input) {
		String result = "";

		try{
			if(input.length()==8) {
				result = input.substring(0,4)+"."+input.substring(4,6)+"."+input.substring(6,8);
			} else {
				result = input;
			}
		}catch (RuntimeException re) {
			return input;
		}
		return result;
	}
	public String dateAddDotLenSix(String input) {
		String result = "";

		try{
			if(input.length()==6) {
				result = input.substring(0,4)+"."+input.substring(4,6);
			} else {
				result = input;
			}
		}catch (RuntimeException re) {
			return input;
		}
		return result;
	}
	/**
	 * 날짜에 .를 추가해서 월까지만 리턴
	 * @param input
	 * @return 날짜 정보
	 */
	public String dateAddDotMonth(String input) {
		String result = "";

		try{
			if(input.length()>=8) {
				result = input.substring(2,4)+"."+input.substring(4,6);
			} else {
				result = input;
			}
		}catch (RuntimeException re) {
			return input;
		}
		return result;
	}
}