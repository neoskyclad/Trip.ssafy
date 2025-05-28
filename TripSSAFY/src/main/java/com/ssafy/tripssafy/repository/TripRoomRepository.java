package com.ssafy.tripssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ssafy.tripssafy.model.dto.TripRoomAddRequest;
import com.ssafy.tripssafy.model.entity.TripRoom;
import com.ssafy.tripssafy.model.entity.TripRoomPlace;

@Mapper
public interface TripRoomRepository {

	@Insert("insert into trip_room (title, start_date, end_date, created_at) "
			+ "values (#{title}, #{start_date}, #{end_date}, now())")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void addRoom(TripRoomAddRequest request);

	@Update("update trip_room set title = #{title}, start_date = #{start_date}, end_date = #{end_date} where id = #{id}")
	public void editRoom(TripRoomAddRequest request);
	
	@Select("select r.id, r.title, r.start_date, r.end_date, r.created_at from "
			+ "trip_room r join user_room_mapping urm on r.id = urm.room_id "
			+ "where urm.user_id = #{userId}")
	public List<TripRoom> findRoomsByUserId(Long userId);
	
	@Select("select * from trip_room where id = #{roomId}")
	public TripRoom getTripRoomById(Long roomId);

    @Select("""
        SELECT *
        FROM trip_room_place
        WHERE room_id = #{tripRoomId}
        ORDER BY visit_date, visit_order
        """)
    List<TripRoomPlace> getPlacesByRoomId(Long tripRoomId);
	
	@Insert("insert into user_room_mapping (user_id, room_id, created_at)"
			+ "values (#{userId}, #{roomId}, now())")
	public void addMember(@Param("userId") Long userId, @Param("roomId") Long roomId);
	
	@Select("select 1 from user_room_mapping where user_id = #{userId} and room_id = #{roomId}")
	public Integer existsUserInRoom(@Param("userId") Long userId, @Param("roomId") Long roomId);

	@Delete("delete from trip_room where id = #{roomId}")
	public int deleteRoom(Long roomId);
}
