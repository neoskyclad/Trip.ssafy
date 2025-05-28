<template>
  <button
    v-if="
      userStore.role === 'ROLE_TRAVEL_AGENCY' || userStore.role === 'ROLE_ADMIN'
    "
    class="ml-auto px-4 py-2 mt-4 bg-ssafy_blue text-white rounded hover:bg-blue-700 transition mb-2"
    @click="openPromotionCreate = true"
  >
    홍보글 작성
  </button>

  <div
    class="relative w-full overflow-hidden min-h-[300px] bg-white rounded-lg shadow-sm"
  >
    <!-- 콘텐츠가 없을 때 -->
    <div
      v-if="promotions.length === 0"
      class="flex flex-col items-center justify-center h-full py-12 text-gray-500"
    >
      <img src="/warning-page.png" alt="없음" class="w-20 h-20 mb-4" />
      <p class="text-lg">상품이 없습니다.</p>
    </div>

    <!-- 콘텐츠가 있을 때 -->
    <template v-else>
      <!-- 왼쪽 버튼 -->
      <button
        @click="scrollLeft"
        class="absolute left-0 top-1/2 transform -translate-y-1/2 z-10 p-2 bg-white shadow rounded-full hover:bg-gray-100"
      >
        <ChevronLeftIcon class="w-6 h-6 text-gray-600" />
      </button>

      <!-- 카드 컨테이너 -->
      <div
        ref="scrollContainer"
        class="flex justify-center gap-4 overflow-x-auto scrollbar-hide scroll-smooth px-10 py-4"
      >
        <div
          v-for="(item, index) in promotions"
          :key="index"
          @click.prevent="openURL(item.url)"
          class="flex-none w-96 bg-white rounded-lg shadow-md overflow-hidden cursor-pointer transition hover:shadow-lg"
        >
          <img
            :src="item.img"
            alt="상품 이미지"
            class="w-full h-56 object-cover"
          />
          <div class="p-4">
            <h3 class="text-lg font-semibold text-gray-800">
              {{ item.title }}
            </h3>
            <p class="text-sm text-gray-500 mt-1 line-clamp-2">
              {{ item.content }}
            </p>
          </div>
        </div>
      </div>

      <!-- 오른쪽 버튼 -->
      <button
        @click="scrollRight"
        class="absolute right-0 top-1/2 transform -translate-y-1/2 z-10 p-2 bg-white shadow rounded-full hover:bg-gray-100"
      >
        <ChevronRightIcon class="w-6 h-6 text-gray-600" />
      </button>
    </template>
  </div>

  <PromotionCreateModal
    v-if="openPromotionCreate"
    :content-id="props.contentId"
    @close-form="openPromotionCreate = false"
  />
</template>

<script setup>
import { ref, onMounted } from "vue";
import { ChevronLeftIcon, ChevronRightIcon } from "lucide-vue-next";
import PromotionCreateModal from "./PromotionCreateModal.vue";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";

const props = defineProps({
  contentId: Number,
});

const scrollContainer = ref(null);
const openPromotionCreate = ref(false);
const userStore = useUserStore();

const scrollLeft = () => {
  scrollContainer.value.scrollBy({ left: -280, behavior: "smooth" });
};

const scrollRight = () => {
  scrollContainer.value.scrollBy({ left: 280, behavior: "smooth" });
};

const promotions = ref([]);
onMounted(async () => {
  try {
    const response = await api.get(`/promotion/get`, {
      params: { contentId: props.contentId },
    });
    promotions.value = response.data;
    console.log(response.data);
  } catch (err) {
    console.log(err);
  }
});

const openURL = (url) => {
  window.open(url, "_blank");
};
</script>

<style scoped>
.scrollbar-hide::-webkit-scrollbar {
  display: none;
}
.scrollbar-hide {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
