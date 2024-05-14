<script setup lang="ts">
import { FormEvent } from "@/data/types/Entities";
import { ref } from "vue";
import FormItem from "@/components/FormItem.vue";
import { formatDate } from "@/utils/helpers";

const props = withDefaults(
  defineProps<{
    label?: string;
    total?: number;
    patientsList?: FormEvent[];
  }>(),
  {}
);

const showPatientsList = ref(false);
</script>

<template>
  <div class="form-details">
    <div class="detail-shortcut" :class="{ open: showPatientsList && total }">
      <div class="detail">
        <div class="label">{{ label }}</div>
        <div class="total">{{ total + " patients" }}</div>
      </div>
      <button @click="showPatientsList = !showPatientsList">
        <font-awesome-icon icon="circle-plus" id="icon" />
      </button>
    </div>

    <Transition name="fade">
      <div class="patients-list" v-if="showPatientsList && total">
        <ul>
          <FormItem
            v-for="patient in patientsList"
            :key="patient.customerFirstName"
            :name="patient.customerFirstName + ' ' + patient.customerLastName"
            :date="formatDate(patient.timestamp)"
          />
        </ul>
      </div>
    </Transition>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.fade-enter-to,
.fade-leave-from {
  opacity: 1;
}

.form-details {
  width: 90%;
}
.detail-shortcut {
  display: flex;
  align-items: center;
  justify-content: space-between;

  background-color: @sugar;
  padding: 20px 50px;
  border-radius: 15px;

  &.open {
    background-color: transparent;
  }

  .detail {
    display: flex;
    gap: 10px;
    .label,
    .total {
      color: @smoke;
      font-size: 20px;
      font-weight: bold;
    }
  }

  button {
    background-color: transparent;
    border: none;

    #icon {
      height: 30px;
      color: @smoke;
    }
  }
}
.patients-list {
  background-color: @sugar;

  padding: 20px 50px;
  max-height: 20vh;

  ul {
    list-style-type: none;
    padding-left: 0px;
  }
}
</style>
