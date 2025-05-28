<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <main class="flex-1 px-10 py-10">
      <h2 class="text-3xl font-semibold text-gray-800 mb-6">게시글 수정</h2>

      <PostUpdateForm
        v-if="renderUpdateForm"
        :post="post"
        @close-form="router.go(-1)"
      />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import PostUpdateForm from "@/components/post/PostUpdateForm.vue";
import { useRouter, useRoute } from "vue-router";
import { ref, onMounted } from "vue";
import api from "@/api/axios";
const router = useRouter();
const route = useRoute();
const post = ref({});
const renderUpdateForm = ref(false);
onMounted(async () => {
  try {
    const response = await api.get(`/post/${route.params.id}`);
    post.value = response.data.post;
    renderUpdateForm.value = true;
  } catch (err) {
    console.log(err);
  }
});
</script>

<style scoped></style>
