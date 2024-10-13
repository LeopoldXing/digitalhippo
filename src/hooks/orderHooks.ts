import { useQuery } from "react-query";
import { OrderApiType } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

const usePollOrder = ({ orderId, accessToken }: { orderId: string, accessToken: string }) => {

  const pollOrderRequest = async (): Promise<OrderApiType> => {
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
  const { data: order, isLoading } = useQuery<OrderApiType, Error>(['pollOrder', orderId], pollOrderRequest, {
    enabled: true,
    refetchInterval: (data) => data?.isPaid ? false : 1000
  });

  return { order, isLoading }
}

export { usePollOrder }