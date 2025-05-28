<template>
  <div
    class="fixed inset-0 z-50 bg-black bg-opacity-40 flex items-center justify-center"
  >
    <div class="bg-white p-6 rounded-lg shadow-md w-96 relative">
      <!-- 닫기 버튼 -->
      <button class="absolute top-2 right-3 text-2xl" @click="$emit('close')">
        &times;
      </button>

      <h2 class="text-xl font-bold mb-4 text-center">방 정보 수정</h2>

      <!-- 제목 -->
      <label class="block mb-2 text-gray-700">방 제목</label>
      <input
        v-model="editedTitle"
        class="w-full p-2 border border-gray-300 rounded mb-4"
        type="text"
      />

      <!-- 날짜 -->
      <label class="block mb-2 text-gray-700">여행 기간</label>
      <div class="flex gap-2">
        <input
          type="date"
          v-model="editedStartDate"
          class="flex-1 p-2 border rounded"
        />
        <input
          type="date"
          v-model="editedEndDate"
          class="flex-1 p-2 border rounded"
        />
      </div>

      <!-- 버튼 -->
      <div class="flex justify-end gap-2 mt-6">
        <button
          @click="$emit('close')"
          class="bg-gray-200 hover:bg-gray-300 transition px-4 py-2 rounded"
        >
          취소
        </button>
        <button
          @click="save"
          class="bg-ssafy_blue hover:bg-blue-700 text-white px-4 py-2 rounded"
        >
          저장
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  room: Object,
});
const emit = defineEmits(["close", "saved"]);

const editedTitle = ref(props.room.tripRoom.title);
const editedStartDate = ref(props.room.tripRoom.startDate);
const editedEndDate = ref(props.room.tripRoom.endDate);

const save = () => {
  const editedRoom = props.room;
  editedRoom.tripRoom.title = editedTitle.value;
  editedRoom.tripRoom.startDate = editedStartDate.value;
  editedRoom.tripRoom.endDate = editedEndDate.value;
  emit("saved", editedRoom);
  emit("close");
};
</script>
