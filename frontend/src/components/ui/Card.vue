<script setup lang="ts">
defineProps<{
  noPadding?: boolean
}>()
</script>

<template>
  <div class="bg-white/80 dark:bg-slate-900/80 backdrop-blur-xl border border-white/20 shadow-xl rounded-2xl overflow-hidden ring-1 ring-slate-900/5 dark:ring-white/10">
    <div :class="noPadding ? '' : 'p-6'">
      <slot />
    </div>
  </div>
</template>
