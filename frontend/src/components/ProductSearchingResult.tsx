import React from 'react';
import { ProductSearchingResultType } from "@/hooks/productHooks";
import ProductListing from "@/components/ProductListing";
import { ProductApiType, searchingCondition } from "@/types";

const ProductSearchingResult = ({ result, condition, resultCount }: { result?: ProductSearchingResultType, condition: searchingCondition, resultCount: number }) => {
  if (!result) return;
  const productList: ProductApiType[] = result.results
  const category = condition.category
  let title = undefined
  if (category !== 'all') {
    if (category === 'ui_kits') title = 'UI KITS';
    else if (category === 'icons') title = 'ICONS';
  }
  const subtitle = `${resultCount} results found`

  return (
      <section className='py-10 mx-4'>
        <div className='md:flex md:items-center md:justify-between mb-4'>
          <div className='max-w-2xl px-4 lg:max-w-4xl lg:px-0'>
            {title && productList && productList.length > 0 ? (
                <h1 className='text-lg font-bold text-gray-900 sm:text-xl'>{title}</h1>) : null}
            {subtitle && result.resultCount > 0 ? (<p className='mt-2 text-sm text-muted-foreground'>{subtitle}</p>) : null}
          </div>
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

export default ProductSearchingResult;