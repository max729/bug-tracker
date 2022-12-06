<template>
    <main class="form-signin w-100 m-auto">
        <form @submit.prevent="submit">
            <h1 class="h3 mb-3 fw-normal">Please insert email!</h1>

            <div v-if="notify.cls" :class="'alert alert-' + notify.cls" role="alert">
                {{ notify.message }}
            </div>
            <div class="form-floating my-3">
                <input v-model="data.email" type="email" class="form-control" placeholder="name@example.com">
                <label>Email address</label>
            </div>


            <button class="w-100 btn btn-lg btn-primary" type="submit">Send Mail</button>


        </form>


    </main>
</template>
    
<script>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
    name: "Forgot",
    setup() {
        const data = reactive({
            email: ""
        });
        const notify = reactive({
            cls: '',
            message: ''
        });

        const router = useRouter();

        const submit = async () => {

            const response = await axios.post("/appUsers/forgot", data,
                {
                    'Content-Type': 'application/json'
                });

            if (response.status === 200) {
                notify.cls = "success"
                notify.message = "Email was sent";
            } else {
                notify.cls = "danger"
                notify.message = "Email not found";

            }

        }

        return {
            data,
            submit,
            notify
        }
    }

}

</script>
    
<style scoped>
.form-signin .form-floating:focus-within {
    z-index: 2;
}

.form-signin input[type="email"] {
    margin-bottom: -1px;
    border-bottom-right-radius: 0;
    border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
    margin-bottom: 10px;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
}
</style>