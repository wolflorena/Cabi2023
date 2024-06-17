<script setup lang="ts">
import { Service } from "@/data/types/Entities";
import Swal from "sweetalert2";
import { computed, ref, onMounted, onUnmounted } from "vue";
import { mountComponent } from "../customer/form-content/MountComponent";
import ServiceInformationModal from "./ServiceInformationModal.vue";

const props = defineProps<{
  services: Service[];
}>();

const searchQuery = ref("");
const showDropdown = ref(false);

const filteredServices = computed(() => {
  if (!searchQuery.value) {
    return [];
  }
  return props.services.filter((service) =>
    service.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const handleSearch = () => {
  if (searchQuery.value) {
    showDropdown.value = true;
  } else {
    showDropdown.value = false;
  }
};

function handleClickOnService(service: Service) {
  const serviceInformationModal = mountComponent(ServiceInformationModal, {
    service: service,
  });
  Swal.fire({
    html: serviceInformationModal,
  });
}

const closeDropdown = (event: Event) => {
  const dropdown = document.querySelector(".search-results");
  const searchInput = document.querySelector(".search-bar");
  if (
    dropdown &&
    !dropdown.contains(event.target as Node) &&
    searchInput &&
    !searchInput.contains(event.target as Node)
  ) {
    showDropdown.value = false;
  }
};

const showDropdownOnFocus = () => {
  if (searchQuery.value) {
    showDropdown.value = true;
  }
};

onMounted(() => {
  document.addEventListener("click", closeDropdown);
});

onUnmounted(() => {
  document.removeEventListener("click", closeDropdown);
});
</script>

<template>
  <div class="searchBar-container">
    <input
      type="text"
      placeholder=" Search..."
      v-model="searchQuery"
      @input="handleSearch"
      @focus="showDropdownOnFocus"
      class="search-bar"
    />
    <div v-if="showDropdown && filteredServices.length" class="search-results">
      <ul>
        <li
          v-for="service in filteredServices"
          :key="service.serviceId"
          @click="handleClickOnService(service)"
        >
          {{ service.name }}
        </li>
      </ul>
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
.searchBar-container {
  display: flex;
  position: relative;
  .search-bar {
    padding: 5px;
    font-size: 16px;
    height: 24px;
    border: 2px solid @upside-background;
    border-radius: 12px;
    outline: none;

    &:focus {
      border-color: @upside-background-hover;
    }
  }

  .search-results {
    position: absolute;
    top: 40px;
    left: 50%;
    transform: translateX(-50%);
    background-color: white;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    width: 300px;
    max-height: 200px;
    overflow-y: auto;
    z-index: 11;
    ul {
      list-style-type: none;
      padding: 0;
      margin: 0;
      li {
        padding: 10px;
        border-bottom: 1px solid #ccc;
        &:last-child {
          border-bottom: none;
        }
        &:hover {
          background-color: #f1f1f1;
          cursor: pointer;
        }
      }
    }
  }
}
</style>
