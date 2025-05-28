package com.ssafy.tripssafy.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

//관광지 API 요청을 위한 Request Form Data
@Data
@NoArgsConstructor
public class TourSpotSearchRequest {
	//한 페이지 결과 수
	private int numOfRows = 10;
	
	//페이지 번호 (1부터 시작)
	private int pageNo;
	
	//OS구분 : IOS, AND, WIN, ETC
	private String MobileOS = "ETC";
	
	//서비스 명
	private String MobileApp = "TripSSAFY";
	
	//response type json (null이면 xml)
	private String _type = "json";
	
	//지역코드
	private String areaCode;
	
	//시군구 코드
	private String sigunguCode;
	
	//관광지 종류 코드
	private Integer contentTypeId;
	
	//요청 키워드 검색어
	private String keyword;
	
	
}
