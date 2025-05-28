import { createRouter, createWebHistory } from "vue-router";
import HomePage from "../views/HomePage.vue";
import KakaoCallback from "@/components/callback/KakaoCallback.vue";
import MyPage from "@/views/MyPage.vue";
import RoomPage from "@/views/RoomPage.vue";
import SearchPage from "@/views/SearchPage.vue";
import RecommendPage from "@/views/RecommendPage.vue";
import BoardPage from "@/views/BoardPage.vue";
import BoardCreatePage from "@/views/BoardCreatePage.vue";
import BoardDetailPage from "@/views/BoardDetailPage.vue";
import { useUserStore } from "@/stores/userStore";
import BoardUpdatePage from "@/views/BoardUpdatePage.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: HomePage,
  },
  // {
  //   path: '/login/oauth2/code/kakao',
  //   name: 'Kakao Login',
  //   component: KakaoCallback
  // },
  {
    path: "/mypage",
    name: "My Page",
    component: MyPage,
  },
  {
    path: "/search",
    name: "Search Page",
    component: SearchPage,
  },
  {
    path: "/room",
    name: "Room",
    component: RoomPage,
  },
  {
    path: "/recommend",
    name: "Recommendation",
    component: RecommendPage,
  },
  {
    path: "/post",
    name: "Notice",
    component: BoardPage,
  },
  {
    path: "/post/:id",
    name: "BoardDetail",
    component: BoardDetailPage,
  },
  {
    path: "/post/form",
    name: "BoardCreate",
    component: BoardCreatePage,
  },
  {
    path: "/post/form/:id",
    name: "BoardUpdate",
    component: BoardUpdatePage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 라우팅이 바뀔 때마다 실행됨
// router.beforeEach(async (to, from, next) => {
//   const userStore = useUserStore()

//   if (userStore.accessToken && !userStore.userInfo) {
//     try {
//       await userStore.fetchUserInfo()  // 사용자 정보 갱신
//     } catch (e) {
//       console.error('유저 정보 갱신 실패:', e)
//       userStore.logout()
//       next('/login')  // 유효하지 않은 경우 로그인 페이지로
//       return
//     }
//   }

//   next()
// })

export default router;
