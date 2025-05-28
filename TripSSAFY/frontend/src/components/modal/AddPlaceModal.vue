<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg w-96">
            <h2 class="text-lg font-semibold mb-4">여행 장소 추가</h2>
            <!-- 장소 이름, 메모 등 입력 -->
            <input v-model="placeName" class="w-full border p-2 rounded mb-4" placeholder="장소 이름 입력" />

            <div class="flex justify-end gap-2">
                <button class="text-sm bg-gray-200 hover:bg-gray-300 transition px-4 py-2 rounded" @click="$emit('close')">취소</button>
                <button class="text-sm bg-ssafy_blue hover:bg-blue-700 transition text-white px-4 py-2 rounded" @click="submitPlace">추가</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
const props = defineProps({ latlng: Object });
const emit = defineEmits(['close', 'submit']);

const placeName = ref('');

const submitPlace = () => {
    // 실제 추가 로직 (ex: emit 또는 API 호출)
    const spot = {
        contentid: -1,
        title: placeName.value,
        mapx: props.latlng.getLng(),
        mapy: props.latlng.getLat(),
    }
    emit('submit', spot);
};
</script>
