'use client'

import React, { useEffect, useState } from 'react';
import SearchForm from "@/forms/SearchForm";
import { searchingCondition } from "@/types";
import { useSearchProduct } from "@/hooks/productHooks";
import ProductSearchingResult from "@/components/ProductSearchingResult";
import PaginationSelector from "@/components/PaginationSelector";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import qs from 'qs';

const SearchingSection = () => {
  const router = useRouter();
  const searchParam = useSearchParams();
  const pathname = usePathname();


  const defaultConditions: searchingCondition = {
    keyword: searchParam.get("keyword") || "",
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    category: searchParam.get("category"),
    topPrice: Number.parseFloat(searchParam.get("topPrice") || "-1"),
    bottomPrice: Number.parseFloat(searchParam.get("bottomPrice") || "-1"),
    size: Number.parseInt(searchParam.get("size") || "8"),
    current: Number.parseInt(searchParam.get("current") || "1"),
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    sortingStrategy: searchParam.get("sortingStrategy") || "POPULARITY",
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    sortingDirection: searchParam.get("sortingDirection") || "DESC"
  }
  if (!defaultConditions.category) {
    defaultConditions.category = 'all'
  }


  const [conditions, setConditions] = useState(defaultConditions)
  const { productSearchingResult, isLoading } = useSearchProduct({ condition: defaultConditions });

  const updateUrl = () => {
    const paramString = qs.stringify(conditions)
    router.replace(`${pathname}/?${paramString}`)
  }

  useEffect(() => {
    updateUrl()
  }, [conditions]);
  
  const handlePageChange = async (targetPageNumber: number): Promise<void> => {
    setConditions(prevState => ({ ...prevState, current: targetPageNumber }))
  }

  const handleSearch = async (condition: searchingCondition) => {
    // eslint-disable-next-line no-unused-vars,@typescript-eslint/no-unused-vars
    setConditions(prevState => condition)
  }

  return (
      <div className="w-full mt-[60px]">
        <SearchForm onSearch={handleSearch} isLoading={isLoading} defaultConditions={defaultConditions}/>
        <ProductSearchingResult result={productSearchingResult} condition={defaultConditions} resultCount={productSearchingResult?.resultCount || 0}/>
        {productSearchingResult && productSearchingResult.totalPage > 1 && (
            <PaginationSelector totalPage={productSearchingResult?.totalPage || 1} current={productSearchingResult?.current || 1}
                                onPageChange={handlePageChange}/>
        )}
      </div>
  );
};

export default SearchingSection;