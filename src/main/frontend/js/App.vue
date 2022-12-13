<template>

  <div class="container-fluid overflow-hidden">
    <div class="row vh-100 overflow-auto">
      <Nav v-if="!auth" />
      <Sidebar v-if="auth" />
      
      <div class="col d-flex flex-column h-100">
        <router-view></router-view>
      </div>





    </div>
  </div>





</template>

<script setup>
import Nav from "./components/Nav.vue";
import Sidebar from "./components/Sidebar.vue";
import { useStore } from 'vuex';
import { computed, onMounted, ref } from 'vue';

const store = useStore();
let auth = computed(() => store.state.auth);

</script>



<style scoped>
.form-signin {
  max-width: 330px;
  padding: 15px;
}


</style>
