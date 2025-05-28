<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
  >
    <div
      class="bg-white p-6 rounded-md shadow-lg w-[1200px] overflow-y-auto relative"
      @click.stop
    >
      <!-- 닫기 버튼 -->
      <button
        @click="$emit('closeForm')"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>
      <h2 class="text-2xl font-semibold text-gray-800">광고 작성</h2>
      <div class="mt-6">
        <label class="block mb-1 font-medium text-gray-700">이미지 첨부</label>
        <input
          type="file"
          @change="handleFileChange"
          class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-ssafy_blue hover:file:bg-blue-100"
        />
      </div>
      <!-- URL -->
      <div class="mt-6">
        <label class="block mb-1 font-medium text-gray-700">URL</label>
        <input
          type="text"
          v-model="promotionForm.url"
          class="w-full border border-gray-300 rounded-lg p-2 focus:ring-blue-400 focus:outline-none"
          placeholder="바로가기 URL 입력"
        />
      </div>
      <!-- 제목 -->
      <div class="mt-6">
        <label class="block mb-1 font-medium text-gray-700">제목</label>
        <input
          type="text"
          v-model="promotionForm.title"
          class="w-full border border-gray-300 rounded-lg p-2 focus:ring-blue-400 focus:outline-none"
          placeholder="제목을 입력하세요"
        />
      </div>

      <!-- 내용 -->
      <div class="mt-6">
        <label class="block mb-1 font-medium text-gray-700">내용</label>
        <textarea
          v-model="promotionForm.content"
          rows="5"
          class="w-full border border-gray-300 rounded-lg p-2 focus:ring-blue-400 focus:outline-none resize-none"
          placeholder="내용을 입력하세요"
        ></textarea>
      </div>

      <!-- 제출 버튼 -->
      <div class="text-right mt-6">
        <button
          @click.prevent="handlePost"
          class="px-4 py-2 bg-ssafy_blue text-white rounded hover:bg-blue-700 transition"
        >
          게시
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import api from "@/api/axios";
import { showToastError, showToastSuccess } from "@/api/toastMsg";
import { ref } from "vue";
const emit = defineEmits(["closeForm"]);
const promotionForm = ref({
  contentId: "",
  title: "",
  content: "",
  url: "",
  file: null,
});

const props = defineProps({
  contentId: Number,
});

const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB 제한
const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    if (file.size > MAX_FILE_SIZE) {
      showToastError("파일 크기가 5MB를 초과할 수 없습니다.");
      event.target.value = ""; // 입력값 초기화 (파일 선택 취소)
      return;
    }

    promotionForm.value.file = file;
  }
};

const handlePost = async () => {
  if (
    promotionForm.value.title.trim() === "" ||
    promotionForm.value.content.trim() === "" ||
    !promotionForm.value.file ||
    promotionForm.value.url.trim() === ""
  ) {
    showToastError("입력하지 않은 칸이 있습니다.");
    return;
  }

  const formData = new FormData();
  formData.append("contentId", props.contentId);
  formData.append("title", promotionForm.value.title);
  formData.append("content", promotionForm.value.content);
  formData.append("file", promotionForm.value.file);
  formData.append("url", promotionForm.value.url);

  try {
    const response = await api.post(`/promotion`, formData);
    showToastSuccess("광고가 등록되었습니다.");
    setTimeout(() => {
      location.reload();
    }, 1500);
  } catch (err) {
    if (err.response.status === 403) {
      showToastError("등록 권한이 없습니다");
    } else {
      showToastError(err.response.data.errorMsg);
    }
    console.log(err);
  }
};
</script>

<style scoped></style>
