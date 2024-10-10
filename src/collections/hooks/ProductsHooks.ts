import { PayloadRequest } from "payload/types";
import { Product } from "@/payload-types";
import { getCookie } from "cookies-next";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

const beforeChangeProductHook = async ({ data, req, operation }: {
  data: Partial<Product>,
  req: PayloadRequest,
  operation: string | number
}) => {
  if (operation === 'create') {
    if (req.user) {
      data.user = req.user.id;
    } else {
      throw new Error('Unauthorized, can not create product');
    }
  }
}

/**
 * create product in backend
 * @param req
 * @param operation
 * @param doc
 */
const afterChangeProductHook = async ({ req, operation, doc }: {
  req: PayloadRequest,
  operation: string,
  doc: Product
}) => {
  console.log("准备创建product")
  console.log(doc)
  console.log(`${BASE_URL}/api/product`);
  console.log(`Bearer ${getCookie("digitalhippo-access-token", { req })}`)
  if (operation === 'create') {
    try {
      const response = await fetch(`${BASE_URL}/api/product`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          "Authorization": `Bearer ${getCookie("digitalhippo-access-token", { req })}`
        },
        body: JSON.stringify(doc)
      });

      const responseData = await response.json();
      console.log('API调用成功: ', responseData);
    } catch (error) {
      console.error('API调用失败: ', error);
    }
  }
}

const afterDeleteProductHook = async ({ req, id, doc }: { req: PayloadRequest, id: string | number, doc: Product }) => {

}

export { afterChangeProductHook, afterDeleteProductHook, beforeChangeProductHook }