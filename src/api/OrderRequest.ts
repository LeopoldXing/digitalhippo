const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;
const FRONTEND_ENDPOINT = process.env.NEXT_PUBLIC_FRONTEND_URL;

const getOrderRequest = async ({ orderId, accessToken }: { orderId: string, accessToken: string }) => {
  const response = await fetch(`${BASE_URL}/api/stripe/order/${orderId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${accessToken}`
    }
  })
  if (!response.ok) {
    throw new Error(`Failed to retrieve order ${orderId}`)
  }
  return response.json();
}

const createPayloadOrder = async ({ userId, productIdList, isPaid }: { userId: string, productIdList: string[], isPaid: boolean }) => {
  const response = await fetch(`${FRONTEND_ENDPOINT}/api/orders`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      user: userId,
      products: productIdList,
      _isPaid: isPaid
    })
  })
  if (!response.ok) {
    throw new Error("Failed to create order in Payload")
  }
  return response.json()
}

export { getOrderRequest, createPayloadOrder }