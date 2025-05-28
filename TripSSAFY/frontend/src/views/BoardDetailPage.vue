<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <main class="flex-1 px-20 py-10">
      <router-link
        to="/post"
        class="flex items-center gap-1 text-base text-left text-gray-500 hover:text-gray-700 transition mb-3 ml-1 w-fit"
      >
        <img
          src="@/assets/icons/previous-icon.png"
          alt="뒤로가기"
          class="w-4 h-4"
        />
        뒤로가기
      </router-link>
      <PostDetail :open-post-id="route.params.id" />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import api from "@/api/axios";
import PostDetail from "@/components/post/PostDetail.vue";

const route = useRoute();
</script>

<style scoped></style>
