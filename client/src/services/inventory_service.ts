const API_URL = "http://localhost:9090/api/inventory";

async function addProduct(product: string, quantity: number) {
  let response;
  let connectionError = false;
  try {
    response = await fetch(`${API_URL}`, {
      method: "POST",
      body: JSON.stringify({
        product,
        quantity,
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8",
      },
    });
  } catch (error) {
    connectionError = true;
  }

  if (connectionError) {
    throw new Error("Server connection error");
  }

  const json = await response?.json();
  if (response && !response.ok) {
    throw new Error(json.message);
  }

  return json;
}

async function getAllPageable(pageSize: number, pageNumber: number) {
  const response = await fetch(
    `${API_URL}/allPage?pageSize=${pageSize}&pageNumber=${pageNumber}`
  );

  const json = await response.json();
  return json;
}

async function getProductById(id: number) {
  const response = await fetch(`${API_URL}?inventoryId=${id}`);

  const json = await response.json();
  return json;
}

async function deleteProduct(id: number) {
  const response = fetch(API_URL + "?inventoryId=" + id, {
    method: "DELETE",
  });
  return response;
}

async function updateProduct(
  inventoryId: number,
  productName: string,
  quantity: number
) {
  const response = await fetch(
    `${API_URL}/update-product?inventoryId=${inventoryId}`,
    {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        product: productName,
        quantity: quantity,
      }),
    }
  );
}

export {
  addProduct,
  getAllPageable,
  getProductById,
  deleteProduct,
  updateProduct,
};
