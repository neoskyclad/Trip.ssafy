<template>
  <div>
    <draggable
      v-model="localPlaces"
      group="places"
      animation="200"
      itemKey="id"
      @end="checkIfChanged"
    >
      <template #item="{ element, index }">
        <div @click="handleClick(element)" class="cursor-pointer">
          <TourScheduleDetail
            :place="element"
            :index="index"
            @memo="emit('memo', element.id)"
            @delete="emit('delete', element.id)"
          />
        </div>
      </template>
    </draggable>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import draggable from "vuedraggable";
import TourScheduleDetail from "./TourScheduleDetail.vue";
import { defineEmits } from "vue";

const emit = defineEmits(["memo", "delete", "changed", "show-review"]);

// 외부로부터 받은 props
const props = defineProps({
  placesForCurrentDay: {
    type: Array,
    required: true,
  },
});

// 드래그를 위해 props를 복사 (v-model은 반응형 ref여야 작동)
const localPlaces = ref([...props.placesForCurrentDay]);

// 드래그 끝나고 순서 변경 확인
function checkIfChanged() {
  const originalIds = props.placesForCurrentDay.map((p) => p.id);
  const currentIds = localPlaces.value.map((p) => p.id);

  const isChanged = originalIds.some((id, index) => id !== currentIds[index]);

  if (isChanged) {
    emit("changed", localPlaces.value); // 부모에서 수정 버튼 보이게
  } else {
    emit("changed", null); // 부모에서 수정 버튼 숨기기
  }
}

function handleClick(place) {
  emit("show-review", place); // 부모에서 모달 띄우도록 전달
}

// props 변경되면 반영
watch(
  () => props.placesForCurrentDay,
  (newVal) => {
    localPlaces.value = [...newVal];
  }
);
</script>
