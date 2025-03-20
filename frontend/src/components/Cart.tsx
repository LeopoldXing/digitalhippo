'use client';

import { Sheet, SheetContent, SheetFooter, SheetHeader, SheetTitle, SheetTrigger } from "@/components/ui/sheet";
import { ShoppingCart } from "lucide-react";
import { Separator } from "@/components/ui/separator";
import { formatPrice } from "@/lib/utils";
import { transactionFee } from "@/config";
import Link from "next/link";
import { buttonVariants } from "@/components/ui/button";
import Image from "next/image";
import { cartHooks } from "@/hooks/cartHooks";
import { useEffect, useState } from "react";
import CartItem from "@/components/CardItem";
import { ScrollArea } from "@/components/ui/scroll-area";
import { User } from "@/types";

const Cart = ({user}: {user: User | undefined}) => {
  const { items } = cartHooks()
  const itemCount = items.length

  const [isMounted, setIsMounted] = useState(false)

  useEffect(() => {
    setIsMounted(true)
  }, [])

  const cartTotal = items.reduce(
      (total, { product }) => total + product.price,
      0
  )

  return (
      <Sheet>
        <SheetTrigger className='group -m-2 flex items-center p-2'>
          <ShoppingCart aria-hidden='true' className='h-6 w-6 flex-shrink-0 text-gray-400 group-hover:text-gray-500'/>
          <span className='ml-2 text-sm font-medium text-gray-700 group-hover:text-gray-800'>{isMounted ? itemCount : 0}</span>
        </SheetTrigger>
        <SheetContent className='w-full pr-0 sm:max-w-lg flex flex-col'>
          <SheetHeader className='space-y-2.5 pr-6'>
            <SheetTitle>Cart ({itemCount})</SheetTitle>
          </SheetHeader>
          {itemCount ? (
              <>
                <div className='flex w-full flex-col pr-6'>
                  <ScrollArea>
                    {items.map(({ product }) => (<CartItem product={product} key={product.id}/>))}
                  </ScrollArea>
                </div>
                <div className='space-y-4 pr-6'>
                  <Separator/>
                  <div className='space-y-1.5 pr-6'>
                    <div className='flex'>
                      <span className='flex-1'>Shipping</span>
                      <span>Free</span>
                    </div>
                    <div className='flex'>
                      <span className='flex-1'>Transaction Fee</span>
                      <span>{formatPrice(transactionFee)}</span>
                    </div>
                    <div className='flex'>
                      <span className='flex-1'>Total</span>
                      <span>{formatPrice(cartTotal + transactionFee)}</span>
                    </div>
                  </div>
                  <SheetFooter>
                    <SheetTrigger asChild>
                      <Link href={`/checkout?loggedIn=${user ? 'true' : 'false'}`} className={buttonVariants({ className: 'w-full' })}>Continue
                        to Checkout</Link>
                    </SheetTrigger>
                  </SheetFooter>
                </div>
              </>
          ) : (
              <div className='h-full flex flex-col items-center justify-center space-y-1'>
                <div aria-hidden={true} className='relative mb-4 h-60 w-60 text-muted-foreground'>
                  <Image src='/hippo-empty-cart.png' alt='empty shopping cart image' fill/>
                </div>
                <span className='text-xl font-semibold'>Cart is Empty</span>
                <SheetTrigger asChild>
                  <Link href='/products' className={buttonVariants({
                    variant: 'link',
                    size: 'sm',
                    className: 'text-sm text-muted-foreground'
                  })}>Add items to your cart to checkout</Link>
                </SheetTrigger>
              </div>
          )}
        </SheetContent>
      </Sheet>
  );
};

export default Cart;