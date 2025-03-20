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
import { cloudStorage } from "@payloadcms/plugin-cloud-storage";
import { s3Adapter } from "@payloadcms/plugin-cloud-storage/s3";

dotenv.config({
  path: path.resolve(__dirname, '../.env'),
})

export default buildConfig({
  serverURL: process.env.NEXT_PUBLIC_FRONTEND_URL || 'https://digitalhippo.leopoldhsing.cc:41400',
  collections: [Users, Products, Media, ProductFiles, Orders],
  plugins: [
    // Pass the plugin to Payload
    cloudStorage({
      collections: {
        // Enable cloud storage for Media collection
        "media": {
          // Create the S3 adapter
          adapter: s3Adapter({
            config: {
              endpoint: `https://${process.env.S3_ENDPOINT_HOST}`,
              region: process.env.REGION,
              credentials: {
                accessKeyId: process.env.ACCESS_KEY!,
                secretAccessKey: process.env.SECRET_KEY!
              }
            },
            bucket: process.env.S3_BUCKET!
          }),
          prefix: "product_images"
        },
        "product_files": {
          // Create the S3 adapter
          adapter: s3Adapter({
            config: {
              endpoint: `https://${process.env.S3_ENDPOINT_HOST}`,
              region: process.env.REGION,
              credentials: {
                accessKeyId: process.env.ACCESS_KEY!,
                secretAccessKey: process.env.SECRET_KEY!
              }
            },
            bucket: process.env.S3_BUCKET!
          }),
          prefix: "product_files"
        }
      }
    })
  ],
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