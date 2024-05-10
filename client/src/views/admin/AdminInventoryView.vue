<script setup lang="ts">
import { ref, onMounted } from "vue";

import Sidebar from "@/components/Sidebar.vue";
import Pagination from "@/components/Pagination.vue";
import CustomModal from "@/components/CustomModal.vue";
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

const productName = ref("");
const quantity = ref(0);

const currentPage = ref(1);
const totalPages = ref(0);

const inventory = ref<Product[]>([]);
const selectedProduct = ref<Product>();

async function loadProducts() {
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    inventory.value = res.pagedInventory.content;
    totalPages.value = Math.ceil(res.total / 10);
  });
}

async function changePage(pageNumber: number) {
  currentPage.value = pageNumber;
  await getAllPageable(10, currentPage.value - 1).then((res: any) => {
    inventory.value = res.pagedInventory.content;
    totalPages.value = Math.ceil(res.total / 10);
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
        <table>
          <thead>
            <tr>
              <th>No.#</th>
              <th>Product Name</th>
              <th>Quantity</th>
              <th>Actions</th>
              <th>
                <button @click="showAddModal = true" class="add-button">
                  Add new product
                </button>
              </th>
            </tr>
          </thead>
          <tr style="height: 20px">
            <td colspan="5"></td>
          </tr>
          <tbody>
            <tr v-for="(product, index) in inventory">
              <td>{{ 10 * (currentPage - 1) + index + 1 }}</td>
              <td>
                {{ product.product }}
              </td>
              <td>
                {{ product.quantity }}
              </td>
              <td>
                <div class="actions">
                  <button @click="showEditModal(product.inventoryId)">
                    <font-awesome-icon icon="pen" id="icon" />
                  </button>

                  <button @click="showDeleteModal(product.inventoryId)">
                    <font-awesome-icon icon="trash-can" id="icon" />
                  </button>
                </div>
              </td>
              <td class="empty-column"></td>
            </tr>
          </tbody>
        </table>

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

        thead tr {
          height: 8vh;
          background-color: @gray;
          color: @white;
          font-size: large;

          th:first-child {
            border-bottom-left-radius: 40px;
          }
        }

        .add-button {
          background-color: transparent;
          border: 1px solid @white;
          color: @white;
          font-size: large;
          padding: 7px;
          cursor: pointer;
        }

        tbody {
          margin-top: 20px;
        }
        tbody tr {
          height: 8vh;
          font-size: 20px;
          td {
            text-align: center;
            .date {
              display: flex;
              flex-direction: column;

              #time {
                font-size: 20px;
                font-weight: 500;
              }

              #date {
                font-size: 12px;
              }
            }

            .actions {
              button {
                border: none;
                background-color: transparent;
                cursor: pointer;
              }
            }

            &:first-child {
              border-top-left-radius: 20px;
              border-bottom-left-radius: 20px;
            }
          }
          &:hover {
            background-color: @sugar;
          }
          &:nth-child(odd) {
            background-color: @light-gray;
          }
        }
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