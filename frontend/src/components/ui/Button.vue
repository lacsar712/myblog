<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger'
  size?: 'sm' | 'md' | 'lg'
  disabled?: boolean
  loading?: boolean
  block?: boolean
}>()

const emit = defineEmits(['click'])

const classes = computed(() => {
  const base = 'inline-flex items-center justify-center rounded-lg font-medium transition-all focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed'
  
  const variants = {
    primary: 'bg-primary text-white hover:bg-blue-700 focus:ring-primary',
    secondary: 'bg-white text-slate-700 border border-slate-300 hover:bg-slate-50 focus:ring-slate-200',
    ghost: 'text-slate-600 hover:bg-slate-100 hover:text-slate-900 focus:ring-slate-200',
    danger: 'bg-danger text-white hover:bg-red-700 focus:ring-danger',
  }

  const sizes = {
    sm: 'px-3 py-1.5 text-sm',
    md: 'px-4 py-2 text-sm',
    lg: 'px-6 py-3 text-base',
  }

  return [
    base,
    variants[props.variant || 'primary'],
    sizes[props.size || 'md'],
    props.block ? 'w-full' : '',
  ].join(' ')
})
</script>

<template>
  <button :class="classes" :disabled="disabled || loading" @click="emit('click', $event)">
    <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>
    <slot />
  </button>
</template>
