const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

export const addToCart = async ({ productId, accessToken }: { productId: string, accessToken: string | undefined }) => {
  const response = await fetch(`${BASE_URL}/api/cart/${productId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${accessToken}`
    }
  })
  if (!response.ok) {
    throw new Error("Failed to add this item to the cart")
  }
}

export const removeFromCart = async ({ productId, accessToken }: { productId: string, accessToken: string | undefined }) => {
  const response = await fetch(`${BASE_URL}/api/cart/${productId}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${accessToken}`
    }
  })
  if (!response.ok) {
    throw new Error("Failed to remove this item from the cart")
  }
}
