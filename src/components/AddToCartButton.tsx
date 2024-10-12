'use client'

import { useEffect, useState } from 'react'
import { Button } from './ui/button'
import { ProductApiType } from "@/types";
import { cartHooks } from "@/hooks/cartHooks";
import { CookieValueTypes, getCookie } from "cookies-next";

const AddToCartButton = ({ product }: { product: ProductApiType }) => {
  const { addItem } = cartHooks()
  const [isSuccess, setIsSuccess] = useState(false)
  const accessToken: CookieValueTypes = getCookie("digitalhippo-access-token")

  useEffect(() => {
    const timeout = setTimeout(() => {
      setIsSuccess(false)
    }, 2000)

    return () => clearTimeout(timeout)
  }, [isSuccess])

  return (
      <Button size='lg' className='w-full' onClick={() => {
        addItem({product, accessToken})
        setIsSuccess(true)
      }}>
        {isSuccess ? 'Added!' : 'Add to cart'}
      </Button>
  )
}

export default AddToCartButton
