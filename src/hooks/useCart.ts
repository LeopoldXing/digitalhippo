import { Product } from "@/payload-types";
import { CartItem, ProductApiType } from "@/types";

type CartState = {
  items: CartItem[]
  addItem: (product: Product) => void
  removeItem: (productId: string) => void
  clearCart: () => void
}

const useCart = () => {
  const addItem = (product: ProductApiType) => {
    console.log(111)
  }

  return addItem
}

export { useCart }