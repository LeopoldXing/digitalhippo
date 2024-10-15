"use client"

import React from 'react';
import Link from "next/link";
import ProductListing from "@/components/ProductListing";
import { useSearchProduct } from "@/hooks/productHooks";
import { ProductApiType, searchingCondition } from "@/types";

interface ProductReelProps {
  title?: string
  subtitle?: string
  href?: string
  query: searchingCondition
  exclude?: string
}

const ProductReel = (props: ProductReelProps) => {
  const { title, subtitle, href, query, exclude } = props

  const { productSearchingResult, isLoading } = useSearchProduct({ condition: query })

  let productList: (ProductApiType | null)[] = []
  if (Array.isArray(productSearchingResult?.results) && productSearchingResult?.results.length > 0) {
    productList = productSearchingResult?.results || []
  } else if (isLoading) {
    productList = new Array<null>(4).fill(null)
  }

  if (exclude) {
    productList = productList.filter(product => {
      return product?.payloadId !== exclude
    })
  }

  return (
      <section className='py-12'>
        <div className='md:flex md:items-center md:justify-between mb-4'>
          <div className='max-w-2xl px-4 lg:max-w-4xl lg:px-0'>
            {title ? (<h1 className='text-2xl font-bold text-gray-900 sm:text-3xl'>{title}</h1>) : null}
            {subtitle ? (<p className='mt-2 text-sm text-muted-foreground'>{subtitle}</p>) : null}
          </div>

          {href ? (
              <Link href={href} className='hidden text-sm font-medium text-blue-600 hover:text-blue-500 md:block'>
                Shop the collection{' '}
                <span aria-hidden='true'>&rarr;</span>
              </Link>
          ) : null}
        </div>

        <div className='relative'>
          <div className='mt-6 flex items-center w-full'>
            <div className='w-full grid grid-cols-2 gap-x-4 gap-y-10 sm:gap-x-6 md:grid-cols-4 md:gap-y-10 lg:gap-x-8'>
              {productList.length > 0 ? (
                  productList.map((product, i) => (<ProductListing key={`product-${i}`} product={product} index={i}/>))
              ) : (
                  "No results found"
              )}
            </div>
          </div>
        </div>
      </section>
  );
};

export default ProductReel;