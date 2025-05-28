<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
  >
    <div class="bg-white p-6 rounded-md shadow-lg w-80 relative" @click.stop>
      <!-- X 닫기 버튼 -->
      <button
        @click="closeModal"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>

      <!-- 타이틀 -->
      <h2 class="text-2xl font-semibold text-center text-gray-800 mb-6">
        어떤 회원이신가요?
      </h2>

      <!-- 버튼 그룹 -->
      <div class="flex flex-col gap-4">
        <button
          @click="goToRegister('ROLE_USER')"
          class="w-full py-3 px-4 bg-ssafy_blue hover:bg-blue-600 text-white rounded-lg font-medium transition"
        >
          일반 회원
        </button>
        <button
          @click="goToRegister('ROLE_TRAVEL_AGENCY')"
          class="w-full py-3 px-4 bg-ssafy_orange hover:bg-orange-400 text-white rounded-lg font-medium transition"
        >
          여행사 회원
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  openRegisterModal: Function,
});
const emit = defineEmits(["close"]);
const closeModal = () => {
  emit("close");
};

const goToRegister = (role) => {
  closeModal();
  props.openRegisterModal(role);
};
</script>

<style scoped></style>
