package com.ssafy.tripssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.ssafy.tripssafy.model.dto.BoardPostAddRequest;
import com.ssafy.tripssafy.model.dto.PostItemResponse;
import com.ssafy.tripssafy.model.dto.PostUpdateRequest;
import com.ssafy.tripssafy.model.dto.ReviewPostAddRequest;
import com.ssafy.tripssafy.model.dto.RoomPostAddRequest;
import com.ssafy.tripssafy.model.entity.Post;

@Mapper
public interface PostRepository {
	
	@Insert("insert into post (title, img, content, rating, place_id, post_type, writer_id, created_at, updated_at)"
			+ " values (#{request.title}, #{request.imgUrl}, #{request.content}, #{request.rating}, #{request.placeId}, 'REVIEW', #{userId}, now(), null)")
	public void addReviewPost(@Param("request") ReviewPostAddRequest request, @Param("userId") Long userId);
	
	@Insert("insert into post (title, img, content, room_id, post_type, writer_id, created_at, updated_at)"
			+ " values (#{request.title}, #{request.imgUrl}, #{request.content}, #{request.roomId}, 'room', #{userId}, now(), null)")
	public void addRoomPost(@Param("request") RoomPostAddRequest request, @Param("userId") Long userId);
	
	@Insert("insert into post (title, img, content, post_type, writer_id, created_at, updated_at)"
			+ " values (#{request.title}, #{request.imgUrl}, #{request.content}, #{request.type}, #{userId}, now(), null)")
	public void addBoardPost(@Param("request")BoardPostAddRequest request, @Param("userId") Long userId);
	
	@Select("""
			select p.id, u.profile_img, u.nickname, p.title, p.content, p.rating, p.img, p.view_count, p.writer_id, p.created_at, p.updated_at
			from post p join user u on p.writer_id = u.id
			where p.place_id = #{placeId}
			""")
	public List<PostItemResponse> getReviewPostsByPlaceId(Long placeId);
	
	@Select("""
			select p.id, u.profile_img, u.nickname, p.title, p.content, p.rating, p.img, p.view_count, p.writer_id, p.created_at, p.updated_at
			from post p join user u on p.writer_id = u.id
			where p.room_id = #{roomId}
			""")
	public List<PostItemResponse> getRoomPostsByRoomId(Long roomId);
	
	@SelectProvider(type = PostSQLProvider.class, method = "buildGetPostsByPage")
	public List<PostItemResponse> getPostsByTypeAndSearchQuery(@Param("search") String search, 
																@Param("limit") int limit, 
																@Param("type") String type, 
																@Param("offset") int offset);
	
	@SelectProvider(type = PostSQLProvider.class, method="buildPostsCount")
	public Integer getPostsCount(@Param("type") String type, @Param("search") String search);
	
	@Update("update post set title = #{request.title}, content = #{request.content}, rating = #{request.rating}, img = #{request.imgUrl}, updated_at = now() where id = #{postId}")
	public void updatePost(@Param("request") PostUpdateRequest request, @Param("postId") Long postId);
	
	@Select("select * from post where id = #{id}")
	public Post getPostById(Long id);
	
	@Select("""
			select p.id, u.profile_img, u.nickname, p.title, p.content, p.rating, p.img, p.view_count, p.writer_id, p.created_at, p.updated_at
			from post p join user u on p.writer_id = u.id
			where p.id = #{id}
			""")
	public PostItemResponse getPostDetailById(Long id);
	
	@Delete("delete from post where id = #{id}")
	public void deletePost(Long id);

	@Update("update post set view_count = view_count + 1 where id = #{id}")
	public void addViewCount(Long id);
}
