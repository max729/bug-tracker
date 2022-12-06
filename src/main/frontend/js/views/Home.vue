<template >
    <div class="container mt-5 text-center">
        <h1>{{ auth ? message :'you are not logged in' }}</h1>
    </div>
</template>
<script>
import { computed, onMounted, ref } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';

export default {
    name: "Home",
    setup () {
        const message = ref( 'you are not logged in');

        const store = useStore();
        let auth = computed(()=> store.state.auth );

        onMounted( async () =>{

            try{
                const { data } = await axios.get("/appUsers/token");

                message.value = "Hi " + data.firstName + " " + data.lastName;

                await store.dispatch('setAuth', true);
            } catch (e) {
                await store.dispatch('setAuth', false);
            }
        });
    
        return {
            message,
            auth
            
        };
    
    
    }

    
}

    

</script>
<style >
    
</style>