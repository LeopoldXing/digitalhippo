import { z } from 'zod'
import {
  privateProcedure,
  router,
} from './trpc'
import { TRPCError } from '@trpc/server'
import { getPayloadClient } from '../get-payload'

export const orderRouter = router({
  createOrder: privateProcedure
      .input(z.object({ payloadProductIds: z.array(z.string()) }))
      .mutation(async ({ ctx, input }) => {
        const { user } = ctx
        const { payloadProductIds } = input

        if (payloadProductIds.length === 0) {
          throw new TRPCError({ code: 'BAD_REQUEST' })
        }

        const payload = await getPayloadClient()

        const order = await payload.create({
          collection: 'orders',
          data: {
            _isPaid: false,
            products: payloadProductIds,
            user: user.id,
          },
        })
        return order;
      })
})
