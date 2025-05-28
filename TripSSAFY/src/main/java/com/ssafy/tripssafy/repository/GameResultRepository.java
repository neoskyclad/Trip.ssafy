package com.ssafy.tripssafy.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ssafy.tripssafy.model.dto.GameResultAddRequest;

@Mapper
public interface GameResultRepository {
	
	@Insert("insert into game_result (trip_place_id, result_url, created_at) values (#{tripPlaceId}, #{resultURL}, now())")
	int addResult(GameResultAddRequest request);
	
	@Select("select result_url from game_result where trip_place_id = #{placeId}")
	String getResultURL(Long placeId);
}
