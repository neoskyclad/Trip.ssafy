package com.ssafy.tripssafy.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.tripssafy.exception.EntityNotFoundException;
import com.ssafy.tripssafy.model.dto.CommentItemResponse;
import com.ssafy.tripssafy.model.dto.BoardPostAddRequest;
import com.ssafy.tripssafy.model.dto.PostDetailResponse;
import com.ssafy.tripssafy.model.dto.PostItemResponse;
import com.ssafy.tripssafy.model.dto.PostPaginationResponse;
import com.ssafy.tripssafy.model.dto.PostUpdateRequest;
import com.ssafy.tripssafy.model.dto.ReviewPostAddRequest;
import com.ssafy.tripssafy.model.dto.RoomPostAddRequest;
import com.ssafy.tripssafy.model.entity.Post;
import com.ssafy.tripssafy.repository.CommentRepository;
import com.ssafy.tripssafy.repository.PostRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostRepository postRepository;
	
	private final CommentRepository commentRepository;
	
	private final IdentityService identityService;
	
	private final S3Service s3Service;
	//여행지 리뷰 글 작성
	@Transactional
	public void addReviewPost(ReviewPostAddRequest request) throws IOException {
		//사진이 첨부되어 있다면 사진 s3에 올리기 
		if(request.getFile() != null) {
			String imageUrl = s3Service.uploadFile(request.getFile());
			request.setImgUrl(imageUrl);
		}
		Long currentUserId = identityService.getCurrentUserId();
		
		postRepository.addReviewPost(request, currentUserId);
	}
	
	// 여행 방 글 작성
	@Transactional
	public void addRoomPost(RoomPostAddRequest request) throws IOException {
		if(request.getFile() != null) {
			String imageUrl = s3Service.uploadFile(request.getFile());
			request.setImgUrl(imageUrl);
		}
		Long currentUserId = identityService.getCurrentUserId();
		
		postRepository.addRoomPost(request, currentUserId);
	}
	
	// 일반 게시판 글 작성
	@Transactional
	public void addBoardPost(BoardPostAddRequest request) throws IOException {
		if(request.getFile() != null) {
			String imageUrl = s3Service.uploadFile(request.getFile());
			request.setImgUrl(imageUrl);
		}
		Long currentUserId = identityService.getCurrentUserId();
		
		postRepository.addBoardPost(request, currentUserId);
	}
	//여행지 리뷰 글 불러오기
	public List<PostItemResponse> getReviewPosts(Long placeId) {
		Long myId = identityService.getCurrentUserId();
		
		List<PostItemResponse> reviewPosts = postRepository.getReviewPostsByPlaceId(placeId);
		//나의 게시글인지 set
		for(PostItemResponse item: reviewPosts) {
			if(item.getWriterId() == myId) {
				item.setIsMyPost(true);
			}
		}
		return reviewPosts;
	}
	
	//여행 방 전체 글 불러오기
	public List<PostItemResponse> getRoomPosts(Long roomId) {
		Long myId = identityService.getCurrentUserId();
		
		List<PostItemResponse> reviewPosts = postRepository.getRoomPostsByRoomId(roomId);
		//나의 게시글인지 set
		for(PostItemResponse item: reviewPosts) {
			if(item.getWriterId() == myId) {
				item.setIsMyPost(true);
			}
		}
		return reviewPosts;
	}
	
	//게시글을 댓글과 함께 가져오기
	@Transactional
	public PostDetailResponse getPostDetail(Long postId) throws EntityNotFoundException {
		Long myId = identityService.getCurrentUserId();
		
		PostDetailResponse response = new PostDetailResponse();
		//조회수 증가
		postRepository.addViewCount(postId);
		PostItemResponse post = postRepository.getPostDetailById(postId);
		if(post == null) {
			throw new EntityNotFoundException("게시글이 없습니다.");
		}
		response.setPost(post);
		if(post.getWriterId() == myId) {
			response.getPost().setIsMyPost(true);
		}
		
		List<CommentItemResponse> comments = commentRepository.getCommentsByPostId(postId);
		if(comments.size() > 0) {
			response.setComments(comments);
			for(CommentItemResponse item: comments) {
				if(item.getWriterId() == myId) {
					item.setIsMyComment(true);
				}
			}
		}
		
		return response;
	}
	
	public PostPaginationResponse getNoticePosts(String searchQuery, int pageNo, String type) {
		PostPaginationResponse response = new PostPaginationResponse();
		Long myId = identityService.getCurrentUserId();
		
		List<PostItemResponse> posts = postRepository.getPostsByTypeAndSearchQuery(searchQuery, 8, type, (pageNo-1) * 8);
		for(PostItemResponse item: posts) {
			if(item.getWriterId() == myId) {
				item.setIsMyPost(true);
			}
		}
		
		response.setPosts(posts);
		response.setTotalCount(postRepository.getPostsCount(type, searchQuery));
		
		return response;
	}
	
	//공통 post update service, 작성자만 게시글 수정이 가능하므로 @PreAuthorize로 권한 체크
	@Transactional
//	@PreAuthorize("@postService.isWriter(#postId)")
	public void updatePost(PostUpdateRequest request, Long postId) throws EntityNotFoundException, IOException {
		if(postRepository.getPostById(postId) == null) {
			throw new EntityNotFoundException("해당 id의 post가 없습니다");
		}
		
		//사진 첨부됨 -> s3 올리기
		if(request.getFile() != null) {
			String imageUrl = s3Service.uploadFile(request.getFile());
			request.setImgUrl(imageUrl);
		}
		postRepository.updatePost(request, postId);
	}
	
	public boolean isWriter(Long postId) {
        // DB에서 postId로 post 조회
        Post post = postRepository.getPostById(postId);

        return post.getWriterId().equals(identityService.getCurrentUserId());
    }

	@Transactional
//	@PreAuthorize("@postService.isWriter(#postId)")
	public void deletePost(Long id) throws EntityNotFoundException {
		if(postRepository.getPostById(id) == null) {
			throw new EntityNotFoundException("해당 id의 post가 없습니다");
		}
		postRepository.deletePost(id);
	}
	
}
