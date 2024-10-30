import { ProductApiType, searchingCondition } from "@/types";
import qs from "qs";
import { ProductSearchingResultType } from "@/hooks/productHooks";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'https://digitalhippo-backend.leopoldhsing.cc:31400';

const getProductRequest = async (productId: string | number): Promise<ProductApiType> => {
  const response = await fetch(`${BASE_URL}/api/product/${productId}`, {
    method: 'GET',
    headers: {
      "Content-Type": "application/json"
    },
    cache: 'no-cache'
  })
  if (!response.ok) {
    throw new Error("Error getting product");
  }
  return response.json();
}

const searchProductRequest = async ({ condition }: { condition: searchingCondition }): Promise<ProductSearchingResultType> => {
  const params = qs.stringify(condition);

  const response = await fetch(`${BASE_URL}/api/product/search?${params}`, { method: "GET", cache: "no-cache" })

  if (!response.ok) {
    throw new Error("Error searching product");
  }
  return response.json();
}

export { getProductRequest, searchProductRequest }