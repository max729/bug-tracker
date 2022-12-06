<template>
    <main  class="form-signin w-100 m-auto">
      <form @submit.prevent="submit">
         <h1 class="h3 mb-3 fw-normal">Please enter new Password</h1>

        <div class="form-floating">
          <input v-model="data.password" type="password" class="form-control" placeholder="Password">
          <label >Password</label>
        </div>
    
        <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
    
    
      </form>
      
      
    </main>
    </template>
    
    <script>
    import { reactive } from 'vue';
    import axios from 'axios';
    import { useRouter , useRoute} from 'vue-router';
    
    export default {
      name:   "Login",
      setup() {
        const data = reactive({
          email: "",
          password: ""
        });
    
        const router = useRouter();
        const route = useRoute();
      
        const submit = async () =>{
    
          try{
            // TODO: test (try) and ...data
            const response = await axios.post( "/appUsers/login" , 
                {...data,token: route.params.token}, 
                {
                'Content-Type': 'application/json'
            });
    
    
            await router.push("/login");
    
          } catch (e) {
            console.log(e)
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