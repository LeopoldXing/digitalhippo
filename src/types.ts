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