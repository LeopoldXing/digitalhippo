import { Access, CollectionConfig } from 'payload/types'
import { User } from "@/payload-types";
import { PRODUCT_CATEGORIES } from "../config/index";
import { afterChangeProductHook, beforeDeleteProductHook, beforeChangeProductHook, syncUser } from "./hooks/ProductsSync";

const isAdminOrHasAccess = (): Access => ({ req: { user: _user } }) => {
  const user = _user as User | undefined

  if (!user) return false
  if (user.role === 'admin') return true

  const userProductIDs = (user.products || []).reduce<Array<string>>((acc, product) => {
    if (!product) return acc
    if (typeof product === 'string') {
      acc.push(product)
    } else {
      acc.push(product.id)
    }

    return acc
  }, [])

  return {
    id: { in: userProductIDs }
  }
}

export const Products: CollectionConfig = {
  slug: 'products',
  admin: {
    useAsTitle: 'name',
  },
  hooks: {
    beforeChange: [beforeChangeProductHook],
    afterChange: [afterChangeProductHook, syncUser],
    beforeDelete: [beforeDeleteProductHook]
  },
  access: {
    read: isAdminOrHasAccess(),
    update: isAdminOrHasAccess(),
    delete: isAdminOrHasAccess(),
  },
  fields: [
    {
      name: 'user',
      type: 'relationship',
      relationTo: 'users',
      required: true,
      hasMany: false,
      admin: { condition: () => false }
    },
    {
      name: 'name',
      label: 'Name',
      type: 'text',
      required: true
    },
    {
      name: 'description',
      type: 'textarea',
      label: 'Product details'
    },
    {
      name: 'price',
      label: 'Price in CAD',
      min: 0,
      max: 1000,
      type: 'number',
      required: true
    },
    {
      name: 'category',
      label: 'Category',
      type: 'select',
      options: PRODUCT_CATEGORIES.map(({ label, value }) => ({ label, value })),
      required: true
    },
    {
      name: 'product_files',
      label: 'Product file(s)',
      type: 'relationship',
      required: true,
      relationTo: 'product_files',
      hasMany: false
    },
    {
      name: 'approvedForSale',
      label: 'Product Status',
      type: 'select',
      defaultValue: 'pending',
      access: {
        create: ({ req }) => req.user?.role === 'admin',
        read: ({ req }) => req.user?.role === 'admin',
        update: ({ req }) => req.user?.role === 'admin',
      },
      options: [
        {
          label: 'Pending verification',
          value: 'pending'
        },
        {
          label: 'Approved',
          value: 'approved'
        },
        {
          label: 'Denied',
          value: 'denied'
        }
      ]
    },
    {
      name: 'priceId',
      access: {
        create: () => false,
        read: () => false,
        update: () => false
      },
      type: 'text',
      admin: { hidden: true }
    },
    {
      name: 'stripeId',
      access: {
        create: () => false,
        read: () => false,
        update: () => false
      },
      type: 'text',
      admin: { hidden: true }
    },
    {
      name: 'images',
      type: 'array',
      label: 'Product images',
      minRows: 1,
      maxRows: 10,
      required: true,
      labels: {
        singular: 'Image',
        plural: 'Images'
      },
      fields: [
        {
          name: 'image',
          type: 'upload',
          relationTo: 'media',
          required: true
        },
      ]
    }
  ]
}
