<script setup lang="ts">
import { Service } from "@/data/types/Entities";
import { computed, ref } from "vue";
import SearchBarComponent from "./SearchBarComponent.vue";
import { useRouter } from "vue-router";

const props = defineProps<{
  services: Service[];
}>();

const selectedOption = ref<string>("HOME");
const router = useRouter();

function handleOptionClick(option: string) {
  selectedOption.value = option;
}

function handleAccountClick() {
  router.push("/profile");
}

const isSelected = (option: string) =>
  computed(() => {
    return selectedOption.value === option;
  });
</script>

<template>
  <div class="nav-bar-container">
    <div class="nav-bar-container__logo">
      <img src="@/assets/logo.png" alt="" id="logo-image" />
    </div>
    <div class="nav-bar-container__options">
      <span
        class="option"
        :class="{
          selected: isSelected('HOME').value,
        }"
        @click="handleOptionClick('HOME')"
        >Home</span
      >
      <span
        class="option"
        :class="{
          selected: isSelected('PRODUCTS').value,
        }"
        @click="handleOptionClick('PRODUCTS')"
        >Products</span
      >
      <span
        class="option"
        :class="{
          selected: isSelected('CONTACT').value,
        }"
        @click="handleOptionClick('CONTACT')"
        >Contact</span
      >
      <span
        class="option"
        :class="{
          selected: isSelected('REVIEWS').value,
        }"
        @click="handleOptionClick('REVIEWS')"
        >Reviews</span
      >
    </div>
    <div class="nav-bar-container__quick-menu">
      <SearchBarComponent :services="services" />
      <span>
        <font-awesome-icon icon="circle-question" id="icon" />
      </span>
      <span>
        <font-awesome-icon
          icon="circle-user"
          id="icon"
          @click="handleAccountClick"
        />
      </span>
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";

.nav-bar-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 98vw;
  height: 7vh;
  border-radius: 22px 22px 22px 0px;
  margin-top: 10px;
  background-color: @upside-background;
  border: 2px solid @upside-background;

  &__logo {
    display: flex;
    align-items: center;
    margin-left: 10px;

    #logo-image {
      width: auto;
      height: 9vh;
      cursor: pointer;
    }
  }

  &__options {
    display: flex;
    align-items: center;
    gap: 20px;
    height: 100%;

    .option {
      display: flex;
      align-items: center;
      font-size: 18px;
      font-weight: bold;
      color: @white;
      border: 2px solid transparent;
      padding: 0 10px;
      border-radius: 10px;
      color: @light-gray;
      height: 100%;

      &:hover {
        cursor: pointer;
        color: @gray;
        transition: color 0.3s, color 0.3s;
      }

      &.selected {
        color: @gray;
      }
    }
  }

  &__quick-menu {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-right: 10px;

    #icon {
      font-size: 32px;
      cursor: pointer;
      color: @white;
      transition: color 0.3s;

      &:hover {
        color: @heavy-smoke;
      }
    }
  }
}
</style>
