import { Access, CollectionConfig, PayloadRequest } from 'payload/types'
import { RequestContext } from "payload";
import e from "express";
import { deleteCookie } from "cookies-next";

const adminsAndUser: Access = ({ req: { user } }) => {
  if (!user) return false

  if (user.role === 'admin') return true

  return { id: { equals: user?.id } }
}

export const Users: CollectionConfig = {
  slug: 'users',
  auth: {
    tokenExpiration: 60 * 60,
    verify: false
  },
  access: {
    read: adminsAndUser,
    create: () => true,
    update: ({ req }) => req.user.role === 'admin',
    delete: ({ req }) => req.user.role === 'admin'
  },
  admin: {
    hidden: ({ user }) => user.role !== 'admin',
    defaultColumns: ['id']
  },
  fields: [
    {
      name: 'products',
      label: 'Products',
      admin: { condition: () => false },
      type: 'relationship',
      relationTo: 'products',
      hasMany: true
    },
    {
      name: 'product_files',
      label: 'Product files',
      admin: { condition: () => false },
      type: 'relationship',
      relationTo: 'product_files',
      hasMany: true,
    },
    {
      name: 'role',
      defaultValue: 'user',
      required: true,
      type: 'select',
      options: [
        { label: 'Admin', value: 'admin' },
        { label: 'User', value: 'user' }
      ]
    }
  ],
  hooks: {
    afterLogout: [({ req, res }: { req: PayloadRequest, res: e.Response, context: RequestContext }) => {
      // sign out
      deleteCookie("digitalhippo-access-token", { req, res })
    }]
  }
}
