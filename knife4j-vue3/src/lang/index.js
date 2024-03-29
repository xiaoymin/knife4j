import { createI18n } from 'vue-i18n'
import enLocale from './en';
import zhLocale from './zh';
import jpLocale from './jp';

const messages = {
  'zh-CN': zhLocale,
  'en-US': enLocale,
  'ja-JP': jpLocale,
}

const i18n = createI18n({
  globalInjection: true, //全局生效$t
  locale: 'zh-CN',
  messages,
  legacy: false,
})

export function setupI18n(app) {
  app.use(i18n)
}
