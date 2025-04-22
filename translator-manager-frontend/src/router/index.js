import { createRouter, createWebHistory } from 'vue-router'
import TranslatorView from '../views/TranslatorView.vue'
import DocumentView from '../views/DocumentView.vue'

const routes = [
  {
    path: '/',
    redirect: '/translators'
  },
  {
    path: '/translators',
    name: 'translators',
    component: TranslatorView
  },
  {
    path: '/documents',
    name: 'documents',
    component: DocumentView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router