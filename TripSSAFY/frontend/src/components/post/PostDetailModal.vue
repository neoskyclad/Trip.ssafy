<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
  >
    <div
      class="bg-white p-6 rounded-md shadow-lg w-[1200px] max-h-[90vh] overflow-y-auto relative"
      @click.stop
    >
      <!-- 닫기 버튼 -->
      <button
        @click="closeDetail"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>
      <PostDetail :openPostId="openPostId" />
    </div>
  </div>
</template>

<script setup>
import PostDetail from "./PostDetail.vue";
const emit = defineEmits(["closeDetail"]);
const closeDetail = () => {
  emit("closeDetail");
};
const props = defineProps({
  openPostId: Number,
});
</script>

<style scoped></style>
