<script setup lang="ts">
import Sidebar from "@/components/Sidebar.vue";
import TableHeaderButton from "@/components/TableHeaderButton.vue";
import { Doctor } from "@/data/types/Entities";
import { DoctorSidebarOptions } from "@/data/types/SidebarOptions";
import { getDoctorById } from "@/services/doctor_service";
import { onMounted, ref } from "vue";
import InfoField from "@/components/InfoField.vue";
import CustomButton from "@/components/CustomButton.vue";

const viewOnly = ref(true);
const doctor = ref<Doctor>();

async function getDoctorDetails() {
  await getDoctorById(1).then((res) => {
    doctor.value = res;
  });
}

onMounted(() => {
  getDoctorDetails();
});
</script>

<template>
  <div class="container">
    <Sidebar :options="DoctorSidebarOptions" />

    <div class="header">
      <TableHeaderButton
        label="View Profile"
        :active="viewOnly"
        @click="viewOnly = true"
      />
      <TableHeaderButton
        label="Edit Profile"
        :active="!viewOnly"
        @click="viewOnly = false"
      />
    </div>

    <div class="details">
      <img src="../../assets/default-avatar.png" alt="" />
      <div class="doctor-info">
        <InfoField
          uuid="first-name"
          label="First Name"
          :input-value="doctor?.firstName"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="last-name"
          label="Last Name"
          :input-value="doctor?.lastName"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="email"
          label="Email"
          :input-value="doctor?.email"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="phone"
          label="Phone Number"
          :input-value="doctor?.phoneNo"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="address"
          label="Address"
          :input-value="doctor?.address"
          :is-readonly="viewOnly"
        />
        <InfoField
          uuid="doe"
          label="Date of Employment"
          :input-value="doctor?.dateOfEmployment"
          :is-readonly="viewOnly"
        />
      </div>

      <div v-if="!viewOnly" class="button">
        <CustomButton
          text="Save"
          :is-main="false"
          height="50"
          width="100"
          font-size="20"
        />
      </div>
    </div>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  position: relative;

  .header {
    position: absolute;
    top: 0;
    right: 0;
    background-color: @gray;
    height: 7vh;
    width: 50vw;
    border-bottom-left-radius: 40px;

    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30px;

    span {
      color: @white;
      font-size: large;
    }
  }

  .details {
    width: 85vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 12vh;

    img {
      height: 200px;
    }

    .doctor-info {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      width: 90%;
      column-gap: 2vw;
      row-gap: 5vh;
    }

    .button {
      position: absolute;
      bottom: 50px;
    }
  }
}
</style>
