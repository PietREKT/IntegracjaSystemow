import Toast, {useToast} from "vue-toastification";

export default function Note(){
    const toast = useToast();
    function success(message){
        toast.success(message)
    }

    function error(message){
        toast.error(message)
    }

    return {
        success,
        error
    }
}