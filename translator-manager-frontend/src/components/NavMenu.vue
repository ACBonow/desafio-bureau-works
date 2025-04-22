<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand fw-bold" to="/">Translator Manager</router-link>
      <div class="navbar-nav me-auto">
        <router-link 
          class="nav-item nav-link px-3 py-2 rounded-3 mx-1" 
          :class="{ 'bg-primary': $route.path === '/translators', 'bg-dark-subtle': $route.path !== '/translators' }"
          to="/translators"
        >
          <i class="bi bi-translate me-1"></i>
          {{ $t('translator.title') }}
        </router-link>
        <router-link 
          class="nav-item nav-link px-3 py-2 rounded-3 mx-1" 
          :class="{ 'bg-primary': $route.path === '/documents', 'bg-dark-subtle': $route.path !== '/documents' }"
          to="/documents"
        >
          <i class="bi bi-file-text me-1"></i>
          {{ $t('document.title') }}
        </router-link>
      </div>
      <div class="navbar-nav">
        <select class="form-select form-select-sm" v-model="$i18n.locale">
          <option value="pt-BR">Português</option>
          <option value="en-US">English</option>
        </select>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavMenu'
}
</script>

<style scoped>
.navbar {
  margin-bottom: 2rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.navbar-brand {
  font-size: 1.5rem;
  color: #fff !important;
}

.nav-link {
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.85) !important;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.15) !important;
  color: #fff !important;
}

.nav-link.bg-primary {
  background-color: #0d6efd !important;
  color: #fff !important;
}

.nav-link.bg-dark-subtle {
  background-color: rgba(255, 255, 255, 0.05) !important;
}

.form-select {
  width: auto;
  margin-left: 1rem;
  background-color: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
}

.form-select:focus {
  background-color: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.3);
  color: #fff;
}

.form-select option {
  background-color: #343a40;
  color: #fff;
}
</style>