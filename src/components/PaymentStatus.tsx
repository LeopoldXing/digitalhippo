'use client'

import { useRouter } from 'next/navigation'
import { useEffect } from 'react'
import { usePollOrder } from "@/hooks/orderHooks";
import { getCookie } from "cookies-next";
import { getUserCartItem } from "@/api/CartRequest";
import { ProductApiType } from "@/types";
import { cartHooks } from "@/hooks/cartHooks";

interface PaymentStatusProps {
  orderEmail: string
  orderId: string
  isPaid: boolean
}

const PaymentStatus = ({ orderEmail, orderId, isPaid }: PaymentStatusProps) => {
  const router = useRouter()
  const accessToken = getCookie("digitalhippo-access-token") || "";
  const { order } = usePollOrder({ orderId, accessToken })

  // align cart item
  const { clearCart, addItem } = cartHooks()
  const alignCartItem = async () => {
    const productList: ProductApiType[] = await getUserCartItem(accessToken);
    clearCart()
    productList.forEach(item => addItem({ product: item, accessToken: undefined, sendNotification: false, updateBackendCart: false }))
  }
  useEffect(() => {
    alignCartItem()
  }, []);

  useEffect(() => {
    if (order?.isPaid) {
      router.refresh()
    }
  }, [order?.isPaid, router])

  return (
      <div className='mt-16 grid grid-cols-2 gap-x-4 text-sm text-gray-600'>
        <div>
          <p className='font-medium text-gray-900'>Shipping To</p>
          <p>{orderEmail}</p>
        </div>

        <div>
          <p className='font-medium text-gray-900'>Order Status</p>
          <p>{isPaid ? 'Payment successful' : 'Pending payment'}</p>
        </div>
      </div>
  )
}

export default PaymentStatus
