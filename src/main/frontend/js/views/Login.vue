<template>
<main  class="form-signin w-100 m-auto">
  <form @submit.prevent="submit">
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
    <a onclick="event.preventDefault()" v-on:click="guestLogin" href="#" >As Guest</a>

  </form>
  
  
</main>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';



const data = reactive({
  email: "",
  password: ""
});

const router = useRouter();

const submit = async () => {

  try {
    const response = await axios.post("/appUsers/login", data,
      {
        'Content-Type': 'application/json',
        withCredentials: true
      });

    axios.defaults.headers.common['Authorization'] = "Bearer " + response.data.token;

    await router.push("/");

  } catch (e) {
    console.log("Not Found")
  }


}

const guestLogin = async () => {

  try {
    const response = await axios.post("/appUsers/login", { email: "aadwwd@daw.de", password: "12345" },
      {
        'Content-Type': 'application/json',
        withCredentials: true
      });

    axios.defaults.headers.common['Authorization'] = "Bearer " + response.data.token;

    await router.push("/");

  } catch (e) {
    console.log(e)
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