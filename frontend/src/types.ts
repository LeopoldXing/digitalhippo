import { PRODUCT_CATEGORIES } from "@/config";

export type Category = typeof PRODUCT_CATEGORIES[number]

export type User = {
  id: string;
  payloadId: string;
  username?: string;
  email: string;
}

export type ErrorResponseType = {
  error: string,
  message: string,
  path: string,
  status: number,
  timestamp: string,
  trace: string
}

export type ProductApiType = {
  id?: string,
  payloadId: string,
  name: string,
  description?: string,
  price: number,
  priceId: string,
  stripeId: string,
  category: 'ui_kits' | 'icons',
  approvedForSale?: ('pending' | 'approved' | 'denied')
  productFileUrl: string,
  productImages: ProductImageType[]
}

export type ProductImageType = {
  payloadId: string,
  filename: string,
  filesize: number,
  height: number,
  width: number,
  mimeType: string,
  fileType: 'thumbnail' | 'card' | 'tablet',
  url: string
}

export type OrderApiType = {
  id: string,
  payloadId: string,
  user: User,
  products: ProductApiType[],
  isPaid: boolean
}

export type CartItem = {
  product: ProductApiType
}

export type searchingCondition = {
  keyword: string;
  category: 'ui_kits' | 'icons' | 'all';
  topPrice: number;
  bottomPrice: number;
  size: number;
  current: number;
  sortingStrategy: 'CREATED_TIMESTAMP' | 'POPULARITY' | 'RELEVANCE' | 'PRICE';
  sortingDirection: 'DESC' | 'ASC' | 'NONE';
}