<script setup lang="ts">
import { ref } from "vue";
import SidebarButton from "./SidebarButton.vue";
import { useRoute } from "vue-router";

const hideSidebar = ref(false);
</script>

<template>
  <button
    @click="hideSidebar = !hideSidebar"
    class="hideSidebar"
    :class="{ hided: hideSidebar }"
  >
    <font-awesome-icon id="icon" icon="bars" />
  </button>
  <div class="sidebar" v-show="!hideSidebar">
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

.sidebar {
  height: 100vh;
  width: 15vw;
  background-color: @blue;
  border-top-right-radius: 100px;

  .logo-container {
    padding-top: 20px;
    display: flex;
    justify-content: center;

    img {
      width: 200px;
    }
  }

  .selections {
    display: flex;
    align-items: flex-end;
    flex-direction: column;
    gap: 15px;
  }
}

.hideSidebar {
  cursor: pointer;
  background-color: transparent;
  border: none;
  position: absolute;
  top: 10px;
  left: 10px;

  #icon {
    color: @black;
    height: 20px;
  }
}
</style>
