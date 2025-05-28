<template>
  <div class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded-lg w-[500px] shadow-lg">
      <button
        @click="closeModal"
        class="flex justify-self-end text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>
      <!-- 페이지 내용 -->
      <form @submit.prevent="handleRegister">
        <keep-alive>
          <component
            :is="currentStepComponent"
            v-model:title="title"
            v-model:startDate="startDate"
            v-model:endDate="endDate"
            v-model:invitedUsers="invitedUsers"
          />
        </keep-alive>
        <!-- 버튼 -->
        <div class="flex justify-between mt-6">
          <button
            v-if="step > 0"
            @click="prevStep"
            class="px-4 py-2 bg-gray-200 hover:bg-gray-300 transition rounded"
          >
            이전
          </button>
          <div v-else class="flex-1"></div>
          <button
            @click="nextStep"
            class="px-4 py-2 bg-ssafy_blue hover:bg-blue-700 transition text-white rounded"
          >
            {{ step < steps.length - 1 ? "다음" : "완료" }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineEmits } from "vue";
import api from "@/api/axios";
import StepRoomInfo from "./steps/StepRoomInfo.vue";
import StepTripDate from "./steps/StepTripDate.vue";
import StepInvite from "./steps/StepInvite.vue";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const step = ref(0);
const steps = [StepRoomInfo, StepTripDate, StepInvite];
const currentStepComponent = computed(() => steps[step.value]);

const title = ref("");
const startDate = ref(null);
const endDate = ref(null);
const invitedUsers = ref([]);

const emit = defineEmits(["close"]);

const nextStep = () => {
  if (step.value === 0) {
    if (!title.value.trim()) {
      showToastError("여행 제목을 입력해주세요.");
      return;
    }
  } else if (step.value === 1) {
    if (!startDate.value || !endDate.value) {
      showToastError("시작일과 종료일을 모두 입력해주세요.");
      return;
    }
    if (new Date(startDate.value) > new Date(endDate.value)) {
      showToastError("시작일은 종료일보다 앞서야 합니다.");
      return;
    }
  }

  if (step.value < steps.length - 1) {
    step.value++;
  } else {
    // 최종 제출 처리
    createRoom();
  }
};

const closeModal = () => {
  emit("close");
};

const toKSTDateString = (date) => {
  const adjusted = new Date(date.getTime() + 9 * 60 * 60 * 1000);
  return adjusted.toISOString().split("T")[0];
};
const createRoom = async () => {
  const invitedUsernames = invitedUsers.value.map((user) => user.username);

  const roomData = {
    title: title.value,
    start_date: toKSTDateString(startDate.value),
    end_date: toKSTDateString(endDate.value),
    invitedUsernames: invitedUsernames,
  };

  try {
    await api.post("/room", roomData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    showToastSuccess("여행 방 만들기 성공!");
  } catch (error) {
    console.log(error);
    showToastError("여행 방 만들기 실패");
  } finally {
    // setTimeout(() => window.location.reload(), 1500);
    // emit('close')
  }
};

const prevStep = () => {
  if (step.value > 0) step.value--;
};
</script>
