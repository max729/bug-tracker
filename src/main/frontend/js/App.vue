<script >
import Nav from "./components/Nav.vue";

export default {
  components: {
    Nav
  }
}


</script>

<template>
  <Nav />

  <router-view></router-view>

</template>

<style scoped>
.form-signin {
  max-width: 330px;
  padding: 15px;
}


</style>
