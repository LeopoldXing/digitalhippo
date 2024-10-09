import { getCookie } from "cookies-next";
import { PayloadRequest } from "payload/types";
import { Product } from "@/payload-types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

const afterChangeProductHook = async ({ req, operation, doc }: { req: PayloadRequest, operation: string, doc: Product }) => {
  console.log("准备创建product")
  console.log(doc)
  console.log(req)
  if (operation === 'create') {
    try {
      const response = await fetch(`https://${BASE_URL}/api/product`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          "Authorization": `Bearer ${getCookie("digitalhippo-access-token")}`
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

export { afterChangeProductHook, afterDeleteProductHook }