<script >
import Nav from "./components/Nav.vue";
import Sidebar from "./components/Sidebar.vue";

export default {
  components: {
    Nav,
    Sidebar
}
}


</script>

<template>
  <Nav />

  <div class="container-fluid">
    <div class="row">
<Sidebar />
    <div class="col">
      <router-view></router-view>

    </div>

    </div>
    
    
  </div>
  
  

  

</template>

<style scoped>
.form-signin {
  max-width: 330px;
  padding: 15px;
}


</style>
