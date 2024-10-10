import { Access, CollectionConfig } from 'payload/types'
import { User } from "../payload-types";
import { v4 as uuidv4 } from "uuid";

const isAdminOrHasAccessToImages = (): Access => async ({ req }) => {
  const user = req.user as User | undefined

  if (!user) return false
  if (user.role === 'admin') return true

  return {
    user: {
      equals: req.user.id
    }
  }
}

export const Media: CollectionConfig = {
  slug: 'media',
  hooks: {
    beforeChange: [({ req, data }) => {
      const originalFilename = data.filename || '111.png'
      const extension = originalFilename.split('.').pop();
      const filename = `${uuidv4()}.${extension}`;
      const thumbnail = data.sizes.thumbnail
      const card = data.sizes.card
      const tablet = data.sizes.tablet
      if (thumbnail) {
        data.sizes.thumbnail.filename = `thumbnail_${filename}`
      }
      if (card) {
        data.sizes.card.filename = `card_${filename}`
      }
      if (tablet) {
        data.sizes.tablet.filename = `tablet_${filename}`
      }
      return { ...data, user: req.user.id }
    }]
  },
  access: {
    read: async ({ req }) => {
      const referer = req.headers.referer
      if (!req.user || !referer?.includes('sell')) {
        return true
      }
      return await isAdminOrHasAccessToImages()({ req })
    },
    delete: isAdminOrHasAccessToImages(),
    update: isAdminOrHasAccessToImages()
  },
  admin: {
    hidden: ({ user }) => user.role !== 'admin'
  },
  upload: {
    staticURL: '/media',
    imageSizes: [
      {
        name: 'thumbnail',
        width: 400,
        height: 300,
        position: 'centre'
      },
      {
        name: 'card',
        width: 768,
        height: 1024,
        position: 'centre'
      },
      {
        name: 'tablet',
        width: 1024,
        height: undefined,
        position: 'centre'
      },
    ],
    mimeTypes: ['image/*']
  },
  fields: [
    {
      name: 'user',
      type: 'relationship',
      relationTo: 'users',
      required: true,
      hasMany: false,
      admin: { condition: () => false }
    }
  ]
}
