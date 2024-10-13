'use client'

import { useRouter } from 'next/navigation'
import { useEffect } from 'react'
import { usePollOrder } from "@/hooks/orderHooks";
import { getCookie } from "cookies-next";

interface PaymentStatusProps {
  orderEmail: string
  orderId: string
  isPaid: boolean
}

const PaymentStatus = ({ orderEmail, orderId, isPaid }: PaymentStatusProps) => {
  const router = useRouter()
  const accessToken = getCookie("digitalhippo-access-token") || "";
  const { order } = usePollOrder({ orderId, accessToken })

  useEffect(() => {
    if (order?.isPaid) router.refresh()
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
