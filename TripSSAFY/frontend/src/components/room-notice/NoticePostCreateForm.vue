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
        @click="closeModal"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>

      <!-- 게시글 등록 폼 -->
      <h2 class="text-2xl font-semibold text-gray-800 p-2">게시글 작성</h2>
      <!-- 제목 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">제목</label>
        <input
          v-model="title"
          type="text"
          class="w-full border rounded-lg p-2 focus:outline-none focus:ring focus:border-blue-400"
          placeholder="제목을 입력하세요"
        />
      </div>

      <!-- 이미지 첨부 및 미리보기 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1"
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
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">내용</label>
        <div class="editor-container" ref="editorRef"></div>
      </div>

      <!-- 등록 버튼 -->
      <div class="text-right">
        <button
          @click="submitPost"
          class="bg-ssafy_blue hover:bg-blue-700 transition mt-4 text-white px-4 py-2 rounded-lg"
        >
          등록
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const title = ref("");
const imageFile = ref(null);
const imagePreview = ref(null);
const editorRef = ref(null);
let editorInstance = null;

const emit = defineEmits(["close"]);
const userStore = useUserStore();
const props = defineProps({
  roomId: Number,
});
const closeModal = () => {
  emit("close");
};
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
    imagePreview.value = URL.createObjectURL(file);
  }
};

onMounted(() => {
  //toastui editor 설정
  editorInstance = new Editor({
    el: editorRef.value,
    height: "400px",
    initialEditType: "wysiwyg",
    previewStyle: "vertical",
    hideModeSwitch: false, // 이걸 꼭 false로 유지
  });
});

const submitPost = async () => {
  //content를 html로 저장한다.
  const content = editorInstance.getHTML();

  const formData = new FormData();
  formData.append("roomId", props.roomId);
  formData.append("title", title.value);
  formData.append("content", content);
  if (imageFile.value) {
    formData.append("file", imageFile.value);
  }
  try {
    const response = await api.post(`/post/room`, formData);
    showToastSuccess("방 게시글이 등록되었습니다.");
    closeModal();
  } catch (err) {
    console.log(err);
  }
};
</script>
<style></style>
