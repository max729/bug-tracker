<template >
    <div class="container mt-5 text-center">
        <h1>{{ "Hi" + user.firstName + ", " + user.lastName + "you are logged in as" + user.userRole  }}</h1>
    </div>




</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';


const message = ref('you are not logged in');

const store = useStore();
let auth = computed(() => store.state.auth);
let user = computed(() => store.state.user);

onMounted(async () => {

    try {
        const { data } = await axios.get("/appUsers/token");

        message.value = "Hi " + data.firstName + " " + data.lastName;

        await store.dispatch('setAuth', true);
    } catch (e) {
        await store.dispatch('setAuth', false);
    }
});


    

</script>
<style >
    
</style>