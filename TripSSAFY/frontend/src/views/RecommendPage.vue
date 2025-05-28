<template>
  <div class="min-h-screen flex flex-col">
    <Header />

    <main class="flex-1 px-32 py-12">
      <h1 class="text-3xl font-bold text-center">
        여행지를 간단하게 추천 받아요!
      </h1>
      <p class="text-center text-gray-600 py-4">
        AI 추천을 통해 나만의 특별한 여행을 계획해보세요.
      </p>

      <RecommendModal class="mt-8" @register="isRegisterModalOpen = true" />
      <RegisterModal v-if="isRegisterModalOpen" @close="onRegisterClose" />
    </main>

    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import RecommendModal from "@/components/modal/RecommendModal.vue";
import RegisterModal from "@/components/modal/RegisterModal.vue";
import { useRouter } from "vue-router";

const router = useRouter();
const isRegisterModalOpen = ref(false);

onMounted(() => {});

const onRegisterClose = () => {
  isRegisterModalOpen.value = false;
  router.push("/");
};
</script>

<style scoped></style>
