<template>
<main class="form-signin w-100 m-auto">
  <form @submit.prevent="submit" class="d-grid gap-3">
     <h1 class="h3 mb-3 fw-normal">Please Register</h1>

    <div class="form-floating">
      <input v-model="data.id" type="text" class="form-control"  placeholder="Userame">
      <label >User Name</label>
    </div>

    <div class="form-floating">
      <input v-model="data.email" type="email" class="form-control"  placeholder="name@example.com">
      <label >Email address</label>
    </div>

    <div class="form-floating">
      <input v-model="data.firstName" type="text" class="form-control"  placeholder="firstname">
      <label >Firstname</label>
    </div>

    <div class="form-floating">
      <input v-model="data.lastName" type="text" class="form-control" placeholder="firstname">
      <label >Lastname</label>
    </div>

    <div class="form-floating">
      <input v-model="data.password" type="password" class="form-control" placeholder="Password">
      <label >Password</label>
    </div>



    <button class="w-100 btn btn-lg btn-primary" type="submit">Register</button>
  </form>
</main>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from "vue-router";

const data = reactive({
  id : "",
  firstName: "",
  lastName: "",
  email: "",
  password: ""
});

const router = useRouter();

const submit = async () => {
  try {

    await axios.post("/appUsers/register", data, {
      'Content-Type': 'application/json',
    });

    await router.push('/login');

  } catch (e) {
    console.log(e);

  }
};


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