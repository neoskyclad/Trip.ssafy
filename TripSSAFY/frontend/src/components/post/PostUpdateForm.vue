<template>
  <!-- 제목 -->
  <div>
    <div>
      <label class="block text-lg font-medium text-gray-700 mb-1">제목</label>
      <input
        v-model="post.title"
        type="text"
        class="w-full border rounded-lg p-2 focus:outline-none focus:ring focus:border-blue-400"
        placeholder="제목을 입력하세요"
      />
    </div>

    <!-- 이미지 첨부 및 미리보기 -->
    <div class="mt-6">
      <label class="block text-lg font-medium text-gray-700 mb-1"
        >사진 첨부</label
      >
      <input
        type="file"
        @change="handleFileChange"
        class="block w-full text-lg text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
      />
      <div v-if="imagePreview" class="mt-3">
        <img
          :src="imagePreview"
          alt="미리보기"
          class="w-48 h-auto rounded-lg border"
        />
      </div>
    </div>

    <!-- 내용 (Toast UI Editor) -->
    <div class="mt-6">
      <label class="block text-lg font-medium text-gray-700 mb-1">내용</label>
      <div class="editor-container" id="editor"></div>
    </div>

    <!-- 등록 버튼 -->
    <div class="text-right mt-5">
      <button
        @click="router.push('/post')"
        class="bg-gray-200 border hover:bg-gray-300 text-black px-4 py-2 rounded-lg mx-4"
      >
        취소
      </button>
      <button
        @click="submitPost"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg"
      >
        등록
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/api/axios";
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import router from "@/router";
import { showToastSuccess, showToastError } from "@/api/toastMsg";

const { post } = defineProps({
  post: Object,
});
const emit = defineEmits(["closeForm"]);
let editorInstance;
onMounted(() => {
  //toastui editor 설정
  editorInstance = new Editor({
    el: document.querySelector("#editor"),
    height: "400px",
    initialEditType: "wysiwyg",
    previewStyle: "vertical",
    hideModeSwitch: false, // 이걸 꼭 false로 유지
    initialValue: post.content,
  });
});
const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB 제한
const uploadedFile = ref(null);
const handleFileChange = (event) => {
  uploadedFile.value = event.target.files[0];
  if (uploadedFile.value) {
    if (uploadedFile.value.size > MAX_FILE_SIZE) {
      showToastError("파일 크기가 5MB를 초과할 수 없습니다.");
      event.target.value = ""; // 입력값 초기화 (파일 선택 취소)
      return;
    }
  }
};

const submitPost = async () => {
  const formData = new FormData();
  formData.append("title", post.title);
  formData.append("content", editorInstance.getHTML());
  if (uploadedFile.value) {
    formData.append("file", uploadedFile.value);
  }

  try {
    const response = await api.patch(`/post/${post.id}`, formData);
    showToastSuccess("수정되었습니다.");
    emit("closeForm");
  } catch (err) {
    showToastError(err.response.data.errorMsg);
    console.error(err);
  }
};
</script>

<style lang="scss" scoped></style>
