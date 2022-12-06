<template>
<main @submit.prevent="submit" class="form-signin w-100 m-auto">
  <form>
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
    
    

  </form>
  
  <button v-on:click="guestLogin" class="w-100 btn btn-lg btn-primary my-4" >As Guest</button>
</main>
</template>

<script>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name:   "Login",
  setup() {
    const data = reactive({
      email: "",
      password: ""
    });

    const router = useRouter();
  
    const submit = async () =>{

      try{
        const response = await axios.post( "/appUsers/login" , data, 
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



    return {
      data,
      submit,
      guestLogin
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