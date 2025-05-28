<template>
  <div class="relative flex flex-col items-center space-y-4">
    <!-- 룰렛 타이틀 -->
    <div v-if="selectedIndex >= 0" class="flex">
      <img src="@/assets/gifs/congrats.gif" class="w-12 h-12" />
      <h2 class="text-2xl font-semibold">
        축!
        <span class="text-orange-500"
          >{{ memberList[selectedIndex].nickname }}
        </span>
        님 당첨!
      </h2>
      <img src="@/assets/gifs/congrats.gif" class="w-12 h-12" />
    </div>
    <h2 v-else class="text-xl font-semibold">누가 정산하게 될까요?</h2>
    <!-- ▼ 화살표 (룰렛 중심 위를 가리킴) -->
    <div
      class="absolute top-[20px] z-20"
      style="
        width: 10px;
        height: 40px;
        background: black;
        border-radius: 0 0 10px 10px;
      "
    ></div>

    <!-- 룰렛 캔버스 -->
    <canvas
      ref="canvasRef"
      width="300"
      height="300"
      class="border rounded-full shadow transition-transform duration-1000 ease-out"
    />

    <!-- 회전 버튼 -->
    <div class="flex justify-center">
      <button
        @click="rotate"
        class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
      >
        룰렛 시작!
      </button>
      <button
        v-if="selectedIndex >= 0"
        @click="submitGameResult"
        class="bg-orange-500 ml-4 text-white px-4 py-2 rounded hover:bg-orange-600 transition"
      >
        결과 전송
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from "vue";
import api from "@/api/axios";
import { showToastError, showToastSuccess } from "@/api/toastMsg";
const props = defineProps({
  placeId: Number,
});
const canvasRef = ref(null);
const rotateDeg = ref(0);
const memberList = inject("selectedRoom")?.value?.memberList || [];
const emit = defineEmits(["selected"]);
const colors = [
  "#dc0936",
  "#e6471d",
  "#f7a416",
  "#efe61f",
  "#60b236",
  "#209b6c",
  "#169ed8",
  "#0d66e4",
  "#87207b",
  "#be107f",
  "#e7167b",
];
const selectedIndex = ref(-1);

const newMake = () => {
  const canvas = canvasRef.value;
  if (!canvas || !memberList.length) return;

  const ctx = canvas.getContext("2d");
  const [cw, ch] = [canvas.width / 2, canvas.height / 2];
  const arc = (2 * Math.PI) / memberList.length;

  ctx.clearRect(0, 0, canvas.width, canvas.height);

  for (let i = 0; i < memberList.length; i++) {
    ctx.beginPath();
    ctx.fillStyle = colors[i % colors.length];
    ctx.moveTo(cw, ch);
    ctx.arc(cw, ch, cw - 2, arc * i - Math.PI / 2, arc * (i + 1) - Math.PI / 2);
    ctx.fill();
    ctx.closePath();
  }

  ctx.fillStyle = "#000";
  ctx.textAlign = "center";
  ctx.font = "14px sans-serif";

  for (let i = 0; i < memberList.length; i++) {
    const angle = arc * i + arc / 2 - Math.PI / 2;
    ctx.save();
    ctx.translate(
      cw + Math.cos(angle) * (cw - 40),
      ch + Math.sin(angle) * (ch - 40)
    );
    ctx.rotate(angle + Math.PI / 2);
    ctx.fillText(memberList[i].nickname, 0, 0);
    ctx.restore();
  }

  ctx.fillStyle = "#000";
  ctx.beginPath();
  ctx.arc(cw, ch, 3, 0, Math.PI * 2);
  ctx.fill();
  ctx.closePath();
};

const rotateCanvas = () => {
  const canvas = canvasRef.value;
  if (!canvas) return;

  const temp = document.createElement("canvas");
  temp.width = canvas.width;
  temp.height = canvas.height;

  const tempCtx = temp.getContext("2d");
  tempCtx.drawImage(canvas, 0, 0);

  const ctx = canvas.getContext("2d");
  const [cw, ch] = [canvas.width / 2, canvas.height / 2];
  const deg = (rotateDeg.value % 360) * (Math.PI / 180); // 최종 회전 각도

  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.save();
  ctx.translate(cw, ch);
  ctx.rotate(deg);
  ctx.translate(-cw, -ch);
  ctx.drawImage(temp, 0, 0);
  ctx.restore();
};

const rotate = () => {
  const canvas = canvasRef.value;
  selectedIndex.value = -1;
  if (!canvas) return;

  canvas.style.transform = `initial`;
  canvas.style.transition = `initial`;

  setTimeout(() => {
    const ran = Math.floor(Math.random() * memberList.length);
    const arc = 360 / memberList.length;
    rotateDeg.value = 360 - arc * (ran + 1) + 3600 + arc / 3;

    canvas.style.transform = `rotate(${rotateDeg.value}deg)`;
    canvas.style.transition = `transform 2s ease-out`;

    setTimeout(() => {
      selectedIndex.value = ran;
      console.log(selectedIndex.value);
    }, 2000);
  }, 1);
};

const submitGameResult = async () => {
  rotateCanvas();
  const submitForm = {
    tripPlaceId: props.placeId,
    resultURL: canvasRef.value.toDataURL(),
  };
  console.log(submitForm);
  emit("selected", selectedIndex.value);
  try {
    const response = await api.post(`/expense/game-result`, submitForm);
    showToastSuccess("결과가 전송되었습니다.");
  } catch (err) {
    showToastError("게임 전송 실패!");
    console.log(err);
  }
};
onMounted(newMake);
</script>
