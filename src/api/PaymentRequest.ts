const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'https://digitalhippo-backend.leopoldhsing.cc:31400';

type createPaymentSessionRequestType = {
  productIdList: string[],
  payloadOrderId: string,
  accessToken: string
}
export const createPaymentSessionRequest = async ({ productIdList, payloadOrderId, accessToken }: createPaymentSessionRequestType) => {
  const response = await fetch(`${BASE_URL}/api/stripe/payment/checkout-session`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${accessToken}`
    },
    body: JSON.stringify({ payloadOrderId, productIdList })
  })
  if (!response.ok) {
    throw new Error("Failed to create payment session");
  }
  return response.text()
}