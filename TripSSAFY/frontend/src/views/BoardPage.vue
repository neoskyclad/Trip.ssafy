<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <main class="flex-1 px-32 py-8 flex flex-col">
      <h1 class="text-4xl font-bold text-center">게시판</h1>
      <div class="flex border-b mt-4 mb-2 text-2xl">
        <button
          @click="
            type = 'notice';
            goSearch();
          "
          :class="[
            'w-1/2 py-2 text-center text-lg font-bold',
            type == 'notice'
              ? 'border-b-2 border-blue-500 text-blue-600 font-semibold'
              : 'text-gray-500 hover:text-blue-600',
          ]"
        >
          공지사항
        </button>
        <button
          @click="
            type = 'qna';
            goSearch();
          "
          :class="[
            'w-1/2 py-2 text-center text-lg font-bold',
            type == 'qna'
              ? 'border-b-2 border-blue-500 text-blue-600 font-semibold'
              : 'text-gray-500 hover:text-blue-600',
          ]"
        >
          Q&amp;A
        </button>
      </div>
      <div class="flex justify-between py-2">
        <h1 class="text-3xl font-bold">
          전체 <span class="text-ssafy_blue"> {{ totalCount }}</span
          >건
        </h1>
        <div class="flex gap-4">
          <div
            class="flex items-center border border-gray-300 rounded-lg px-4 py-2 bg-white w-64"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-5 h-5 text-gray-400 mr-2"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1010.5 18a7.5 7.5 0 006.15-3.35z"
              />
            </svg>
            <input
              type="text"
              placeholder="검색어를 입력하세요."
              v-model="searchQuery"
              @keyup.enter="goSearch"
              class="w-full outline-none text-sm text-gray-700 placeholder-gray-400 bg-transparent"
            />
          </div>
          <button
            v-if="showCreateButton"
            type="button"
            class="bg-ssafy_blue text-white py-2 px-4 rounded transition"
            @click="goCreatePage()"
          >
            게시글 작성
          </button>
        </div>
      </div>

      <div v-if="totalCount > 0">
        <div
          v-for="post in posts"
          :key="post.id"
          @click.prevent="router.push(`/post/${post.id}`)"
          class="border rounded-lg p-4 mb-4 bg-white shadow hover:shadow-md transition cursor-pointer"
        >
          <!-- 프로필 & 닉네임 & 날짜 -->
          <div class="flex items-center mb-2 text-sm text-gray-500">
            <img
              :src="post.profileImg || '/default_profile.png'"
              alt="프로필 이미지"
              class="w-8 h-8 rounded-full mr-2 object-cover"
            />
            <span class="font-semibold text-gray-700 mr-2">{{
              post.nickname
            }}</span>
            <span class="mr-1">·</span>
            <span>{{ formatDate(post.createdAt) }}</span>
            <span v-if="post.updatedAt" class="ml-2 text-xs text-gray-400">
              (수정됨: {{ formatDate(post.updatedAt) }})
            </span>
            <!-- 수정, 삭제 드롭다운 -->
            <div v-if="post.isMyPost" class="ml-auto">
              <div class="relative">
                <button
                  @click.stop.prevent="toggleDropdown(post.id)"
                  class="w-8 h-8 flex items-center justify-center rounded border border-transparent hover:border-gray-300 transition text-gray-500 hover:text-black"
                >
                  ⋮
                </button>

                <div
                  v-if="showDropdown === post.id"
                  class="absolute right-0 top-full mt-1 w-24 bg-white border rounded shadow z-50"
                >
                  <button
                    @click.stop="router.push(`post/form/${post.id}`)"
                    class="block w-full px-4 py-2 text-left hover:bg-gray-100"
                  >
                    수정
                  </button>
                  <button
                    @click.stop="
                      openDeleteConfirm = true;
                      selectedPostId = post.id;
                    "
                    class="block w-full text-left px-4 py-2 text-red-600 hover:bg-red-100 transition"
                  >
                    삭제
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 제목 -->
          <h2 class="text-lg font-semibold text-gray-900 truncate mb-1">
            {{ post.title }}
          </h2>

          <!-- 내용 요약 -->
          <p class="text-sm text-gray-700 line-clamp-2">
            {{ stripHtml(post.content).slice(0, 100)
            }}{{ post.content.length > 100 ? "..." : "" }}
          </p>
        </div>
      </div>
      <div
        v-else
        class="flex-1 flex flex-col justify-center items-center text-center"
      >
        <img
          src="/warning-page.png"
          width="128px"
          height="128px"
          class="mb-4"
        />
        <p class="py-8 text-lg text-gray-500">게시글이 없습니다.</p>
      </div>

      <Pagination
        :currentPage="pageNo"
        :totalPages="totalPages"
        @update:currentPage="changePage"
        class="mt-auto pt-8"
      />
    </main>
    <Footer />
    <DeleteConfirmModal
      v-if="openDeleteConfirm"
      @confirm-delete="onDelete"
      @cancel="openDeleteConfirm = false"
    />
  </div>
</template>

<script setup>
import Header from "@/components/common/Header.vue";
import Footer from "@/components/common/Footer.vue";
import DeleteConfirmModal from "@/components/modal/DeleteConfirmModal.vue";
import { ref, onMounted, watch, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";
import Pagination from "@/components/common/Pagination.vue";
import { showToastSuccess, showToastError } from "@/api/toastMsg";
const route = useRoute();
const router = useRouter();

const posts = ref([]);
const pageNo = ref(Number(route.query.page) || 1);
const totalPages = ref(1); // 총 페이지 수, 백엔드에서 받아오는 경우 여기에 설정
const totalCount = ref(0);
const searchQuery = ref(route.query.search || "");
const type = ref(route.query.type || "notice"); // 기본 타입 설정

const fetchPosts = async () => {
  const queryParams = {
    search: searchQuery.value,
    page: pageNo.value,
    type: type.value,
  };
  try {
    const response = await api.get(`/post/notice`, { params: queryParams });
    console.log(response.data);
    posts.value = response.data.posts; // 백엔드 응답 구조에 따라 조정
    totalCount.value = response.data.totalCount;
    totalPages.value = Math.ceil(totalCount.value / 8); // 예: 전체 페이지 수
  } catch (err) {
    console.error(err);
  }
};

// 검색 버튼 클릭 시
const goSearch = () => {
  router.push({
    path: "/post",
    query: {
      type: type.value,
      search: searchQuery.value,
      page: 1,
    },
  });
};

const goCreatePage = () => {
  router.push({
    path: "/post/form",
    query: {
      type: type.value,
    },
  });
};

// 페이지 변경 시
const changePage = (newPage) => {
  router.push({
    path: "/post",
    query: {
      ...route.query,
      page: newPage,
    },
  });
};

watch(
  () => route.query,
  () => {
    pageNo.value = Number(route.query.page) || 1;
    searchQuery.value = route.query.search || "";
    type.value = route.query.type || "notice";
    fetchPosts();
  },
  { immediate: true } // 첫 마운트 시에도 실행되게 함
);
// 날짜 포맷 함수
const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  return date.toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
};

// HTML 태그 제거용
const stripHtml = (html) => {
  const div = document.createElement("div");
  div.innerHTML = html;
  return div.textContent || div.innerText || "";
};

//수정, 삭제 dropdown용 변수, 함수
const openDeleteConfirm = ref(false);
const selectedPostId = ref(0);
const showDropdown = ref(0);

function toggleDropdown(id) {
  if (showDropdown.value === 0) showDropdown.value = id;
  else showDropdown.value = 0;
}
const onDelete = async () => {
  try {
    const response = api.delete(`/post/${selectedPostId.value}`);
    showToastSuccess("삭제되었습니다");
    setTimeout(() => location.reload(), 1500);
  } catch (err) {
    showToastError(err.response.data);
  } finally {
    openDeleteConfirm.value = false;
  }
};

const userStore = useUserStore();
const showCreateButton = computed(() => {
  return (
    userStore.role !== "" &&
    (type.value === "qna" || userStore.role === "ROLE_ADMIN")
  );
});
</script>

<style scoped></style>
