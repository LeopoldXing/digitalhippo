import qs from 'qs';
import { useQuery } from "react-query";
import { ProductApiType } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

export type ProductSearchingConditionType = {
  keyword?: string;
  categoryList?: string[];
  topPrice?: number;
  bottomPrice?: number;
  category?: 'ui_kits' | 'icons';
  size?: number;
  current?: number;
  sortingStrategy?: 'CREATED_TIMETAMP' | 'POPULARITY' | 'RELEVANCE';
  sortingDirection?: 'DESC' | 'ASC' | 'NONE';
}
export type ProductSearchingResultType = {
  results: ProductApiType[];
  resultCount: number;
  totalPage: number;
  current: number;
  size: number;
}
const useSearchProduct = ({ condition }: { condition: ProductSearchingConditionType }) => {

  const searchProductRequest = async (): Promise<ProductSearchingResultType> => {
    const params = qs.stringify(condition);

    const response = await fetch(`${BASE_URL}/api/product/search?${params}`, { method: "GET" })

    if (!response.ok) {
      throw new Error("Error searching product");
    }
    return response.json();
  }

  const { data: productSearchingResult, isLoading } = useQuery(
      'searchProductRequest',
      searchProductRequest
  )

  return { productSearchingResult, isLoading }
}

const useGetProduct = (productId: string) => {

  const getProductRequest = async (): Promise<ProductApiType> => {
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

  const { data: product, isLoading } = useQuery(
      "getProduct",
      getProductRequest
  )

  return { product, isLoading }
}

export { useSearchProduct, useGetProduct }