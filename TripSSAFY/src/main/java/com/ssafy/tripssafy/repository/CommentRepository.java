package com.ssafy.tripssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ssafy.tripssafy.model.dto.CommentAddRequest;
import com.ssafy.tripssafy.model.dto.CommentItemResponse;

@Mapper
public interface CommentRepository {

	@Insert("insert into comment (content, writer_id, post_id, created_at)"
			+ " values (#{request.content}, #{userId}, #{request.postId}, now())")
	void addComment(@Param("request") CommentAddRequest request, @Param("userId") Long userId);
	
	@Update("update comment set content = #{content}, updated_at = now() where id = #{id}")
	int updateComment(@Param("id") Long id, @Param("content") String content);
	
	@Delete("delete from comment where id = #{id}")
	int deleteComment(Long id);

	@Select("select c.id, u.profile_img, u.nickname, c.content, c.writer_id, c.created_at, c.updated_at"
			+ " from comment c join user u on c.writer_id = u.id where c.post_id = #{postId}")
	List<CommentItemResponse> getCommentsByPostId(Long postId);
}
