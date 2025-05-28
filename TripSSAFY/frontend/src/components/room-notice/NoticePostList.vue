<template>
  <aside class="w-96 bg-white border shadow-xl overflow-y-auto">
    <!-- 상단 영역 -->
    <div
      class="flex justify-between items-center bg-gray-100 px-5 py-4 border-b"
    >
      <span class="font-semibold text-gray-700 text-2xl">📢 공지 사항</span>
      <button
        @click="$emit('close')"
        class="text-gray-500 hover:text-gray-800 text-2xl leading-none"
        type="button"
        aria-label="닫기"
      >
        &times;
      </button>
    </div>
    <!-- 공지 리스트 -->
    <div class="divide-y divide-gray-200">
      <div
        v-for="(notice, index) in notices"
        :key="index"
        class="relative p-4 hover:bg-gray-50 transition cursor-pointer"
        @click="openDetailForm(notice.id)"
      >
        <!-- 제목 & 내용 -->
        <div class="text-base font-semibold text-gray-900 mb-1 truncate">
          <!-- 프로필 이미지 + 작성자 정보 -->
          <div class="flex items-center gap-3">
            <img
              v-if="notice.profileImg"
              :src="notice.profileImg"
              alt="프로필"
              class="w-8 h-8 rounded-full object-cover"
            />
            <img
              v-else
              src="/default_profile.png"
              alt="프로필"
              class="w-8 h-8 rounded-full object-cover"
            />
            <div>
              <div class="text-sm font-medium text-gray-800 truncate">
                {{ notice.nickname }}
              </div>
              <span class="text-xs text-gray-500">
                생성: {{ formatDate(notice.createdAt) }}
              </span>
              <span v-if="notice.updatedAt" class="text-xs text-gray-500">
                / 수정: {{ formatDate(notice.updatedAt) }}
              </span>
            </div>
          </div>

          <!-- 드롭다운 버튼 (오른쪽 상단) -->
          <div v-if="notice.isMyPost" class="absolute top-4 right-4">
            <div class="relative">
              <button
                @click.stop.prevent="toggleDropdown(notice.id)"
                class="text-gray-500 hover:text-black"
              >
                ⋮
              </button>
              <div
                v-if="showDropdown === notice.id"
                class="absolute right-0 top-full mt-1 w-24 bg-white border rounded shadow z-50"
              >
                <button
                  @click.stop="onEditButtonClick(notice)"
                  class="block w-full px-3 py-2 text-left font-light hover:bg-gray-100"
                >
                  수정
                </button>
                <button
                  @click.stop="
                    openDeleteConfirm = true;
                    selectedPostId = notice.id;
                  "
                  class="block w-full px-3 py-2 text-left text-red-500 font-medium hover:bg-red-100 transition"
                >
                  삭제
                </button>
              </div>
            </div>
          </div>
        </div>

        <img :src="notice.img" class="max-h-48 mx-auto" />
        <div class="mb-1 text-sm font-semibold text-gray-900 truncate">
          {{ notice.title }}
        </div>
        <div
          class="text-sm text-gray-700 line-clamp-5"
          v-html="notice.content"
        ></div>
      </div>
    </div>

    <!-- 공지 작성 버튼 -->
    <div class="flex justify-center px-4 py-2">
      <button
        @click="openCreateForm"
        class="bg-ssafy_blue text-white text-sm px-4 py-2 my-4 rounded hover:bg-blue-700 transition"
      >
        게시글 작성
      </button>
    </div>
  </aside>

  <PostDetailModal
    v-if="openPostId > 0"
    :openPostId
    @close-detail="openPostId = 0"
  />
  <DeleteConfirmModal
    v-if="openDeleteConfirm"
    @confirm-delete="onDelete"
    @cancel="openDeleteConfirm = false"
  />
  <NoticePostUpdateForm
    v-if="openUpdateForm"
    :post="selectedPost"
    @close-form="onUpdate"
  />
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";
import DeleteConfirmModal from "../modal/DeleteConfirmModal.vue";
import NoticePostUpdateForm from "./NoticePostUpdateModal.vue";
import PostDetailModal from "../post/PostDetailModal.vue";
import { showToastSuccess } from "@/api/toastMsg";
const emit = defineEmits(["close", "create"]);

const props = defineProps({
  roomId: Number,
});
const closeNotice = () => {
  emit("close");
};
const openCreateForm = () => {
  emit("create");
};
const openDeleteConfirm = ref(false);
const openUpdateForm = ref(false);
const showDropdown = ref(0);

function toggleDropdown(id) {
  if (showDropdown.value === 0) showDropdown.value = id;
  else showDropdown.value = 0;
}
const openPostId = ref(0);
const selectedPostId = ref(0);
const selectedPost = ref({});
const openDetailForm = (id) => {
  openPostId.value = id;
};

const notices = ref([]);
const loadNotices = async () => {
  try {
    const response = await api.get(`/post/room/${props.roomId}`);
    notices.value = response.data;
  } catch (err) {
    console.error(err);
  }
};

defineExpose({
  loadNotices,
});

onMounted(loadNotices);

const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
};

const onEditButtonClick = (notice) => {
  showDropdown.value = 0;
  selectedPost.value = { ...notice };
  openUpdateForm.value = true;
};

const onUpdate = async () => {
  openUpdateForm.value = false;
  await loadNotices();
};
const onDelete = async () => {
  try {
    const response = api.delete(`/post/${selectedPostId.value}`);
    showToastSuccess("삭제되었습니다.");
    openDeleteConfirm.value = false;
    showDropdown.value = 0;
    await loadNotices();
  } catch (err) {
    console.log(err);
  } finally {
    openDeleteConfirm.value = false;
  }
};
</script>

<style scoped>
.line-clamp-5 {
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
