'use client'

import { z } from "zod";
import { searchingCondition } from "@/types";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormControl, FormField, FormItem } from "@/components/ui/form";
import { ChevronDown, Search } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import React, { useEffect, useState } from "react";
import { cn } from "@/lib/utils";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { PRICE_RANGE, PRODUCT_CATEGORIES, SORTING_OPTIONS } from "@/config";

const searchFormSchema = z.object({
  keyword: z.string(),
  category: z.string(),
  topPrice: z.coerce.number({
    invalid_type_error: "must be a valid number"
  }),
  bottomPrice: z.coerce.number({
    invalid_type_error: "must be a valid number"
  }),
  size: z.number(),
  current: z.number(),
  sortingStrategy: z.string(),
  sortingDirection: z.string()
})

type SearchFormType = z.infer<typeof searchFormSchema>;

type ProductSearchingFormProps = {
  // eslint-disable-next-line no-unused-vars
  onSearch: (condition: searchingCondition) => void,
  isLoading: boolean;
  defaultConditions?: searchingCondition;
}

const ProductSearchingForm = ({ onSearch, isLoading = false, defaultConditions }: ProductSearchingFormProps) => {
  const form = useForm<SearchFormType>({
    resolver: zodResolver(searchFormSchema),
    defaultValues: defaultConditions
  })

  useEffect(() => {
    form.reset({ ...defaultConditions })
  }, [form, defaultConditions]);

  const onSubmit = (formDataJson: SearchFormType) => {
    onSearch({
      keyword: formDataJson.keyword,
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-expect-error
      category: formDataJson.category,
      topPrice: formDataJson.topPrice,
      bottomPrice: formDataJson.bottomPrice,
      size: formDataJson.size,
      current: formDataJson.current,
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-expect-error
      sortingStrategy: formDataJson.sortingStrategy,
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-expect-error
      sortingDirection: formDataJson.sortingDirection
    })
  }

  // category
  let defaultSelectedCategory = 'all'
  let defaultCategoryLabel = "All Category"
  if (defaultConditions?.category === 'ui_kits' || defaultConditions?.category === 'icons') {
    defaultSelectedCategory = defaultConditions?.category || 'all'
    defaultCategoryLabel = PRODUCT_CATEGORIES.find(c => c.value === defaultSelectedCategory)?.label || 'All Category'
  }
  const [selectedCategory, setSelectedCategory] = useState(defaultSelectedCategory)
  const [categoryLabel, setCategoryLabel] = useState(defaultCategoryLabel)
  const handleCategorySelect = (value: string, label: string) => {
    setCategoryLabel(label);
    form.setValue('category', value)
    setSelectedCategory(value);
    form.setValue('current', 1)
  }

  // sort
  let defaultSortingOption = "featured"
  if (defaultConditions?.sortingStrategy === "PRICE") {
    if (defaultConditions.sortingDirection === "DESC") {
      defaultSortingOption = "price_desc"
    } else {
      defaultSortingOption = "price_asc"
    }
  } else if (defaultConditions?.sortingStrategy === "CREATED_TIMESTAMP") {
    defaultSortingOption = "created_timestamp"
  }
  const [selectedSortingOption, setSelectedSortingOption] = useState(defaultSortingOption)
  const handleSortingOptionSelect = (value: string) => {
    const sortingOption = SORTING_OPTIONS.find(option => option.value === value)
    form.setValue('sortingDirection', sortingOption?.sortingDirection || 'DESC')
    form.setValue('sortingStrategy', sortingOption?.sortingStrategy || 'POPULARITY')
    setSelectedSortingOption(value)
  }

  // price range
  let defaultPriceRange = 0
  if (defaultConditions?.bottomPrice) {
    defaultPriceRange = PRICE_RANGE.find(range => range.bottomPrice === defaultConditions.bottomPrice)?.value || 0
  }
  const [selectedPriceRange, setSelectedPriceRange] = useState(defaultPriceRange);
  const handleSelectPriceRange = (value: number) => {
    const priceOption = PRICE_RANGE.find(option => option.value === value)
    form.setValue('bottomPrice', priceOption?.bottomPrice || -1)
    form.setValue('topPrice', priceOption?.topPrice || -1)
    form.setValue('current', 1)
    setSelectedPriceRange(value)
  }

  useEffect(() => {
    form.handleSubmit(onSubmit)();
  }, [selectedPriceRange, selectedCategory, selectedSortingOption]);

  return (
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="w-full">
          <div className="w-full p-3 border rounded-full border-gray-200 flex justify-start items-center gap-1">
            <FormField control={form.control} name='keyword' render={({ field }) => (
                <FormItem className="w-full">
                  <FormControl>
                    <Input {...field} className="w-full border-none shadow-none text-lg focus-visible:ring-0"
                           placeholder="Search what you want.."/>
                  </FormControl>
                </FormItem>
            )}/>
            <Button onClick={form.handleSubmit(onSubmit)} variant={'ghost'} className="rounded-full">
              <Search strokeWidth={2.5} size={28} className="text-blue-500 hidden md:block"/>
            </Button>
          </div>

          <div className="flex h-16 items-center justify-between border-b border-gray-100">
            <div className="relative flex items-center">
              {/*  price range  */}
              <DropdownMenu>
                <DropdownMenuTrigger disabled={isLoading} asChild className="mx-1 flex items-center">
                  <Button variant={'ghost'}>
                    {PRICE_RANGE.find(option => option.value === selectedPriceRange)?.label}
                    <ChevronDown className="ml-2 h-4 w-4"/>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                  {PRICE_RANGE.map((priceRange) => (
                      <DropdownMenuItem key={priceRange.value}
                                        onSelect={() => handleSelectPriceRange(priceRange.value)}
                                        className={cn({ 'font-bold': selectedPriceRange === priceRange.value })}>
                        {priceRange.label}
                      </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
            <div className="relative flex items-center">
              {/*  category  */}
              <DropdownMenu>
                <DropdownMenuTrigger disabled={isLoading} asChild className="mx-1 flex items-center">
                  <Button variant={'ghost'}>
                    {categoryLabel}
                    <ChevronDown className="ml-2 h-4 w-4"/>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                  <DropdownMenuItem className={cn({ 'font-bold': selectedCategory === 'all' })} onSelect={() => {
                    handleCategorySelect("all", "All Category")
                    form.handleSubmit(onSubmit)
                  }}>
                    All Category
                  </DropdownMenuItem>
                  {PRODUCT_CATEGORIES.map((category) => (
                      <DropdownMenuItem key={category.value} className={cn({ 'font-bold': selectedCategory === category.value })}
                                        onSelect={() => handleCategorySelect(category.value, category.label)}>
                        {category.label}
                      </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
              <span className='h-6 w-px bg-gray-200' aria-hidden='true'/>
              {/*  sort  */}
              <DropdownMenu>
                <DropdownMenuTrigger disabled={isLoading} asChild className="mx-1 flex items-center">
                  <Button variant={'ghost'}>
                    {SORTING_OPTIONS.find(option => option.value === selectedSortingOption)?.label}
                    <ChevronDown className="ml-2 h-4 w-4"/>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                  {SORTING_OPTIONS.map((sortingOption) => (
                      <DropdownMenuItem key={sortingOption.value}
                                        onSelect={() => handleSortingOptionSelect(sortingOption.value)}
                                        className={cn({ 'font-bold': selectedSortingOption === sortingOption.value })}>
                        {sortingOption.label}
                      </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>

          </div>
        </form>
      </Form>
  )
}

export default ProductSearchingForm;
