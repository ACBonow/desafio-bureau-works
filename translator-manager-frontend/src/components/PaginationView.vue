<template>
  <nav aria-label="Navegação de páginas" class="mt-4">
    <ul class="pagination justify-content-center">
      <li class="page-item" :class="{ disabled: first }">
        <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">{{ $t('common.previous') }}</a>
      </li>
      <li
          class="page-item"
          v-for="page in totalPages"
          :key="page"
          :class="{ active: page - 1 === currentPage }"
      >
        <a class="page-link" href="#" @click.prevent="changePage(page - 1)">{{ page }}</a>
      </li>
      <li class="page-item" :class="{ disabled: last }">
        <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">{{ $t('common.next') }}</a>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  props: {
    currentPage: { type: Number, required: true },
    totalPages: { type: Number, required: true },
    first: { type: Boolean, required: true },
    last: { type: Boolean, required: true },
  },
  methods: {
    changePage(page) {
      this.$emit('change', page);
    },
  },
};
</script>