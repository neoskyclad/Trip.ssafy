<template>
  <div class="max-w-xl mx-auto p-6 bg-white rounded-2xl shadow-md space-y-6">
    <!-- 별점 -->
    <div>
      <label class="block mb-1 font-medium text-gray-700">별점</label>
      <div class="flex items-center space-x-1">
        <button
          v-for="star in 5"
          :key="star"
          @click="selectRating(star)"
          @mouseover="hoverRating = star"
          @mouseleave="hoverRating = 0"
          class="focus:outline-none"
        >
          <svg
            class="w-6 h-6 transition-colors"
            :class="{
              'text-ssafy_orange':
                star <= (hoverRating || updatePostObject.rating),
              'text-gray-300': star > (hoverRating || updatePostObject.rating),
            }"
            fill="currentColor"
            viewBox="0 0 20 20"
          >
            <path
              d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.967a1
                  1 0 00.95.69h4.175c.969 0 1.371 1.24.588
                  1.81l-3.38 2.455a1 1 0 00-.364 1.118l1.287
                  3.966c.3.922-.755 1.688-1.54 1.118l-3.38-2.455a1
                  1 0 00-1.176 0l-3.38 2.455c-.784.57-1.838-.196-1.54-1.118l1.287-3.966a1
                  1 0 00-.364-1.118L2.05 9.394c-.783-.57-.38-1.81.588-1.81h4.175a1
                  1 0 00.95-.69l1.286-3.967z"
            />
          </svg>
        </button>
      </div>
    </div>

    <!-- 이미지 업로드 -->
    <div>
      <label class="block mb-1 font-medium text-gray-700">이미지 첨부</label>
      <input
        type="file"
        @change="handleFileChange"
        class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-ssafy_blue hover:file:bg-blue-100"
      />
    </div>

    <!-- 제목 -->
    <div>
      <label class="block mb-1 font-medium text-gray-700">제목</label>
      <input
        type="text"
        v-model="updatePostObject.title"
        class="w-full border border-gray-300 rounded-lg p-2 focus:ring-blue-400 focus:outline-none"
        placeholder="제목을 입력하세요"
      />
    </div>

    <!-- 내용 -->
    <div>
      <label class="block mb-1 font-medium text-gray-700">내용</label>
      <textarea
        v-model="updatePostObject.content"
        rows="5"
        class="w-full border border-gray-300 rounded-lg p-2 focus:ring-blue-400 focus:outline-none resize-none"
        placeholder="내용을 입력하세요"
      ></textarea>
    </div>

    <!-- 제출 버튼 -->
    <div class="text-right">
      <button
        @click.prevent="handlePost"
        class="px-4 py-2 bg-ssafy_blue text-white rounded hover:bg-blue-700 transition"
      >
        게시
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";
import { showToastError, showToastSuccess } from "@/api/toastMsg";
const props = defineProps({
  updateId: Number,
  updatePostObject: Object,
});

//등록 후 닫음
const emit = defineEmits(["closeForm"]);
//별점 선택 컨트롤
const hoverRating = ref(0);
const selectRating = (star) => {
  props.updatePostObject.rating = star;
};

//프로필 이미지 업로드
const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB 제한
const handleFileChange = (event) => {
  props.updatePostObject.file = event.target.files[0];
  if (file) {
    if (file.size > MAX_FILE_SIZE) {
      showToastError("파일 크기가 5MB를 초과할 수 없습니다.");
      event.target.value = ""; // 입력값 초기화 (파일 선택 취소)
      return;
    }
  }
};
const userStore = useUserStore();

//게시글 전송
const handlePost = async () => {
  if (!userStore.isLoggedIn) {
    showToastError("로그인이 필요합니다.");
    return;
  }

  //제목, 내용 비었는지 확인
  if (
    props.updatePostObject.title.trim() === "" ||
    props.updatePostObject.content.trim() === ""
  ) {
    showToastError("제목, 내용을 입력해주세요.");
    return;
  }
  const formData = new FormData();
  formData.append("rating", props.updatePostObject.rating); // 별도 value 참조
  formData.append("title", props.updatePostObject.title);
  formData.append("content", props.updatePostObject.content);
  if (props.updatePostObject.file) {
    formData.append("file", props.updatePostObject.file);
  }

  try {
    const response = await api.patch(`/post/${props.updateId}`, formData);
    showToastSuccess("리뷰 등록 성공!");
    emit("closeForm");
  } catch (error) {
    showToastError(error.response.data.errorMsg);
    console.error(error);
  }
};
</script>
