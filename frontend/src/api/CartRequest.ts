const BASE_URL = process.env.BACKEND_URL;

export const getUserCartItem = async (accessToken: string) => {
  const response = await fetch(`${BASE_URL}/api/cart`, {
    method: 'GET',
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`
    }
  })
  return response.json();
}

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
