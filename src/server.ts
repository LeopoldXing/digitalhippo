import express from "express";
import { nextApp, nextHandler } from "../next-utils";
import * as trpcExpress from '@trpc/server/adapters/express'
import { getPayloadClient } from "./get-payload";
import { appRouter } from "./trpc";
import { inferAsyncReturnType } from "@trpc/server";

const app = express()
const PORT = Number(process.env.PORT) || 3006

const createContext = ({ req, res }: trpcExpress.CreateExpressContextOptions) => ({ req, res })
export type ExpressContext = inferAsyncReturnType<typeof createContext>

const start = async () => {
  const payload = await getPayloadClient({
    initOptions: {
      express: app,
      onInit: async (cms) => {
        cms.logger.info(`Admin URL: ${cms.getAdminURL()}`)
      },
    },
  })

  if (process.env.NEXT_BUILD) {
    app.listen(PORT, async () => {
      payload.logger.info(
          'Next.js is building for production'
      )
      // @ts-expect-error
      await nextBuild(path.join(__dirname, '../'))
      process.exit()
    })
    return
  }

  app.use('/api/trpc', trpcExpress.createExpressMiddleware({
    router: appRouter,
    createContext
  }))
  app.use((req, res) => nextHandler(req, res))

  nextApp.prepare().then(() => {
    payload.logger.info('Next.js started')

    app.listen(PORT, async () => {
      payload.logger.info(
          `Next.js App URL: ${process.env.NEXT_PUBLIC_FRONTEND_URL}`
      )
    })
  })
}

start()