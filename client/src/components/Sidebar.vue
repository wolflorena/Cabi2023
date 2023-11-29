<script setup lang="ts">
import SidebarButton from "./SidebarButton.vue";
import { useRoute } from "vue-router";
</script>

<template>
  <div class="container">
    <div class="logo-container">
      <img src="@/assets/logo.png" alt="" />
    </div>
    <div class="selections">
      <SidebarButton
        :selected="useRoute().path === '/dashboard'"
        text="Dashboard"
        icon="table-columns"
      />
      <SidebarButton
        :selected="useRoute().path === ''"
        text="Appointments"
        icon="calendar-check"
      />
      <SidebarButton
        :selected="useRoute().path === ''"
        text="Patients"
        icon="hospital-user"
      />
      <SidebarButton
        :selected="useRoute().path === ''"
        text="Inventory"
        icon="cart-flatbed"
      />
      <SidebarButton
        :selected="useRoute().path === ''"
        text="Forms"
        icon="highlighter"
      />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  height: 100vh;
  width: 20vw;
  background-color: @blue;
  border-top-right-radius: 100px;
}

.logo-container {
  display: flex;
  justify-content: center;
}

.selections {
  display: flex;
  align-items: flex-end;
  flex-direction: column;
  gap: 15px;
}
</style>
