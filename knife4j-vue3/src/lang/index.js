import { createI18n } from 'vue-i18n'
import enLocale from './en';
import zhLocale from './zh';

const messages = {
  'zh-CN': zhLocale,
  'en-US': enLocale,
}

const i18n = createI18n({
  globalInjection: true, //ćšć±çæ$t
  locale: 'zh-CN',
  messages,
  legacy: false,
})

export function setupI18n(app) {
  app.use(i18n)
}
