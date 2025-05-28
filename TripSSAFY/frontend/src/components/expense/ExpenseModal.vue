<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
  >
    <div
      class="bg-white p-6 rounded-md shadow-lg w-[1200px] max-h-[90vh] overflow-y-auto relative"
      @click.stop
    >
      <!-- 닫기 버튼 -->
      <button
        @click="$emit('close')"
        class="absolute top-2 right-3 text-gray-500 hover:text-black text-3xl"
      >
        &times;
      </button>
      <h2 class="text-2xl font-bold mb-6">💰정산</h2>
      <div v-if="expenses.length > 0" class="space-y-4">
        <div v-if="gameResultURL" class="flex flex-col items-center">
          <div
            class="absolute top-[60px]"
            style="
              width: 10px;
              height: 40px;
              background: black;
              border-radius: 0 0 10px 10px;
            "
          ></div>
          <img :src="gameResultURL" />
        </div>
        <div
          v-for="expense in expenses"
          :key="expense.username"
          class="flex items-center space-x-4 border-b pb-2"
        >
          <!-- 프로필 이미지 -->
          <img
            v-if="expense.profileImg"
            :src="expense.profileImg"
            class="w-10 h-10 rounded-full object-cover"
          />
          <img
            v-else
            src="/default_profile.png"
            class="w-10 h-10 rounded-full object-cover"
          />

          <!-- 닉네임과 금액 -->
          <div class="flex-1 flex items-center justify-between">
            <p class="text-gray-800 font-medium">{{ expense.nickname }}</p>
            <div class="relative w-24">
              <input
                type="text"
                :value="expense.amount.toLocaleString()"
                readonly
                class="w-full pr-6 text-right border-gray-400 bg-transparent focus:outline-none focus:border-blue-500"
              />
              <span
                class="absolute right-0 top-1/2 -translate-y-1/2 text-gray-500 text-sm"
                >&#8361;</span
              >
            </div>
          </div>
        </div>
      </div>

      <!-- 정산 내역 추가 form -->
      <div v-else>
        <div class="flex items-center gap-4 w-full mb-8">
          <!-- Label -->
          <label
            for="total-price"
            class="text-lg font-medium text-gray-700 whitespace-nowrap"
          >
            총 액
          </label>

          <!-- Input -->
          <input
            type="number"
            id="total-price"
            v-model.number="totalPrice"
            placeholder="금액 입력"
            class="w-48 px-3 py-2 border border-gray-300 rounded-md text-sm text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          />

          <!-- 1/N 버튼 -->
          <button
            class="shrink-0 px-4 py-2 text-sm font-semibold text-white bg-blue-600 rounded-md hover:bg-blue-700 transition"
            @click="splitEqually"
          >
            1/N
          </button>
          <button
            class="shrink-0 px-4 py-2 text-sm font-semibold text-white bg-blue-600 rounded-md hover:bg-blue-700 transition"
            @click.prevent="openGameRoullete = !openGameRoullete"
          >
            몰빵 룰렛!
          </button>
        </div>
        <!-- 룰렛 게임 화면! -->
        <ExpenseRoulette
          v-if="openGameRoullete"
          :placeId="placeId"
          @selected="(idx) => updateExpenses(idx)"
        />
        <div
          v-for="(member, index) in memberList"
          :key="member.nickname"
          class="flex items-center space-x-4 border-b pb-2"
        >
          <img
            v-if="member.profileImg"
            :src="member.profileImg"
            class="w-10 h-10 rounded-full object-cover"
          />
          <img
            v-else
            src="/default_profile.png"
            class="w-10 h-10 rounded-full object-cover"
          />

          <div class="flex-1 flex items-center justify-between">
            <p class="text-gray-800 font-medium">{{ member.nickname }}</p>
            <div class="relative w-48">
              <input
                type="number"
                v-model.number="amounts[index]"
                class="w-full pr-6 text-right border-gray-400 border-b focus:outline-none focus:border-blue-500"
              />
              <span
                class="absolute right-0 top-1/2 -translate-y-1/2 text-gray-500 text-sm"
                >&#8361;</span
              >
            </div>
          </div>
        </div>
        <div class="flex justify-end">
          <button
            class="mt-4 w-16 ml-auto bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 transition"
            @click="submitExpenses"
          >
            정산
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import api from "@/api/axios";
import { ref, onMounted, inject } from "vue";
import ExpenseRoulette from "./ExpenseRoulette.vue";
import { showToastSuccess, showToastError } from "@/api/toastMsg";
const props = defineProps({
  placeId: Number,
});
const emit = defineEmits("close");
const expenses = ref([]);
const memberList = inject("selectedRoom").value.memberList;
const gameResultURL = ref(null);

const openGameRoullete = ref(false);
const totalPrice = ref(0);
const amounts = ref(new Array(memberList.length).fill(0));

const submitForm = ref([]);
const loadExpenses = async () => {
  try {
    const response = await api.get(`/expense/${props.placeId}`);
    expenses.value = response.data;
    loadGameImage();
  } catch (err) {
    showToastError("정산 내역 불러오기 실패!");
    console.log(err);
  }
};

const loadGameImage = async () => {
  try {
    const response = await api.get(`/expense/game-result/${props.placeId}`);
    gameResultURL.value = response.data;
  } catch (err) {
    console.log(err);
  }
};
onMounted(loadExpenses);

const submitExpenses = async () => {
  //submit form 채우기
  submitForm.value = memberList.map((member, idx) => ({
    nickname: member.nickname,
    tripPlaceId: props.placeId,
    amount: amounts.value[idx],
  }));
  try {
    const response = await api.post("/expense", submitForm.value);
    showToastSuccess("정산이 완료되었습니다!");
    loadExpenses();
  } catch (err) {
    showToastError("정산 내역 추가 실패!");
    console.log(err);
  }
};

const splitEqually = () => {
  const each = Math.floor(totalPrice.value / memberList.length);
  for (let i = 0; i < amounts.value.length; i++) {
    amounts.value[i] = each;
  }
};

const updateExpenses = (idx) => {
  console.log(idx);
  amounts.value = amounts.value.map((_, i) =>
    i === idx ? totalPrice.value : 0
  );
  openGameRoullete.value = false;
};
</script>

<style scoped></style>
