<template>
  <NoticePostList
    v-if="selectedRoom && currentView == 'NoticeView'"
    :roomId="selectedRoom.tripRoom.id"
    @close="setView('RoomView')"
    @create="noticeCreateModal = true"
    ref="noticePostListRef"
  />
  <ExpenseSidebar
    v-if="selectedRoom && currentView == 'ExpenseView'"
    :roomId="selectedRoom.tripRoom.id"
    @close="setView('RoomView')"
  />
  <!-- 사이드바 펼침 여부 -->
  <aside
    :class="['transition-all duration-300', isCollapsed ? 'w-16' : 'w-64']"
    class="bg-white p-4"
  >
    <div v-if="!isCollapsed" class="py-32">
      <button
        class="flex justify-center h-10 w-full mb-2 p-2 bg-white border rounded hover:bg-gray-200 transition"
        @click="handleClick"
      >
        <img
          src="@/assets/icons/plan-icon.png"
          alt="여행 계획"
          class="w-5 h-5 mr-2 inline-block align-middle"
        />
        <span class="inline-block align-middle">여행 계획</span>
      </button>

      <button
        class="flex justify-center h-10 w-full mb-2 p-2 bg-white border rounded hover:bg-gray-200 transition"
        @click="setView('ExpenseView')"
      >
        <img
          src="@/assets/icons/budget-icon.png"
          alt="여행 정산"
          class="w-5 h-5 mr-2 inline-block align-middle"
        />
        <span class="inline-block align-middle">여행 정산</span>
      </button>

      <button
        class="flex justify-center h-10 w-full mb-2 p-2 bg-white border rounded hover:bg-gray-200 transition"
        @click="setView('NoticeView')"
      >
        <img
          src="@/assets/icons/note-icon.png"
          alt="공지 사항"
          class="w-5 h-5 mr-2 inline-block align-middle"
        />
        <span class="inline-block align-middle">공지 사항</span>
      </button>
      <!-- <button
        class="flex justify-center h-10 w-full mb-2 p-2 bg-white border rounded hover:bg-gray-200 transition"
        @click="setView('BoardView')"
      >
        <img
          src="@/assets/icons/clipboard-icon.png"
          alt="게시판"
          class="w-5 h-5 mr-2 inline-block align-middle"
        />
        <span class="inline-block align-middle">게시판</span>
      </button> -->
    </div>
  </aside>
  <NoticePostCreateForm
    v-if="noticeCreateModal"
    :roomId="selectedRoom.tripRoom.id"
    @close="handleCreateFormClose"
  />
</template>

<script setup>
import NoticePostList from "@/components/room-notice/NoticePostList.vue";
import NoticePostCreateForm from "@/components/room-notice/NoticePostCreateForm.vue";
import { defineProps, inject, ref, nextTick } from "vue";
import ExpenseModal from "../expense/ExpenseModal.vue";
import ExpenseSidebar from "../expense/ExpenseSidebar.vue";

// 방 공지 글 관련 모달, sidebar
const noticeCreateModal = ref(false);
const currentView = ref("RoomView"); // 초기 View 컴포넌트

const selectedRoom = inject("selectedRoom");

const props = defineProps({
  isCollapsed: Boolean,
});

function setView(view) {
  currentView.value = view;
}

function handleClick() {
  setView("RoomView");
  nextTick(() => {
    window.scrollTo({ top: document.body.scrollHeight, behavior: "smooth" });
  });
}

const noticePostListRef = ref(null);
function handleCreateFormClose() {
  noticeCreateModal.value = false;
  noticePostListRef.value?.loadNotices();
}
</script>
