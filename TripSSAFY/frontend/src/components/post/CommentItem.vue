<template>
  <div class="p-4 relative">
    <!-- 사용자 정보 -->
    <div class="flex items-center justify-between space-x-3">
      <div class="flex items-center space-x-3">
        <img
          v-if="comment.profileImg"
          :src="comment.profileImg"
          alt="Profile"
          class="w-10 h-10 rounded-full object-cover"
        />
        <img
          v-else
          src="/default_profile.png"
          alt="Profile"
          class="w-10 h-10 rounded-full object-cover"
        />
        <!-- 생성/수정 날짜 -->
        <div>
          <span class="font-semibold">{{ comment.nickname }}</span>
          <div class="text-sm text-gray-500 mt-1">
            <span>{{ formatDateTime(comment.createdAt) }}</span>
            <span v-if="comment.updatedAt" class="ml-4">
              (수정됨) : {{ formatDateTime(comment.updatedAt) }}
            </span>
          </div>
        </div>
      </div>

      <!-- 드롭다운 버튼 (내 댓글일 경우) -->
      <div v-if="comment.isMyComment" class="ml-auto relative">
        <button
          @click="toggleDropdown"
          class="w-8 h-8 flex items-center justify-center rounded border border-transparent hover:border-gray-300 transition text-gray-500 hover:text-black"
        >
          ⋮
        </button>
        <div
          v-if="showDropdown"
          class="absolute right-0 mt-2 w-24 bg-white border rounded shadow z-10"
        >
          <button
            @click="onEditButtonClick"
            class="block w-full px-3 py-2 text-left hover:bg-gray-100 transition"
          >
            수정
          </button>
          <button
            @click="openDeleteConfirm = true"
            class="block w-full text-left px-4 py-2 text-red-600 hover:bg-red-100 transition"
          >
            삭제
          </button>
        </div>
      </div>
    </div>

    <!-- 본문 수정 -->
    <textarea
      id="content"
      v-if="editComment"
      v-model="comment.content"
      rows="4"
      class="w-full border border-gray-300 rounded-lg p-2 focus:ring-blue-400 focus:outline-none resize-none"
    />
    <div v-else class="mt-3 text-gray-800">
      {{ comment.content }}
    </div>
    <button
      v-if="editComment"
      @click="onUpdate"
      class="w-16 ml-auto px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition"
    >
      수정
    </button>
  </div>
  <DeleteConfirmModal
    v-if="openDeleteConfirm"
    @confirm-delete="onDelete"
    @cancel="openDeleteConfirm = false"
  />
  <hr />
</template>

<script setup>
import { ref, defineProps } from "vue";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";
import qs from "qs";
import { showToastError, showToastSuccess } from "@/api/toastMsg";
import DeleteConfirmModal from "../modal/DeleteConfirmModal.vue";

const userStore = useUserStore();
const editComment = ref(false);
const openDeleteConfirm = ref(false);

const { comment } = defineProps({
  comment: Object,
});

const showDropdown = ref(false);

function toggleDropdown() {
  showDropdown.value = !showDropdown.value;
}

const emit = defineEmits(["delete", "update"]);
const onEditButtonClick = () => {
  editComment.value = true;
  showDropdown.value = false;
};

const onDelete = async () => {
  try {
    const response = await api.delete(`/comment/${comment.id}`);
    openDeleteConfirm.value = false;
    showToastSuccess("댓글이 삭제되었습니다");
    emit("delete");
  } catch (err) {
    console.log(err);
  }
};

const onUpdate = async () => {
  const content = comment.content;
  try {
    const response = await api.patch(
      `/comment/${comment.id}`,
      qs.stringify({ content: content })
    );
    editComment.value = false;
    emit("update");
  } catch (err) {
    showToastError("댓글 수정 실패!");
    console.log(err);
  }
};
function formatDateTime(input) {
  const date = new Date(input); // input은 ISO 문자열이나 Date 객체

  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, "0"); // 월은 0부터 시작
  const dd = String(date.getDate()).padStart(2, "0");

  const hh = String(date.getHours()).padStart(2, "0");
  const min = String(date.getMinutes()).padStart(2, "0");

  return `${yyyy}-${mm}-${dd} ${hh}:${min}`;
}
</script>

<style scoped>
/* 필요시 추가 스타일 */
</style>
