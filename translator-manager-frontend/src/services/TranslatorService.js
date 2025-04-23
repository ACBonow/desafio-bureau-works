// TranslatorService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/translators';

export default {
    getAll(page, size) {
        return axios.get(API_URL, { params: { page, size } });
    },
    save(translator) {
        if (translator.id) {
            return axios.put(`${API_URL}/${translator.id}`, translator);
        }
        return axios.post(API_URL, translator);
    },
    delete(id) {
        return axios.delete(`${API_URL}/${id}`);
    },
    search(query) {
        return axios.get(`${API_URL}/search?search=${query}`);
    },
    upload(file) {
        const formData = new FormData();
        formData.append('file', file);
        return axios.post(`${API_URL}/upload`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
    },
};