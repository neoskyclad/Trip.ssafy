package com.ssafy.tripssafy.repository;

import java.util.Map;

/**
 * Search Query가 포함된 경우와 포함되지 않은 경우 동적 sql을 생성하기 위한 Provider
 */
public class PostSQLProvider {
    public String buildGetPostsByPage(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
        		select p.id, u.profile_img, u.nickname, p.title, p.content, p.rating, p.img, p.view_count, p.writer_id, p.created_at, p.updated_at
				from post p join user u on p.writer_id = u.id
				where p.post_type = #{type}
        		""");
        if (params.get("search") != null && !params.get("search").toString().isEmpty()) {
            sb.append("AND title LIKE CONCAT('%', #{search}, '%') ");
        }
        sb.append("ORDER BY created_at DESC ");
        sb.append("LIMIT #{limit} OFFSET #{offset}");
        return sb.toString();
    }
    
    public String buildPostsCount(Map<String, Object> params) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("""
    			select count(*) from post where post_type = #{type}
    			""");
    	if (params.get("search") != null && !params.get("search").toString().isEmpty()) {
            sb.append("AND title LIKE CONCAT('%', #{search}, '%') ");
        }
    	return sb.toString();
    }
}
