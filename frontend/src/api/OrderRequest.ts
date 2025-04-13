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

const createPayloadOrder = async ({ userId, productPayloadIds, isPaid, payloadToken }: {
  userId: string,
  productPayloadIds: string[],
  isPaid: boolean,
  payloadToken: string
}) => {
  const response = await fetch(`${FRONTEND_ENDPOINT}/api/orders`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${payloadToken}`
    },
    body: JSON.stringify({
      user: userId,
      products: productPayloadIds,
      _isPaid: isPaid
    })
  })
  if (!response.ok) {
    throw new Error("Failed to create order in Payload")
  }
  return response.json()
}

export { getOrderRequest, createPayloadOrder }