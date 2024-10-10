import { PayloadRequest } from "payload/types";
import { Product } from "@/payload-types";
import { getCookie } from "cookies-next";
import { ProductApiType } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;
const S3_ENDPOINT_HOST = process.env.S3_ENDPOINT_HOST;
const S3_BUCKET = process.env.S3_BUCKET;

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
  console.log("----------------------------------------------")
  console.log(`Bearer ${getCookie("digitalhippo-access-token", { req })}`)

  const productData: ProductApiType = {
    filename: doc.name,
    description: doc.description || '',
    price: doc.price,
    category: doc.category,
    productFileUrl: '',
    productImages: []
  }

  if (['create', 'update'].includes(operation)) {
    try {
      if (doc.product_files) {
        const productFileDoc = await req.payload.findByID({
          collection: 'product_files',
          id: doc.product_files as string
        });
        console.log('找到的 product_files document:', productFileDoc);
        const productFileUrl = `https://${S3_BUCKET}.${S3_ENDPOINT_HOST}/${productFileDoc.prefix}/${productFileDoc.filename}`;
        console.log('productFileUrl:', productFileUrl);
        productData.productFileUrl = productFileUrl;
      }
      if (Array.isArray(doc.images) && doc.images.length > 0) {
        console.log("该产品的图片：")
        for (const image of doc.images) {
          console.log(image)
          const currentImage = await req.payload.findByID({
            collection: 'media',
            id: image.image as string
          })
          console.log(currentImage)
          const thumbnail = currentImage?.sizes?.thumbnail
          const card = currentImage?.sizes?.card
          const tablet = currentImage?.sizes?.tablet

          if (thumbnail) {
            productData.productImages.push({
              filename: thumbnail.filename!,
              filesize: thumbnail.filesize!,
              fileType: "thumbnail",
              mimeType: thumbnail.mimeType!,
              width: thumbnail.width!,
              height: thumbnail.height!,
              url: `https://${S3_BUCKET}.${S3_ENDPOINT_HOST}/${currentImage.prefix}/${thumbnail.filename}`
            })
          }

          if (card) {
            productData.productImages.push({
              filename: card.filename!,
              filesize: card.filesize!,
              fileType: "card",
              mimeType: card.mimeType!,
              width: card.width!,
              height: card.height!,
              url: `https://${S3_BUCKET}.${S3_ENDPOINT_HOST}/${currentImage.prefix}/${card.filename}`
            })
          }

          if (tablet) {
            productData.productImages.push({
              filename: tablet.filename!,
              filesize: tablet.filesize!,
              fileType: "tablet",
              mimeType: tablet.mimeType!,
              width: tablet.width!,
              height: tablet.height!,
              url: `https://${S3_BUCKET}.${S3_ENDPOINT_HOST}/${currentImage.prefix}/${tablet.filename}`
            })
          }
        }
      }

      console.log("传给后端的productData:")
      console.log(productData)

      const response = await fetch(`${BASE_URL}/api/product`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          "Authorization": `Bearer ${getCookie("digitalhippo-access-token", { req })}`
        },
        body: JSON.stringify(productData)
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