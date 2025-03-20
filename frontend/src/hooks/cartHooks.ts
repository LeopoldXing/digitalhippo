import { CartItem, ProductApiType } from "@/types";
import { create } from 'zustand'
import { createJSONStorage, persist } from 'zustand/middleware'
import { toast } from "sonner";
import { addToCart, removeFromCart } from "@/api/CartRequest";

type CartState = {
  items: CartItem[]
  // eslint-disable-next-line no-unused-vars
  addItem: ({ product, accessToken, sendNotification }: {
    product: ProductApiType,
    accessToken: string | undefined,
    sendNotification?: boolean,
    updateBackendCart?: boolean
  }) => void
  // eslint-disable-next-line no-unused-vars
  removeItem: ({ productId, accessToken }: { productId: string, accessToken: string | undefined }) => void
  clearCart: () => void
  getItems: () => CartItem[]
  // eslint-disable-next-line no-unused-vars
  hasItem: (productId: string) => boolean
}

const cartHooks = create<CartState>()(
    persist(
        (set, get) => ({
          items: [],
          addItem: ({ product, accessToken, sendNotification = true, updateBackendCart = true }) => set((state) => {
            const productId = product.id!
            // determine if this item already in the cart
            if (state.items.find(cartItem => cartItem.product.id === productId)) {
              return state
            }
            if (accessToken && updateBackendCart) {
              addToCart({ productId, accessToken })
            }
            if (sendNotification) {
              toast.success("Items added!")
            }
            return { items: [...state.items, { product }] }
          }),
          removeItem: ({ productId, accessToken }) => set((state) => {
            if (accessToken) {
              removeFromCart({ productId, accessToken })
            }
            return { items: state.items.filter((item) => item.product.id !== productId) }
          }),
          clearCart: () => set({ items: [] }),
          getItems: () => get().items,
          hasItem: (productId: string) => get().items.filter(product => product.product.id === productId).length > 0,
        }),
        {
          name: 'digitalhippo-cart-storage',
          storage: createJSONStorage(() => localStorage)
        }
    )
)

export { cartHooks }