<script setup lang="ts">
import { TreatmentType } from "@/data/types/Entities";
import PieChart from "@/components/PieChart.vue";
import DashboardRowPieChart from "@/components/DashboardRowPieChart.vue";
import { computed } from "vue";

const props = withDefaults(
  defineProps<{
    treatments?: TreatmentType[];
  }>(),
  {}
);

function generateColor(index: number): string {
  const hue = 220 + ((index * 10) % 60); // hue interval for the color purple
  const saturation = 20;
  const lightness = 20 + index * 5;

  return `hsl(${hue}, ${saturation}%, ${lightness}%)`;
}

const colors = computed(
  () => props.treatments?.map((_, index) => generateColor(index)) || []
);
</script>

<template>
  <div class="container">
    <h1>Treatment Types</h1>
    <PieChart :treatments="treatments" :colors="colors" />

    <div class="legend">
      <DashboardRowPieChart
        v-for="(service, index) in treatments"
        :service-name="service.serviceName"
        :color="colors[index]"
      />
      <DashboardRowPieChart
        v-if="!treatments || treatments.length === 0"
        :service-name="'NO TREATMENTS'"
        :color="'#495570'"
      />
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  height: 33vw;
  border: 1px solid @gray;
  border-radius: 20px;
  padding: 30px;
  width: 300px;

  display: flex;
  flex-direction: column;
  gap: 30px;

  h1 {
    text-align: center;
    color: @gray;
  }

  .legend {
    max-height: 120px;
    overflow-y: scroll;
  }
}
</style>
