import qs from 'qs';
import { useQuery } from "react-query";
import { ProductApiType, searchingCondition } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'https://digitalhippo-backend.leopoldhsing.cc:31400';

export type ProductSearchingResultType = {
  results: ProductApiType[];
  resultCount: number;
  totalPage: number;
  current: number;
  size: number;
}
const useSearchProduct = ({ condition }: { condition: searchingCondition }) => {

  const searchProductRequest = async (): Promise<ProductSearchingResultType> => {
    const params = qs.stringify(condition);

    const response = await fetch(`${BASE_URL}/api/product/search?${params}`, {
      method: "GET",
      cache: "no-cache"
    })

    if (!response.ok) {
      throw new Error("Error searching product");
    }
    return response.json();
  }

  const { data: productSearchingResult, isLoading } = useQuery(
      [
        'searchProductRequest',
        condition.category,
        condition.current,
        condition.size,
        condition.sortingDirection,
        condition.sortingStrategy,
        condition.bottomPrice,
        condition.topPrice,
        condition.keyword
      ],
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