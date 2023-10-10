<script lang="ts">
import { type Ref, ref } from 'vue'
import type Checklist from '../models/checklist'

export default {
  async setup() {
    const checklists: Ref<Checklist[]> = ref([])
    const formName: Ref<string> = ref('')
    await fetchChecklists()

    async function fetchChecklists(): Promise<void> {
      const response = await fetch('http://localhost:8080/api/checklists')
      checklists.value = (await response.json()) as Checklist[]
    }

    async function addChecklist(): Promise<void> {
      const response = await fetch('http://localhost:8080/api/checklists', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: formName.value,
          items: [],
        })
      })
      await fetchChecklists()
      formName.value = ''
    }

    return {
      checklists,
      formName,
      fetchChecklists,
      addChecklist
    }
  }
}
</script>

<template>
  <div>
    <h1>Checklists</h1>
    <ul>
      <li v-for="checklist in checklists" :key="checklist.id">
        {{ checklist.name }}
      </li>
    </ul>
    <button @click="fetchChecklists">Refresh</button>
    <form @submit.prevent="addChecklist">
      <label for="name">Name</label>
      <input type="text" id="name" v-model="formName" />
      <button type="submit">Add</button>
    </form>
  </div>
</template>