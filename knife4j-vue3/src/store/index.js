import { createPinia } from 'pinia'

export function setupStore(app) {
  app.use(createPinia())
}
