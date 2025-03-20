import dotenv from "dotenv"
import path from 'path'
import type { InitOptions } from "payload/config";
import payload, { Payload } from "payload";

dotenv.config({
  path: path.resolve(__dirname, '../.env.local')
});

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
let cached = (global as never).payload
if (!cached) {
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
  cached = (global as never).payload = {
    client: null,
    promise: null,
  }
}

interface Args {
  initOptions?: Partial<InitOptions>
}

export const getPayloadClient = async ({ initOptions }: Args = {}): Promise<Payload> => {
  if (!process.env.PAYLOAD_SECRET) {
    throw new Error('PAYLOAD_SECRET is missing')
  }

  if (cached.client) {
    return cached.client
  }

  if (!cached.promise) {
    cached.promise = payload.init({
      secret: process.env.PAYLOAD_SECRET,
      local: !initOptions?.express,
      ...(initOptions || {}),
    })
  }

  try {
    cached.client = await cached.promise
  } catch (e: unknown) {
    cached.promise = null
    throw e
  }

  return cached.client
}