<template>
  <div class="container">
    <div v-if="isLoading" class="spinner-overlay">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2>{{ $t('translator.title') }}</h2>
      <div class="d-flex align-items-center">
        <div class="me-3">
          <label class="form-label me-2">{{ $t('common.itemsPerPage') }}</label>
          <select class="form-select form-select-sm d-inline-block w-auto" v-model="translators.size" @change="loadTranslators(0)">
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
            <option value="100">100</option>
            <option value="1000">1000</option>
          </select>
        </div>
        <button class="btn btn-primary me-2" @click="showAddModal">
          {{ $t('translator.newTranslator') }}
        </button>
        <label class="btn btn-secondary">
          {{ $t('common.uploadCSV') }}
          <input type="file" accept=".csv" class="d-none" @change="handleFileUpload" />
        </label>
      </div>
    </div>

    <TableView
        :headers="headers"
        :fields="fields"
        :items="translators.content"
        :actions="actions"
        :currentPage="translators.number"
        :pageSize="translators.size"
    />

    <Pagination
        :currentPage="translators.number"
        :totalPages="translators.totalPages"
        :first="translators.first"
        :last="translators.last"
        @change="changePage"
    />

    <ModalView
        v-if="showModal"
        :title="editingTranslator.id ? $t('translator.editTranslator') : $t('translator.newTranslator')"
        @close="closeModal"
        @save="saveTranslator"
    >
      <form>
        <div class="mb-3">
          <label class="form-label">{{ $t('translator.name') }}</label>
          <input v-model="editingTranslator.name" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label">{{ $t('translator.email') }}</label>
          <input v-model="editingTranslator.email" type="email" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label">{{ $t('translator.sourceLanguage') }}</label>
          <input v-model="editingTranslator.sourceLanguage" type="text" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label">{{ $t('translator.targetLanguage') }}</label>
          <input v-model="editingTranslator.targetLanguage" type="text" class="form-control" required />
        </div>
      </form>
    </ModalView>
  </div>
</template>

<script>
import TableView from '@/components/TableView.vue';
import PaginationView from '@/components/PaginationView.vue';
import ModalView from '@/components/ModalView.vue';
import TranslatorService from '@/services/TranslatorService.js';

export default {
  components: { TableView, Pagination: PaginationView, ModalView },
  data() {
    return {
      translators: { content: [], number: 0, size: 10, totalPages: 0, first: true, last: true },
      showModal: false,
      isLoading: false,
      editingTranslator: { name: '', email: '', sourceLanguage: '', targetLanguage: '' },
      headers: [
        { label: '#', style: 'width: 50px' },
        { label: this.$t('translator.name') },
        { label: this.$t('translator.email') },
        { label: this.$t('translator.sourceLanguage') },
        { label: this.$t('translator.targetLanguage') },
        { label: this.$t('common.actions'), style: 'width: 100px' },
      ],
      fields: ['index', 'name', 'email', 'sourceLanguage', 'targetLanguage'],
      actions: [
        { class: 'btn btn-sm btn-primary me-2', icon: 'bi bi-pencil', title: this.$t('common.edit'), handler: this.editTranslator },
        { class: 'btn btn-sm btn-danger', icon: 'bi bi-x-lg', title: this.$t('common.delete'), handler: this.deleteTranslator },
      ],
    };
  },
  created() {
    this.loadTranslators();
  },
  methods: {
    async loadTranslators(page = 0) {
      try {
        const response = await TranslatorService.getAll(page, this.translators.size);
        this.translators = response.data;
      } catch (error) {
        console.error('Erro ao carregar tradutores:', error.response?.data || error.message);
        alert(error.response?.data || 'Erro ao carregar tradutores.');
      }
    },
    changePage(page) {
      this.loadTranslators(page);
    },
    showAddModal() {
      this.editingTranslator = { name: '', email: '', sourceLanguage: '', targetLanguage: '' };
      this.showModal = true;
    },
    editTranslator(translator) {
      this.editingTranslator = { ...translator };
      this.showModal = true;
    },
    async saveTranslator() {
      try {
        await TranslatorService.save(this.editingTranslator);
        this.closeModal();
        this.loadTranslators();
      } catch (error) {
        console.error('Erro ao salvar tradutor:', error.response?.data || error.message);
        alert(error.response?.data || 'Erro ao salvar tradutor.');
      }
    },
    async deleteTranslator(translator) {
      try {
        await TranslatorService.delete(translator.id);
        this.loadTranslators();
      } catch (error) {
        console.error('Erro ao deletar tradutor:', error.response?.data || error.message);
        alert(error.response?.data || 'Erro ao deletar tradutor. Verifique se ele possui documentos associados.');
      }
    },
    async handleFileUpload(event) {
      this.isLoading = true;

      try {
        const file = event.target.files[0];
        await TranslatorService.upload(file);
        this.loadTranslators();
      } catch (error) {
        console.error('Erro ao fazer upload do arquivo:', error.response?.data || error.message);
        alert(error.response?.data || 'Erro ao fazer upload do arquivo.');
        location.reload(true);
      }
      finally {
        this.isLoading = false;
      }
    },
    closeModal() {
      this.showModal = false;
      location.reload(true);

    },
  },
};
</script>


<style>
.spinner-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1050;
}
</style>