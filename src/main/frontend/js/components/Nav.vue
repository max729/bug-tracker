<template>
    <header class="p-3 text-bg-dark">
    <div class="container">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">

        <ul class="nav col-12 col-sm-auto me-sm-auto mb-2 justify-content-center mb-md-0">
          <li><router-link to="/login" class="nav-link px-2 text-secondary">Bug-Tracker</router-link></li>
        </ul>



          <router-link to="/login" class="btn btn-outline-light me-2">Login</router-link>
          <router-link to="/register" type="button" class="btn btn-warning">Register</router-link >

      </div>
    </div>
  </header>
</template>

