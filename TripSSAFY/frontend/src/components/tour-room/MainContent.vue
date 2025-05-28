<template>
  <main class="relative flex-1 min-h-full p-10 overflow-hidden">
    <!-- 배경 레이어 -->
    <div class="absolute inset-0 -z-10 pointer-events-none">
      <!-- 상단 하늘색 배경 -->
      <div
        class="absolute top-0 left-0 w-full h-1/2 bg-sky-200 opacity-20"
        style="clip-path: polygon(0 0, 100% 0, 100% 80%, 0 100%)"
      ></div>

      <!-- 하단 연두색 배경 -->
      <div
        class="absolute bottom-0 left-0 w-full h-1/2 bg-lime-100 opacity-20"
        style="clip-path: polygon(0 0, 100% 20%, 100% 100%, 0 100%)"
      ></div>
    </div>

    <!-- 기존 콘텐츠 -->
    <div
      v-if="selectedRoom"
      class="bg-white rounded-2xl shadow-xl p-5 space-y-6 w-full max-w-screen-xl mx-auto"
    >
      <button
        @click="unselectRoom"
        class="flex justify-self-end text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>

      <RoomDetail
        :delete-room-confirm="deleteRoomConfirm"
        :editRoom="editRoom"
      />
    </div>

    <div v-else class="relative h-full">
      <div
        class="absolute inset-0 flex flex-col items-center justify-center text-center space-y-4"
      >
        <!-- 흰 배경 박스 추가 -->
        <div
          class="bg-white bg-opacity-80 rounded-full p-10 backdrop-blur-sm max-w-md w-full"
        >
          <img
            src="@/assets/images/empty-room-image.png"
            alt="빈 방 이미지"
            class="w-48 h-48 object-contain mb-4 mx-auto"
          />
          <p class="text-black font-bold text-4xl py-6">
            여행 방을 선택해주세요.
          </p>
          <p class="text-gray-700 text-xl font-medium mb-4">
            아직 속한 방이 없으신가요?
          </p>
          <button
            @click="openRoomModal"
            class="px-6 py-3 bg-ssafy_blue text-white rounded-lg hover:bg-blue-700 transition font-semibold shadow"
          >
            여행 방 만들기
          </button>
        </div>
      </div>
    </div>

    <RoomModal v-if="isRoomOpen" @close="closeRoomModal" />
    <DeleteConfirmModal
      v-if="isDeleteConfirmOpen"
      @cancel="closeDeleteConfirmModal"
      @confirmDelete="deleteRoom"
    />
    <RoomEditModal
      v-if="isEditModalOpen"
      :room="selectedRoom"
      @close="isEditModalOpen = false"
      @saved="updateSelectedRoom"
    />
  </main>
</template>

<script setup>
import { ref, inject, provide, defineEmits } from "vue";
import api from "@/api/axios";

import RoomDetail from "./RoomDetail.vue";
import RoomModal from "@/components/modal/RoomModal.vue";
import DeleteConfirmModal from "@/components/modal/DeleteConfirmModal.vue";
import RoomEditModal from "@/components/modal/RoomEditModal.vue";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const emit = defineEmits(["updateRoom"]);

const selectedRoom = inject("selectedRoom");

const isRoomOpen = ref(false);

const isDropdownOpen = ref(false);
provide("isDropdownOpen", isDropdownOpen);
const isDeleteConfirmOpen = ref(false);
const isEditModalOpen = ref(false);

const rightCollapsed = inject("rightCollapsed");

const unselectRoom = () => {
  selectedRoom.value = null;
  rightCollapsed.value = true;
  isDropdownOpen.value = false;
};

const openRoomModal = () => {
  isRoomOpen.value = true;
};

const closeRoomModal = () => {
  isRoomOpen.value = false;
};

const editRoom = () => {
  isDropdownOpen.value = false;
  isEditModalOpen.value = true;
};

const deleteRoom = async function () {
  try {
    await api.delete("/room", {
      params: {
        id: selectedRoom.value.tripRoom.id,
      },
    });
    showToastSuccess("여행 방 삭제 성공!");
    setTimeout(() => {
      window.location.reload();
    }, 1500);
  } catch (error) {
    console.log(error);
    showToastError("여행 방 삭제 실패");
  }
};

const updateSelectedRoom = async (updatedRoom) => {
  try {
    // 사용자 초대 일단 보류
    // const invitedUsernames = invitedUsers.value.map((user) => user.username);

    const updatedRoomData = {
      id: updatedRoom.tripRoom.id,
      title: updatedRoom.tripRoom.title,
      start_date: updatedRoom.tripRoom.startDate,
      end_date: updatedRoom.tripRoom.endDate,
      invitedUsernames: [],
    };
    console.log("Updated room data:", updatedRoomData);
    await api.put("/room", updatedRoomData);

    showToastSuccess("여행 방 수정 성공!");
    emit("updateRoom");
    selectedRoom.value = updatedRoom;
    isEditModalOpen.value = false;
  } catch (error) {
    console.error("Error updating room:", error);
  }
};

const deleteRoomConfirm = function () {
  openDeleteConfirmModal();
};

const openDeleteConfirmModal = () => {
  isDeleteConfirmOpen.value = true;
};

const closeDeleteConfirmModal = () => {
  isDeleteConfirmOpen.value = false;
  isDropdownOpen.value = false;
};
</script>

<style scoped>
/* clip-path에 브라우저 호환성 문제 있는 경우 */
</style>
