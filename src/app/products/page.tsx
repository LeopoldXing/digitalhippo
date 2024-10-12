import MaxWidthWrapper from '@/components/MaxWidthWrapper'
import ProductReel from '@/components/ProductReel'
import { PRODUCT_CATEGORIES } from '@/config'

type Param = string | string[] | undefined

interface ProductsPageProps {
  searchParams: { [key: string]: Param }
}

const parse = (param: Param) => {
  return typeof param === 'string' ? param : undefined
}

const ProductsPage = ({ searchParams }: ProductsPageProps) => {
  const sort = parse(searchParams.sort)
  const parsedCategory = parse(searchParams.category)
  let category: 'ui_kits' | 'icons' | undefined = undefined
  if (parsedCategory === 'ui_kits' || parsedCategory === 'icons') {
    category = parsedCategory
  }

  const label = PRODUCT_CATEGORIES.find(({ value }) => value === category)?.label

  return (
      <MaxWidthWrapper>
        <ProductReel title={label ?? 'Browse high-quality assets'}
                     query={{ category, size: 40, sortingDirection: sort === 'DESC' || sort === 'ASC' ? sort : 'NONE' }}/>
      </MaxWidthWrapper>
  )
}

export default ProductsPage
