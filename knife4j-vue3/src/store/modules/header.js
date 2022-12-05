import { defineStore } from 'pinia'

export const useHeadersStore = defineStore('Headers',{
  state() {
    return {
      userCurrent: {}
    }
  },
  actions: {
    getCurrentUser() {
      this.userCurrent = {
        name: '八一菜刀',
        avatar: ''
      }
    }
  }
})
