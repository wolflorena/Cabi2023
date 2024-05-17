<script setup lang="ts">
import { computed } from "vue";

const props = withDefaults(
  defineProps<{
    totalPages: number;
    currentPage: number;
  }>(),
  {}
);

const emit = defineEmits(["changePage"]);

const goToPage = (page: number) => {
  if (page < 1 || page > props.totalPages || page === props.currentPage) return;
  emit("changePage", page);
};

const pagesToShow = computed(() => {
  let pages = [];
  let start = props.currentPage;
  let end = props.totalPages;

  if (start !== end && start !== end - 1 && start > 0) {
    pages.push(start);
  }
  if (props.totalPages > 1 && start < end - 1 && start + 1 > 0) {
    pages.push(start + 1);
  }

  if (props.totalPages > 5 && end - start >= 4) {
    pages.push("...");
  }

  if (start + 1 !== end - 1 && start !== end && end > 1) {
    pages.push(end - 1);
  }
  if (end) {
    pages.push(end);
  }

  return pages;
});
</script>

<template>
  <div class="pagination-container" v-if="totalPages">
    <ul class="page">
      <li class="page__btn" @click="goToPage(currentPage - 1)">
        <font-awesome-icon id="icon" icon="chevron-left" />
      </li>

      <li
        v-for="page in pagesToShow"
        :key="page"
        :class="['page__numbers', { active: page === currentPage }]"
        @click="goToPage(+page)"
      >
        <span :class="{ page__dots: page === '...' }">{{ page }}</span>
      </li>

      <li class="page__btn" @click="goToPage(currentPage + 1)">
        <font-awesome-icon id="icon" icon="chevron-right" />
      </li>
    </ul>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

ul {
  list-style-type: none;
}

.pagination-container {
  position: absolute;
  bottom: 0;
  right: 0;
  margin-right: 3rem;

  .page {
    display: flex;
    background-color: transparent;
    border-radius: 0.6rem;

    &__numbers,
    &__btn,
    &__dots {
      display: flex;
      justify-content: center;
      align-items: center;
      margin: 0.3rem;
      font-size: 1rem;
      cursor: pointer;
    }

    &__dots {
      width: 1rem;
      height: 1rem;
      color: @blue;
      cursor: initial;
    }

    &__numbers {
      width: 1.6rem;
      height: 1.6rem;
      border-radius: 0.4rem;

      &:hover {
        color: @gray;
      }

      &.active {
        color: #ffffff;
        background: @gray;
        font-weight: 600;
      }
    }

    &__btn {
      color: @black;

      #icon {
        height: 11px;
      }
    }
  }
}
</style>
