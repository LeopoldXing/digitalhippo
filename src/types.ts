import { PRODUCT_CATEGORIES } from "@/config";

export type Category = typeof PRODUCT_CATEGORIES[number]

export type User = {
  id: string;
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
  filename: string,
  description?: string,
  price: number,
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