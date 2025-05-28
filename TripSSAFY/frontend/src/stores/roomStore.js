import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import { useUserStore } from './userStore';
import api from "@/api/axios";

export const useRoomStore = defineStore('room', () => {

    const _tripRoom = ref({});
    const tripRoom = computed(() => _tripRoom.value);

    const _userStore = useUserStore();

    const fetchTripRoom = async (id) => {
        const response = await api.get(`/room/${id}`);
        if (response.status === 200) {
            console.log(response.data);
            _tripRoom.value = response.data;
        } else {
            console.error('Failed to fetch trip room data');
        }
    }

    return {
        tripRoom,

        fetchTripRoom,
    };
})