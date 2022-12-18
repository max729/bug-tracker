<template>
<main  class="form-signin w-100 m-auto">
  <form @submit.prevent="submit(true)">
     <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

    <div class="form-floating">
      <input v-model="data.email" type="email" class="form-control" placeholder="name@example.com">
      <label >Email address</label>
    </div>
    <div class="form-floating">
      <input v-model="data.password" type="password" class="form-control" placeholder="Password">
      <label >Password</label>
    </div>


    <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>

    
    <div class="my-3">
      <router-link to="/forgot">Forgot password?</router-link>
    </div>
    <a onclick="event.preventDefault()" v-on:click="submit(false)" href="#" >As Guest</a>

  </form>
  
  
</main>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';


const store = useStore();
const router = useRouter();

const data = reactive({
  email: "",
  password: ""
});

const submit = async (asUser) => {

  const guestUser = { email: "aadwwd@daw.de", password: "12345" }

  const response = await axios.post("/appUsers/login", asUser ? data : guestUser,
    {
      'Content-Type': 'application/json',
      withCredentials: true
    }
  )


  axios.defaults.headers.common['Authorization'] = "Bearer " + response.data.token;

  const { data } = await axios.get("/appUsers/fromToken");

  await store.dispatch('setUser', data);

  await store.dispatch('setAuth', true);

  await router.push("/ticket");


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