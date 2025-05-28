package com.ssafy.tripssafy.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ssafy.tripssafy.model.dto.LoginRequest;
import com.ssafy.tripssafy.model.dto.LoginResponse;
import com.ssafy.tripssafy.model.dto.RoomMemberResponse;
import com.ssafy.tripssafy.model.dto.SignupRequest;
import com.ssafy.tripssafy.model.dto.UpdatePasswordRequest;
import com.ssafy.tripssafy.model.dto.UpdateProfileRequest;
import com.ssafy.tripssafy.model.entity.Role;
import com.ssafy.tripssafy.model.entity.User;

@Mapper
public interface UserRepository {
	
	@Select("select * from user where id = #{id}")
	Optional<User> findById(Long id);

	@Select("select * from user where username = #{username}")
	Optional<User> findByUsername(String username);
	
	@Select("""
			select * from user where username like concat('%', #{username}, '%')
			or nickname like concat('%', #{username}, '%')
			order by char_length(username), char_length(nickname)
			limit 5
			""")
	
	List<User> findByUsernameOrNickname(String username);
	
	@Insert("INSERT INTO user (username, password, nickname, role, created_at) VALUES (#{username}, #{password}, #{nickname}, #{role}, now())")
	int insert(SignupRequest request);
	
	@Update("UPDATE user SET profile_img = #{profileImg} WHERE id = #{id}")
	int updateProfileImage(UpdateProfileRequest request);

	@Update("UPDATE user SET password = #{password} WHERE username = #{username}")
	int updatePassword(LoginRequest request);
	
	@Select("""
	        SELECT u.nickname, u.profile_img
	        FROM user u
	        JOIN user_room_mapping urm ON u.id = urm.user_id
	        WHERE urm.room_id = #{tripRoomId}
	        """)
	List<RoomMemberResponse> getUsersByTripRoomId(Long tripRoomId);

	
}
