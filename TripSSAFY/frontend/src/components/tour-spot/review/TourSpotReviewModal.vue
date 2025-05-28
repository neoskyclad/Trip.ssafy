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
        @click="closeModal"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>
      <h2 class="text-2xl font-bold mb-6">리뷰</h2>
      <main class="flex gap-6">
        <div class="w-1/2 space-y-4">
          <div v-if="imageUrl" class="relative w-full h-68">
            <!-- 로딩 중일 때 대체 이미지 (작게, 가운데 정렬) -->
            <div
              v-if="isImgLoading"
              class="absolute inset-0 flex items-center justify-center bg-gray-100 rounded-md"
            >
              <img
                src="@/assets/gifs/loading.gif"
                class="w-12 h-12"
                alt="loading"
              />
            </div>

            <!-- 실제 이미지 -->
            <img
              :src="imageUrl"
              @load="isImgLoading = false"
              class="rounded-md w-full h-68 object-cover transition-opacity duration-300"
              :class="{ 'opacity-0': isImgLoading }"
              alt="tour image"
            />
          </div>
          <h3 class="text-xl font-semibold mr-3">{{ title }}</h3>
          <div class="flex items-start gap-2">
            <span class="text-xl font-bold">{{ averageReview }}</span>
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
            <span class="text-bold text-gray-600">리뷰 </span>
            <p>{{ reviewPosts.length }}</p>
            <button
              class="w-20 ml-auto bg-ssafy_blue text-white py-2 px-1 rounded hover:bg-blue-700 transition"
              @click.prevent="formOpen = 'create'"
            >
              리뷰 작성
            </button>
          </div>
        </div>
        <!-- 리뷰 글 container -->
        <div class="flex-1 max-h-[800px] overflow-y-auto">
          <template v-if="formOpen === 'create'">
            <TourSpotReviewCreateForm :contentId @close-form="loadPosts" />
          </template>
          <template v-if="formOpen === 'list'">
            <TourSpotReviewItem
              v-for="review in reviewPosts"
              @update="openUpdateForm"
              @delete="loadPosts"
              :review="review"
            />
          </template>
          <template v-if="formOpen === 'update'">
            <TourSpotReviewUpdateForm
              :updateId
              :updatePostObject
              @close-form="loadPosts"
            />
          </template>
          <hr />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, defineEmits } from "vue";
import api from "@/api/axios";
import TourSpotReviewItem from "./TourSpotReviewItem.vue";
import TourSpotReviewCreateForm from "./TourSpotReviewCreateForm.vue";
import TourSpotReviewUpdateForm from "./TourSpotReviewUpdateForm.vue";
const props = defineProps({
  contentId: Number,
  title: String,
  imageUrl: String,
});
const emit = defineEmits(["review"]);
const reviewPosts = ref([]);
const formOpen = ref("list");
const averageReview = computed(() => {
  const total = reviewPosts.value.reduce((sum, post) => sum + post.rating, 0);
  const count = reviewPosts.value.length;
  const reviewValue = count === 0 ? 0 : (total / count).toFixed(1);
  return reviewValue; // 소수점 1자리
});

const isImgLoading = ref(true);

//포스트 로드
const loadPosts = async () => {
  formOpen.value = "list";
  try {
    const response = await api.get(`/post/review/${props.contentId}`);
    reviewPosts.value = response.data;
    emit("review", averageReview.value);
    console.log(reviewPosts.value);
  } catch (err) {
    console.log(err);
  }
};

onMounted(loadPosts);

const closeModal = () => {
  emit("close");
};

//수정 form을 위한 정보
const updateId = ref(0);
const updatePostObject = ref({});
const openUpdateForm = (reviewObject) => {
  updateId.value = reviewObject.id;
  updatePostObject.value = {
    title: reviewObject.title,
    content: reviewObject.content,
    rating: reviewObject.rating,
  };

  formOpen.value = "update";
};
</script>

<style scoped></style>
