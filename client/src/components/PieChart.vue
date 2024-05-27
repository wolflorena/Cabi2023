<script setup lang="ts">
import { TreatmentType } from "@/data/types/Entities";
import { onMounted, ref, watch } from "vue";

const props = withDefaults(
  defineProps<{
    treatments?: TreatmentType[];
    colors?: string[];
  }>(),
  {}
);

const pieChart = ref<HTMLDivElement | null>(null);
const tooltip = ref<HTMLDivElement | null>(null);
const tooltipVisible = ref(false);
const tooltipText = ref("");

function generateConicGradient(
  percentages: number[],
  colors: string[] | undefined
): string {
  if (percentages.length !== colors?.length) {
    throw new Error(
      "The lengths of percentages and colors arrays must be equal."
    );
  }

  let gradientString = "conic-gradient(from 0deg, ";
  let currentAngle = 0;

  for (let i = 0; i < percentages.length; i++) {
    const nextAngle = currentAngle + percentages[i] * 3.6;
    gradientString += `${colors[i]} ${currentAngle}deg ${nextAngle}deg`;
    if (i < percentages.length - 1) {
      gradientString += ", ";
    }
    currentAngle = nextAngle;
  }

  gradientString += ")";

  return gradientString;
}

function createPieChart(data: TreatmentType[] | undefined) {
  if (!pieChart.value) return;

  if (!data || data.length === 0) {
    pieChart.value.style.backgroundImage =
      "conic-gradient(#495570 0deg 360deg)";
    return;
  }

  const total = data.reduce((sum, item) => sum + item.count, 0);
  const percentages = data.map((item) => (item.count / total) * 100);

  const gradient = generateConicGradient(percentages, props.colors);
  pieChart.value.style.backgroundImage = gradient;

  pieChart.value.innerHTML = "";

  let angles = [0];
  let sum = 0;
  data.forEach((e) => {
    sum += (e.count / total) * 360;
    angles.push(sum);
  });
}

onMounted(() => {
  createPieChart(props.treatments);
});

watch(
  () => props.treatments,
  (newData) => {
    createPieChart(newData);
  },
  { deep: true }
);

function showTooltip(event: MouseEvent) {
  const pieRect = pieChart.value?.getBoundingClientRect();
  if (!pieRect || !tooltip.value || !pieChart.value) return;

  const x = event.clientX - pieRect.left;
  const y = event.clientY - pieRect.top;
  const centerX = pieRect.width / 2;
  const centerY = pieRect.height / 2;

  const angle =
    (Math.atan2(y - centerY, x - centerX) * (180 / Math.PI) + 360 + 90) % 360;
  console.log(angle);
  const radius = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

  if (radius <= pieRect.width / 2) {
    const data = props.treatments || [];
    const total = data.reduce((sum, item) => sum + item.count, 0);

    let angles = [0];
    let sum = 0;
    data.forEach((e) => {
      sum += (e.count / total) * 360;
      angles.push(sum);
    });

    for (let i = 0; i < data.length; i++) {
      if (angle >= angles[i] && angle < angles[i + 1]) {
        tooltipText.value = `${data[i].serviceName} ${(
          (data[i].count / total) *
          100
        ).toFixed(1)}%`;
        tooltipVisible.value = true;
        tooltip.value.style.left = `${event.clientX - 300}px`;
        tooltip.value.style.top = `${event.clientY - 200}px`;
        return;
      }
    }
  }
  tooltipVisible.value = false;
}

function hideTooltip() {
  tooltipVisible.value = false;
}
</script>

<template>
  <div class="chart-container">
    <div
      ref="pieChart"
      class="pie-chart"
      @mousemove="showTooltip"
      @mouseout="hideTooltip"
    ></div>
    <div ref="tooltip" class="tooltip" v-show="tooltipVisible">
      {{ tooltipText }}
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.chart-container {
  position: relative;
  width: 300px;
  height: 300px;

  .pie-chart {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    background-size: 100% 100%;
  }
}

.tooltip {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px;
  border-radius: 3px;
  pointer-events: none;
  z-index: 10;
}
</style>
