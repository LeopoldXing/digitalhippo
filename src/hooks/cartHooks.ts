import { CartItem, ProductApiType } from "@/types";
import { create } from 'zustand'
import { createJSONStorage, persist } from 'zustand/middleware'
import { toast } from "sonner";

type CartState = {
  items: CartItem[]
  addItem: (product: ProductApiType) => void
  removeItem: (productId: string) => void
  clearCart: () => void
}

const cartHooks = create<CartState>()(
    persist(
        (set) => ({
          items: [],
          addItem: (product) => set((state) => {
            // determine if this item already in the cart
            if (state.items.find(cartItem => cartItem.product.id === product.id)) {
              toast.warning("Item already in the cart!")
              return state
            }
            toast.success("Items added!")
            return { items: [...state.items, { product }] }
          }),
          removeItem: (id) => set((state) => ({
            items: state.items.filter((item) => item.product.id !== id)
          })),
          clearCart: () => set({ items: [] }),
        }),
        {
          name: 'digitalhippo-cart-storage',
          storage: createJSONStorage(() => localStorage)
        }
    )
)

export { cartHooks }