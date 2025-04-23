<template>
        <div class="container">
          <div v-if="isLoading" class="spinner-overlay">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>{{ $t('document.title') }}</h2>
            <div class="d-flex align-items-center">
              <div class="me-3">
                <label class="form-label me-2">{{ $t('common.itemsPerPage') }}</label>
                <select class="form-select form-select-sm d-inline-block w-auto" v-model="documents.size" @change="loadDocuments(0)">
                  <option value="5">5</option>
                  <option value="10">10</option>
                  <option value="20">20</option>
                  <option value="50">50</option>
                  <option value="100">100</option>
                  <option value="1000">1000</option>
                </select>
              </div>
              <button class="btn btn-primary me-2" @click="showAddModal">
                {{ $t('document.newDocument') }}
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
              :items="documents.content"
              :actions="actions"
              :currentPage="documents.number"
              :pageSize="documents.size"
          />

          <Pagination
              :currentPage="documents.number"
              :totalPages="documents.totalPages"
              :first="documents.first"
              :last="documents.last"
              @change="changePage"
          />

          <ModalView
              v-if="showModal"
              :title="editingDocument.id ? $t('document.editDocument') : $t('document.newDocument')"
              @close="closeModal"
              @save="saveDocument"
          >
            <form>
              <div class="mb-3">
                <label class="form-label">{{ $t('document.subject') }}</label>
                <input v-model="editingDocument.subject" type="text" class="form-control" required />
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('document.content') }}</label>
                <textarea v-model="editingDocument.content" class="form-control" required></textarea>
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('document.locale') }}</label>
                <input v-model="editingDocument.locale" type="text" class="form-control" />
              </div>
              <div class="mb-3">
                <label class="form-label">{{ $t('document.author') }}</label>
                <input v-model="editingDocument.author" type="text" class="form-control" required />
              </div>
              <div class="autocomplete">
                <label class="form-label">{{ $t('translator.selectTranslator') }}</label>
                <input
                    type="text"
                    class="form-control"
                    v-model="searchQuery"
                    @input="fetchTranslators"
                    placeholder="Digite para buscar tradutores..."
                />
                <ul v-if="showDropdown && filteredTranslators.length" class="dropdown-menu show">
                  <li
                      v-for="translator in filteredTranslators"
                      :key="translator.id"
                      class="dropdown-item"
                      @click="selectTranslator(translator)"
                  >
                    {{ translator.name +" (" + translator.sourceLanguage + ") " }}
                  </li>
                </ul>
              </div>
            </form>
          </ModalView>
        </div>
      </template>

      <script>
      import TableView from '@/components/TableView.vue';
      import PaginationView from '@/components/PaginationView.vue';
      import ModalView from '@/components/ModalView.vue';
      import DocumentService from '@/services/DocumentService.js';
      import TranslatorService from '@/services/TranslatorService.js';

      export default {
        components: { TableView, Pagination: PaginationView, ModalView },
        data() {
          return {
            searchQuery: '', // Texto digitado no campo de busca
            filteredTranslators: [], // Resultados filtrados da API
            showDropdown: false, // Controla a exibição do dropdown
            documents: { content: [], number: 0, size: 10, totalPages: 0, first: true, last: true },
            showModal: false,
            isLoading: false, // Variável para controlar o estado de carregamento
            editingDocument: { subject: '', content: '', locale: '', author: '' },
            headers: [
              { label: '#', style: 'width: 50px' },
              { label: this.$t('document.subject') },
              { label: this.$t('document.locale') },
              { label: this.$t('document.author') },
              { label: this.$t('document.translator') },
              { label: this.$t('common.actions'), style: 'width: 100px' },
            ],
            fields: ['index', 'subject', 'locale', 'author', 'translator.name'],
            actions: [
              {
                class: 'btn btn-sm btn-primary me-2',
                icon: 'bi bi-pencil',
                title: this.$t('common.edit'),
                handler: this.editDocument,
              },
              {
                class: 'btn btn-sm btn-danger',
                icon: 'bi bi-x-lg',
                title: this.$t('common.delete'),
                handler: this.deleteDocument,
              },
            ],
          };
        },
        created() {
          this.loadDocuments();
        },
        methods: {
          async loadDocuments(page = 0) {
            try {
              const response = await DocumentService.getAll(page, this.documents.size);
              this.documents = response.data;
            } catch (error) {
              console.error('Erro ao carregar documentos:', error.response?.data || error.message);
              alert(error.response?.data || 'Erro ao carregar documentos.');
            }
          },
          changePage(page) {
            this.loadDocuments(page);
          },
          showAddModal() {
            this.editingDocument = { subject: '', content: '', locale: '', author: '' };
            this.showModal = true;
          },
          editDocument(document) {
            this.editingDocument = { ...document };
            this.showModal = true;
          },
          async saveDocument() {
            try {
              await DocumentService.save(this.editingDocument);
              this.closeModal();
              this.loadDocuments();
            } catch (error) {
              console.error('Erro ao salvar documento:', error.response?.data || error.message);
              alert(error.response?.data || 'Erro ao salvar documento.');
            }
          },
          async deleteDocument(document) {
            try {
              await DocumentService.delete(document.id);
              this.loadDocuments();
            } catch (error) {
              console.error('Erro ao deletar documento:', error.response?.data || error.message);
              alert(error.response?.data || 'Erro ao deletar documento.');
            }
          },
          async handleFileUpload(event) {
            this.isLoading = true; // Ativa o spinner

            try {
              const file = event.target.files[0];
              await DocumentService.upload(file);
              this.loadDocuments();
            } catch (error) {
              console.error('Erro ao fazer upload do arquivo:', error.response?.data || error.message);
              alert((error.response?.data || 'Erro ao fazer upload do arquivo.') +
                  '.Verifique se possui tradutores para todos os idiomas do arquivo.');

              location.reload(true);
            }
            finally {
              this.isLoading = false; // Desativa o spinner
            }
          },
          closeModal() {
            this.showModal = false;
            location.reload(true);

          },
          async fetchTranslators() {
            if (this.searchQuery.length < 2) {
              // Não busca se o texto for muito curto
              this.filteredTranslators = [];
              this.showDropdown = false;
              return;
            }

            try {
              const response = await TranslatorService.search(this.searchQuery); // Método para buscar tradutores
              this.filteredTranslators = response.data;
              this.showDropdown = true;
            } catch (error) {
              console.error('Erro ao buscar tradutores:', error.response?.data || error.message);
              this.filteredTranslators = [];
              this.showDropdown = false;
            }
          },
          selectTranslator(translator) {
            this.searchQuery = translator.name; // Preenche o campo com o nome selecionado
            this.editingDocument.translator = translator; // Define o tradutor no objeto editingDocument
            this.showDropdown = false; // Fecha o dropdown
            this.$emit('translatorSelected', translator); // Emite o tradutor selecionado para o componente pai
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
.autocomplete {
  position: relative;
}
.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  z-index: 1050;
  max-height: 200px;
  overflow-y: auto;
}
</style>