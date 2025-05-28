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
        @click="$emit('closeForm')"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>
      <!-- 게시글 등록 폼 -->
      <h2 class="text-2xl font-semibold text-gray-800 p-2">게시글 수정</h2>
      <PostUpdateForm :post="post" @close-form="$emit('closeForm')" />
    </div>
  </div>
</template>

<script setup>
import PostUpdateForm from "../post/PostUpdateForm.vue";
const emit = defineEmits(["closeForm"]);
const { post } = defineProps({
  post: Object,
});
</script>

<style scoped></style>
