<template>
<main @submit.prevent="submit" class="form-signin w-100 m-auto">
  <form class="d-grid gap-3">
     <h1 class="h3 mb-3 fw-normal">Please Register</h1>

    <div class="form-floating">
      <input v-model="data.email" type="email" class="form-control"  placeholder="name@example.com">
      <label >Email address</label>
    </div>

    <div class="form-floating">
      <input v-model="data.first_name" type="text" class="form-control"  placeholder="firstname">
      <label >Firstname</label>
    </div>

    <div class="form-floating">
      <input v-model="data.last_name" type="text" class="form-control" placeholder="firstname">
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

<script>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from "vue-router";

export default {
  name:   "Register",
  setup() {
    const data = reactive({
      firstName: "",
      lastName: "",
      email: "",
      password: ""
    }

    
    );

    const router = useRouter();

    const submit = async () =>{


      try{
        
        await axios.post( "/appUsers/register" , data,{
          'Content-Type': 'application/json',
        });

        await router.push('/login');

      } catch (e) {
        console.log(e);

      }
        

      
      

    }

    return {
      data,
      submit
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