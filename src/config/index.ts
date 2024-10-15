export const PRODUCT_CATEGORIES = [
  {
    label: 'UI Kits',
    value: 'ui_kits' as const,
    featured: [
      {
        name: 'Editor picks',
        href: `/products?category=ui_kits`,
        imageSrc: '/nav/ui-kits/mixed.jpg',
      },
      {
        name: 'New Arrivals',
        href: '/products?category=ui_kits&sortingStrategy=CREATED_TIMESTAMP&sortingDirection=DESC',
        imageSrc: '/nav/ui-kits/blue.jpg',
      },
      {
        name: 'Bestsellers',
        href: '/products?category=ui_kits&sortingStrategy=POPULARITY&sortingDirection=DESC',
        imageSrc: '/nav/ui-kits/purple.jpg',
      },
    ],
  },
  {
    label: 'Icons',
    value: 'icons' as const,
    featured: [
      {
        name: 'Favorite Icon Picks',
        href: `/products?category=icons`,
        imageSrc: '/nav/icons/picks.jpg',
      },
      {
        name: 'New Arrivals',
        href: '/products?category=icons&sortingStrategy=CREATED_TIMESTAMP&sortingDirection=DESC',
        imageSrc: '/nav/icons/new.jpg',
      },
      {
        name: 'Bestselling Icons',
        href: '/products?category=icons&sortingStrategy=POPULARITY&sortingDirection=DESC',
        imageSrc: '/nav/icons/bestsellers.jpg',
      },
    ],
  },
]

export const transactionFee = 1

export const SORTING_OPTIONS = [
  { label: 'Featured', value: 'featured', sortingStrategy: 'POPULARITY', sortingDirection: 'DESC' },
  { label: 'Price: Low to high', value: 'price_asc', sortingStrategy: 'PRICE', sortingDirection: 'ASC' },
  { label: 'Price: High to low', value: 'price_desc', sortingStrategy: 'PRICE', sortingDirection: 'DESC' },
  { label: 'Newest arrivals', value: 'created_timestamp', sortingStrategy: 'CREATED_TIMESTAMP', sortingDirection: 'DESC' }
]

export const PRICE_RANGE = [
  { label: 'All Prices', value: 0, bottomPrice: -1, topPrice: -1 },
  { label: '$ 0 - 10', value: 1, bottomPrice: 0, topPrice: 9.99 },
  { label: '$ 10 - 50', value: 2, bottomPrice: 10, topPrice: 49.99 },
  { label: '$ 50 - 100', value: 3, bottomPrice: 50, topPrice: 99.99 },
  { label: '$ 100 - 200', value: 4, bottomPrice: 100, topPrice: 199.99 },
  { label: '$ 200+', value: 5, bottomPrice: 200, topPrice: -1 }
]