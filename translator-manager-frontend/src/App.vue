<template>
  <div id="app">
    <nav-menu />
    <router-view />
  </div>
</template>

<script>
import NavMenu from './components/NavMenu.vue'

export default {
  name: 'App',
  components: {
    NavMenu
  }
}
</script>

<style>
#app {
  font-family: Arial, sans-serif;
}
</style>