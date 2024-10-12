import { ProductApiType } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

const getProductRequest = async (productId: string | number): Promise<ProductApiType> => {
  const response = await fetch(`${BASE_URL}/api/product/${productId}`, {
    method: 'GET',
    headers: {
      "Content-Type": "application/json"
    }
  })
  if (!response.ok) {
    throw new Error("Error getting product");
  }
  return response.json();
}

export { getProductRequest }