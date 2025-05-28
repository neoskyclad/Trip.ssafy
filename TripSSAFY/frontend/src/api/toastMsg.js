import { useToast } from "vue-toast-notification";

const toast = useToast();

export const showToastSuccess = (msg) => {
  toast.open({
    message: msg,
    type: "success",
    duration: 1500,
    position: "top",
  });
};

export const showToastError = (msg) => {
  toast.open({
    message: msg,
    type: "error",
    duration: 1500,
    position: "top",
  });
};
