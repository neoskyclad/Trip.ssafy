<template>
  <div
    class="max-w-2xl mx-auto bg-white rounded-2xl shadow-md p-4 mb-4 border border-gray-100 flex items-center justify-between cursor-pointer"
    @click="emit('click')"
  >
    <div class="flex-1">
      <div class="flex mb-2">
        <h3 class="text-xl font-semibold mr-3">
          {{ spotItem.title }} {{ spotItem.contenttypeid }}
        </h3>
        <img
          width="28px"
          v-if="spotItem.contenttypeid == 12"
          src="@/assets/icons/attraction-icon.png"
        />
        <img
          width="28px"
          v-else-if="spotItem.contenttypeid == 14"
          src="@/assets/icons/culture-icon.png"
        />
        <img
          width="28px"
          v-else-if="spotItem.contenttypeid == 15"
          src="@/assets/icons/festival-icon.png"
        />
        <img
          width="28px"
          v-else-if="spotItem.contenttypeid == 25"
          src="@/assets/icons/tour-course-icon.png"
        />
        <img
          width="28px"
          v-else-if="spotItem.contenttypeid == 28"
          src="@/assets/icons/leisure-icon.png"
        />
        <img
          width="28px"
          v-if="spotItem.contenttypeid == 32"
          src="@/assets/icons/accommodation-icon.png"
        />
        <img
          width="28px"
          v-else-if="spotItem.contenttypeid == 38"
          src="@/assets/icons/shopping-icon.png"
        />
        <img
          width="28px"
          v-else-if="spotItem.contenttypeid == 39"
          src="@/assets/icons/restaurant-icon.png"
        />
      </div>
      <p class="text-gray-600">{{ spotItem.addr1 }}</p>
      <div class="flex items-center space-x-1">
        <svg
          v-for="star in 5"
          :key="star"
          class="w-6 h-6"
          :class="{
            'text-ssafy_orange': star <= Math.round(averageReview),
            'text-gray-300': star > Math.round(averageReview),
          }"
          fill="currentColor"
          viewBox="0 0 20 20"
        >
          <path
            d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.967a1
        1 0 00.95.69h4.175c.969 0 1.371 1.24.588
        1.81l-3.38 2.455a1 1 0 00-.364 1.118l1.287
        3.966c.3.922-.755 1.688-1.54 1.118l-3.38-2.455a1
        1 0 00-1.176 0l-3.38 2.455c-.784.57-1.838-.196-1.54-1.118l1.287-3.966a1
        1 0 00-.364-1.118L2.05 9.394c-.783-.57-.38-1.81.588-1.81h4.175a1
        1 0 00.95-.69l1.286-3.967z"
          />
        </svg>
      </div>
      <div>
        <button
          type="submit"
          class="w-24 mt-3 bg-ssafy_blue text-white py-2 px-4 rounded hover:bg-blue-700 transition"
          @click.prevent="openDetailModal = true"
        >
          상세보기
        </button>
        <button
          type="submit"
          class="w-24 mt-3 ml-2 bg-ssafy_orange text-white py-2 px-4 rounded hover:bg-orange-400 transition"
          @click.prevent="openReview"
        >
          리뷰보기
        </button>
      </div>
    </div>
    <img
      v-if="spotItem.firstimage"
      :src="spotItem.firstimage"
      alt="관광지 이미지"
      class="w-40 h-28 rounded-md object-cover ml-4"
    />
  </div>
  <TourSpotDetailModal
    :contentId="spotItem.contentid"
    v-if="openDetailModal"
    @close="closeDetailModal"
  />
  <TourSpotReviewModal
    :contentId="spotItem.contentid"
    :title="spotItem.title"
    :imageUrl="spotItem.firstimage"
    v-if="openReviewModal"
    @close="closeReviewModal"
    @review="handleReview"
  />
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import TourSpotDetailModal from "./TourSpotDetailModal.vue";
import TourSpotReviewModal from "./review/TourSpotReviewModal.vue";
import api from "@/api/axios";

onMounted(async () => {
  await loadPosts();

  const total = reviewPosts.value.reduce((sum, post) => sum + post.rating, 0);
  const count = reviewPosts.value.length;
  averageReview.value = count === 0 ? 0 : (total / count).toFixed(1);
});

const emit = defineEmits(["click"]);
const { spotItem } = defineProps({
  spotItem: Object,
});
const openDetailModal = ref(false);
const openReviewModal = ref(false);

const openReview = () => {
  openReviewModal.value = true;
};

const closeDetailModal = () => {
  openDetailModal.value = false;
};
const closeReviewModal = () => {
  openReviewModal.value = false;
};

const averageReview = ref(0);

const reviewPosts = ref([]);
const loadPosts = async () => {
  try {
    const response = await api.get(`/post/review/${spotItem.contentid}`);
    reviewPosts.value = response.data;
    console.log(reviewPosts.value);
  } catch (err) {
    console.log(err);
  }
};

const handleReview = (avg) => {
  averageReview.value = avg;
}

</script>

<style scoped>
/* 추가적인 스타일은 필요 시 여기에 작성 */
</style>
