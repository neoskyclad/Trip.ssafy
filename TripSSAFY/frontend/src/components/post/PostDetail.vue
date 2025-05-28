<template>
  <h1 class="font-bold text-3xl py-2">{{ postDetail.post.title }}</h1>
  <div class="flex items-center gap-3 mb-2">
    <img
      v-if="postDetail.post.profileImg"
      :src="postDetail.post.profileImg"
      alt="프로필"
      class="w-8 h-8 rounded-full object-cover"
    />
    <img
      v-else
      src="/default_profile.png"
      alt="프로필"
      class="w-8 h-8 rounded-full object-cover"
    />
    <div>
      <span class="text-md font-medium text-gray-800 truncate">
        {{ postDetail.post.nickname }} &#124;
      </span>
      <span class="ml-2 text-sm text-gray-500">
        생성: {{ formatDateTime(postDetail.post.createdAt) }}
      </span>
      <span v-if="postDetail.post.updatedAt" class="ml-2 text-sm text-gray-500">
        (수정됨): {{ formatDateTime(postDetail.post.updatedAt) }}
      </span>
    </div>
    <div class="ml-auto text-md flex gap-3 px-4">
      조회 수
      <span class="text-blue-500 font-bold">
        {{ postDetail.post.viewCount }}</span
      >
      댓글
      <span class="text-green-500 font-bold">
        {{ postDetail.comments.length }}
      </span>
    </div>
  </div>
  <hr />
  <img
    v-if="postDetail.post.img"
    :src="postDetail.post.img"
    class="h-48 m-auto"
  />
  <div id="viewer" class="mt-5 min-h-[30vh]"></div>

  <!-- comment create -->
  <img :src="userStore.profileImage" class="w-8 h-8 rounded-full border" />
  <textarea
    v-model="comment.content"
    rows="3"
    class="mt-2 w-full border border-gray-300 rounded-lg p-2 focus:ring-blue-400 focus:outline-none resize-none"
    placeholder="댓글을 작성하세요"
  ></textarea>
  <div class="flex justify-end">
    <button
      class="w-16 ml-auto bg-ssafy_blue text-white py-2 px-4 rounded hover:bg-blue-700 transition"
      @click.prevent="submitComment"
    >
      등록
    </button>
  </div>

  <CommentItem
    v-for="comment in postDetail.comments"
    :comment
    @delete="loadDetail"
    @update="loadDetail"
  />
</template>
<script setup>
import { ref, onMounted } from "vue";
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import api from "@/api/axios";
import { useUserStore } from "@/stores/userStore";
import CommentItem from "./CommentItem.vue";
import qs from "qs";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const userStore = useUserStore();
const props = defineProps({
  openPostId: Number,
});

const postDetail = ref({
  post: {},
  comments: [],
});
const comment = ref({
  postId: props.openPostId,
  content: "",
});

let viewerInstance = null;
const loadDetail = async () => {
  try {
    const response = await api.get(`/post/${props.openPostId}`);
    postDetail.value = response.data;

    //toastui viewer 설정
    viewerInstance = new Editor.factory({
      el: document.querySelector("#viewer"),
      height: "400px",
      viewer: true,
      initialValue: postDetail.value.post.content,
    });
  } catch (error) {
    showToastError("게시글 조회 실패!");
  }
};
onMounted(loadDetail);

const submitComment = async () => {
  if (!comment.value.content.trim()) {
    showToastError("내용을 입력해주세요.");
    return;
  }
  try {
    const response = await api.post(`/comment`, qs.stringify(comment.value));
    showToastSuccess("댓글 등록 성공!");
    comment.value.content = "";
    loadDetail();
  } catch (err) {
    showToastError("댓글 등록 실패!");
    console.log(err);
  }
};
function formatDateTime(input) {
  const date = new Date(input); // input은 ISO 문자열이나 Date 객체

  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, "0"); // 월은 0부터 시작
  const dd = String(date.getDate()).padStart(2, "0");

  const hh = String(date.getHours()).padStart(2, "0");
  const min = String(date.getMinutes()).padStart(2, "0");

  return `${yyyy}-${mm}-${dd} ${hh}:${min}`;
}
</script>
