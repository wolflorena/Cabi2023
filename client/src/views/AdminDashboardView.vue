<script setup lang="ts">
import { ref, computed } from "vue";
import Sidebar from "@/components/Sidebar.vue";
import MonthScheduler from "@/components/MonthScheduler.vue";

const doctors = ref(["Doctor 1", "Doctor 2", "Doctor 3", "Ana"]);
const displayDropdown = ref(false);
const searchQuery = ref("");
const selectedDoctors = ref(["Abccc"]);
const showCalendars = ref(true);

function selectDoctors(doctor: string) {
  selectedDoctors.value.push(doctor);
  displayDropdown.value = false;
}

const filteredDoctors = computed(() => {
  return doctors.value.filter((doctor) =>
    doctor.toUpperCase().includes(searchQuery.value.toUpperCase())
  );
});

function toggleDropdown() {
  displayDropdown.value = !displayDropdown.value;
}

function toggleCalendars() {
  showCalendars.value = !showCalendars.value;
}

function updateSearchQuery(event: Event) {
  const target = event.target as HTMLInputElement;
  searchQuery.value = target.value;
}
</script>

<template>
  <div class="container">
    <Sidebar />
    <div class="settings">
      <button class="add-button">
        <font-awesome-icon id="icon" icon="circle-plus" />
        <span>Add appointment</span>
      </button>

      <div class="add-calendar">
        <label
          >Add calendar...
          <input
            type="text"
            placeholder="Search for a doctor"
            @click="toggleDropdown"
            @keyup="updateSearchQuery"
        /></label>

        <div class="dropdown" v-show="displayDropdown">
          <span
            v-for="doctor in filteredDoctors"
            class="element"
            @click="selectDoctors(doctor)"
          >
            {{ doctor }}</span
          >
        </div>
      </div>
      <div class="calendars">
        <div class="title">
          <h4>Available calendars</h4>
          <button @click="toggleCalendars" v-show="showCalendars">
            <font-awesome-icon icon="chevron-up" id="icon" />
          </button>
          <button @click="toggleCalendars" v-show="!showCalendars">
            <font-awesome-icon icon="chevron-down" id="icon" />
          </button>
        </div>
        <div class="doctors">
          <span v-for="doctor in selectedDoctors" v-show="showCalendars">{{
            doctor
          }}</span>
        </div>
      </div>
    </div>
    <MonthScheduler />
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.container {
  display: flex;
  align-items: center;

  .settings {
    height: 100vh;
    width: 20vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 100px;
    padding: 0 15px;
    .add-button {
      background-color: @green;
      border: none;
      border-radius: 20px;
      width: 19vw;
      height: 40px;
      cursor: pointer;

      display: flex;
      align-items: center;
      gap: 20px;

      #icon {
        height: 25px;
        color: @gray;
      }

      span {
        font-size: 15px;
        color: @gray;
      }
    }
    .add-calendar {
      position: relative;
      label {
        color: @font-dark-gray;
        font-weight: bolder;
        display: flex;
        flex-direction: column;
        gap: 5px;
        width: 19vw;

        input {
          height: 3vh;
          border: none;
          background-color: @light-gray;
          box-sizing: border-box;

          padding: 3px;

          &:focus {
            border-color: transparent;
            outline: none;
          }
        }
      }

      .dropdown {
        background-color: @light-gray;
        width: 19vw;
        position: absolute;
        top: 100%;
        max-height: 100px;
        overflow: scroll;
        scrollbar-color: @gray;

        display: flex;
        flex-direction: column;
        gap: 5px;

        .element {
          border-bottom: 0.1px solid @gray;
          cursor: pointer;

          &:hover {
            background-color: @red;
          }
        }
      }
    }

    .calendars {
      color: @font-dark-gray;
      font-weight: bolder;
      width: 19vw;

      .title {
        display: flex;
        align-items: center;
        justify-content: space-between;

        button {
          background-color: transparent;
          border: none;

          #icon {
            color: @font-dark-gray;
          }
        }
      }

      .doctors {
        height: 20vh;
        display: flex;
        flex-direction: column;

        max-height: 20vh;
        overflow: scroll;
        scrollbar-color: @gray;
        span {
          color: @black;
          font-weight: 300;
        }
      }
    }
  }
}
</style>
