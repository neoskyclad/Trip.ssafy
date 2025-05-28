package com.ssafy.tripssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ssafy.tripssafy.model.dto.ExpenseAddRequest;
import com.ssafy.tripssafy.model.dto.PlaceExpenseResponse;
import com.ssafy.tripssafy.model.dto.RoomExpenseResponse;

@Mapper
public interface ExpenseRepository {
	
	@Insert("""
		    insert into expenses (user_id, trip_place_id, amount)
		    select u.id, #{tripPlaceId}, #{amount}
		    from user u
		    where u.nickname = #{nickname}
		""")
		int addExpense(ExpenseAddRequest request);
	
	@Select("select tp.place_name, e.amount, tp.visit_date from expenses e "
			+ "join trip_room_place tp on e.trip_place_id = tp.id where tp.room_id = #{roomId} and e.user_id = #{userId} and e.amount > 0")
	List<RoomExpenseResponse> getExpensesByRoomAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);
	
	@Select("select u.profile_img, u.nickname, e.amount from expenses e"
			+ " join user u on u.id = e.user_id where e.trip_place_id = #{placeId}")
	List<PlaceExpenseResponse> getExpensesByPlaceId(Long placeId);
	
	
}
