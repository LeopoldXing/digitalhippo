const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

export const createPaymentSessionRequest = async ({ productIdList, accessToken }: { productIdList: string[], accessToken: string }) => {
  const response = await fetch(`${BASE_URL}/api/order/create-payment-session`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${accessToken}`
    },
    body: JSON.stringify(productIdList)
  })
  if (!response.ok) {
    throw new Error("Failed to create payment session");
  }
  return response.json()
}