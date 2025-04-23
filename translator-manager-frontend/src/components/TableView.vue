<template>
  <div>
    <!-- Campo de filtro -->
    <div class="mb-3">
      <input
          type="text"
          class="form-control"
          placeholder="Filtrar..."
          v-model="filterTerm"
          @input="applyFilters"
      />
    </div>

    <table class="table table-sm table-hover">
      <thead class="table-light">
      <tr>
        <th
            v-for="(header, headerIndex) in headers"
            :key="headerIndex"
            :style="header.style"

        >
          {{ header.label }}

        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item, itemIndex) in filteredItems" :key="item.id">
        <td v-for="(field, fieldIndex) in fields" :key="fieldIndex">
          {{ field === 'index' ? (currentPage * pageSize) + itemIndex + 1 : getNestedValue(item, field) }}
        </td>
        <td>
          <div class="d-flex justify-content-center">
            <button
                v-for="(action, actionIndex) in actions"
                :key="actionIndex"
                :class="action.class"
                @click="action.handler(item)"
                :title="action.title"
            >
              <i :class="action.icon"></i>
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  props: {
    headers: { type: Array, required: true },
    fields: { type: Array, required: true },
    items: { type: Array, required: true },
    actions: { type: Array, required: true },
    currentPage: { type: Number, default: 0 },
    pageSize: { type: Number, default: 10 },
  },
  data() {
    return {
      filterTerm: '',

      filteredItems: [...this.items],
    };
  },
  watch: {
    items: {
      handler(newItems) {
        this.filteredItems = [...newItems];
        this.applyFilters();
      },
      deep: true,
    },
  },
  methods: {
    getNestedValue(obj, path) {
      return path.split('.').reduce((acc, key) => acc && acc[key] ? acc[key] : '-', obj);
    },
    applyFilters() {
      const term = this.filterTerm.toLowerCase();
      this.filteredItems = this.items.filter((item) =>
          this.fields.some((field) =>
              String(this.getNestedValue(item, field)).toLowerCase().includes(term)
          )
      );
    },

  },
};
</script>

