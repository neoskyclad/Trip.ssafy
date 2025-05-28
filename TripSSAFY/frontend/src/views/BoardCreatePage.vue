<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <main class="flex-1 px-10 py-10">
      <h2 class="text-3xl font-semibold text-gray-800">게시글 작성</h2>
      <!-- 제목 -->
      <div class="mt-6">
        <label class="block text-lg font-medium text-gray-700 mb-1">제목</label>
        <input
          v-model="title"
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
          @change="handleImage"
          class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
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
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import api from "@/api/axios";
import { showToastSuccess, showToastError } from "@/api/toastMsg";
let editorInstance;
const title = ref("");
const imageFile = ref(null);
onMounted(() => {
  editorInstance = new Editor({
    el: document.querySelector("#editor"),
    height: "400px",
    initialEditType: "wysiwyg",
    previewStyle: "vertical",
    hideModeSwitch: false, // 이걸 꼭 false로 유지
  });
});

const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB 제한
const handleImage = (e) => {
  const file = e.target.files[0];
  if (file) {
    if (file.size > MAX_FILE_SIZE) {
      showToastError("파일 크기가 5MB를 초과할 수 없습니다.");
      e.target.value = ""; // 입력값 초기화 (파일 선택 취소)
      return;
    }

    imageFile.value = file;
  }
};

const route = useRoute();
const router = useRouter();

const submitPost = async () => {
  //content를 html로 저장한다.
  const content = editorInstance.getHTML();
  console.log("content: " + content);

  const formData = new FormData();
  formData.append("title", title.value);
  formData.append("content", content);
  formData.append("type", route.query.type);
  if (imageFile.value) {
    formData.append("file", imageFile.value);
  }
  try {
    const response = await api.post(`/post/notice`, formData);
    showToastSuccess("게시글이 등록되었습니다.");
    setTimeout(() => {
      router.go(-1);
    }, 1500);
  } catch (err) {
    showToastError(err.response.data.errorMsg);
  }
};
</script>

<style scoped></style>
