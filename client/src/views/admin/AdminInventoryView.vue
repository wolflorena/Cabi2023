<script setup lang="ts">
import { ref, onMounted } from "vue";

import Sidebar from "@/components/Sidebar.vue";
import Pagination from "@/components/Pagination.vue";
import CustomModal from "@/components/CustomModal.vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import ActionButton from "@/components/ActionButton.vue";
import TableHeader from "@/components/TableHeader.vue";
import TableRow from "@/components/TableRow.vue";
import { Product } from "@/data/types/Entities";

import { AdminSidebarOptions } from "@/data/types/SidebarOptions";
import {
  getAllPageable,
  addProduct,
  getProductById,
  deleteProduct,
  updateProduct,
} from "@/services/inventory_service";

const showDelete = ref(false);
const showAddModal = ref(false);
const showEdit = ref(false);
const isLoading = ref(false);

const productName = ref("");
const quantity = ref(0);

const currentPage = ref(1);
const totalPages = ref(0);

const inventory = ref<Product[]>([]);
const selectedProduct = ref<Product>();

async function loadProducts() {
  isLoading.value = true;
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    inventory.value = res.pagedInventory.content;
    totalPages.value = Math.ceil(res.total / 10);
    isLoading.value = false;
  });
}

async function changePage(pageNumber: number) {
  isLoading.value = true;
  currentPage.value = pageNumber;
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    inventory.value = res.pagedInventory.content;
    totalPages.value = Math.ceil(res.total / 10);
    isLoading.value = false;
  });
}

onMounted(() => {
  loadProducts();
});

async function showDeleteModal(productId: number) {
  showDelete.value = true;
  await getProductById(productId).then(
    (res: Product) => (selectedProduct.value = res)
  );
}

async function createProduct() {
  await addProduct(productName.value, quantity.value);
  loadProducts();
  showAddModal.value = false;
  productName.value = "";
  quantity.value = 0;
}

async function showEditModal(productId: number) {
  showEdit.value = true;
  await getProductById(productId).then((res: Product) => {
    selectedProduct.value = res;
    productName.value = res.product;
    quantity.value = res.quantity;
  });
}

async function editProduct() {
  if (selectedProduct.value) {
    await updateProduct(
      selectedProduct.value.inventoryId,
      productName.value,
      quantity.value
    );
    loadProducts();
    showEdit.value = false;
  }
}

async function deleteProductFromInventory() {
  if (selectedProduct.value) {
    await deleteProduct(selectedProduct.value.inventoryId);
    loadProducts();
    showDelete.value = false;
  }
}
</script>

<template>
  <div class="container">
    <Sidebar :options="AdminSidebarOptions" />

    <div class="products">
      <div class="products-container">
        <table v-if="inventory.length > 0">
          <TableHeader
            :columns="['Product Name', 'Quantity', 'Actions']"
            :has-action-button="true"
            @action-triggered="showAddModal = true"
          />
          <tbody>
            <TableRow
              v-for="(product, index) in inventory"
              :columns="[
                10 * (currentPage - 1) + index + 1,
                product.product,
                product.quantity,
              ]"
              :index="index"
              :has-empty-column="true"
            >
              <div class="actions">
                <ActionButton
                  icon-token="pen"
                  @action-triggered="showEditModal(product.inventoryId)"
                />
                <ActionButton
                  icon-token="trash-can"
                  @action-triggered="showDeleteModal(product.inventoryId)"
                />
              </div>
            </TableRow>
          </tbody>
        </table>

        <img
          src="../../assets/nodata.svg"
          alt=""
          v-else-if="inventory.length === 0 && isLoading === false"
        />

        <LoadingSpinner v-else />

        <Pagination
          :total-pages="totalPages"
          :current-page="currentPage"
          @change-page="changePage"
        />
      </div>
      <CustomModal
        :show="showDelete"
        @button2="showDelete = false"
        @button1="deleteProductFromInventory"
      >
        <span class="delete-text">{{
          "Are you sure you want to delete " +
          selectedProduct?.product +
          " from the inventory?"
        }}</span></CustomModal
      >
    </div>
    <CustomModal
      :show="showAddModal"
      button1-text="Add Product"
      @button2="showAddModal = false"
      @button1="createProduct"
      title="New Product"
    >
      <div class="selection">
        <div class="option">
          <label>Product</label>
          <input type="text" placeholder="" v-model="productName" />
        </div>
        <div class="option">
          <label>Quantity</label>
          <span>
            <button @click="quantity = quantity - 1">-</button>
            <input
              type="number"
              placeholder=""
              id="quantity"
              v-model="quantity"
            />
            <button @click="quantity = quantity + 1">+</button>
          </span>
        </div>
      </div>
    </CustomModal>

    <CustomModal
      :show="showEdit"
      button1-text="Delete"
      @button2="showEdit = false"
      @button1="editProduct"
      :title="selectedProduct?.product"
    >
      <div class="selection">
        <div class="option">
          <label>Product</label>
          <input
            type="text"
            :placeholder="selectedProduct?.product"
            v-model="productName"
          />
        </div>
        <div class="option">
          <label>Quantity</label>
          <span>
            <button @click="quantity = quantity - 1">-</button>
            <input
              type="text"
              :placeholder="selectedProduct?.quantity.toString()"
              id="quantity"
              v-model="quantity"
            />
            <button @click="quantity = quantity + 1">+</button>
          </span>
        </div>
      </div>
    </CustomModal>
  </div>
</template>

<style scoped lang="less">
@import (reference) "@/assets/styles.less";

.container {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .products {
    width: 83vw;
    .products-container {
      width: 100%;
      height: 100vh;
      position: relative;

      table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        color: @gray;

        tbody {
          margin-top: 20px;
        }
      }

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 70%;
      }
    }
    .delete-text {
      color: @white;
    }
  }

  .selection {
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 10px;
    .option {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 20px;

      label {
        color: @sugar;
        font-size: 18px;
      }

      input {
        width: 160px;
        background-color: @font-gray;
        border-radius: 10px;
        outline: none;
        color: @font-darker-gray;
        font-size: 15px;
        padding: 5px;
        border: none;

        &#quantity {
          width: 30px;
          border-radius: 5px;
          text-align: center;
        }

        &:focus {
          background-color: @sugar;
        }
      }

      span {
        width: 170px;
        display: flex;
        justify-content: space-evenly;
        align-items: center;

        button {
          background-color: transparent;
          border: none;
          cursor: pointer;
          color: @white;
          font-size: 20px;
        }
      }
    }
  }
}
</style>
