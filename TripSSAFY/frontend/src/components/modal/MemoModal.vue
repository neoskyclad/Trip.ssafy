<!-- src/components/MemoModal.vue -->
<template>
  <div class="modal-backdrop">
    <div class="modal">
      <h3>{{ spot.placeName }}의 메모</h3>
      <textarea v-model="memo" rows="5" cols="40" />
      <div class="actions">
        <button @click="saveMemo">저장</button>
        <button @click="close">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import api from "@/api/axios";
import { showToastError, showToastSuccess } from "@/api/toastMsg";

const props = defineProps({
  spot: Object,
});
const emits = defineEmits(["close", "saved"]);

const memo = ref(props.spot?.memo ?? "");

watch(
  () => props.spot,
  () => {
    memo.value = props.spot?.memo ?? "";
  }
);

const close = () => emits("close");

const saveMemo = async () => {
  const id = props.spot.id;
  try {
    await api.patch("/place/memo", {
      tripRoomPlaceId: id,
      memo: memo.value,
    });
    showToastSuccess("메모 저장 완료!");
    emits("saved");
    close();
  } catch (err) {
    console.error("메모 저장 실패", err);
    showToastError("메모 저장 실패");
  }
};
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal {
  background: white;
  padding: 20px;
  border-radius: 8px;
  min-width: 300px;
}
.actions {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
