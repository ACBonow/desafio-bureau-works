// DocumentService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/documents';

export default {
    async getAll(page, size) {
        try {
            return await axios.get(API_URL, { params: { page, size } });
        } catch (error) {
            console.error('Erro ao buscar documentos:', error.response?.data || error.message);
            throw error;
        }
    },
    async save(document) {
        try {
            if (document.id) {
                return await axios.put(`${API_URL}/${document.id}`, document);
            }
            return await axios.post(API_URL, document);
        } catch (error) {
            console.error('Erro ao salvar documento:', error.response?.data || error.message);
            throw error;
        }
    },
    async delete(id) {
        try {
            return await axios.delete(`${API_URL}/${id}`);
        } catch (error) {
            console.error('Erro ao deletar documento:', error.response?.data || error.message);
            throw error;
        }
    },
    async upload(file) {
        try {
            const formData = new FormData();
            formData.append('file', file);
            return await axios.post(`${API_URL}/upload`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' },
            });
        } catch (error) {
            console.error('Erro ao fazer upload do arquivo:', error.response?.data || error.message);
            throw new Error((error.response?.data || error.message) + '. Verifique se possui tradutores para todos os idiomas do arquivo.');        }
    },
};