<script setup lang="ts">
import { onMounted, ref } from "vue";
import NavBar from "../home-page/NavBar.vue";
import { useHomePageStore } from "@/store/useHomePageStore";
import homePagePlaceholderImage from "@/assets/clinic-overview.avif";
import { useRouter } from "vue-router";
import { ShowoffDoctor, doctorsToShow } from "../home-page/doctorsShowoff";

const { services, fetchAllServices } = useHomePageStore();
const router = useRouter();

function handleIconClick() {
  router.push("/profile");
}

const doctorShowoff = ref<ShowoffDoctor>(doctorsToShow[0]);
const currentIndex = ref(0);
function handleDoctorPictureClick() {
  currentIndex.value = (currentIndex.value + 1) % doctorsToShow.length;
  doctorShowoff.value = doctorsToShow[currentIndex.value];
}

onMounted(async () => {
  await fetchAllServices();
});
</script>

<template>
  <div class="home-page-container">
    <div class="content-container">
      <NavBar :services="services" />
      <div class="content-container__first">
        <div class="buffer-space"></div>
        <div class="title">
          Your Journey to a Perfect Smile <br/> 
          Starts Here
          <font-awesome-icon
            icon="circle-chevron-right"
            beat
            :style="{ fontSize: '54px', cursor: 'pointer' }"
            @click="handleIconClick"
          />
          </div>
        <div class="carrousel-image">
          <img :src="homePagePlaceholderImage" class="placeholder-image" />
        </div>
      </div>
      <div class="content-container__second">
        <div class="buffer-space">
          <div class="header">Get to know our employees</div>
        </div>
        <div class="buffer-space additional" />
        <div class="description" v-if="doctorShowoff">
          <div
            class="carrousel-doctor-images"
            :style="{ cursor: 'pointer' }"
            @click="handleDoctorPictureClick"
          >
            <img :src="doctorShowoff.image" class="placeholder-image" />
          </div>
          <p class="description-for-doctor">
            <span class="name"> {{ doctorShowoff.name }} </span>
            {{ doctorShowoff.description }}

            <div class="click-image">
              <font-awesome-icon icon="left-long" /> Click on the image
            </div>
          </p>
          <div class="statistics-block">
            <font-awesome-icon
              icon="star"
              :style="{ color: 'yellow', fontSize: '38px' }"
            />
            <span :style="{ fontSize: '22px' }"> Rating </span>
            <span :style="{ fontSize: '22px' }">
              {{ doctorShowoff.rating }}</span
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
@import (reference) "@/assets/styles.less";
@import url("https://fonts.googleapis.com/css2?family=Lato:wght@300;400;700&display=swap");
@import url('https://fonts.googleapis.com/css2?family=Barlow:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap');
.home-page-container {

  font-family: "Barlow", sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100vw;
  height: 100vh;
  background-color: @under-background;

  .content-container {
    margin: 10px 20px;
    height: 100%;
    &__first {
      background-color: @under-background;
      border-radius: 0px 0px 22px 22px;

      position: relative;
      display: flex;
      flex-direction: column;
      width: 100%;
      height: 40vh;

      align-self: flex-start;

      .buffer-space {
        position: absolute;
        top: 0;
        width: 96vw;
        height: 20px;
        background-color: @upside-background;
      }

      .title {
        background-color: @upside-background;
        font-size: 64px;
        font-weight: bold;
        color: @black;

        width: 56.8vw;
        border-radius: 0px 0px 22px 22px;
        height: 100%;
        font-family: "Lato", sans-serif;
        padding-top: 10vh;
        padding-left: 6vw;
      }

      .carrousel-image {
        position: absolute;
        top: 0;
        right: 0;
        width: 35vw;
        height: 50vh;
        background-color: @upside-background;
        border: 1vh solid @under-background;
        border-right: 0;
        border-radius: 22px;
        z-index: 10;

        overflow: hidden;
      }
    }

    &__second {
      background-color: @under-background;
      border-radius: 22px;

      position: relative;
      display: flex;
      width: 98vw;

      margin-top: 1vh;
      height: 48vh;
      align-self: flex-start;

      .buffer-space {
        position: absolute;
        top: 0;
        width: 62.8vw;

        border-radius: 22px 22px 0px 0px;
        height: 60px;
        background-color: @heavy-smoke;

        &.additional {
          background-color: @heavy-smoke;
          top: 50px;
          height: 50px;
          border-radius: 0px 0px 22px 0px;
          width: 100%;
        }

        .header {
          font-size: 28px;
          color: white;
          padding-top: 10px;
          margin-left: 20px;
        }
      }

      .description {
        display: flex;
        margin-top: 100px;
        background-color: @heavy-smoke;
        color: white;
        width: 100%;
        border-radius: 0px 22px 22px 22px;

        .carrousel-doctor-images {
          position: absolute;
          top: 50px;
          width: 280px;
          height: 300px;
          margin-left: 20px;
          border-radius: 22px;
          border: 1vh solid @under-background;
          overflow: hidden;
        }

        .description-for-doctor {
          .name {
            font-size: 36px;
            margin-bottom: 8px;
          }

          .click-image {
            font-size: 18px;
            color: @red;
            position: absolute;
            bottom: 100px;
          }
          width: 900px;
          margin-left: 340px;
          display: flex;
          flex-direction: column;
        }

        .statistics-block {
          position: absolute;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          gap: 5px;
          top: 40%;
          width: 100px;
          height: 120px;
          right: 28vw;
          border-radius: 22px;
          background-color: @heavy-smoke;
        }
      }
    }
  }
}

.placeholder-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}
</style>
