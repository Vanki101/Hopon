<template>
  <button
    :class="buttonClasses"
    :disabled="disabled || loading"
    :type="type"
    :aria-label="ariaLabel"
    @click="handleClick"
  >
    <!-- Loading spinner -->
    <span v-if="loading" class="button-spinner"></span>
    
    <!-- Icon slot (before text) -->
    <span v-if="$slots.icon && !loading" class="button-icon">
      <slot name="icon"></slot>
    </span>
    
    <!-- Button text -->
    <span v-if="$slots.default" class="button-text">
      <slot></slot>
    </span>
    
    <!-- Icon slot (after text) -->
    <span v-if="$slots.iconAfter && !loading" class="button-icon-after">
      <slot name="iconAfter"></slot>
    </span>
  </button>
</template>

<script setup>
import { computed } from 'vue'

// Props
const props = defineProps({
  // Variant: primary, secondary, danger, success, outline
  variant: {
    type: String,
    default: 'primary',
    validator: (value) => ['primary', 'secondary', 'danger', 'success', 'outline', 'ghost'].includes(value)
  },
  
  // Size: small, medium, large
  size: {
    type: String,
    default: 'medium',
    validator: (value) => ['small', 'medium', 'large'].includes(value)
  },
  
  // Type: button, submit, reset
  type: {
    type: String,
    default: 'button',
    validator: (value) => ['button', 'submit', 'reset'].includes(value)
  },
  
  // States
  disabled: {
    type: Boolean,
    default: false
  },
  
  loading: {
    type: Boolean,
    default: false
  },
  
  // Full width
  fullWidth: {
    type: Boolean,
    default: false
  },
  
  // Icon only (no text)
  iconOnly: {
    type: Boolean,
    default: false
  },
  
  // Accessibility
  ariaLabel: {
    type: String,
    default: null
  }
})

// Emits
const emit = defineEmits(['click'])

// Computed
const buttonClasses = computed(() => {
  return [
    'hop-button',
    `hop-button--${props.variant}`,
    `hop-button--${props.size}`,
    {
      'hop-button--full-width': props.fullWidth,
      'hop-button--icon-only': props.iconOnly,
      'hop-button--loading': props.loading,
      'hop-button--disabled': props.disabled
    }
  ]
})

// Methods
const handleClick = (event) => {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style scoped>
/* Base button styles */
.hop-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-lg);
  border: none;
  border-radius: var(--border-radius);
  font-size: var(--font-size-base);
  font-weight: 500;
  font-family: var(--font-family);
  cursor: pointer;
  transition: all 0.2s ease;
  text-decoration: none;
  white-space: nowrap;
  position: relative;
}

.hop-button:focus {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}

.hop-button:active {
  transform: translateY(1px);
}

/* Variants */
.hop-button--primary {
  background-color: var(--color-primary);
  color: var(--color-white);
}

.hop-button--primary:hover:not(:disabled) {
  background-color: #1d4ed8;
}

.hop-button--secondary {
  background-color: var(--color-secondary);
  color: var(--color-white);
}

.hop-button--secondary:hover:not(:disabled) {
  background-color: #475569;
}

.hop-button--danger {
  background-color: var(--color-danger);
  color: var(--color-white);
}

.hop-button--danger:hover:not(:disabled) {
  background-color: #dc2626;
}

.hop-button--success {
  background-color: var(--color-success);
  color: var(--color-white);
}

.hop-button--success:hover:not(:disabled) {
  background-color: #059669;
}

.hop-button--outline {
  background-color: transparent;
  color: var(--color-primary);
  border: 2px solid var(--color-primary);
}

.hop-button--outline:hover:not(:disabled) {
  background-color: var(--color-primary);
  color: var(--color-white);
}

.hop-button--ghost {
  background-color: transparent;
  color: var(--color-primary);
}

.hop-button--ghost:hover:not(:disabled) {
  background-color: var(--color-gray-100);
}

/* Sizes */
.hop-button--small {
  padding: var(--spacing-xs) var(--spacing-md);
  font-size: var(--font-size-sm);
  gap: var(--spacing-xs);
}

.hop-button--medium {
  padding: var(--spacing-sm) var(--spacing-lg);
  font-size: var(--font-size-base);
}

.hop-button--large {
  padding: var(--spacing-md) var(--spacing-xl);
  font-size: var(--font-size-lg);
}

/* Icon only */
.hop-button--icon-only {
  padding: var(--spacing-sm);
  aspect-ratio: 1;
}

.hop-button--icon-only.hop-button--small {
  padding: var(--spacing-xs);
}

.hop-button--icon-only.hop-button--large {
  padding: var(--spacing-md);
}

/* Full width */
.hop-button--full-width {
  width: 100%;
}

/* Loading state */
.hop-button--loading {
  cursor: wait;
  pointer-events: none;
}

.button-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid currentColor;
  border-top-color: transparent;
  border-radius: 50%;
  animation: button-spin 0.6s linear infinite;
}

@keyframes button-spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Disabled state */
.hop-button:disabled,
.hop-button--disabled {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}

/* Icon styles */
.button-icon,
.button-icon-after {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.button-icon svg,
.button-icon-after svg {
  width: 20px;
  height: 20px;
}

.hop-button--small .button-icon svg,
.hop-button--small .button-icon-after svg {
  width: 16px;
  height: 16px;
}

.hop-button--large .button-icon svg,
.hop-button--large .button-icon-after svg {
  width: 24px;
  height: 24px;
}

/* Text styles */
.button-text {
  line-height: 1;
}

/* Responsive */
@media (max-width: 768px) {
  .hop-button {
    padding: var(--spacing-md) var(--spacing-lg);
  }
  
  .hop-button--small {
    padding: var(--spacing-sm) var(--spacing-md);
  }
}
</style>