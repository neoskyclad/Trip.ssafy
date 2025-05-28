package com.ssafy.tripssafy.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.ssafy.tripssafy.model.dto.TourSpotAddRequest;
import com.ssafy.tripssafy.model.dto.TripPlaceSetMemoRequest;

import lombok.NonNull;

@Mapper
public interface TripPlaceRepository {
	
	@Insert("INSERT INTO trip_room_place (room_id, place_id, place_name, place_mapx, place_mapy, visit_date, visit_order) " +
	        "VALUES (#{roomId}, #{placeId}, #{placeName}, #{mapX}, #{mapY}, #{visitDate}, #{visitOrder})")
	public void addPlace(TourSpotAddRequest request);
	
	@Update("update trip_room_place set visit_order = #{spotIndex} where id = #{roomId}")
	public void editPlace(@Param("roomId") Long roomId, @Param("spotIndex") int spotIndex);
	
	@Delete("delete from trip_room_place where id = #{tripRoomPlaceId}")
	public int deletePlace(Long tripRoomPlaceId);
	
	@Update("update trip_room_place set memo = #{memo} where id = #{tripRoomPlaceId}")
	public int setMemo(TripPlaceSetMemoRequest request);

	
	@Select("select 1 from trip_room_place where id = #{id}")
	public Integer findPlaceById(Long id);
}

