<template>
  <div class="chart-container">
    <div class="y-axis">
      <div
        class="y-label"
        v-for="(n, index) in yAxisLabels"
        :key="n"
        :style="{ top: `${60 * index - 7}px` }"
      >
        {{ n }}
      </div>
    </div>
    <div class="chart-area">
      <div
        class="horizontal-line"
        v-for="(n, index) in yAxisLabels"
        :key="n"
        :style="{ top: `${20 * n}px` }"
      ></div>
      <div class="chart">
        <div
          class="bar"
          v-for="item in chartData"
          :key="item.week"
          @mousemove="showTooltip($event, item.appointments)"
          @mouseout="hideTooltip"
        >
          <div
            class="bar-inner"
            :style="{ height: item.appointments * scale + 'px' }"
          ></div>
          <span class="label">{{ item.week }}</span>
        </div>
      </div>
    </div>
    <div class="tooltip" v-show="tooltipVisible" :style="tooltipStyle">
      {{ tooltipText }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const data = ref([
  { week: "Week 1", appointments: 5 },
  { week: "Week 2", appointments: 8 },
  { week: "Week 3", appointments: 12 },
  { week: "Week 4", appointments: 9 },
]);

const chartData = ref(data.value);
const scale = ref(20);
const tooltipVisible = ref(false);
const tooltipText = ref("");
const tooltipStyle = ref({
  left: "0px",
  top: "0px",
});

const maxAppointments = Math.max(
  ...data.value.map((item) => item.appointments)
);
const yAxisLabels = ref(
  Array.from(
    { length: 5 },
    (_, i) => i * Math.ceil(maxAppointments / 4)
  ).reverse()
);

function showTooltip(event: MouseEvent, number: number) {
  tooltipText.value = number + " appointments";
  tooltipStyle.value = {
    left: `${event.clientX + 30}px`,
    top: `${event.clientY + 10}px`,
  };
  tooltipVisible.value = true;
}

function hideTooltip() {
  tooltipVisible.value = false;
}
</script>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.chart-container {
  height: calc(50% - 60px);
  text-align: center;
  border: 1px solid @gray;
  border-radius: 20px;
  padding: 30px;
  display: flex;
  position: relative;

  .y-axis {
    position: relative;
    margin-right: 20px;

    .y-label {
      font-weight: bold;
      position: absolute;
      right: 0;
    }
  }

  .chart-area {
    width: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
    height: 270px;

    .horizontal-line {
      position: absolute;
      width: 100%;
      border-top: 1px dashed @gray;
      z-index: 0;
    }

    .chart {
      display: flex;
      justify-content: space-around;
      align-items: flex-end;
      height: 100%;

      .bar {
        width: 15%;
        display: flex;
        flex-direction: column;
        align-items: center;

        .bar-inner {
          width: 100%;
          background-color: @blue;
          border-radius: 5px;
          z-index: 1;
        }

        .label {
          margin-top: 10px;
          font-weight: bold;
        }
      }
    }
  }

  .tooltip {
    position: fixed;
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 5px;
    border-radius: 3px;
    pointer-events: none;
    z-index: 10;
  }
}
</style>
