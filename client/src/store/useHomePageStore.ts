import { Service } from "@/data/types/Entities";
import { getAllServices } from "@/services/service_service";
import { ref } from "vue";

const services = ref<Service[]>([]);

function loadServicesFromCache() {
  const cachedServices = localStorage.getItem("services");
  if (cachedServices) {
    services.value = JSON.parse(cachedServices);
  }
}

async function fetchAllServices() {
  loadServicesFromCache();
  if (services.value.length === 0) {
    const response: Service[] = await getAllServices();
    if (response.length > 0) {
      services.value = response;
      localStorage.setItem("services", JSON.stringify(response));
    }
  }
}
export function useHomePageStore() {
  return {
    services,
    fetchAllServices,
  };
}
