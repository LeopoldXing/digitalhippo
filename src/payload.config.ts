import { buildConfig } from "payload/config";
import path from "path";
import dotenv from "dotenv";
import { slateEditor } from "@payloadcms/richtext-slate";
import { webpackBundler } from "@payloadcms/bundler-webpack";
import { mongooseAdapter } from "@payloadcms/db-mongodb";
import { Media } from "./collections/Media";
import { Products } from "./collections/Products";
import { Users } from "./collections/Users";
import { ProductFiles } from "./collections/ProductFiles";
import { Orders } from "./collections/Orders";

dotenv.config({
  path: path.resolve(__dirname, '../.env'),
})

export default buildConfig({
  serverURL: process.env.NEXT_PUBLIC_FRONTEND_URL || '',
  collections: [Users, Products, Media, ProductFiles, Orders],
  routes: {
    admin: '/sell'
  },
  admin: {
    user: 'users',
    bundler: webpackBundler(),
    meta: {
      titleSuffix: '- DigitalHippo',
      favicon: '/favicon.ico',
      ogImage: '/thumbnail.jpg',
    }
  },
  rateLimit: {
    max: 2000
  },
  editor: slateEditor({}),
  db: mongooseAdapter({
    url: process.env.MONGODB_URL!,
  }),
  typescript: {
    outputFile: path.resolve(__dirname, 'payload-types.ts'),
  },
})