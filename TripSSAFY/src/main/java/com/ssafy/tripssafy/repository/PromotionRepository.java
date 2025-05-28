package com.ssafy.tripssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ssafy.tripssafy.model.dto.PromotionAddRequest;
import com.ssafy.tripssafy.model.entity.Promotion;

@Mapper
public interface PromotionRepository {

	@Select("select * from promotion where content_id=#{id} order by created_at desc limit 10")
	List<Promotion> getLatestTen(Long id);

	@Insert("""
			insert into promotion (content_id, title, content, img, url, writer_id, created_at)
			values (#{request.contentId}, #{request.title}, #{request.content}, #{request.imgUrl}, #{request.url}, #{userId}, now())
			""")
	void insert(@Param("request") PromotionAddRequest request, @Param("userId") Long userId);

	@Delete("delete from promotion where id = #{id}")
	int delete(Long id);

	@Select("select * from promotion where id = #{id}")
	Promotion findById(Long id);
}
