package com.ssafy.tripssafy.controller;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ssafy.tripssafy.model.dto.TourSpotDetailResponse;
import com.ssafy.tripssafy.model.dto.TourSpotSearchRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tour-spot")
@Slf4j
public class TourSpotController {
	
	private final RestTemplate restTemplate;

	@Autowired
	public TourSpotController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Value("${tour-api.key}")
	private String apiSearchKey;
	
	private final String TOUR_API_URL = "http://apis.data.go.kr/B551011/KorService1";
	
	@Operation(summary = "지역 코드 전체 검색", description = "모든 지역 코드를 불러옵니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "API 호출 밑 지역코드 로드 성공"),
	    @ApiResponse(responseCode = "400", description = "RequestParam 형식 오류"),
	    @ApiResponse(responseCode = "500", description = "TourApi4.0 서버 오류"),
	})
	@GetMapping("/area-code")
	public ResponseEntity<?> getAreaCode(){
		String searchURI = TOUR_API_URL + "/areaCode1";
		
		URI uri = UriComponentsBuilder
				.fromUriString(searchURI)
                .queryParam("serviceKey", apiSearchKey)
                .queryParam("numOfRows", 17)	//다 가져오기
                .queryParam("pageNo", 1)
                .queryParam("MobileApp", "TripSSAFY")
                .queryParam("MobileOS", "ETC")
                .queryParam("_type", "json")
                .build(true) // true → 인코딩 자동 처리
                .toUri();
		
		log.info("Request URL: {}", uri.toString());
		
		try {
			Object response = restTemplate.getForObject(uri, Object.class);
		    return ResponseEntity.ok(response);
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
		    log.error("API error response: {}", ex.getResponseBodyAsString());
		    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("errorMsg", ex.getMessage()));
		} catch (Exception ex) {
		    log.error("Unexpected error: ", ex);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMsg", ex.getMessage()));
		}
	}
	
	@Operation(summary = "시군구 코드 전체 검색", description = "특정 areaCode의 모든 시군구 코드를 불러옵니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "API 호출 밑 시군구 코드 로드 성공"),
	    @ApiResponse(responseCode = "400", description = "RequestParam 형식 오류"),
	    @ApiResponse(responseCode = "500", description = "TourApi4.0 서버 오류"),
	})
	@GetMapping("/sigungu-code/{area-code}")
	public ResponseEntity<?> getSigunguCode(@PathVariable("area-code") int areaCode){
		String searchURI = TOUR_API_URL + "/areaCode1";
		
		URI uri = UriComponentsBuilder
				.fromUriString(searchURI)
                .queryParam("serviceKey", apiSearchKey)
                .queryParam("numOfRows", 50)	//다 가져오기 위해 최대 개수 설정
                .queryParam("pageNo", 1)
                .queryParam("areaCode", areaCode)
                .queryParam("MobileApp", "TripSSAFY")
                .queryParam("MobileOS", "ETC")
                .queryParam("_type", "json")
                .build(true) // true → 인코딩 자동 처리
                .toUri();
		
		log.info("Request URL: {}", uri.toString());
		
		try {
			
		    Object response = restTemplate.getForObject(uri, Object.class);
		    return ResponseEntity.ok(response);
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
		    log.error("API error response: {}", ex.getResponseBodyAsString());
		    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("errorMsg", ex.getMessage()));
		} catch (Exception ex) {
		    log.error("Unexpected error: ", ex);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMsg", ex.getMessage()));
		}
	}
	
	@Operation(summary = "관광지 검색", description = "searchRequest에 keyword param을 포함하여 관광지 검색 API를 호출합니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "API 호출 밑 관광지 로드 성공"),
	    @ApiResponse(responseCode = "400", description = "RequestParam 형식 오류"),
	    @ApiResponse(responseCode = "500", description = "TourApi4.0 서버 오류"),
	})
	@GetMapping("/search")
	public ResponseEntity<?> searchWithKeyword(
			@ModelAttribute TourSpotSearchRequest searchRequest) {
		
		String searchURI = TOUR_API_URL + "/searchKeyword1";
		
		String encodedKeyword = URLEncoder.encode(searchRequest.getKeyword(), StandardCharsets.UTF_8);

		URI uri = UriComponentsBuilder
                .fromUriString(searchURI)
                .queryParam("serviceKey", apiSearchKey)
                .queryParam("numOfRows", searchRequest.getNumOfRows())
                .queryParam("pageNo", searchRequest.getPageNo())
                .queryParam("MobileApp", searchRequest.getMobileApp())
                .queryParam("MobileOS", searchRequest.getMobileOS())
                .queryParam("contentTypeId", searchRequest.getContentTypeId())
                .queryParam("_type", searchRequest.get_type())
                .queryParam("keyword", encodedKeyword)
                .queryParam("areaCode", searchRequest.getAreaCode())
                .queryParam("sigunguCode", searchRequest.getSigunguCode())
                .build(true) // true → 인코딩 자동 처리
                .toUri();
		
		log.info("Request URL: {}", uri.toString());
		
		try {
			Object response = restTemplate.getForObject(uri, Object.class);
		    return ResponseEntity.ok(response);
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
		    log.error("API error response: {}", ex.getResponseBodyAsString());
		    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("errorMsg", ex.getMessage()));
		} catch (Exception ex) {
		    log.error("Unexpected error: ", ex);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMsg", ex.getMessage()));
		}
	}

	
	@Operation(summary = "관광지 상세 정보 (공통)", description = "contentId를 받아서 관광지의 공통 상세 정보를 가져옵니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "API 호출 밑 관광지 공통 정보 로드 성공"),
	    @ApiResponse(responseCode = "500", description = "TourApi4.0 서버 오류"),
	})
	@GetMapping("/{contentId}")
	public ResponseEntity<?> getDetailCommon(@PathVariable("contentId") Long contentId) {
		
		String searchURI = TOUR_API_URL + "/detailCommon1";
		
		URI uri = UriComponentsBuilder
                .fromUriString(searchURI)
                .queryParam("serviceKey", apiSearchKey)
                .queryParam("MobileApp", "TripSSAFY")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("_type", "json")
                .queryParam("defaultYN", "Y")
                .queryParam("firstImageYN", "Y")
                .queryParam("addrinfoYN", "Y")
                .queryParam("mapinfoYN", "Y")
                .queryParam("overviewYN", "Y")
                .build(true) // true → 인코딩 자동 처리
                .toUri();
		log.info("Request URL: {}", uri.toString());
		try {
		    Object response = restTemplate.getForObject(uri, Object.class);
		    return ResponseEntity.ok(response);
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
		    log.error("API error response: {}", ex.getResponseBodyAsString());
		    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("errorMsg", ex.getMessage()));
		} catch (Exception ex) {
		    log.error("Unexpected error: ", ex);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMsg", ex.getMessage()));
		}
	}
	
	@Operation(summary = "관광지 상세 정보 (상세)", description = "contentId를 받아서 관광지의 상세 정보를 가져옵니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "API 호출 밑 관광지 공통 정보 로드 성공"),
	    @ApiResponse(responseCode = "500", description = "TourApi4.0 서버 오류"),
	})
	@GetMapping("/detail")
	public ResponseEntity<?> getDetailIntro(@RequestParam("contentId") Long contentId, @RequestParam("contentTypeId") Long contentTypeId) {
		
		String searchURI = TOUR_API_URL + "/detailIntro1";
		
		URI uri = UriComponentsBuilder
                .fromUriString(searchURI)
                .queryParam("serviceKey", apiSearchKey)
                .queryParam("MobileApp", "TripSSAFY")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("_type", "json")
                .build(true) // true → 인코딩 자동 처리
                .toUri();
		log.info("Request URL: {}", uri.toString());
		try {
			Object response = restTemplate.getForObject(uri, Object.class);
			return ResponseEntity.ok(response);

		} catch (HttpClientErrorException | HttpServerErrorException ex) {
		    log.error("API error response: {}", ex.getResponseBodyAsString());
		    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("errorMsg", ex.getMessage()));
		} catch (Exception ex) {
		    log.error("Unexpected error: ", ex);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMsg", ex.getMessage()));
		}
	}
	
	@Operation(summary = "관광지 상세 정보 (상세)", description = "contentId를 받아서 관광지의 상세 정보를 가져옵니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "API 호출 밑 관광지 공통 정보 로드 성공"),
	    @ApiResponse(responseCode = "500", description = "TourApi4.0 서버 오류"),
	})
	@GetMapping("/image")
	public ResponseEntity<?> getDetailImages(@RequestParam("contentId") Long contentId) {
		
		String searchURI = TOUR_API_URL + "/detailImage1";
		
		URI uri = UriComponentsBuilder
                .fromUriString(searchURI)
                .queryParam("serviceKey", apiSearchKey)
                .queryParam("MobileApp", "TripSSAFY")
                .queryParam("MobileOS", "ETC")
                .queryParam("contentId", contentId)
                .queryParam("_type", "json")
                .queryParam("imageYN", "Y")
                .queryParam("subImageYN", "Y")
                .build(true) // true → 인코딩 자동 처리
                .toUri();
		log.info("Request URL: {}", uri.toString());
		try {
			Object response = restTemplate.getForObject(uri, Object.class);
			return ResponseEntity.ok(response);

		} catch (HttpClientErrorException | HttpServerErrorException ex) {
		    log.error("API error response: {}", ex.getResponseBodyAsString());
		    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("errorMsg", ex.getMessage()));
		} catch (Exception ex) {
		    log.error("Unexpected error: ", ex);
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errorMsg", ex.getMessage()));
		}
	}
}
