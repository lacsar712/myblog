<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  modelValue: string | number
  type?: string
  placeholder?: string
  label?: string
  error?: string
  disabled?: boolean
}>() // Using defineModel if Vue version allows, but safe fallback to props/emit

const emit = defineEmits(['update:modelValue'])

const value = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
</script>

<template>
  <div class="space-y-1">
    <label v-if="label" class="block text-sm font-medium text-slate-700 dark:text-slate-200">
      {{ label }}
    </label>
    <div class="relative">
      <input
        v-model="value"
        :type="type || 'text'"
        :placeholder="placeholder"
        :disabled="disabled"
        class="block w-full rounded-lg border-slate-200 bg-white/50 backdrop-blur-sm px-4 py-2.5 text-slate-900 placeholder:text-slate-400 focus:border-primary focus:ring-2 focus:ring-primary/20 sm:text-sm sm:leading-6 dark:bg-slate-800/50 dark:border-slate-700 dark:text-white dark:placeholder:text-slate-500 transition-all shadow-sm"
        :class="{ 'border-danger focus:border-danger focus:ring-danger/20': error }"
      />
    </div>
    <p v-if="error" class="text-xs text-danger">{{ error }}</p>
  </div>
</template>
