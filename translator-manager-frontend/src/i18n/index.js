import { createI18n } from 'vue-i18n'
import ptBR from './locales/pt-BR'
import enUS from './locales/en-US'

const messages = {
  'pt-BR': ptBR,
  'en-US': enUS
}

const i18n = createI18n({
  legacy: false,
  locale: 'pt-BR',
  fallbackLocale: 'en-US',
  messages
})

export default i18n 