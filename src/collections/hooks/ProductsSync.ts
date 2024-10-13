import { PayloadRequest } from "payload/types";
import { Product } from "@/payload-types";
import { getCookie } from "cookies-next";
import { ProductApiType } from "@/types";
import { AfterChangeHook, BeforeChangeHook, BeforeDeleteHook } from "payload/dist/collections/config/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;
const S3_ENDPOINT_HOST = process.env.S3_ENDPOINT_HOST;
const S3_BUCKET = process.env.S3_BUCKET;

/**
 * add user info before modify product
 * @param data
 * @param req
 * @param operation
 */
const beforeChangeProductHook: BeforeChangeHook<Product> = async ({ data, req, operation }: {
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
const afterChangeProductHook: AfterChangeHook<Product> = async ({ req, operation, doc }: {
  req: PayloadRequest,
  operation: string,
  doc: Product
}) => {
  // initialize product data
  const productData: ProductApiType = {
    payloadId: doc.id,
    name: doc.name,
    description: doc.description || '',
    price: doc.price,
    priceId: doc.priceId || "",
    stripeId: doc.stripeId || "",
    category: doc.category,
    approvedForSale: doc.approvedForSale || 'pending',
    productFileUrl: '',
    productImages: []
  }

  // determine if this action is create or update
  if (['create', 'update'].includes(operation)) {
    try {
      await constructProductData(doc, req, productData);

      // send request
      const response = await fetch(`${BASE_URL}/api/product`, {
        method: operation === 'create' ? 'POST' : 'PUT',
        headers: {
          'Content-Type': 'application/json',
          "Authorization": `Bearer ${getCookie("digitalhippo-access-token", { req })}`
        },
        body: JSON.stringify(productData)
      });
      const product = await response.json();
      doc.priceId = product.priceId || "";
      doc.stripeId = doc.stripeId || "";
      return doc;
    } catch (error) {
      console.log(`create / update product failed: ${error}`)
    }
  }
}

/**
 * delete product
 * @param req
 * @param doc
 */
const beforeDeleteProductHook: BeforeDeleteHook = async ({ req, id }: {
  req: PayloadRequest,
  id: string | number,
}) => {
  // get accessToken
  const accessToken = getCookie("digitalhippo-access-token", { req });

  // get product
  const doc: Product = await req.payload.findByID({
    collection: "products",
    id: id
  })

  // send request
  try {
    await fetch(`${BASE_URL}/api/product/${doc.id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        "Authorization": `Bearer ${accessToken}`
      }
    })
    return doc;
  } catch (error) {
    console.log(`delete product failed: ${error}`)
  }
}

/**
 * sync user
 * @param req
 * @param doc
 */
const syncUser: AfterChangeHook<Product> = async ({ req, doc }: { req: PayloadRequest, doc: Product }) => {
  const fullUser = await req.payload.findByID({
    collection: 'users',
    id: req.user.id
  })

  if (fullUser && typeof fullUser === 'object') {
    const { products } = fullUser

    const allIDs = [
      ...(products?.map((product) => typeof product === 'object' ? product?.id : product) || [])
    ]

    const createdProductIDs = allIDs.filter(
        (id, index) => allIDs.indexOf(id) === index
    )

    const dataToUpdate = [...createdProductIDs, doc.id]

    await req.payload.update({
      collection: 'users',
      id: fullUser.id,
      data: {
        products: dataToUpdate,
      },
    })
  }
}

async function constructProductData(doc: Product, req: PayloadRequest, productData: ProductApiType) {
  if (doc.product_files) {
    // try to find the corresponding product file
    const productFileDoc = await req.payload.findByID({
      collection: 'product_files',
      id: typeof doc.product_files === 'object' ? doc.product_files.id : doc.product_files
    });
    // construct product file s3 url
    productData.productFileUrl = `https://${S3_BUCKET}.${S3_ENDPOINT_HOST}/${productFileDoc.prefix}/${productFileDoc.filename}`;
  }
  if (Array.isArray(doc.images) && doc.images.length > 0) {
    // deal with images
    for (const image of doc.images) {
      const currentImage = await req.payload.findByID({
        collection: 'media',
        id: typeof image.image === 'object' ? image.image.id : image.image
      })
      const thumbnail = currentImage?.sizes?.thumbnail
      const card = currentImage?.sizes?.card
      const tablet = currentImage?.sizes?.tablet

      if (thumbnail) {
        productData.productImages.push({
          payloadId: currentImage.id,
          filename: currentImage.filename!,
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
          payloadId: currentImage.id,
          filename: currentImage.filename!,
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
          payloadId: currentImage.id,
          filename: currentImage.filename!,
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
}

export { syncUser, afterChangeProductHook, beforeDeleteProductHook, beforeChangeProductHook }