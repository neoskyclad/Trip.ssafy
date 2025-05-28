<template>
    <div class="modal-overlay">
        <div class="modal">
            <p class="message">정말로 삭제하시겠습니까?</p>
            <div class="button-group">
                <button @click="cancel">취소</button>
                <button @click="confirmDelete">삭제</button>
            </div>
        </div>
    </div>
</template>

<script setup>
const emit = defineEmits(['confirmDelete', 'cancel'])

const cancel = () => {
    emit('cancel')
}

const confirmDelete = () => {
    emit('confirmDelete')
}
</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 999;
}

.modal {
    background-color: white;
    padding: 30px 40px;
    border-radius: 12px;
    text-align: center;
    min-width: 300px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.message {
    font-size: 18px;
    margin-bottom: 20px;
}

.button-group {
    display: flex;
    justify-content: space-between;
    gap: 20px;
}

.button-group button {
    flex: 1;
    padding: 10px 0;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 14px;
}

.button-group button:first-child {
    background-color: lightgray;
}

.button-group button:last-child {
    background-color: #ff4d4d;
    color: white;
}
</style>
