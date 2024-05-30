<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    color?: string;
  }>(),
  {
    color: "#ef4b4c",
  }
);
</script>

<template>
  <div class="loader"></div>
</template>

<style scoped lang="less">
.loader {
  width: 40px;
  height: 40px;

  border: 4px solid v-bind(color);
  border-top: 4px solid white;
  border-radius: 50%;

  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.loader {
  -webkit-transition-property: -webkit-transform;
  -webkit-transition-duration: 1.2s;
  -webkit-animation-name: rotate;
  -webkit-animation-iteration-count: infinite;
  -webkit-animation-timing-function: linear;

  -moz-transition-property: -moz-transform;
  -moz-animation-name: rotate;
  -moz-animation-duration: 1.2s;
  -moz-animation-iteration-count: infinite;
  -moz-animation-timing-function: linear;

  transition-property: transform;
  animation-name: rotate;
  animation-duration: 1.2s;
  animation-iteration-count: infinite;
  animation-timing-function: linear;
}

@-webkit-keyframes rotate {
  from {
    -webkit-transform: rotate(0deg);
  }
  to {
    -webkit-transform: rotate(360deg);
  }
}

@-moz-keyframes rotate {
  from {
    -moz-transform: rotate(0deg);
  }
  to {
    -moz-transform: rotate(360deg);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
